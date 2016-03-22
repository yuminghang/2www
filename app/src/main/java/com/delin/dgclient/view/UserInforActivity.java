package com.delin.dgclient.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.delin.dgclient.ApplicationContext;
import com.delin.dgclient.R;
import com.delin.dgclient.api.Config;
import com.delin.dgclient.notification.NotificationCenter;
import com.delin.dgclient.notification.NotificationName;
import com.delin.dgclient.service.UserBSRemoveLogin;
import com.delin.dgclient.util.HttpHelper;
import com.delin.dgclient.util.JSONUtil;
import com.myideaway.easyapp.core.lib.BizCookie;


import java.util.HashMap;


/**
 * Created by Administrator on 2015/10/26 0026.
 */
public class UserInforActivity extends BaseActivity {

    private ImageView headPicImageView;
    private LinearLayout goBackLinearLayout;

    private ExpandableListView userExpandableListView;

    private Button logoutButton;

    private String userName;
    private String mobile;
    private String[] groups={"会员中心","我的券包","我的订单","我的停车","我的餐馆"};
    private String[][] children = {{"商场会员卡","反馈意见"},{"优惠券","活动券","礼品兑换券",
            "游戏奖品券"},{"电影订单"},{"停车记录","缴费记录"},{"我的外卖"
            ,"我的点菜","我的位子","我的排队","我的团购","我的评价","我的支付"}};
    private ExpandableAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_infor);
        Intent intent = getIntent();
        userName = intent.getStringExtra("userName");
        mobile = intent.getStringExtra("mobile");
        userExpandableListView = (ExpandableListView) findViewById(R.id.userExpandableListView);
        logoutButton = (Button) findViewById(R.id.logoutButton);
        goBackLinearLayout = (LinearLayout) findViewById(R.id.goBackLinearLayout);
        goBackLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });
        View headView = layoutInflater.inflate(R.layout.item_head_user,userExpandableListView,false);
        headPicImageView = (ImageView) headView.findViewById(R.id.headPicImageView);
        headPicImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserInforActivity.this,UserInformationActivity.class);
                startActivity(intent);

            }
        });
        userExpandableListView.addHeaderView(headView);
        adapter = new ExpandableAdapter(this,groups,children);
        userExpandableListView.setAdapter(adapter);
    }

    private void logout(){
        new HttpHelper(Config.URL_SERVER_USER_LOGOUT, BizCookie.initBizCookie().getCookie()) {
            @Override
            protected void onPostExecute(Integer integer) {
                HashMap hashMap = JSONUtil.toHashMap(this.getResult());
                if (hashMap.get("status").equals("success")){
                    showShortToast((String) hashMap.get("msg"));
                    finish();
                    removeToken();
                    ApplicationContext.getInstance().setCookie(null);
                    NotificationCenter.getInstance().sendNotification(NotificationName.LOGOUT);
                }
            }
        }.execute();
    }

    private void removeToken(){
        UserBSRemoveLogin userBSRemoveLogin = new UserBSRemoveLogin(this);
        userBSRemoveLogin.asyncExecute();
    }

    public class ExpandableAdapter extends BaseExpandableListAdapter {
        private LayoutInflater inflater;
        private String[] groups;
        private String[][] children;
        public ExpandableAdapter(Context context,String[] groups,String[][] children) {
            this.children =children;
            this.groups = groups;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return children[groupPosition][childPosition];
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition,
                                 boolean isLastChild, View convertView, ViewGroup viewGroup) {
            if (convertView ==null){
                convertView = inflater.inflate(R.layout.item_child,viewGroup,false);
            }
            LinearLayout lineLinearLayout = (LinearLayout) convertView.findViewById(R.id.lineLinearLayout);
            if (childPosition ==children[groupPosition].length-1){
                lineLinearLayout.setVisibility(View.GONE);
            }else {
                lineLinearLayout.setVisibility(View.VISIBLE);
            }
            TextView child = (TextView) convertView.findViewById(R.id.childTextView);
            child.setText(children[groupPosition][childPosition]);
            return convertView;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return children[groupPosition].length;
        }

        @Override
        public Object getGroup(int groupPosition) {
            return groups;
        }

        @Override
        public int getGroupCount() {
            return groups.length;
        }
        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded,
                                 View convertView, ViewGroup viewGroup) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_group,viewGroup,false);
            }

            TextView textView = (TextView) convertView.findViewById(R.id.groupTextView);
            textView.setText(groups[groupPosition]);
            return convertView;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }
}
