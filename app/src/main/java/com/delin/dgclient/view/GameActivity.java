package com.delin.dgclient.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListPopupWindow;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.delin.dgclient.R;
import com.delin.dgclient.api.Config;
import com.delin.dgclient.util.HttpHelper;
import com.delin.dgclient.util.JSONUtil;
import com.delin.dgclient.util.L;
import com.fengmap.android.analysis.navi.FMNaviAnalyser;
import com.fengmap.android.analysis.navi.FMNaviResult;
import com.fengmap.android.map.FMMap;
import com.fengmap.android.map.FMMapView;
import com.fengmap.android.map.event.OnFMMapClickListener;
import com.fengmap.android.map.event.OnFMMapInitListener;
import com.fengmap.android.map.event.OnFMMapLongPressListener;
import com.fengmap.android.map.event.OnFMMapUpdateEvent;
import com.fengmap.android.map.geometry.FMMapCoord;
import com.fengmap.android.map.geometry.FMScreenCoord;
import com.fengmap.android.map.layer.FMLineLayer;
import com.fengmap.android.map.marker.FMLineMarker;
import com.fengmap.android.map.marker.FMSegment;
import com.fengmap.android.map.style.FMLineMarkerStyle;
import com.google.gson.internal.LinkedTreeMap;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2016/3/10 0010.
 */
public class GameActivity extends BaseActivity{
    private RatingBar ratingBar;
    private FMMapView fmMapView;

