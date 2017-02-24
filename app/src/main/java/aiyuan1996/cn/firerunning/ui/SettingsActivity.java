package aiyuan1996.cn.firerunning.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import aiyuan1996.cn.firerunning.R;
import aiyuan1996.cn.firerunning.ui.PushActivity.PushSetActivity;
import aiyuan1996.cn.firerunning.ui.UserActivity.UserManagerActivity;

public class SettingsActivity extends AppCompatActivity {
    private ListView listView;
    private String[] settings_item = {"推送设置","用户设置","添加安全联系人"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        listView = (ListView)findViewById(R.id.list_settings);
        ArrayAdapter<String>adapter = new ArrayAdapter<String>(SettingsActivity.this,
                android.R.layout.simple_list_item_1,settings_item);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent;
                if(position == 0){
                    intent = new Intent(getApplicationContext(), PushSetActivity.class);
                    startActivity(intent);
                }else if(position == 1){
                    intent = new Intent(getApplicationContext(),UserManagerActivity.class);
                    startActivity(intent);
                }else if(position == 2){
                    intent = new Intent(getApplicationContext(),AddSafeContactsActivity.class);
                    startActivity(intent);
                }

            }
        });
    }
}
