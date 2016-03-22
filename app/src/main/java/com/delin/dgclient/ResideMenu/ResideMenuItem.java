package com.delin.dgclient.ResideMenu;



import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.delin.dgclient.R;
import com.delin.dgclient.notification.Notification;
import com.delin.dgclient.notification.NotificationCenter;
import com.delin.dgclient.notification.NotificationListener;
import com.delin.dgclient.notification.NotificationName;
import com.delin.dgclient.view.CustomImageView;


/**
 * User: special Date: 13-12-10 Time: 下午11:05 Mail: specialcyci@gmail.com
 */
public class ResideMenuItem extends LinearLayout {


	/** menu item title */
	public static LinearLayout serviceLinearLayout;
	public static LinearLayout aboutUsLinearLayout;
	public static LinearLayout loginLinearLayout;
	public static CustomImageView headPicImageView;
	public static LinearLayout userInforLinearLayout;
	public static Button loginButton;
	public static Button registerButton;
	public static TextView userNameTextView;
	public static LinearLayout couponLinearLayout;
	public static LinearLayout shareLinearLayout;
	public static LinearLayout settingLinearLayout;

	public ResideMenuItem(Context context) {
		super(context);
		initViews(context);

	}


	private void initViews(Context context) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.residemenu_item, this);

		aboutUsLinearLayout = (LinearLayout) findViewById(R.id.aboutUsLinearLayout);
		serviceLinearLayout = (LinearLayout) findViewById(R.id.serviceUsLinearLayout);
		loginLinearLayout = (LinearLayout) findViewById(R.id.loginLinearLayout);
		loginButton = (Button) findViewById(R.id.loginButton);
		registerButton  = (Button) findViewById(R.id.registerButton);
		headPicImageView = (CustomImageView) findViewById(R.id.headPicImageView);
		userInforLinearLayout = (LinearLayout) findViewById(R.id.userInforLinearLayout);
		userNameTextView = (TextView) findViewById(R.id.userNameTextView);
		couponLinearLayout = (LinearLayout) findViewById(R.id.couponLinearLayout);
		shareLinearLayout = (LinearLayout) findViewById(R.id.shareLinearLayout);
		settingLinearLayout = (LinearLayout) findViewById(R.id.settingLinearLayout);
		NotificationCenter.getInstance().register(new String[]{NotificationName.HEAD_PHOTO_CHANGE}, new NotificationListener() {
			@Override
			public void handleNotification(Notification notification) {

				headPicImageView.setImageBitmap((Bitmap) notification.getBody());
			}
		});
	}

}
