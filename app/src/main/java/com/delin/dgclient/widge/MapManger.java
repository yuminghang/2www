package com.delin.dgclient.widge;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.Toast;

import com.delin.dgclient.ApplicationContext;
import com.delin.dgclient.R;
import com.delin.dgclient.db.DBHelper;
import com.delin.dgclient.model.Beacon;
import com.delin.dgclient.notification.Notification;
import com.delin.dgclient.notification.NotificationCenter;
import com.delin.dgclient.notification.NotificationListener;
import com.delin.dgclient.notification.NotificationName;
import com.delin.dgclient.util.JSONUtil;
import com.delin.dgclient.util.L;
import com.fengmap.android.FMErrorMsg;
import com.fengmap.android.analysis.navi.FMNaviAnalyser;
import com.fengmap.android.analysis.navi.FMNaviResult;
import com.fengmap.android.map.FMMap;
import com.fengmap.android.map.FMMapExtent;
import com.fengmap.android.map.FMMapInfo;
import com.fengmap.android.map.FMMapView;
import com.fengmap.android.map.event.OnFMMapClickListener;
import com.fengmap.android.map.event.OnFMMapInitListener;
import com.fengmap.android.map.event.OnFMMapLongPressListener;
import com.fengmap.android.map.event.OnFMMapUpdateEvent;
import com.fengmap.android.map.geometry.FMMapCoord;
import com.fengmap.android.map.geometry.FMScreenCoord;
import com.fengmap.android.map.layer.FMImageLayer;
import com.fengmap.android.map.layer.FMLineLayer;
import com.fengmap.android.map.layer.FMLocationLayer;
import com.fengmap.android.map.marker.FMImageMarker;
import com.fengmap.android.map.marker.FMLineMarker;
import com.fengmap.android.map.marker.FMLocationMarker;
import com.fengmap.android.map.marker.FMSegment;
import com.fengmap.android.map.style.FMImageMarkerStyle;
import com.fengmap.android.map.style.FMLineMarkerStyle;
import com.fengmap.android.map.style.FMLocationMarkerStyle;
import com.fengmap.android.map.style.FMStyle;
import com.fengmap.android.utils.FMLog;


import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2016/3/11 0011.
 */
public class MapManger extends FMMapView {
    private FMMap fmMap;
    private FMLocationLayer locLayer;             //定位图层
    private FMLineLayer lineLayer;
    private FMNaviAnalyser naviAnalyser;
    private FMMapExtent ex;                       //地图范围
    private int clicked = 0;
    private FMMapInfo scene;
    private FMImageLayer imageLayer; //图片标注物图层
    private Random rd;
    private Context context;

    private FMLocationMarker marker;
    public static boolean MapIsShow = false;

    public MapManger(Context context) {
        super(context);
        this.context = context;
        rd = new Random();
        initMap();
    }

    public MapManger(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        rd = new Random();
        initMap();
    }

    public MapManger(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        rd = new Random();
        initMap();
    }


