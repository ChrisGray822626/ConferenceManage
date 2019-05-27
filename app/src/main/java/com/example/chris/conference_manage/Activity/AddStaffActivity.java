package com.example.chris.conference_manage.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.chris.conference_manage.R;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AddStaffActivity extends AppCompatActivity {

    private EditText idEdit;
    private EditText nameEdit;
    private EditText passwordEdit;
    private EditText telephoneEdit;
    private EditText rankEdit;
    private Button submit;

    private String id;
    private String name;
    private String password;
    private String telephone;
    private String rank;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_staff);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        idEdit = (EditText)findViewById(R.id.id);
        nameEdit = (EditText)findViewById(R.id.name);
        passwordEdit = (EditText)findViewById(R.id.password);
        telephoneEdit = (EditText)findViewById(R.id.telephone);
        rankEdit = (EditText)findViewById(R.id.rank);
        submit = (Button)findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                id = idEdit.getText().toString();
                name = nameEdit.getText().toString();
                password = passwordEdit.getText().toString();
                telephone = telephoneEdit.getText().toString();
                rank = rankEdit.getText().toString();
                postStaff();
            }
        });
    }

    private void postStaff() {
        new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        OkHttpClient client = new OkHttpClient();
                        RequestBody requestBody = new FormBody.Builder()
                                //.add("id",id)
                                .add("name",name )
                                .add("password",password )
                                .add("telephone",telephone )
                                .add("rank",rank )

                                .build();
                        Request request = new Request.Builder()
                                .post(requestBody)
                                .url("http://47.102.131.72:8080/staff")
                                .build();
                        Call call = client.newCall(request);
                        call.enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                //失败调用
                                Log.e("return", "onFailure: " );
                            }
                            @Override
                            public void onResponse(Call call, final Response response) throws IOException {
                                //成功调用
                                Log.e("return", "onResponse: " );
                                String responseData  = response.body().string();
                                Log.d("test",responseData);

                                Looper.prepare();
                                AlertDialog.Builder builder = new AlertDialog.Builder(AddStaffActivity.this);
                                builder.setTitle("添加信息");
                                builder.setMessage("ID：" + id
                                        + "\n姓名：" + name
                                        + "\n密码：" + password
                                        + "\n手机：" + telephone
                                        + "\n级别：" + rank
                                );
                                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        finish();
                                    }
                                });
                                builder.show();
                                Looper.loop();
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
    }
}
