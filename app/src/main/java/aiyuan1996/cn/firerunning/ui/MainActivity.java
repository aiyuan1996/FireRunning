package aiyuan1996.cn.firerunning.ui;

import android.Manifest;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import aiyuan1996.cn.firerunning.R;
import aiyuan1996.cn.firerunning.Utils.PushUtil;
import aiyuan1996.cn.firerunning.Utils.ToastUtils;
import aiyuan1996.cn.firerunning.entity.UserEntity;
import aiyuan1996.cn.firerunning.ui.PushActivity.PushSetActivity;
import aiyuan1996.cn.firerunning.ui.PushActivity.webViewActivity;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by aiyuan on 2017/2/20
 */
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public static boolean isForeground = false;
    private static final String TAG = "MainActivity";
    private android.support.v7.app.AlertDialog photoDialog;
    public static final String PHOTO_IMAGE_FILE_NAME = "fileImg.jpg";
    public static final int CAMERA_REQUEST_CODE = 100;
    public static final int IMAGE_REQUEST_CODE = 101;
    public static final int RESULT_REQUEST_CODE = 102;
    private static final int REQUEST_BLUETOOTH_PERMISSION = 10;
    private File tempFile = null;
    CircleImageView userImage;
    TextView tel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();



        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View navHeaderView = navigationView.inflateHeaderView(R.layout.nav_header_main);
        userImage = (CircleImageView)navHeaderView.findViewById(R.id.profile_image);
        String telString = getIntent().getStringExtra("tel");
        tel = (TextView)navHeaderView.findViewById(R.id.tel);
        tel.setText(telString);
        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(R.string.dialog_title).setMessage(R.string.dialog_message);
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainActivity.this,"从服务器获取地图",Toast.LENGTH_SHORT).show();
            }
        });
        builder.setCancelable(false);
        builder.create().show();
        initview();
        registerMessageReceiver();  // used for receive msg
    }

    private void initview() {
        if (BmobUser.getCurrentUser()!=null) {
            Log.d(TAG, "BmobUser.getCurrentUser()!=null");
            UserEntity userEntity = BmobUser.getCurrentUser(UserEntity.class);
            if (userEntity.getAvatar()!=null) {
                Log.d(TAG, "userEntity.getAvatar()!=null");

                if(userEntity.getAvatar().getFileUrl() != null){
                    Log.d(TAG, "图片不为空");
                    Glide.with(MainActivity.this).load(userEntity.getAvatar().getFileUrl()).into(userImage);
                }else{
                    Log.d(TAG, "图片为空");
                }
            }
        }
    }

    /**
     * 点击头像的提示对话框
     */
    private void showDialog() {
        photoDialog = new android.support.v7.app.AlertDialog.Builder(MainActivity.this).create();
        photoDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        photoDialog.show();
        Window window = photoDialog.getWindow();
        window.setContentView(R.layout.dialog_photo); // 修改整个dialog窗口的显示
        window.setGravity(Gravity.BOTTOM);

        WindowManager.LayoutParams lp = photoDialog.getWindow().getAttributes();
        DisplayMetrics dm = new DisplayMetrics();
        window.getWindowManager().getDefaultDisplay().getMetrics(dm);
        lp.width = dm.widthPixels;
        photoDialog.getWindow().setAttributes(lp); // 设置宽度

        photoDialog.findViewById(R.id.btn_camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toCamera();
            }
        });
        photoDialog.findViewById(R.id.btn_picture).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toPicture();
            }
        });
        photoDialog.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                photoDialog.dismiss();
            }
        });
    }

    /**
     * 跳转相机
     */
    public void toCamera() {
        requestWESPermission(); // 安卓6.0以上需要申请权限
        photoDialog.dismiss();
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); // 调用系统的拍照功能
        // 判断内存卡是否可用，可用的话就进行储存
        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                Uri.fromFile(new File(Environment.getExternalStorageDirectory(), PHOTO_IMAGE_FILE_NAME)));
        startActivityForResult(intent, CAMERA_REQUEST_CODE);
    }

    /**
     * 跳转相册
     */
    private void toPicture() {
        photoDialog.dismiss();
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, IMAGE_REQUEST_CODE);
    }

    /**
     * 动态申请权限
     */
    private void requestWESPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                // 判断是否需要 向用户解释，为什么要申请该权限
                if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE))
                    ToastUtils.showShort(MainActivity.this,"Need write external storage permission.");
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_BLUETOOTH_PERMISSION);
                return;
            } else {
            }
        } else {
        }
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }
    @Override
    protected void onResume() {
        isForeground = true;
        super.onResume();
    }


    @Override
    protected void onPause() {
        isForeground = false;
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case IMAGE_REQUEST_CODE: // 相册数据
                if (data != null) {
                    startPhotoZoom(data.getData());
                }
                break;
            case CAMERA_REQUEST_CODE: // 相机数据
                tempFile = new File(Environment.getExternalStorageDirectory(), PHOTO_IMAGE_FILE_NAME);
                startPhotoZoom(Uri.fromFile(tempFile));
                break;
            case RESULT_REQUEST_CODE: // 有可能点击舍弃
                if (data != null) {
                    // 拿到图片设置, 然后需要删除tempFile
                    setImageToView(data);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 设置icon并上传服务器
     * @param data
     */
    private void setImageToView(Intent data) {
        Bundle bundle = data.getExtras();
        if (bundle != null) {
            final Bitmap bitmap = bundle.getParcelable("data");
            final BmobFile bmobFile = new BmobFile(bitmapToFile(bitmap));

            bmobFile.uploadblock(new UploadFileListener() {
                @Override
                public void done(BmobException e) {
                    if(e==null) {
                        // 此时上传成功
                        UserEntity userEntity = new UserEntity();
                        userEntity.setAvatar(bmobFile);// 获取文件并赋值给实体类
                        BmobUser bmobUser = BmobUser.getCurrentUser();
                        userEntity.update(bmobUser.getObjectId(), new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    userImage.setImageBitmap(bitmap);
                                    ToastUtils.showShort(MainActivity.this, getString(R.string.avatar_editor_success));
                                } else {
                                    ToastUtils.showShort(MainActivity.this, getString(R.string.avatar_editor_failure));
                                }
                            }
                        });
                    } else {
                        ToastUtils.showShort(MainActivity.this, getString(R.string.avatar_editor_failure));
                    }
                    // 既然已经设置了图片，我们原先的就应该删除
                    if (tempFile != null) {
                        tempFile.delete();
                        //LogUtils.i("JAVA", "tempFile已删除");
                    }
                }
                @Override
                public void onProgress(Integer value) {
                    // 返回的上传进度（百分比）
                }
            });
        }
    }

    /**
     * Bitmap转File
     */
    public File bitmapToFile(Bitmap bitmap) {
        tempFile = new File(Environment.getExternalStorageDirectory(), PHOTO_IMAGE_FILE_NAME);
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(tempFile));
            if (bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos)) {
                bos.flush();
                bos.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tempFile;
    }

    /**
     * 裁剪
     * @param uri
     */
    private void startPhotoZoom(Uri uri) {
        if (uri == null) {
            //LogUtils.e("JAVA", "裁剪uri == null");
            return;
        }
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // 裁剪宽高比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // 裁剪图片的质量
        intent.putExtra("outputX", 320);
        intent.putExtra("outputY", 320);
        // 发送数据
        intent.putExtra("return-data", true);
        startActivityForResult(intent, RESULT_REQUEST_CODE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
            //Toast.makeText(this,"nav_camera",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(),webViewActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_gallery) {
            Toast.makeText(this,"nav_gallery",Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_push) {
            Log.d(TAG, "消息推送1");
            Intent intent = new Intent(getApplicationContext(), PushSetActivity.class);
            Log.d(TAG, "消息推送2");
            this.startActivity(intent);
            Log.d(TAG, "消息推送3");
        } else if (id == R.id.nav_manage) {
            Toast.makeText(this,"nav_manage",Toast.LENGTH_SHORT).show();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //for receive customer msg from jpush server
    private MessageReceiver mMessageReceiver;
    public static final String MESSAGE_RECEIVED_ACTION = "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";

    public void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MESSAGE_RECEIVED_ACTION);
        registerReceiver(mMessageReceiver, filter);
    }

    public class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
                String messge = intent.getStringExtra(KEY_MESSAGE);
                String extras = intent.getStringExtra(KEY_EXTRAS);
                StringBuilder showMsg = new StringBuilder();
                showMsg.append(KEY_MESSAGE + " : " + messge + "\n");
                if (!PushUtil.isEmpty(extras)) {
                    showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
                }
                new  android.support.v7.app.AlertDialog.Builder(MainActivity.this).
                        setTitle("您的专属消息").setMessage(messge).setPositiveButton("确定" ,  null)
                        .setCancelable(false).show();
            }
        }
    }


}
