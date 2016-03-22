package com.delin.dgclient.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;


import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.delin.dgclient.R;
import com.delin.dgclient.model.exception.AppException;
import com.myideaway.easyapp.core.lib.L;
import com.myideaway.easyapp.core.lib.exception.RemoteServiceException;
import com.myideaway.easyapp.core.lib.service.BizService;
import com.myideaway.easyapp.core.lib.service.Service;



public abstract class BaseFragmentActivity extends FragmentActivity {

    private ProgressDialog progressDialog;
    private boolean dialogIsShowing = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
            showShortToast(getString(R.string.global_message_remote_app_error));
        } else {
            showShortToast(getString(R.string.global_message_unknown_error));
        }
    }
}
