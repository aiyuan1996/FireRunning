package aiyuan1996.cn.firerunning.ui.UserActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import aiyuan1996.cn.firerunning.R;

public class UserManagerActivity extends AppCompatActivity {
    private ListView listView;
    private String[] user_manage_item = {"更改密码","注销登陆"};
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_manage);
        listView = (ListView)findViewById(R.id.user_manage);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(UserManagerActivity.this,
                android.R.layout.simple_list_item_1,user_manage_item);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    intent = new Intent(UserManagerActivity.this,ChangePasswordActivity.class);
                    startActivity(intent);
                }else if (position == 1){
                    intent = new Intent(UserManagerActivity.this,LoginActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
}
