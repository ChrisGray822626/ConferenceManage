package com.example.chris.conference_manage.Activity;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chris.conference_manage.Adapter.InfoAdapter;
import com.example.chris.conference_manage.Adapter.OrderRootAdapter;
import com.example.chris.conference_manage.Adapter.RoomAdapter;
import com.example.chris.conference_manage.Adapter.StaffAdapter;
import com.example.chris.conference_manage.AnimUtil;
import com.example.chris.conference_manage.Class.Info;
import com.example.chris.conference_manage.Class.Order;
import com.example.chris.conference_manage.Class.Room;
import com.example.chris.conference_manage.Class.Staff;
import com.example.chris.conference_manage.DividerItemDecoration;
import com.example.chris.conference_manage.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MenuRootActivity extends AppCompatActivity {

    private List<Order> orderList = new ArrayList<>();
    private List<Staff> staffList = new ArrayList<>();
    private List<Room> roomList = new ArrayList<>();
    private List<Info> infoList = new ArrayList<>();
    private LinearLayout GetOrder;
    private LinearLayout GetRoom;
    private LinearLayout GetStaff;
    private LinearLayout GetAccount;
    private ImageView orderImg;
    private ImageView roomImg;
    private ImageView staffImg;
    private ImageView accountImg;
    private TextView orderText;
    private TextView roomText;
    private TextView staffText;
    private TextView accountText;
    private TextView accountName;
    private TextView accountId;
    private TextView addRoom;
    private TextView addStaff;
    private TextView title;
    private RecyclerView recyclerView;
    private RecyclerView head;
    private ImageView accountPhoto;
    private ImageView add;
    private Button exit;
    private LinearLayout rootInfo;
    private LinearLayout listInterface;
    private PopupWindow mPopupWindow;
    private AnimUtil animUtil;

    private float bgAlpha = 1f;
    private boolean bright = false;
    private static final long DURATION = 500;
    private static final float START_ALPHA = 0.7f;
    private static final float END_ALPHA = 1f;

    private String status = "null";
    private String id;
    private String name;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_root);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        mPopupWindow = new PopupWindow(this);
        animUtil = new AnimUtil();
        add = findViewById(R.id.iv_add);
        GetOrder = (LinearLayout) findViewById(R.id.goto_order);
        GetRoom = (LinearLayout) findViewById(R.id.goto_room);
        GetStaff = (LinearLayout) findViewById(R.id.goto_staff);
        GetAccount = (LinearLayout) findViewById(R.id.goto_account);
        orderImg = (ImageView)findViewById(R.id.order_img);
        roomImg = (ImageView)findViewById(R.id.room_img);
        staffImg = (ImageView)findViewById(R.id.staff_img);
        accountImg = (ImageView)findViewById(R.id.account_img);
        accountName = (TextView)findViewById(R.id.account_name);
        accountId = (TextView)findViewById(R.id.account_id);
        title = (TextView)findViewById(R.id.tv_title);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        head = (RecyclerView) findViewById(R.id.head);
        accountPhoto = (ImageView)findViewById(R.id.account_photo);
        exit = (Button)findViewById(R.id.exit);
        rootInfo = (LinearLayout)findViewById(R.id.root_info);
        listInterface = (LinearLayout)findViewById(R.id.list_interface);
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(this);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        head.setLayoutManager(linearLayoutManager2);
        accountImg.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.account_root_blue));
        title.setText("智能会议室管理");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                getRootInfo();
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPop();
                toggleBright();
            }
        });

        recyclerView.addItemDecoration(new DividerItemDecoration(
                MenuRootActivity.this, DividerItemDecoration.HORIZONTAL_LIST));
        head.addItemDecoration(new DividerItemDecoration(
                MenuRootActivity.this, DividerItemDecoration.HORIZONTAL_LIST));

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MenuRootActivity.this.finish();
            }
        });

        GetOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderImg.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.order_root_blue));
                roomImg.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.room_grey));
                staffImg.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.staff_grey));
                accountImg.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.account_root_grey));
                listInterface.setVisibility(View.VISIBLE);
                rootInfo.setVisibility(View.GONE);
                orderList.clear();
                getAllOrders();
            }
        });

        GetStaff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderImg.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.order_root_grey));
                roomImg.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.room_grey));
                staffImg.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.staff_blue));
                accountImg.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.account_root_grey));
                listInterface.setVisibility(View.VISIBLE);
                rootInfo.setVisibility(View.GONE);
                staffList.clear();
                getAllStaffs();
            }
        });

        GetRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderImg.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.order_root_grey));
                roomImg.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.room_blue));
                staffImg.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.staff_grey));
                accountImg.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.account_root_grey));
                listInterface.setVisibility(View.VISIBLE);
                rootInfo.setVisibility(View.GONE);
                roomList.clear();
                getAllRooms();
            }
        });

        GetAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderImg.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.order_root_grey));
                roomImg.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.room_grey));
                staffImg.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.staff_grey));
                accountImg.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.account_root_blue));
                listInterface.setVisibility(View.GONE);
                rootInfo.setVisibility(View.VISIBLE);
                infoList.clear();
                getRootInfo();
            }
        });
    }

    private void getAllOrders() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .get()
                            .url("http://47.102.131.72:8080/orders")
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
                recyclerView.setAdapter(adapter);
            }
        });
    }

    private void getAllStaffs() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .get()
                            .url("http://47.102.131.72:8080/staffs")
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
                            parseStaffJSONObject(responseData);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private  void parseStaffJSONObject(final String jsonData){
        try{
            JSONArray jsonArray = new JSONArray(jsonData);

            for(int i = 0;i < jsonArray.length();i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String id = jsonObject.getInt("id") + "";
                String password = jsonObject.getString("password");
                String name = jsonObject.getString("name");
                String telephone = jsonObject.getString("telephone");
                String rank = jsonObject.getString("rank");
                Staff staff = new Staff(id,password,name,telephone,rank);
                staffList.add(staff);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Staff staff_top = new Staff("工号","密码","姓名","手机","级别");
                List<Staff> headList = new ArrayList<>();
                headList.add(staff_top);
                StaffAdapter adapter = new StaffAdapter(headList);
                head.setAdapter(adapter);
            }
        });

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                StaffAdapter adapter = new StaffAdapter(staffList);
                recyclerView.setAdapter(adapter);
            }
        });
    }

    private void getAllRooms() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .get()
                            .url("http://47.102.131.72:8080/rooms")
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
                            parseRoomJSONObject(responseData);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private  void parseRoomJSONObject(final String jsonData){
        try{
            JSONArray jsonArray = new JSONArray(jsonData);

            for(int i = 0;i < jsonArray.length();i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String id = jsonObject.getInt("id") + "";
                String people = jsonObject.getInt("people") + "";
                String projector = int_ss(jsonObject.getInt("projector"));
                String computer = int_ss(jsonObject.getInt("computer"));
                String microphone = int_ss(jsonObject.getInt("microphone"));
                String conditions = jsonObject.getString("conditions");
                Room room = new Room(id,people,projector,computer,microphone,conditions);
                roomList.add(room);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Room room_top = new Room("编号","人数","投影仪","电脑","麦克风","使用情况");
                List<Room> headList = new ArrayList<>();
                headList.add(room_top);
                RoomAdapter adapter = new RoomAdapter(headList);
                head.setAdapter(adapter);
            }
        });

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                RoomAdapter adapter = new RoomAdapter(roomList);
                recyclerView.setAdapter(adapter);
            }
        });
    }

    private void getRootInfo() {
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
                            parseJSONWithJSONObject(responseData);
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

    private void parseJSONWithJSONObject(String jsonData) {
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            id = jsonObject.getString("id");
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

    private String int_ss(Integer i) {
        if(i == 1)
            return "有";
        else
            return "无";
    }

    private void showPop() {
        // 设置布局文件
        mPopupWindow.setContentView(LayoutInflater.from(this).inflate(R.layout.pop_root_add, null));
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

        addRoom = mPopupWindow.getContentView().findViewById(R.id.add_room);
        addStaff = mPopupWindow.getContentView().findViewById(R.id.add_staff);

        addRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPopupWindow.dismiss();
                Intent intent = new Intent(MenuRootActivity.this,AddRoomActivity.class);
                startActivity(intent);
            }
        });

        addStaff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPopupWindow.dismiss();
                Intent intent = new Intent(MenuRootActivity.this,AddStaffActivity.class);
                startActivity(intent);
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
