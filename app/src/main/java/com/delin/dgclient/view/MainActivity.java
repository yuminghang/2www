package com.delin.dgclient.view;



import android.content.DialogInterface;
import android.content.Intent;



import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.delin.dgclient.ApplicationContext;
import com.delin.dgclient.R;
import com.delin.dgclient.ResideMenu.ResideMenu;
import com.delin.dgclient.ResideMenu.ResideMenuItem;
import com.delin.dgclient.api.Config;
import com.delin.dgclient.notification.Notification;
import com.delin.dgclient.notification.NotificationCenter;
import com.delin.dgclient.notification.NotificationListener;
import com.delin.dgclient.notification.NotificationName;
import com.delin.dgclient.service.PositionAccuracyBSLoad;
import com.delin.dgclient.service.UserBSLoadLogin;
import com.delin.dgclient.service.UserBSLogin;
import com.delin.dgclient.service.UserBSSaveLogin;
import com.google.gson.internal.LinkedTreeMap;
import com.myideaway.easyapp.core.lib.BizCookie;
import com.myideaway.easyapp.core.lib.service.Service;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import cn.jpush.android.api.JPushInterface;


public class MainActivity extends BaseFragmentActivity implements
        View.OnClickListener {

    private ResideMenu resideMenu;

    private ResideMenuItem itemLogin;


    private RadioButton mapRadioButton, couponsRadioButton;

    private boolean is_closed = true;
    private long mExitTime;

    private ImageView splash;
    private Button leftMenu;
    private TextView titleTextView;
    private String userName = "立即登录";
    private String imageURL;
    private String mobile;
    private DisplayImageOptions options;
    private ApplicationContext applicationContext = ApplicationContext.getInstance();
    public static boolean isForeground = false;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        splash = (ImageView) findViewById(R.id.splash);
        splash.setVisibility(View.GONE);

        titleTextView = (TextView) findViewById(R.id.titleTextView);
        loadUserSetting();
        setUpMenu();
        changeFragment(new MapTab());
        setListener();
        loadToken();
        NotificationCenter.getInstance().register(new String[]{NotificationName.LOGIN_SUCCESS}, new NotificationListener() {
            @Override
            public void handleNotification(Notification notification) {
                LinkedTreeMap msg = (LinkedTreeMap) notification.getBody();
                if (msg != null) {
                    userName = (String) msg.get("name");
                    imageURL = (String) msg.get("avatar");
                    mobile = (String) msg.get("mobile");
                    if (!is_closed) {
                        is_closed = true;
                        resideMenu.closeMenu();
                    }
                    setListener();
                }
            }
        });
        NotificationCenter.getInstance().register(new String[]{NotificationName.LOGOUT}, new NotificationListener() {
            @Override
            public void handleNotification(Notification notification) {
                userName = "立即登录";
                imageURL = null;
                mobile = null;
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                setListener();
            }
        });
    }


    @SuppressWarnings("deprecation")
    private void setUpMenu() {
        leftMenu = (Button) findViewById(R.id.headPicButton);

        mapRadioButton = (RadioButton) findViewById(R.id.mapRadioButton);
        couponsRadioButton = (RadioButton) findViewById(R.id.couponsRadioButton);

        resideMenu = new ResideMenu(this);
        resideMenu.setBackground(R.mipmap.img_frame_background);
        resideMenu.attachToActivity(this);
        resideMenu.setMenuListener(menuListener);

        resideMenu.setScaleValue(0.5f);
        // 禁止使用右侧菜单
        resideMenu.setDirectionDisable(ResideMenu.DIRECTION_RIGHT);

        itemLogin = new ResideMenuItem(this);
        resideMenu.addMenuItem(itemLogin, ResideMenu.DIRECTION_LEFT);
    }

    private void setListener() {


        mapRadioButton.setOnClickListener(this);
        couponsRadioButton.setOnClickListener(this);
        itemLogin.aboutUsLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                intent.putExtra("flog", "关于我们");
                startActivity(intent);
            }
        });

        itemLogin.serviceLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                intent.putExtra("flog", "服务条款");
                startActivity(intent);
            }
        });
        itemLogin.settingLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                intent.putExtra("flog", "设置");
                startActivity(intent);
            }
        });
        itemLogin.shareLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ShareActivity.class);
                startActivity(intent);
            }
        });
        itemLogin.couponLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                intent.putExtra("flog", "优惠券");
                startActivity(intent);
            }
        });

        itemLogin.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        itemLogin.registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });


        itemLogin.userInforLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userName.equals("立即登录")) {
                    new MassageDialog.Builder(MainActivity.this)
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
                                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                }
                            }).create().show();
                } else {
                    Intent intent = new Intent(MainActivity.this, UserInforActivity.class);
                    intent.putExtra("userName", userName);
                    intent.putExtra("mobile", mobile);
                    intent.putExtra("imageURL", imageURL);
                    startActivity(intent);
                }


            }
        });


        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.bg_girl) // resource or drawable
                .showImageForEmptyUri(R.mipmap.bg_girl) // resource or drawable
                .showImageOnFail(R.mipmap.bg_girl) // resource or drawable
                .cacheInMemory(true) // default
                .cacheOnDisk(false) // default
                .cacheInMemory(true)//设置下载的图片是否缓存在内存中
                .considerExifParams(true)
                .build();
        if (imageURL != null) {
            ImageLoader.getInstance().displayImage(Config.URL_SERVER_HEAD_PIC + imageURL,
                    itemLogin.headPicImageView, options);
        }
        if (!userName.equals("立即登录")) {
            itemLogin.loginLinearLayout.setVisibility(View.GONE);
            itemLogin.userNameTextView.setVisibility(View.VISIBLE);
            itemLogin.userNameTextView.setText(userName);
        }
        leftMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return resideMenu.dispatchTouchEvent(ev);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.mapRadioButton) {
            titleTextView.setText("INDOGO");
            changeFragment(new MapTab());
        } else if (view.getId() == R.id.couponsRadioButton) {
            titleTextView.setText("优惠券");
            changeFragment(new CouponsTab());
        }
    }

    private ResideMenu.OnMenuListener menuListener = new ResideMenu.OnMenuListener() {
        @Override
        public void openMenu() {
            is_closed = false;
            leftMenu.setVisibility(View.GONE);
        }

        @Override
        public void closeMenu() {
            is_closed = true;
            leftMenu.setVisibility(View.VISIBLE);
        }
    };

    private void changeFragment(Fragment targetFragment) {
        resideMenu.clearIgnoredViewList();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_fragment, targetFragment, "fragment")
                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    // What good method is to access resideMenu？
    public ResideMenu getResideMenu() {
        return resideMenu;
    }

    // 监听手机上的BACK键
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // 判断菜单是否关闭
            if (is_closed) {
                // 判断两次点击的时间间隔（默认设置为2秒）
                if ((System.currentTimeMillis() - mExitTime) > 2000) {
                    Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();

                    mExitTime = System.currentTimeMillis();
                } else {
                    finish();
                    System.exit(0);
                    super.onBackPressed();
                }
            } else {
                resideMenu.closeMenu();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 读取本地存储的Token值
     */

    private void loadToken() {
        UserBSLoadLogin userBSLoadLogin = new UserBSLoadLogin(this);
        try {
            UserBSLoadLogin.ServiceResult serviceResult = (UserBSLoadLogin.ServiceResult) userBSLoadLogin.syncExecute();
            HashMap serviceResultData = (HashMap) serviceResult.getData();
            if (serviceResultData != null) {
                autoLogin(serviceResultData);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 将token存储到本地
     *
     * @param token
     */

    private void saveToken(String token) {
        UserBSSaveLogin userBSSaveLogin = new UserBSSaveLogin(getBaseContext());
        userBSSaveLogin.setToken(token);
        runBizService(userBSSaveLogin);
    }


    private void loadUserSetting() {
        PositionAccuracyBSLoad positionAccuracyBSLoad = new PositionAccuracyBSLoad(MainActivity.this);
        runBizService(positionAccuracyBSLoad, new Service.OnCompleteHandler() {
            @Override
            public void onComplete(Service service) {

            }
        }, new Service.OnSuccessHandler() {
            @Override
            public void onSuccess(Service service, Object o) {
                PositionAccuracyBSLoad.ServiceResult serviceResult = (PositionAccuracyBSLoad.ServiceResult) o;
                HashMap hashMap = (HashMap) serviceResult.getData();
                if (hashMap != null) {
                    boolean canGetPush = (Boolean) hashMap.get("canGetPush");
                    double positioningAccuracy = (Double) hashMap.get("positionAccuracy");
                    applicationContext.setCanGetPush(canGetPush);
                    applicationContext.setPositioningAccuracy(positioningAccuracy);
                }
            }
        });
    }

    private void autoLogin(HashMap token) {
        showProgressDialog(this, "处理中...");
        UserBSLogin userBSLogin = new UserBSLogin(this);
        userBSLogin.setToken(token);
        runBizService(userBSLogin, new Service.OnCompleteHandler() {
            @Override
            public void onComplete(Service service) {
                dismissProgressDialog();
            }
        }, new Service.OnSuccessHandler() {
            @Override
            public void onSuccess(Service service, Object o) {

                UserBSLogin.ServiceResult serviceResult = (UserBSLogin.ServiceResult) o;
                HashMap hashMap = serviceResult.getHashMap();
                if (hashMap.size() != 0 && hashMap.get("status").equals("success")) {
                    LinkedTreeMap linkedTreeMap = (LinkedTreeMap) hashMap.get("msg");
                    String token = (String) linkedTreeMap.get("token");
                    ApplicationContext.getInstance().setToken(token);
                    String cookie = BizCookie.initBizCookie().getCookie();
                    ApplicationContext.getInstance().setCookie(cookie);
                    saveToken(token);
                    NotificationCenter.getInstance().sendNotification(NotificationName.LOGIN_SUCCESS, linkedTreeMap);

                } else {
                    if (hashMap.size() == 0) {
                        showShortToast("服务器异常");
                    } else {
                        showShortToast((String) hashMap.get("msg"));
                    }

                }
            }
        });

    }

    @Override
    protected void onResume() {
        isForeground = true;
        JPushInterface.onResume(this);
        super.onResume();
    }


    @Override
    protected void onPause() {
        isForeground = false;
        JPushInterface.onPause(this);
        super.onPause();
    }

}