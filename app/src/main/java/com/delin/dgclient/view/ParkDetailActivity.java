package com.delin.dgclient.view;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.delin.dgclient.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2015/11/5 0005.
 */
public class ParkDetailActivity extends BaseActivity {
    private ListView parkingListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stoping_detail);
        parkingListView = (ListView) findViewById(R.id.parkingListView);
        ArrayList arrayList = new ArrayList();
        arrayList.add("当日消费每满100元可换取1小时免费停车券最多可换3小时停车券");
        arrayList.add("当日消费每满100元可换取1小时免费停车券最多可换3小时停车券当日消费每满100元可换取1小时免费停车券最多可换3小时停车券");
        arrayList.add("当日消费每满100元可换取1小时免费停车券最多可换3小时停车券当日消费每满100元可换取1小时免费停车券最多可换3小时停车券当日消费每满100元可换取1小时免费停车券最多可换3小时停车券");
        ParkPrivilegeAdapter parkPrivilegeAdapter = new ParkPrivilegeAdapter(this,arrayList);
        parkingListView.setAdapter(parkPrivilegeAdapter);
        findViewById(R.id.goBackLinearLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private class ParkPrivilegeAdapter extends BaseAdapter{

        private LayoutInflater inflater;
        private ArrayList arrayList;

        public ParkPrivilegeAdapter(Context context,ArrayList arrayList) {
            this.arrayList = arrayList;
            inflater = LayoutInflater.from(context);
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
                view = inflater.inflate(R.layout.item_parking_privilege,viewGroup,false);
            }
            LinearLayout lineLinearLayout = (LinearLayout) view.findViewById(R.id.lineLinearLayout);
            TextView parkingPrivilegeTextView = (TextView) view.findViewById(R.id.parkingPrivilegeTextView);
            parkingPrivilegeTextView.setText((String)arrayList.get(i));
            if (i==arrayList.size()-1){
                lineLinearLayout.setVisibility(View.GONE);
            }

            return view;
        }
    }
}
