package com.example.chris.conference_manage.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;

import com.example.chris.conference_manage.R;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AddRoomActivity extends AppCompatActivity {

    private Spinner peopleSpinner;
    private Spinner computerSpinner;
    private Spinner projectorSpinner;
    private Spinner microphoneSpinner;
    private Button submit;
    private String people;
    private String computer;
    private String projector;
    private String microphone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_room);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        peopleSpinner = (Spinner)findViewById(R.id.people_spinner);
        computerSpinner = (Spinner)findViewById(R.id.computer_spinner);
        projectorSpinner = (Spinner)findViewById(R.id.projector_spinner);
        microphoneSpinner = (Spinner)findViewById(R.id.microphone_spinner);
        submit = (Button)findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postRoom();
            }
        });

        peopleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                people = (String) peopleSpinner.getSelectedItem();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

        computerSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent,View view,int position, long id) {
                computer = ss_ss((String) computerSpinner.getSelectedItem());
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

        projectorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent,View view,int position, long id) {
                projector = ss_ss((String) projectorSpinner.getSelectedItem());
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

        microphoneSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent,View view,int position, long id) {
                microphone = ss_ss((String) microphoneSpinner.getSelectedItem());
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

    }

    private void postRoom() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String conditions = "0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000";
                    OkHttpClient client = new OkHttpClient();
                    RequestBody requestBody = new FormBody.Builder()
                            .add("people",people)
                            .add("projector",projector )
                            .add("computer",computer )
                            .add("microphone",microphone )
                            .add("conditions",conditions )

                            .build();
                    Request request = new Request.Builder()
                            .post(requestBody)
                            .url("http://47.102.131.72:8080/room")
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
                            AlertDialog.Builder builder = new AlertDialog.Builder(AddRoomActivity.this);
                            builder.setTitle("添加信息");
                            builder.setMessage("房间号：" + responseData
                                            + "\n人数：" + people
                                            + "\n是否有电脑：" + computer
                                            + "\n是否有投影仪：" + projector
                                            + "\n是否有麦克风：" + microphone
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

    private String ss_ss(String ss){
        if(ss.equals("是"))
            return "1";
        else
            return "0";
    }
}