    private void initMap() {
        fmMap = this.getFMMap();
        lineLayer = fmMap.getFMLayerProxy().getFMLineLayer();
        naviAnalyser = new FMNaviAnalyser(fmMap);
        naviAnalyser.initById(context.getString(R.string.map_bid));
        fmMap.openMapById(context.getString(R.string.map_bid));
        fmMap.setOnFMMapInitListener(new OnFMMapInitListener() {
            @Override
            public void onMapInitSuccess(String path) {
                fmMap.showCompass(true);
                MapIsShow = true;
                scene = fmMap.getFMMapInfo();
                ex = fmMap.getFMMapExtent();
                locLayer = fmMap.getFMLayerProxy().getFMLocationLayer();  //获取定位或者创建定位图层
                //默认是所有的公共设施层都是显示的，这里设置其隐藏
                locLayer.setVisible(true);
                fmMap.addLayer(locLayer);
                addMarker();

            }

            @Override
            public void onMapInitFailure(String path, int errCode) {
                FMLog.i("FMMap Init", FMErrorMsg.getErrorMsg(errCode));
            }
        });

        final ArrayList<FMMapCoord> points = new ArrayList<FMMapCoord>();
        fmMap.setOnFMMapClickListener(new OnFMMapClickListener() {
            @Override
            public void onMapClick(float x, float y) {
                FMScreenCoord screenCoord = new FMScreenCoord(x, y);
                FMMapCoord point = fmMap.toFMMapCoord(1, screenCoord);
                lineLayer.removeAll();
                points.add(point);
                clicked++;
                Toast.makeText(getContext(), "X:" + point.x + "Y:" + point.y, Toast.LENGTH_SHORT).show();
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

        NotificationCenter.getInstance().register(new String[]{NotificationName.LOCATION_CHANGE}, new NotificationListener() {
            @Override
            public void handleNotification(Notification notification) {
                Beacon beacon = (Beacon) notification.getBody();
                getLocaltion(beacon.getBeacon_x(),beacon.getBeacon_y());
            }
        });
    }

    /**
     * 添加poi标注物。
     *
     * @param layer 图层
     * @param coord 标注物的地图坐标
     */
    private void addMarker(FMImageLayer layer, FMMapCoord coord) {
        FMImageMarker marker = new FMImageMarker(coord);                     //新建Marker对象
        FMImageMarkerStyle style = new FMImageMarkerStyle();                 //新建Marker的样式对象
        style.setImageFromAssets("marker.png");
        style.setFMNodeOffsetType(FMStyle.FMNodeOffsetType.FMNODE_MODEL_ABOVE);        //设置标注物的位置高度位于模型之上
        style.setWidth(100);                                                 //设置标注物的显示尺寸
        style.setHeight(100);
        marker.setStyle(style);                                              //设置样式

        //添加到图层
        layer.addMarker(marker);

        //更新地图
        fmMap.updateMap();
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
            final ArrayList<FMNaviResult> results = naviAnalyser.getNaviResults();


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
            fmMap.updateMap();                    //更新地图


        } else if (type == FMNaviAnalyser.FMRouteCalcuResult.ROUTE_FAILURE_NO_STAIR_FLOORS) {
            Toast.makeText(getContext(), "没有电梯或者扶梯进行跨层分析", Toast.LENGTH_SHORT).show();
        } else if (type == FMNaviAnalyser.FMRouteCalcuResult.ROUTE_FAILURE_NOTSUPPORT_FLOORS) {
            Toast.makeText(getContext(), "不支持跨层分析", Toast.LENGTH_SHORT).show();
        } else if (type == FMNaviAnalyser.FMRouteCalcuResult.ROUTE_PARAM_ERROR) {
            Toast.makeText(getContext(), "路径分析参数出错", Toast.LENGTH_SHORT).show();
        } else if (type == FMNaviAnalyser.FMRouteCalcuResult.ROUTE_FAILURE_TOO_CLOSE) {
            Toast.makeText(getContext(), "太近了", Toast.LENGTH_SHORT).show();
        } else if (type == FMNaviAnalyser.FMRouteCalcuResult.ROUTE_DATABASE_ERROR) {
            Toast.makeText(getContext(), "数据库出错", Toast.LENGTH_SHORT).show();
        } else if (type == FMNaviAnalyser.FMRouteCalcuResult.ROUTE_FAILURE_NO_END) {
            Toast.makeText(getContext(), "数据出错，终点不在其对应层的数据中", Toast.LENGTH_SHORT).show();
        } else if (type == FMNaviAnalyser.FMRouteCalcuResult.ROUTE_FAILURE_NO_START) {
            Toast.makeText(getContext(), "数据错误，起点不在其对应层的数据中", Toast.LENGTH_SHORT).show();
        } else if (type == FMNaviAnalyser.FMRouteCalcuResult.ROUTE_FAILURE_NO_FMDBKERNEL) {
            Toast.makeText(getContext(), "底层指针错误", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 更新标注物。
     *
     * @param marker 标注物
     */
    private void deleteMarker(FMImageMarker marker) {
        imageLayer.removeMarker(marker);
        fmMap.updateMap();
    }

    /**
     * 添加定位标注物。
     */
    private void addMarker() {
        FMLocationMarkerStyle style = new FMLocationMarkerStyle();
        style.setActiveImageFromAssets("active.png");
        style.setStaticImageFromAssets("static.png");

        FMMapCoord point = new FMMapCoord(12980372.654, 4855790.4, 0.0);
        marker = new FMLocationMarker(1, point, style);
        locLayer.addMarker(marker);
        fmMap.updateMap();
    }

    /**
     * 添加定位标注物。
     */
    public void getLocaltion(String x,String y) {
        FMLocationMarkerStyle style = new FMLocationMarkerStyle();
        style.setActiveImageFromAssets("active.png");
        style.setStaticImageFromAssets("static.png");
        FMMapCoord point = new FMMapCoord(Double.parseDouble(x), Double.parseDouble(y), 0.0);
        marker.setPosition(point);
        locLayer.updateMarker(marker);
        fmMap.updateMap();
    }


    public FMMap getFmMap() {
        return fmMap;
    }
}
