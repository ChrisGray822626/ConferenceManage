package com.example.chris.conference_manage.Activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chris.conference_manage.Adapter.OrderStaffAdapter;
import com.example.chris.conference_manage.Class.Info;
import com.example.chris.conference_manage.Class.Order;
import com.example.chris.conference_manage.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.app.AlertDialog.THEME_HOLO_DARK;


public class MenuStaffActivity extends AppCompatActivity {

    private List<Order>outstandingOrderList = new ArrayList<>();
    private List<Order>completedOrderList = new ArrayList<>();
    private List<Info> infoList = new ArrayList<>();
    private LinearLayout orderInfoInterface;
    private LinearLayout orderReserveInterface;
    private LinearLayout personalInfoInterface;
    private LinearLayoutManager linearLayoutManager;
    private LinearLayoutManager linearLayoutManager1;
    private Spinner peopleSpinner;
    private Spinner startHourSpinner;
    private Spinner startMinSpinner;
    private Spinner endHourSpinner;
    private Spinner endMinSpinner;
    private Spinner computerSpinner;
    private Spinner projectorSpinner;
    private Spinner microphoneSpinner;
    private Spinner airSpinner;
    private RecyclerView outstandingOrderRecyclerView;
    private RecyclerView completedOrderRecyclerView;
    private ImageView accountPhoto;
    private ImageView reserveImg;
    private ImageView orderImg;
    private ImageView accountImg;
    private Button exit;
    private Button chooseDate;
    private Button submint;
    private TextView accountName;
    private TextView accountId;
    private LinearLayout goto_order_info;
    private LinearLayout goto_order_reserve;
    private LinearLayout goto_personal_info;
    private EditText setTopic;
    private Calendar calendar = Calendar.getInstance();

