package com.example.chris.conference_manage.Activity;

import android.animation.Animator;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chris.conference_manage.Adapter.InfoAdapter;
import com.example.chris.conference_manage.Adapter.OrderRootAdapter;
import com.example.chris.conference_manage.AnimUtil;
import com.example.chris.conference_manage.Class.Info;
import com.example.chris.conference_manage.Class.Order;
import com.example.chris.conference_manage.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RoomDisActivity extends AppCompatActivity {

    private List<Info> infoList = new ArrayList<>();
    private List<Order> orderList = new ArrayList<>();
    private String position;
    private RecyclerView room_info_recycler_view;
    private RecyclerView room_order_recycler_view;
    private RecyclerView head;
    private ImageView add;
    private TextView delete;
    private TextView title;
    private PopupWindow mPopupWindow;
    private AnimUtil animUtil;
    private String status = "null";

    private float bgAlpha = 1f;
    private boolean bright = false;
    private static final long DURATION = 500;
    private static final float START_ALPHA = 0.7f;
    private static final float END_ALPHA = 1f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dis_room);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        mPopupWindow = new PopupWindow(this);
        animUtil = new AnimUtil();
        add = findViewById(R.id.iv_add);
        title = (TextView)findViewById(R.id.tv_title);
        room_info_recycler_view = (RecyclerView)findViewById(R.id.room_info_recycler_view);
        room_order_recycler_view = (RecyclerView)findViewById(R.id.room_order_recycler_view);
        head = (RecyclerView)findViewById(R.id.head);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(this);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(this);
        Intent intent = getIntent();
        position = intent.getStringExtra("position");
        mPopupWindow.setContentView(LayoutInflater.from(this).inflate(R.layout.pop_dis_add, null));
        title.setText("会议室信息");
        initInfo();
        getRoomOrders();
        room_info_recycler_view.setLayoutManager(linearLayoutManager);
        room_order_recycler_view.setLayoutManager(linearLayoutManager1);
        head.setLayoutManager(linearLayoutManager2);
        InfoAdapter adapter = new InfoAdapter(infoList);
        room_info_recycler_view.setAdapter(adapter);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPop();
                toggleBright();
            }
        });
    }

    private void initInfo() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .get()
                            .url("http://47.102.131.72:8080/room/" + position )
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
                            parseJSONWithJSONObject(responseData);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private  void parseJSONWithJSONObject(String jsonData){
        try{
            JSONObject jsonObject = new JSONObject(jsonData);
            String people = jsonObject.getInt("people") + "";
            String projector = int_ss(jsonObject.getInt("projector"));
            String computer = int_ss(jsonObject.getInt("computer"));
            String microphone = int_ss(jsonObject.getInt("microphone"));
            String conditions = jsonObject.getString("conditions");
            status = jsonObject.optString("status ");
            Log.d("status",status);
            Info info;
            info = new Info("编号：",position);
            infoList.add(info);
            info = new Info("人数：",people);
            infoList.add(info);
            info = new Info("投影仪：",projector);
            infoList.add(info);
            info = new Info("电脑：",computer);
            infoList.add(info);
            info = new Info("麦克风：",microphone);
            infoList.add(info);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getRoomOrders() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .get()
                            .url("http://47.102.131.72:8080/order/room/" + position)
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
                            parseOrderJSONObject(responseData);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private  void parseOrderJSONObject(final String jsonData){
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
                orderList.add(order);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Order order_top = new Order("编号","借用人","房间号","议题","是否需要空调","开始时间","结束时间");
                List<Order> headList = new ArrayList<>();
                headList.add(order_top);
                OrderRootAdapter adapter = new OrderRootAdapter(headList);
                head.setAdapter(adapter);
            }
        });
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                OrderRootAdapter adapter = new OrderRootAdapter(orderList);
                room_order_recycler_view.setAdapter(adapter);
            }
        });
    }

    private String int_ss(Integer i) {
        if(i == 1)
            return "有";
        else
            return "无";
    }

    private void deleteRoom() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .delete()
                            .url("http://47.102.131.72:8080/room/" + position )
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
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void showPop() {
        // 设置布局文件
        mPopupWindow.setContentView(LayoutInflater.from(this).inflate(R.layout.pop_dis_add, null));
        // 为了避免部分机型不显示，我们需要重新设置一下宽高
        mPopupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置pop透明效果
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(0x0000));
        // 设置pop出入动画
        mPopupWindow.setAnimationStyle(R.style.pop_add);
        // 设置pop获取焦点，如果为false点击返回按钮会退出当前Activity，如果pop中有Editor的话，focusable必须要为true
        mPopupWindow.setFocusable(true);
        // 设置pop可点击，为false点击事件无效，默认为true
        mPopupWindow.setTouchable(true);
        // 设置点击pop外侧消失，默认为false；在focusable为true时点击外侧始终消失
        mPopupWindow.setOutsideTouchable(true);
        // 相对于 + 号正下面，同时可以设置偏移量
        mPopupWindow.showAsDropDown(add, -100, 0);
        // 设置pop关闭监听，用于改变背景透明度
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                toggleBright();
            }
        });

        delete = mPopupWindow.getContentView().findViewById(R.id.tag);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPopupWindow.dismiss();
                AlertDialog.Builder builder = new AlertDialog.Builder(RoomDisActivity.this);
                builder.setTitle("删除信息");
                builder.setMessage("确认删除该信息");
                builder.setCancelable(false);
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteRoom();
                        Toast.makeText(RoomDisActivity.this,"订单已删除",Toast.LENGTH_SHORT).show();
                        RoomDisActivity.this.finish();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                builder.show();
            }
        });
    }

    private void toggleBright() {
        // 三个参数分别为：起始值 结束值 时长，那么整个动画回调过来的值就是从0.5f--1f的
        animUtil.setValueAnimator(START_ALPHA, END_ALPHA, DURATION);
        animUtil.addUpdateListener(new AnimUtil.UpdateListener() {
            @Override
            public void progress(float progress) {
                // 此处系统会根据上述三个值，计算每次回调的值是多少，我们根据这个值来改变透明度
                bgAlpha = bright ? progress : (START_ALPHA + END_ALPHA - progress);
                backgroundAlpha(bgAlpha);
            }
        });
        animUtil.addEndListner(new AnimUtil.EndListener() {
            @Override
            public void endUpdate(Animator animator) {
                // 在一次动画结束的时候，翻转状态
                bright = !bright;
            }
        });
        animUtil.startAnimator();
    }

    private void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        // 0.0-1.0
        lp.alpha = bgAlpha;
        getWindow().setAttributes(lp);
        // everything behind this window will be dimmed.
        // 此方法用来设置浮动层，防止部分手机变暗无效
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    }
}
