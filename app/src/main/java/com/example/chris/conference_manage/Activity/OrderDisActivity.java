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
import com.example.chris.conference_manage.AnimUtil;
import com.example.chris.conference_manage.Class.Info;
import com.example.chris.conference_manage.R;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OrderDisActivity extends AppCompatActivity {

    private List<Info> infoList = new ArrayList<>();
    private String position;
    private RecyclerView recyclerView;
    private ImageView add;
    private TextView delete;
    private TextView title;
    private PopupWindow mPopupWindow;
    private AnimUtil animUtil;

    private float bgAlpha = 1f;
    private boolean bright = false;
    private static final long DURATION = 500;
    private static final float START_ALPHA = 0.7f;
    private static final float END_ALPHA = 1f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        setContentView(R.layout.activity_dis_order);

        mPopupWindow = new PopupWindow(this);
        animUtil = new AnimUtil();
        add = findViewById(R.id.iv_add);
        title = (TextView)findViewById(R.id.tv_title);
        recyclerView = (RecyclerView)findViewById(R.id.order_info_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        Intent intent = getIntent();
        position = intent.getStringExtra("position");
        recyclerView.setLayoutManager(linearLayoutManager);
        mPopupWindow.setContentView(LayoutInflater.from(this).inflate(R.layout.pop_dis_add, null));
        title.setText("订单信息");
        initInfo();

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
                            .url("http://47.102.131.72:8080/order/" + position )
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
                            parseInfoWithJSONObject(responseData);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private  void parseInfoWithJSONObject(String jsonData){
        try{
            JSONObject jsonObject = new JSONObject(jsonData);
            String borrower = jsonObject.getString("borrower");
            String room = jsonObject.getString("room");
            String topic = jsonObject.getString("topic");
            String air_conditioner = ss_ss(jsonObject.getString("air_conditioner"));
            String start_time = jsonObject.getString("start_time").substring(12);
            String end_time = jsonObject.getString("end_time").substring(12);
            String people = jsonObject.getString("people");
            String computer = ss_ss(jsonObject.getInt("computer") + "");
            String microphone = ss_ss(jsonObject.getInt("microphone") + "");
            String projector = ss_ss(jsonObject.getInt("projector") + "");

            Info info;
            info = new Info("订单编号：",position);
            infoList.add(info);
            info = new Info("借用人工号：",borrower);
            infoList.add(info);
            info = new Info("会议室编号：",room);
            infoList.add(info);
            info = new Info("议题：",topic);
            infoList.add(info);
            info = new Info("人数：",people);
            infoList.add(info);
            info = new Info("开始时间：",start_time);
            infoList.add(info);
            info = new Info("结束时间：",end_time);
            infoList.add(info);
            info = new Info("是否需要空调：",air_conditioner);
            infoList.add(info);
            info = new Info("是否需要电脑：",computer);
            infoList.add(info);
            info = new Info("是否需要麦克风：",microphone);
            infoList.add(info);
            info = new Info("是否需要投影仪：",projector);
            infoList.add(info);

        } catch (Exception e) {
            e.printStackTrace();
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                InfoAdapter adapter = new InfoAdapter(infoList);
                recyclerView.setAdapter(adapter);
            }
        });
    }

    private void deleteOrder() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .delete()
                            .url("http://47.102.131.72:8080/order/" + position )
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

    private String ss_ss(String i) {
        if(i.equals(1))
            return "是";
        else
            return "否";
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
                AlertDialog.Builder builder = new AlertDialog.Builder(OrderDisActivity.this);
                builder.setTitle("删除信息");
                builder.setMessage("确认删除该信息");
                builder.setCancelable(false);
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteOrder();
                        Toast.makeText(OrderDisActivity.this,"订单已删除",Toast.LENGTH_SHORT).show();
                        OrderDisActivity.this.finish();
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
