package com.delin.dgclient.view;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.delin.dgclient.ApplicationContext;
import com.delin.dgclient.R;
import com.delin.dgclient.api.Config;

import com.delin.dgclient.notification.Notification;
import com.delin.dgclient.notification.NotificationCenter;
import com.delin.dgclient.notification.NotificationListener;
import com.delin.dgclient.notification.NotificationName;
import com.delin.dgclient.util.HttpHelper;
import com.delin.dgclient.util.JSONUtil;

import com.google.gson.internal.LinkedTreeMap;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2015/9/18 0018.
 */
public class CouponsTab extends BaseFragment {

    private ListView couponsListView;
    private TextView noCouponsTextView;

    private CouponsAdapter couponsAdapter;


    @Override
    public int getViewId() {
        return R.layout.tab_coupons;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        couponsListView = (ListView) view.findViewById(R.id.couponsListView);
        noCouponsTextView = (TextView) view.findViewById(R.id.noCouponsTextView);

        NotificationCenter.getInstance().register(new String[]{NotificationName.LOGIN_SUCCESS}, new NotificationListener() {
            @Override
            public void handleNotification(Notification notification) {
                if (getActivity()!=null){
                    getCoupons();
                }

            }
        });
        getCoupons();
    }

    /**
     * 获取优惠券列表
     * 当status=success&&msg不为空是展示列表
     * else
     * 显示无优惠券
     */

    private void getCoupons() {

        HttpHelper httpHelper = new HttpHelper(Config.URL_SERVER_COUPON_INFORMATION,
                null, ApplicationContext.getInstance().getCookie()) {
            @Override
            protected void onPostExecute(Integer integer) {
                if (integer == TASK_RESULT_SUCCESS){

                    progressDialog.dismiss();
                    HashMap serviceResult = JSONUtil.toHashMap(this.getResult());

                    if (serviceResult != null &&
                            serviceResult.size() != 0 &&
                            serviceResult.get("status").equals("success")) {
                        ArrayList<LinkedTreeMap> linkedTreeMap = (ArrayList<LinkedTreeMap>) serviceResult.get("msg");
                        if (linkedTreeMap.size() != 0) {

                            couponsAdapter = new CouponsAdapter(baseContext, linkedTreeMap);
                            couponsListView.setAdapter(couponsAdapter);
                            couponsListView.setVisibility(View.VISIBLE);
                            noCouponsTextView.setVisibility(View.GONE);
                        } else {
                            couponsListView.setVisibility(View.GONE);
                            noCouponsTextView.setVisibility(View.VISIBLE);
                            noCouponsTextView.setText("您暂时没有可用的优惠券");
                        }

                    } else {
                        new MassageDialog.Builder(getActivity())
                                .setTitle("是否登录?")
                                .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                })
                                .setNegativeButton("登录", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                                        startActivity(intent);
                                    }
                                }).create().show();
                        couponsListView.setVisibility(View.GONE);
                        noCouponsTextView.setVisibility(View.VISIBLE);
                        noCouponsTextView.setText("你还没有登录暂时无法获取优惠券信息");
                        ApplicationContext.getInstance().setCookie(null);

                    }
                }else {
                    showShortToast("网络异常，请检查网络");
                }
                progressDialog.dismiss();
            }
        };
        httpHelper.showProgressDialog(getActivity(),"loading......");
        httpHelper.execute();
    }

    /**
     * 自定义列表
     */
    private class CouponsAdapter extends BaseAdapter {
        private ArrayList<LinkedTreeMap> serviceData;
        private LayoutInflater layoutInflater;
        private DisplayImageOptions options;


        public CouponsAdapter(Context context, ArrayList serviceData) {
            this.serviceData = serviceData;

            layoutInflater = LayoutInflater.from(context);
            options = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.mipmap.bg_girl) // resource or drawable
                    .showImageForEmptyUri(R.mipmap.bg_girl) // resource or drawable
                    .showImageOnFail(R.mipmap.bg_girl) // resource or drawable
                    .cacheInMemory(true) // default
                    .cacheOnDisk(false) // default
                    .considerExifParams(true)
                    .displayer(new RoundedBitmapDisplayer(90))
                    .build();
        }

        @Override
        public int getCount() {
            return serviceData.size();
        }

        @Override
        public Object getItem(int i) {
            return serviceData.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {

            if (view == null) {
                view = layoutInflater.inflate(R.layout.item_coupons, viewGroup, false);
            }
            TextView messageTextView = (TextView) view.findViewById(R.id.messageTextView);
            ImageView logoImageView = (ImageView) view.findViewById(R.id.logoImageView);
            LinearLayout itemLinearLayout = (LinearLayout) view.findViewById(R.id.itemLinearLayout);
            messageTextView.setText((String) serviceData.get(i).get("code"));
            ImageLoader.getInstance().displayImage(Config.URL_SERVER_IMAGE + (String) serviceData.get(i).get("store_logo"), logoImageView, options);
            if (serviceData.get(i).get("is_used").equals("1")) {
                itemLinearLayout.setBackgroundColor(Color.WHITE);
                itemLinearLayout.setClickable(false);
            }
            final int position = i;
            itemLinearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), PushDetailActivity.class);
                    intent.putExtra("cId", (String) serviceData.get(position).get("c_id"));
                    startActivity(intent);
                }
            });


            return view;
        }
    }
}
