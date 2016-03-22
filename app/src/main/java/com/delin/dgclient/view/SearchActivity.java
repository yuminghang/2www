package com.delin.dgclient.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.ListView;
import android.widget.TextView;

import com.delin.dgclient.R;
import com.delin.dgclient.api.Config;
import com.delin.dgclient.service.StoreBSGetAll;
import com.delin.dgclient.service.StoreBSGetLayer;
import com.delin.dgclient.service.StoreBSGetType;
import com.delin.dgclient.service.UserBSLoadLogin;
import com.google.gson.internal.LinkedTreeMap;
import com.myideaway.easyapp.core.lib.service.Service;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/10/28 0028.
 */
public class SearchActivity extends BaseActivity {

    private ListView searchStoreListView;
    private LinearLayout goBackLinearLayout;
    private LinearLayout changeFoolLinearLayout;
    private LinearLayout checkStoreLinear;
    private LinearLayout syntheticallyLinearLayout;
    private TextView startMapTextView;
    private EditText searchEditText;
    private Button searchButton;
    private ArrayList FOOLS;
    private ArrayList STORETYPE;
    private ArrayList SYNTHETICALLY;
    private ListPopupWindow listPopupWindow;
    private ListPopupWindowAdapter listPopupWindowAdapter;
    private int width;
    private int height;
    private String keywords= "0";
    private String layer="0";
    private String type = "0";
    private String sort = "0";
    private String page="1";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTypeData();
        setContentView(R.layout.activity_search_store);
        searchEditText = (EditText) findViewById(R.id.searchEditText);
        searchButton = (Button) findViewById(R.id.searchButton);
        searchStoreListView = (ListView) findViewById(R.id.searchStoreListView);
        goBackLinearLayout = (LinearLayout) findViewById(R.id.goBackLinearLayout);
        changeFoolLinearLayout = (LinearLayout) findViewById(R.id.changeFoolLinearLayout);
        checkStoreLinear = (LinearLayout) findViewById(R.id.checkStoreLinear);
        syntheticallyLinearLayout = (LinearLayout) findViewById(R.id.syntheticallyLinearLayout);
        startMapTextView = (TextView) findViewById(R.id.startMapTextView);


        changeFoolLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initPopupWindow(FOOLS,view,0);

            }
        });
        checkStoreLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initPopupWindow(STORETYPE,view,1);
            }
        });
        syntheticallyLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initPopupWindow(SYNTHETICALLY,view,2);
            }
        });

        goBackLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        ViewTreeObserver vto2 = changeFoolLinearLayout.getViewTreeObserver();
        vto2.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                changeFoolLinearLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                width = changeFoolLinearLayout.getWidth();
                height = changeFoolLinearLayout.getHeight();

            }
        });


        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                keywords = searchEditText.getText().toString();
                getAll(keywords, layer, type, sort, page);
            }
        });
    }

    private class SearchAdapter extends BaseAdapter{
        private ArrayList<LinkedTreeMap> arrayList;
        private LayoutInflater layoutInflater;
        private DisplayImageOptions options;

        public SearchAdapter(Context context, ArrayList arrayList) {
            layoutInflater = LayoutInflater.from(context);
            this.arrayList = arrayList;
            options = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.mipmap.logo_2_03) // resource or drawable
                    .showImageForEmptyUri(R.mipmap.logo_2_03) // resource or drawable
                    .showImageOnFail(R.mipmap.logo_2_03) // resource or drawable
                    .cacheInMemory(true) // default
                    .cacheOnDisk(false) // default
                    .considerExifParams(true)
                    .build();
        }

        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public Object getItem(int i) {
            return arrayList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view==null){
                view = layoutInflater.inflate(R.layout.item_search_store,viewGroup,false);
            }
            ImageView storeLogoImageView = (ImageView) view.findViewById(R.id.storeLogoImageView);
            TextView storeNameTextView = (TextView) view.findViewById(R.id.storeNameTextView);
            TextView typeTextView = (TextView) view.findViewById(R.id.typeTextView);
            TextView layerTextView = (TextView) view.findViewById(R.id.layerTextView);
            storeNameTextView.setText((String) arrayList.get(i).get("store_name"));
            layerTextView.setText((String) arrayList.get(i).get("room"));
            typeTextView.setText((String)arrayList.get(i).get("store_type"));
            ImageLoader.getInstance().displayImage(Config.URL_SERVER_IMAGE + arrayList.get(i).get("store_logo"), storeLogoImageView, options);

            final int position = i;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getBaseContext(), ShopDetailActivity.class);
                    intent.putExtra("store_id", (String) arrayList.get(position).get("store_id"));
                    startActivity(intent);
                }
            });

            return view;
        }
    }

    private void initTypeData(){
        SYNTHETICALLY = new ArrayList();
        SYNTHETICALLY.add("人气排序");
        SYNTHETICALLY.add("距离排序");
        SYNTHETICALLY.add("好评排序");
        getLayer();
        getType();
        getAll(keywords, layer, type, sort, page);
    }

    private void initPopupWindow(final ArrayList arrayList,View view, final int which) {
        listPopupWindow = new ListPopupWindow(this);
        //自定义Adapter
        listPopupWindowAdapter = new ListPopupWindowAdapter(arrayList, this);
        listPopupWindow.setAdapter(listPopupWindowAdapter);
        listPopupWindow.setWidth(width);
        if (arrayList.size() >= 7) {
            listPopupWindow.setHeight(height * 4);
        } else {
            listPopupWindow.setHeight(height * (arrayList.size()-1));
        }

        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long k) {
                listPopupWindow.dismiss();
                if (which == 0) {
                    layer = (String) arrayList.get(position);
                    getAll(keywords, layer, type, sort, page);
                } else if (which == 1) {
                    type = (String) arrayList.get(position);
                    getAll(keywords, layer, type, sort, page);
                } else if (which == 2) {
                    sort = (String) arrayList.get(position);
                    getAll(keywords, layer, type, sort, page);
                }


            }
        });

        listPopupWindow.setAnchorView(view);
        listPopupWindow.show();
    }

    private void getLayer(){
        showProgressDialog(this, "正在加载请稍后......");
        StoreBSGetLayer storeBSGetLayer = new StoreBSGetLayer(this);
        runBizService(storeBSGetLayer, new Service.OnCompleteHandler() {
            @Override
            public void onComplete(Service service) {
                dismissProgressDialog();
            }
        }, new Service.OnSuccessHandler() {
            @Override
            public void onSuccess(Service service, Object o) {
                StoreBSGetLayer.ServiceResult result = (StoreBSGetLayer.ServiceResult) o;
                if (result.getStatus().equals("success")) {
                    FOOLS = result.getArrayList();
                }
            }
        });
    }

    private void getType(){
        showProgressDialog(this,"正在加载请稍后......");
        StoreBSGetType storeBSGetType = new StoreBSGetType(this);
        runBizService(storeBSGetType, new Service.OnCompleteHandler() {
            @Override
            public void onComplete(Service service) {
                dismissProgressDialog();
            }
        }, new Service.OnSuccessHandler() {
            @Override
            public void onSuccess(Service service, Object o) {
                StoreBSGetType.ServiceResult result= (StoreBSGetType.ServiceResult) o;
                if (result.getStatus().equals("success")) {
                    STORETYPE = result.getArrayList();
                }
            }
        });
    }

    private void getAll(String keywords,String layer,String type,String sort,String page){
        showProgressDialog(this, "正在加载请稍后......");
        StoreBSGetAll storeBSGetAll = new StoreBSGetAll(this);
        storeBSGetAll.setKeywords(keywords);
        storeBSGetAll.setLayer(layer);
        storeBSGetAll.setType(type);
        storeBSGetAll.setSort(sort);
        storeBSGetAll.setPage(page);
        runBizService(storeBSGetAll, new Service.OnCompleteHandler() {
            @Override
            public void onComplete(Service service) {
                dismissProgressDialog();
            }
        }, new Service.OnSuccessHandler() {
            @Override
            public void onSuccess(Service service, Object o) {
                StoreBSGetAll.ServiceResult result = (StoreBSGetAll.ServiceResult) o;
                if (result.getStatus().equals("success")) {
                    ArrayList arrayList = result.getArrayList();
                    SearchAdapter searchAdapter = new SearchAdapter(getBaseContext(), arrayList);
                    searchStoreListView.setAdapter(searchAdapter);
                } else {
                    showShortToast(result.getMsg());
                }

            }
        });
    }


}
