package aiyuan1996.cn.firerunning.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.dd.processbutton.iml.ActionProcessButton;

import aiyuan1996.cn.firerunning.R;
import aiyuan1996.cn.firerunning.Utils.ProgressGenerator;

public class LoginActivity extends Activity implements ProgressGenerator.OnCompleteListener {

    private static final String TAG = "LoginActivity";
    public static final String EXTRAS_ENDLESS_MODE = "EXTRAS_ENDLESS_MODE";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: LoginActivity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText editEmail = (EditText) findViewById(R.id.editEmail);
        final EditText editPassword = (EditText) findViewById(R.id.editPassword);

        final ProgressGenerator progressGenerator = new ProgressGenerator(this);
        final ActionProcessButton btnSignIn = (ActionProcessButton) findViewById(R.id.btnSignIn);
        Bundle extras = getIntent().getExtras();
        if(extras != null && extras.getBoolean(EXTRAS_ENDLESS_MODE)) {
            btnSignIn.setMode(ActionProcessButton.Mode.ENDLESS);
        } else {
            btnSignIn.setMode(ActionProcessButton.Mode.PROGRESS);
        }
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressGenerator.start(btnSignIn);
                editEmail.setEnabled(false);
                editPassword.setEnabled(false);
            }
        });
    }

    @Override
    public void onComplete() {
        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
        startActivity(intent);
    }

}