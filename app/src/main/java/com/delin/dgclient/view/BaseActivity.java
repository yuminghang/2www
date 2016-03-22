package com.delin.dgclient.view;



import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.delin.dgclient.ApplicationContext;
import com.delin.dgclient.MainApplication;
import com.delin.dgclient.model.exception.AppException;

import com.delin.dgclient.service.CouponsBSGetPush;
import com.myideaway.easyapp.core.lib.L;
import com.myideaway.easyapp.core.lib.exception.RemoteServiceException;
import com.myideaway.easyapp.core.lib.service.BizService;
import com.myideaway.easyapp.core.lib.service.Service;




public abstract class BaseActivity extends Activity {
    private ProgressDialog progressDialog;
    private boolean dialogIsShowing = false;
    public static int REQUEST_ENABLE_BT = 1;//
    public MainApplication mainApplication ;
    public ApplicationContext applicationContext;


    protected LayoutInflater layoutInflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layoutInflater = LayoutInflater.from(getBaseContext());
        mainApplication = (MainApplication) getApplication();
        applicationContext = ApplicationContext.getInstance();

    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }


    public void showProgressDialog(Context context, String msg) {
        if (context != null&&!dialogIsShowing) {
            progressDialog = new ProgressDialog(context, ProgressDialog.THEME_HOLO_LIGHT);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage(msg);
            progressDialog.setCancelable(false);
            progressDialog.show();
            dialogIsShowing = true;
        }

    }


    public void dismissProgressDialog(){
        dialogIsShowing=false;
        progressDialog.dismiss();
    }

    public void showShortToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * 运行BizService
     *
     * @param bizService
     * @param onCompleteHandler
     * @param onSuccessHandler
     * @param onFaultHandler
     */
    protected void runBizService(BizService bizService, Service.OnCompleteHandler onCompleteHandler, Service.OnSuccessHandler onSuccessHandler, final Service.OnFaultHandler onFaultHandler) {

        bizService.asyncExecute(onCompleteHandler, onSuccessHandler, new Service.OnFaultHandler() {
            @Override
            public void onFault(Service service, Exception e) {
                handleException(e);

                if (onFaultHandler != null) {
                    onFaultHandler.onFault(service, e);
                }
            }
        });
    }

    /**
     * 运行BizService
     *
     * @param bizService
     * @param onCompleteHandler
     * @param onSuccessHandler
     */
    protected void runBizService(BizService bizService, Service.OnCompleteHandler onCompleteHandler, Service.OnSuccessHandler onSuccessHandler) {
        runBizService(bizService, onCompleteHandler, onSuccessHandler, null);
    }

    /**
     * 运行BizService
     *
     * @param bizService
     * @param onCompleteHandler
     */
    protected void runBizService(BizService bizService, Service.OnCompleteHandler onCompleteHandler) {
        runBizService(bizService, onCompleteHandler, null, null);
    }
    /**
     * 运行BizService
     *
     * @param bizService
     * @param onSuccessHandler
     */
    protected void runBizService(BizService bizService, Service.OnSuccessHandler onSuccessHandler){
        runBizService(bizService, null, onSuccessHandler, null);
    }

    /**
     * 运行BizService
     *
     * @param bizService
     */
    protected void runBizService(BizService bizService) {
        runBizService(bizService, null, null, null);
    }

    /**
     * 统一对异常进行处理提示
     *
     * @param exception
     */
    protected void handleException(Exception exception) {
        L.e(exception.getMessage(), exception);
        if (exception instanceof RemoteServiceException) {
            showShortToast("网络连接异常，请稍后重试");
        } else if (exception instanceof AppException) {
            showShortToast("程序出错");
        } else {
            showShortToast("未知错误");
        }
    }



    /**
     * 打开蓝牙启动云子SDK
     */
    public void initSensoro(Context context,String storeId){
        if(!mainApplication.isBluetoothEnabled()){
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, REQUEST_ENABLE_BT);
        }else{
            mainApplication.startSensoroSDK();
            Intent intent = new Intent(context,MapActivity.class);
            intent.putExtra("storeId",storeId);
            startActivity(intent);
        }
    }
    /**
     * 判断设备是否支持蓝牙4.0
     * @return boolean 支持返回true，否则返回false
     */
    public boolean isBluetooth4(){
        boolean isBluetooth4 = true;
        if(!this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)){
            isBluetooth4 = false;
        }

        return isBluetooth4;
    }
}