    private String id;
    private String name;
    private String people;
    private String topic;
    private Integer date_year = 0;
    private Integer date_mon;
    private Integer date_day;
    private String start_time;
    private String start_hour;
    private String start_min;
    private String end_time;
    private String end_hour;
    private String end_min;
    private String computer;
    private String projector;
    private String microphone;
    private String air;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_staff);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        orderInfoInterface =(LinearLayout)findViewById(R.id.order_info_interface);
        orderReserveInterface =(LinearLayout)findViewById(R.id.order_reserve_interface);
        personalInfoInterface =(LinearLayout)findViewById(R.id.personal_info_interface);
        goto_order_info =(LinearLayout)findViewById(R.id.goto_order_info);
        goto_order_reserve =(LinearLayout)findViewById(R.id.goto_reserve);
        goto_personal_info =(LinearLayout)findViewById(R.id.goto_personal_info);
        exit = (Button)findViewById(R.id.exit);
        chooseDate = (Button) findViewById(R.id.set_start_date);
        submint = (Button) findViewById(R.id.submit);
        accountId = (TextView)findViewById(R.id.account_id);
        accountName = (TextView)findViewById(R.id.account_name);
        accountPhoto = (ImageView)findViewById(R.id.account_photo);
        reserveImg = (ImageView)findViewById(R.id.reserve_img);
        orderImg = (ImageView)findViewById(R.id.order_img);
        accountImg = (ImageView)findViewById(R.id.account_img);
        setTopic = (EditText) findViewById(R.id.set_topic) ;
        peopleSpinner = (Spinner)findViewById(R.id.people_spinner);
        airSpinner = (Spinner)findViewById(R.id.air_spinner);
        startHourSpinner = (Spinner)findViewById(R.id.start_hour_spinner);
        startMinSpinner = (Spinner)findViewById(R.id.start_min_spinner);
        endHourSpinner = (Spinner)findViewById(R.id.end_hour_spinner);
        endMinSpinner = (Spinner)findViewById(R.id.end_min_spinner);
        computerSpinner = (Spinner)findViewById(R.id.computer_spinner);
        projectorSpinner = (Spinner)findViewById(R.id.projector_spinner);
        microphoneSpinner = (Spinner)findViewById(R.id.microphone_spinner);
        outstandingOrderRecyclerView = (RecyclerView)findViewById(R.id.outstanding_order_recycler_view);
        completedOrderRecyclerView = (RecyclerView)findViewById(R.id.completed_order_recycler_view);
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        accountImg.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.account_staff_blue));

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                getStaffInfo();
            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MenuStaffActivity.this.finish();
            }
        });

        goto_order_reserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reserveImg.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.reserve_blue));
                orderImg.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.order_staff_grey));
                accountImg.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.account_staff_grey));
                orderInfoInterface.setVisibility(View.GONE);
                orderReserveInterface.setVisibility(View.VISIBLE);
                personalInfoInterface.setVisibility(View.GONE);
                submint.setVisibility(View.VISIBLE);
                getOutstandingOrders();
            }
        });

        goto_order_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reserveImg.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.reserve_grey));
                orderImg.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.order_staff_blue));
                accountImg.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.account_staff_grey));
                orderInfoInterface.setVisibility(View.VISIBLE);
                orderReserveInterface.setVisibility(View.GONE);
                personalInfoInterface.setVisibility(View.GONE);
                submint.setVisibility(View.GONE);
                linearLayoutManager = new LinearLayoutManager(MenuStaffActivity.this);
                linearLayoutManager1 = new LinearLayoutManager(MenuStaffActivity.this);
                outstandingOrderRecyclerView.setLayoutManager(linearLayoutManager);
                completedOrderRecyclerView.setLayoutManager(linearLayoutManager1);
                outstandingOrderList.clear();
                getOutstandingOrders();
                completedOrderList.clear();
                getCompletedOrders();
            }
        });

        goto_personal_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reserveImg.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.reserve_grey));
                orderImg.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.order_staff_grey));
                accountImg.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.account_staff_blue));
                orderInfoInterface.setVisibility(View.GONE);
                orderReserveInterface.setVisibility(View.GONE);
                personalInfoInterface.setVisibility(View.VISIBLE);
                submint.setVisibility(View.GONE);
                infoList.clear();
                getStaffInfo();
            }
        });

        chooseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(MenuStaffActivity.this,THEME_HOLO_DARK,new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int mon, int day) {
                        chooseDate.setText(String.format("%d年%d月%d日",year,mon + 1,day));
                        date_year = year;
                        date_mon = mon + 1;
                        date_day = day;
                    }
                },calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        submint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                topic = setTopic.getText().toString();
                start_time = date_year + "-" + date_mon + "-" + date_day + " " + start_hour + ":" + start_min + ":00";
                end_time = date_year + "-" + date_mon + "-" + date_day + " " + end_hour + ":" + end_min + ":00";
                if(topic.isEmpty())
                    Toast.makeText(MenuStaffActivity.this,"议题不得为空",Toast.LENGTH_SHORT).show();
                else if(date_year == 0)
                    Toast.makeText(MenuStaffActivity.this,"请选择会议日期",Toast.LENGTH_SHORT).show();
                else if(Integer.parseInt(end_hour) < Integer.parseInt(start_hour))
                    Toast.makeText(MenuStaffActivity.this,"结束时间不得早于开始时间",Toast.LENGTH_SHORT).show();
                else if(Integer.parseInt(end_hour) == Integer.parseInt(start_hour) && Integer.parseInt(end_min) <= Integer.parseInt(start_min))
                    Toast.makeText(MenuStaffActivity.this,"结束时间不得早于开始时间",Toast.LENGTH_SHORT).show();
                else
                    postOrder();
            }
        });



        peopleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent,View view,int position, long id) {
                people = (String) peopleSpinner.getSelectedItem();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

        startHourSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent,View view,int position, long id) {
                start_hour = (String) startHourSpinner.getSelectedItem();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

        startMinSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent,View view,int position, long id) {
                start_min = (String) startMinSpinner.getSelectedItem();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

        endHourSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent,View view,int position, long id) {
                end_hour = (String) endHourSpinner.getSelectedItem();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

        endMinSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent,View view,int position, long id) {
                end_min = (String) endMinSpinner.getSelectedItem();
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

        airSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent,View view,int position, long id) {
                air = ss_ss((String) airSpinner.getSelectedItem());
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

    }

    private void postOrder() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    RequestBody requestBody = new FormBody.Builder()
                            .add("borrower",id)
                            .add("people",people)
                            .add("topic",topic )
                            .add("air_conditioner",air )
                            .add("start_time",start_time )
                            .add("end_time",end_time )
                            .add("projector",projector )
                            .add("computer",computer )
                            .add("microphone",microphone )
                            .build();
                    Request request = new Request.Builder()
                            .post(requestBody)
                            .url("http://47.102.131.72:8080/order")
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
                            AlertDialog.Builder builder = new AlertDialog.Builder(MenuStaffActivity.this);
                            builder.setTitle("预订结果");
                            if(responseData.equals("Fail"))
                                builder.setMessage("当前预订时间没有合适的会议室！");
                            else
                                builder.setMessage("您预订的房间号为：" + responseData);
                            builder.setCancelable(false);
                            builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
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

    private void getOutstandingOrders() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .get()
                            .url("http://47.102.131.72:8080/orders/outstanding/" + id)
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
                            final String responseData  = response.body().string();
                            Log.d("test",responseData);
                            parseOutstandingOrderJSONObject(responseData);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private  void parseOutstandingOrderJSONObject(final String jsonData){
        try{
            JSONArray jsonArray = new JSONArray(jsonData);
            for(int i = 0;i < jsonArray.length();i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String id = jsonObject.getString("id");
                String borrower = jsonObject.getString("borrower");
                String room = jsonObject.getString("room");
                String topic = jsonObject.getString("topic");
                String air_conditioner = jsonObject.getString("air_conditioner");
                String start_time = jsonObject.getString("start_time");
                String end_time = jsonObject.getString("end_time");
                Order order = new Order(id,borrower,room,topic,air_conditioner,start_time,end_time);
                outstandingOrderList.add(order);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                OrderStaffAdapter adapter = new OrderStaffAdapter(outstandingOrderList);
                outstandingOrderRecyclerView.setAdapter(adapter);
            }
        });
    }

    private void getCompletedOrders() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .get()
                            .url("http://47.102.131.72:8080/orders/completed/" + id)
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
                            final String responseData  = response.body().string();
                            Log.d("test",responseData);
                            parseCompletedOrderJSONObject(responseData);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private  void parseCompletedOrderJSONObject(final String jsonData){
        try{
            JSONArray jsonArray = new JSONArray(jsonData);
            for(int i = 0;i < jsonArray.length();i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String id = jsonObject.getString("id");
                String borrower = jsonObject.getString("borrower");
                String room = jsonObject.getString("room");
                String topic = jsonObject.getString("topic");
                String air_conditioner = jsonObject.getString("air_conditioner");
                String start_time = jsonObject.getString("start_time");
                String end_time = jsonObject.getString("end_time");
                Order order = new Order(id,borrower,room,topic,air_conditioner,start_time,end_time);
                completedOrderList.add(order);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                OrderStaffAdapter adapter = new OrderStaffAdapter(completedOrderList);
                completedOrderRecyclerView.setAdapter(adapter);
            }
        });
    }

    private void getStaffInfo() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .get()
                            .url("http://47.102.131.72:8080/staff/" + id)
                            .build();
                    Call call = client.newCall(request);
                    call.enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            //失败调用
                            Log.e("return", "onFailure: ");
                        }

                        @Override
                        public void onResponse(Call call, final Response response) throws IOException {
                            //成功调用
                            Log.e("return", "onResponse: ");
                            final String responseData = response.body().string();
                            Log.d("test", responseData);
                            parseStaffInfoJSONObject(responseData);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                String url = "http://47.102.131.72:8080/staff/download/" + id + ".jpg";
                final Bitmap bitmap = returnBitMap(url);
                accountPhoto.post(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        accountPhoto.setImageBitmap(bitmap);
                    }
                });
            }
        }).start();
    }

    private void parseStaffInfoJSONObject(String jsonData) {
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            String password = jsonObject.getString("password");
            name = jsonObject.getString("name");
            String telephone = jsonObject.getString("telephone");
            String rank = jsonObject.getString("rank");

        } catch (Exception e) {
            e.printStackTrace();
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                accountId.setText(id);
                accountName.setText(name);
            }
        });
    }

    private Bitmap returnBitMap(String url) {
        URL myFileUrl = null;
        Bitmap bitmap = null;
        try {
            myFileUrl = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            HttpURLConnection conn = (HttpURLConnection) myFileUrl
                    .openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    private String ss_ss(String ss){
        if(ss.equals("是"))
            return "1";
        else
            return "0";
    }
}
