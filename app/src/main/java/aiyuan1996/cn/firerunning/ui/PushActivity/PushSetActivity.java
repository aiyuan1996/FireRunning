package aiyuan1996.cn.firerunning.ui.PushActivity;

import android.app.AlertDialog;
import android.app.Notification;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.LinkedHashSet;
import java.util.Set;

import aiyuan1996.cn.firerunning.R;
import aiyuan1996.cn.firerunning.Utils.PushUtil;
import cn.jpush.android.api.BasicPushNotificationBuilder;
import cn.jpush.android.api.CustomPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.MultiActionsNotificationBuilder;
import cn.jpush.android.api.TagAliasCallback;

import static aiyuan1996.cn.firerunning.R.mipmap.ic_launcher;

/**
 *Created by aiyuan on 2017/2/20
 */

public class PushSetActivity extends ActionBarActivity {
	private static final String TAG = "PushSetActivity";
	private ListView listView;
	private String[] settings_item = {"设置设备标签","设置设备别名","设置通知栏样式","设置推送时间"};
	private TextView tv_tag_alis;
	private EditText et_tag_alis;

	@Override
	public void onCreate(Bundle icicle) {
		Log.d(TAG, "进入PushSetActivity");
		super.onCreate(icicle);
		setContentView(R.layout.push_set_dialog);
		listView = (ListView)findViewById(R.id.list_push_set);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(PushSetActivity.this,
				android.R.layout.simple_list_item_1,settings_item);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				AlertDialog.Builder builder = new AlertDialog.Builder(PushSetActivity.this);
				LayoutInflater factory = LayoutInflater.from(PushSetActivity.this);
				View view_dialog = factory.inflate(R.layout.set_tag_alis, null);
				tv_tag_alis = (TextView)view_dialog.findViewById(R.id.tv_tag_alis);
				et_tag_alis = (EditText)view_dialog.findViewById(R.id.et_tag_alis);
				if(position == 0){

					Log.d(TAG, "onItemClick: ------->>>" + getResources().getString(R.string.tag_hint));
					tv_tag_alis.setText(getResources().getString(R.string.tag_hint));
					builder.setTitle("设置设备标签")
							.setView(view_dialog).setPositiveButton("确定", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									setTag();
								}
							})
							.setNegativeButton("取消", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									dialog.dismiss();
								}
							}).show();

				}else if(position == 1){
					tv_tag_alis.setText(getResources().getString(R.string.alias_hint));
					 builder.setTitle("设置设备别名")
							.setView(view_dialog).setPositiveButton("确定", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									setAlias();
								}
							})
							.setNegativeButton("取消", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									dialog.dismiss();
								}
							}).show();
				}else if(position == 2){
					String[] choices = {"Basic","Custom","AddActions"};
							AlertDialog dialog = new AlertDialog.Builder(PushSetActivity.this)
							.setIcon(android.R.drawable.btn_star)
							.setTitle("设置通知栏样式")
							.setItems(choices, new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									switch (which) {
										case 0:
											setStyleBasic();
											break;
										case 1:
											setStyleCustom();
											break;
										case 2:
											setAddActionsStyle();
											break;
									}
								}
							}).create();
					dialog.show();
				}else if(position == 3){
					Intent intent = new Intent(PushSetActivity.this,SettingTimeActivity.class);
					startActivity(intent);
				}

			}
		});

	}

	private void setTag() {
		String tag = et_tag_alis.getText().toString().trim();

		// 检查 tag 的有效性
		if (TextUtils.isEmpty(tag)) {
			Toast.makeText(PushSetActivity.this, R.string.error_tag_empty, Toast.LENGTH_SHORT).show();
			return;
		}

		// ","隔开的多个 转换成 Set
		String[] sArray = tag.split(",");
		Set<String> tagSet = new LinkedHashSet<String>();
		for (String sTagItme : sArray) {
			if (!PushUtil.isValidTagAndAlias(sTagItme)) {
				Toast.makeText(PushSetActivity.this, R.string.error_tag_gs_empty, Toast.LENGTH_SHORT).show();
				return;
			}
			tagSet.add(sTagItme);
		}

		//调用JPush API设置Tag
		mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_TAGS, tagSet));

	}

	private void setAlias() {
		String alias = et_tag_alis.getText().toString().trim();
		if (TextUtils.isEmpty(alias)) {
			Toast.makeText(PushSetActivity.this, R.string.error_alias_empty, Toast.LENGTH_SHORT).show();
			return;
		}
		if (!PushUtil.isValidTagAndAlias(alias)) {
			Toast.makeText(PushSetActivity.this, R.string.error_tag_gs_empty, Toast.LENGTH_SHORT).show();
			return;
		}

		//调用JPush API设置Alias
		mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, alias));
	}


	/**
	 * 设置通知提示方式 - 基础属性
	 */
	private void setStyleBasic() {
		BasicPushNotificationBuilder builder = new BasicPushNotificationBuilder(PushSetActivity.this);
		builder.statusBarDrawable = ic_launcher;
		builder.notificationFlags = Notification.FLAG_AUTO_CANCEL;  //设置为点击后自动消失
		builder.notificationDefaults = Notification.DEFAULT_SOUND;  //设置为铃声（ Notification.DEFAULT_SOUND）或者震动（ Notification.DEFAULT_VIBRATE）
		JPushInterface.setPushNotificationBuilder(1, builder);
		Toast.makeText(PushSetActivity.this, "Basic Builder - 1", Toast.LENGTH_SHORT).show();
	}


	/**
	 * 设置通知栏样式 - 定义通知栏Layout
	 */
	private void setStyleCustom() {
		CustomPushNotificationBuilder builder = new CustomPushNotificationBuilder(PushSetActivity.this, R.layout.customer_notitfication_layout, R.id.icon, R.id.title, R.id.text);
		builder.layoutIconDrawable = R.mipmap.ic_launcher;
		builder.developerArg0 = "developerArg2";
		JPushInterface.setPushNotificationBuilder(2, builder);
		Toast.makeText(PushSetActivity.this, "Custom Builder - 2", Toast.LENGTH_SHORT).show();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	private void setAddActionsStyle() {
		MultiActionsNotificationBuilder builder = new MultiActionsNotificationBuilder(PushSetActivity.this);
		builder.addJPushAction(R.drawable.jpush_ic_richpush_actionbar_back, "first", "my_extra1");
		builder.addJPushAction(R.drawable.jpush_ic_richpush_actionbar_back, "second", "my_extra2");
		builder.addJPushAction(R.drawable.jpush_ic_richpush_actionbar_back, "third", "my_extra3");
		JPushInterface.setPushNotificationBuilder(10, builder);

		Toast.makeText(PushSetActivity.this, "AddActions Builder - 10", Toast.LENGTH_SHORT).show();
	}


	private final TagAliasCallback mAliasCallback = new TagAliasCallback() {

		@Override
		public void gotResult(int code, String alias, Set<String> tags) {
			String logs;
			switch (code) {
				case 0:
					logs = "Set tag and alias success";
					Log.i(TAG, logs);
					break;

				case 6002:
					logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
					Log.i(TAG, logs);
					if (PushUtil.isConnected(getApplicationContext())) {
						mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 60);
					} else {
						Log.i(TAG, "No network");
					}
					break;

				default:
					logs = "Failed with errorCode = " + code;
					Log.e(TAG, logs);
			}

			PushUtil.showToast(logs, getApplicationContext());
		}

	};

	private final TagAliasCallback mTagsCallback = new TagAliasCallback() {

		@Override
		public void gotResult(int code, String alias, Set<String> tags) {
			String logs;
			switch (code) {
				case 0:
					logs = "Set tag and alias success";
					Log.i(TAG, logs);
					break;

				case 6002:
					logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
					Log.i(TAG, logs);
					if (PushUtil.isConnected(getApplicationContext())) {
						mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_TAGS, tags), 1000 * 60);
					} else {
						Log.i(TAG, "No network");
					}
					break;

				default:
					logs = "Failed with errorCode = " + code;
					Log.e(TAG, logs);
			}

			PushUtil.showToast(logs, getApplicationContext());
		}

	};

	private static final int MSG_SET_ALIAS = 1001;
	private static final int MSG_SET_TAGS = 1002;


	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
				case MSG_SET_ALIAS:
					Log.d(TAG, "Set alias in handler.");
					JPushInterface.setAliasAndTags(getApplicationContext(), (String) msg.obj, null, mAliasCallback);
					break;

				case MSG_SET_TAGS:
					Log.d(TAG, "Set tags in handler.");
					JPushInterface.setAliasAndTags(getApplicationContext(), null, (Set<String>) msg.obj, mTagsCallback);
					break;

				default:
					Log.i(TAG, "Unhandled msg - " + msg.what);
			}
		}
	};

}