    private FMMap fmMap;
    private CustomDialog.Builder customDialog;
    private ListPopupWindow listPopupWindow;
    private FMNaviAnalyser naviAnalyser;
    private FMLineLayer lineLayer;
    private LinkedTreeMap coupon;
    private int clicked = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        Button ruleButton = (Button) findViewById(R.id.ruleButton);
        HashMap hashMap = new HashMap();
        hashMap.put("num", getIntent().getIntExtra("num", 0));
        ruleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startRulerActivity = new Intent(GameActivity.this, RulerActivity.class);
                startActivity(startRulerActivity);
            }
        });
        final Button finishButton = (Button) findViewById(R.id.finishButton);
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ratingBar = (RatingBar) findViewById(R.id.baoziRatingBar);
        int num = (Integer) hashMap.get("num");
        TextView stepSizeTextView = (TextView) findViewById(R.id.stepSizeTextView);
        stepSizeTextView.setText(num + "");
        ratingBar.setRating(num);
    }
    /**
     * 领取包子
     *
     * @param cId 店铺名称
     */
    private void saveBun(String cId) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("c_id", cId);
        params.put("app_id", applicationContext.getAppId());

        HttpHelper httpHelper = new HttpHelper(Config.URL_SERVER_SAVE_BUN, params, applicationContext.getCookie()) {
            @Override
            protected void onPostExecute(Integer integer) {
                super.onPostExecute(integer);

                if (integer == TASK_RESULT_SUCCESS) {
                    HashMap hashMap = JSONUtil.toHashMap(getResult());
                    if (hashMap.get("status").equals("success")) {
                        double number = (Double) hashMap.get("msg");
                        final int num = (int) number;
                        ratingBar.setRating(num);
                        ((TextView) findViewById(R.id.stepSizeTextView)).setText(num + "");
                        if (num == 2) {
                            customDialog = new CustomDialog.Builder(GameActivity.this);
                            customDialog.setNum("2");
                            customDialog.setMessage2("您已集齐2个包子，可以到客服中心兑换礼品哦~");
                            customDialog.create().show();
                        }
                    }
                } else {
                    showShortToast("网络异常，请稍后重试");
                }
                progressDialog.dismiss();
            }
        };
        httpHelper.showProgressDialog(GameActivity.this, "正在处理...");
        httpHelper.execute();
        showDialog(coupon);
    }

    private void showBunDialog(final LinkedTreeMap map) {
        customDialog = new CustomDialog.Builder(this);
        customDialog.setTitle("捡到一个包子");
        customDialog.setMessage(null);
        customDialog.setNegativeButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                saveBun((String) map.get("c_id"));
            }
        });
        customDialog.setPositiveButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        customDialog.create().show();
    }


    /**
     * 获取推送消息
     */
    public void geGameCouponPush() {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("ibeacon_sn", applicationContext.getSerialNumber());
        params.put("app_id", applicationContext.getAppId());
        HttpHelper httpHelper = new HttpHelper(Config.URL_SERVER_LOCATION_PUSH, params, applicationContext.getCookie()) {
            @Override
            protected void onPostExecute(Integer integer) {
                String result = this.getResult();
                L.d("ibeacon_sn" + result);
                if (integer == TASK_RESULT_SUCCESS) {
                    HashMap hashMap = JSONUtil.toHashMap(result.toString());
                    if (hashMap.get("status").equals("success")
                            && hashMap.get("msg").toString().length() > 5) {
                        try {
                            coupon = (LinkedTreeMap) hashMap.get("msg");
                        } catch (ClassCastException e) {
                            L.e(e.toString());
                            coupon = null;
                        }

                    }
                } else {
                    showShortToast("网络异常，请稍后重试");
                }

            }
        };
        httpHelper.execute();
    }

    private void showDialog(final LinkedTreeMap map) {
        if (map != null && map.size() > 1) {
            showCouponDialog(map);
        } else {
            showShortToast("服务器异常");
        }
    }

    private void showCouponDialog(LinkedTreeMap map) {
        customDialog = new CustomDialog.Builder(this);
        customDialog.setMessage("接收到" + map.get("store_name"));
        customDialog.setNegativeButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();
            }
        });
        customDialog.setPositiveButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        customDialog.create().show();
    }

    private void initMap() {
        fmMapView = (FMMapView) findViewById(R.id.mapview);
        fmMap = fmMapView.getFMMap();
        fmMap.loadThemeById("1004");
        lineLayer = fmMap.getFMLayerProxy().getFMLineLayer();
        naviAnalyser = new FMNaviAnalyser(fmMap);
        naviAnalyser.initById(getString(R.string.map_bid));
        fmMap.openMapById(getString(R.string.map_bid));
        fmMap.setOnFMMapInitListener(new OnFMMapInitListener() {
            @Override
            public void onMapInitSuccess(String path) {
                fmMap.showCompass(true);
            }

            @Override
            public void onMapInitFailure(String path, int errCode) {
            }
        });

        final ArrayList<FMMapCoord> points = new ArrayList<FMMapCoord>();
        fmMap.setOnFMMapClickListener(new OnFMMapClickListener() {
            @Override
            public void onMapClick(float x, float y) {
                fmMap.removeAll();
                FMScreenCoord screenCoord = new FMScreenCoord(x, y);
                FMMapCoord point = fmMap.toFMMapCoord(1, screenCoord);
                points.add(point);
                clicked++;
                if (clicked % 2 == 0 && clicked != 0) {
                    anlyzeNavi(1, points.get(clicked - 2), 1, points.get(clicked - 1));
                }
            }
        });



        fmMap.setOnFMMapLongPressListener(new OnFMMapLongPressListener() {
            @Override
            public void onMapLongPress(float x, float y) {

            }
        });
        fmMap.setOnFMMapUpdateEvent(new OnFMMapUpdateEvent() {
            //deltaTime 更新时间戳，绘制一帧所需要的时间
            @Override
            public void onMapUpdate(long deltaTime) {
            }
        });
    }

    /**
     * 路径分析。
     *
     * @param startGroupId 起点所在层
     * @param startPt      起点坐标
     * @param endGroupId   终点所在层
     * @param endPt        终点坐标
     */
    private void anlyzeNavi(int startGroupId, FMMapCoord startPt, int endGroupId, FMMapCoord endPt) {
        //路径分析
        int type = naviAnalyser.analyzeNavi(startGroupId, startPt, endGroupId, endPt, FMNaviAnalyser.FMNaviModule.MODULE_SHORTEST);

        //路径计算结果
        if (type == FMNaviAnalyser.FMRouteCalcuResult.ROUTE_SUCCESS) {
            //获取路径计算成功后的结果集合
            ArrayList<FMNaviResult> results = naviAnalyser.getNaviResults();

            //画线显示路径
            FMLineMarkerStyle lineStyle = new FMLineMarkerStyle();  //创建线的样式
            lineStyle.setFillColor(Color.RED);                    //设置线的颜色
            lineStyle.setLineWidth(0.5f);                            //设置线的宽度
            lineStyle.setLineMode(FMLineMarker.LineMode.FMLINE_CIRCLE);   //设置线的显示模式
            lineStyle.setLineType(FMLineMarker.LineType.FMLINE_DASHED);   //设置线的类型

            FMLineMarker line = new FMLineMarker();            //创建线对象
            line.setStyle(lineStyle);                            //设置线的样式

            //为线添加线段对象，合成一个线对象
            for (FMNaviResult r : results) {
                FMSegment s = new FMSegment(r.getGroupId(), r.getPointList());
                line.addSegment(s);
            }

            lineLayer.addMarker(line);        //添加线对象到线图层
            fmMap.addLayer(lineLayer);
            results.clear();                    //清除结果集，释放内存
            fmMap.updateMap();                    //更新地图

        } else if (type == FMNaviAnalyser.FMRouteCalcuResult.ROUTE_FAILURE_NO_STAIR_FLOORS) {
            Toast.makeText(this, "没有电梯或者扶梯进行跨层分析", Toast.LENGTH_SHORT).show();
        } else if (type == FMNaviAnalyser.FMRouteCalcuResult.ROUTE_FAILURE_NOTSUPPORT_FLOORS) {
            Toast.makeText(this, "不支持跨层分析", Toast.LENGTH_SHORT).show();
        } else if (type == FMNaviAnalyser.FMRouteCalcuResult.ROUTE_PARAM_ERROR) {
            Toast.makeText(this, "路径分析参数出错", Toast.LENGTH_SHORT).show();
        } else if (type == FMNaviAnalyser.FMRouteCalcuResult.ROUTE_FAILURE_TOO_CLOSE) {
            Toast.makeText(this, "太近了", Toast.LENGTH_SHORT).show();
        } else if (type == FMNaviAnalyser.FMRouteCalcuResult.ROUTE_DATABASE_ERROR) {
            Toast.makeText(this, "数据库出错", Toast.LENGTH_SHORT).show();
        } else if (type == FMNaviAnalyser.FMRouteCalcuResult.ROUTE_FAILURE_NO_END) {
            Toast.makeText(this, "数据出错，终点不在其对应层的数据中", Toast.LENGTH_SHORT).show();
        } else if (type == FMNaviAnalyser.FMRouteCalcuResult.ROUTE_FAILURE_NO_START) {
            Toast.makeText(this, "数据错误，起点不在其对应层的数据中", Toast.LENGTH_SHORT).show();
        } else if (type == FMNaviAnalyser.FMRouteCalcuResult.ROUTE_FAILURE_NO_FMDBKERNEL) {
            Toast.makeText(this, "底层指针错误", Toast.LENGTH_SHORT).show();
        }
    }
}
