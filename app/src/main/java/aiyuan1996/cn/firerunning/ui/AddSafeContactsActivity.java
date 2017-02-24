package aiyuan1996.cn.firerunning.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import aiyuan1996.cn.firerunning.R;
import aiyuan1996.cn.firerunning.Utils.ContactInfoParser;
import aiyuan1996.cn.firerunning.adapter.ContactAdapter;
import aiyuan1996.cn.firerunning.entity.ContactInfo;
import aiyuan1996.cn.firerunning.ui.view.ViewHolder;

public class AddSafeContactsActivity extends AppCompatActivity{
    private ListView mListview;
    private ContactAdapter adapter;
    private List<ContactInfo> systemContacts,contactInfoList;
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;

    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_add_safe_contacts);
        contactInfoList = new ArrayList<ContactInfo>();
        initView();
    }

    public void initView(){
        mListview = (ListView) findViewById(R.id.lv_contact);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
        } else {
            new Thread(){
                public void run() {
                    systemContacts = ContactInfoParser.getSystemContact(AddSafeContactsActivity.this);
                    systemContacts.addAll(ContactInfoParser.getSimContacts(AddSafeContactsActivity.this));
                    handler.sendEmptyMessage(10);
                };
            }.start();
            mListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    ContactInfo item = (ContactInfo) adapter.getItem(position);
                    ViewHolder viewHolder = (ViewHolder)view.getTag();
                    viewHolder.checkBox.toggle();
                    adapter.getIsSelected().put(position, viewHolder.checkBox.isChecked());
                    if(viewHolder.checkBox.isChecked() == true){
                        contactInfoList.add(item);
                    }else{
                        contactInfoList.remove(item);
                    }
                    setContactInfoList(contactInfoList);
                }
            });
        }
    }

    Handler handler = new Handler(){
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 10:
                    if(systemContacts != null){
                        adapter = new ContactAdapter(systemContacts, AddSafeContactsActivity.this);
                        mListview.setAdapter(adapter);
                    }
                    break;

                default:
                    break;
            }
        };
    };

    public void setContactInfoList(List<ContactInfo> contactInfoList) {
        this.contactInfoList = contactInfoList;
    }

    public List<ContactInfo> getContactInfoList() {
        return contactInfoList;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                initView();
            } else {
                Toast.makeText(this, "Until you grant the permission, we canot display the names", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
