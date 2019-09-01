package com.example.werewolf.werewolf.Server;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.os.Bundle;
import android.widget.ToggleButton;

import com.example.werewolf.werewolf.R;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

public class ServerActivity extends Activity implements OnClickListener,CompoundButton.OnCheckedChangeListener{
    //定义属性
    private String ID;//输入框内的ID
    private Thread serverThread;//服务线程
    public static  String serverMessage = "";
    public static  String clientMessage = "";//玩家发过来的消息
    public static boolean serverRunning = false;//服务状态标志位
    public static String IP = "";
    public static ExecutorService myExecutorService = null;//线程池
    public static List<Socket> mList = new ArrayList<Socket>();//储存socket的集合
    //定义控件
    private static TextView info_ip_and_port;//显示ip和port
    private static TextView game_info;//显示游戏进程
    private Button startGame, guard, kill, save, poison, creatServer;
    private EditText id_info;//玩家id输入框
    private static ScrollView mScrollView;
    private static LinearLayout dialog;
    private ToggleButton tb_p1,tb_p2,tb_p3,tb_p4,tb_p5,tb_p6,tb_p7,tb_p8,
            tb_p9,tb_p10,tb_p11,tb_p12,tb_p13,tb_p14,tb_p15,tb_p16;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server);
        //控件获取
        startGame = (Button)findViewById(R.id.startGame);
        guard = (Button)findViewById(R.id.guard);
        kill = (Button)findViewById(R.id.kill);
        save = (Button)findViewById(R.id.save);
        poison = (Button)findViewById(R.id.poison);
        creatServer = (Button)findViewById(R.id.creatServer);
        game_info = (TextView)findViewById(R.id.game_info);
        info_ip_and_port = (TextView)findViewById(R.id.info_ip_and_port);
        id_info = (EditText)findViewById(R.id.id_info);
        dialog= (LinearLayout) findViewById(R.id.dialog);
        mScrollView = (ScrollView)findViewById(R.id.mScrollView);
        tb_p1= (ToggleButton) findViewById(R.id.togglebutton_p1);
        tb_p2= (ToggleButton) findViewById(R.id.togglebutton_p2);
        tb_p3= (ToggleButton) findViewById(R.id.togglebutton_p3);
        tb_p4= (ToggleButton) findViewById(R.id.togglebutton_p4);
        tb_p5= (ToggleButton) findViewById(R.id.togglebutton_p5);
        tb_p6= (ToggleButton) findViewById(R.id.togglebutton_p6);
        tb_p7= (ToggleButton) findViewById(R.id.togglebutton_p7);
        tb_p8= (ToggleButton) findViewById(R.id.togglebutton_p8);
        tb_p9= (ToggleButton) findViewById(R.id.togglebutton_p9);
        tb_p10= (ToggleButton) findViewById(R.id.togglebutton_p10);
        tb_p11= (ToggleButton) findViewById(R.id.togglebutton_p11);
        tb_p12= (ToggleButton) findViewById(R.id.togglebutton_p12);
        tb_p13= (ToggleButton) findViewById(R.id.togglebutton_p13);
        tb_p14= (ToggleButton) findViewById(R.id.togglebutton_p14);
        tb_p15= (ToggleButton) findViewById(R.id.togglebutton_p15);
        tb_p16= (ToggleButton) findViewById(R.id.togglebutton_p16);
        //按键监听
        startGame.setOnClickListener(this);
        guard.setOnClickListener(this);
        kill.setOnClickListener(this);
        save.setOnClickListener(this);
        poison.setOnClickListener(this);
        creatServer.setOnClickListener(this);
        tb_p1.setOnCheckedChangeListener(this);
        tb_p2.setOnCheckedChangeListener(this);
        tb_p3.setOnCheckedChangeListener(this);
        tb_p4.setOnCheckedChangeListener(this);
        tb_p5.setOnCheckedChangeListener(this);
        tb_p6.setOnCheckedChangeListener(this);
        tb_p7.setOnCheckedChangeListener(this);
        tb_p8.setOnCheckedChangeListener(this);
        tb_p9.setOnCheckedChangeListener(this);
        tb_p10.setOnCheckedChangeListener(this);
        tb_p11.setOnCheckedChangeListener(this);
        tb_p12.setOnCheckedChangeListener(this);
        tb_p13.setOnCheckedChangeListener(this);
        tb_p14.setOnCheckedChangeListener(this);
        tb_p15.setOnCheckedChangeListener(this);
        tb_p16.setOnCheckedChangeListener(this);
    }

    //创建服务
    public void creatServer(){
        if(!serverRunning) { // 如果没有创建了服务，则创建服务。
            serverRunning = true;
            ServerThread creatServer = new ServerThread();
            serverThread = new Thread(creatServer);//创建服务线程
            serverThread.start();
        }
        else {
            game_info.append("服务已创建！\n");
            scroll2Bottom(mScrollView, dialog);
        }
    }

    //把整型IP地址转换成“*.*.*.*”形式的IP地址
    private  String intToIp(int ip) {
        return (ip & 0xFF) + "." + ((ip >> 8) & 0xFF) + "." + ((ip >> 16) & 0xFF) + "." + ((ip >> 24) & 0xFF);
    }

    //获取IP地址
    public void getIp() {
        // 获取wifi服务
        WifiManager wifiManager = (WifiManager) (ServerActivity.this).getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        //获取32位整型IP地址,并返回
        int ipAddress = wifiInfo.getIpAddress();
        IP = intToIp(ipAddress);
    }

    //接收线程传递来的消息更新UI
    public static Handler mHandler = new Handler() {

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //服务线程传递过来的玩家加入的消息，更新游戏进程消息框
            //接收异常消息，更新游戏进程框消息
            //接受Game类的消息，更新游戏进程框
            if (msg.what == 0) {
                game_info.append(serverMessage + "\n");
                scroll2Bottom(mScrollView, dialog);
            }
            //分配身份传递过来消息，更新玩家框
            else if (msg.what == 1) {
                scroll2Bottom(mScrollView, dialog);
            }
            //创建服务成功，更新ip_and_port框
            else if (msg.what == 2) {
                info_ip_and_port.setText(IP + ":" + ServerThread.PORT);
                game_info.append(serverMessage + "\n");
                scroll2Bottom(mScrollView, dialog);
            }
            //接收玩家发来的ID消息或者投票消息，更新游戏进程框
            else if (msg.what == 3) {
                game_info.append("没收到消息" + "\n");
                scroll2Bottom(mScrollView, dialog);
            }
        }
    };

    // TextView自带滚动条、自动滚到最底
    public static void scroll2Bottom(final ScrollView scroll, final View inner) {
        Handler handler = new Handler();
        handler.post(new Runnable() {

            @Override
            public void run() {
                if (scroll == null || inner == null) {
                    return;
                }
                // 内层高度超过外层
                int offset = inner.getMeasuredHeight()
                        - scroll.getMeasuredHeight();
                if (offset < 0) {

                    offset = 0;
                }
                scroll.scrollTo(0, offset);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.creatServer:
                getIp();
                creatServer();
                break;
            case R.id.startGame:
                Role.roleDistribution(mList.size());//根据socket列表确定人数，分配身份
                Role.sendRole(ServerActivity.this);//为每个玩家发送对应身份
                Game.initializeInfo();//初始化游戏数据
                break;
            case R.id.guard:
                ID = id_info.getText().toString();//取得编辑框中我们输入的ID
                Game.guard(ID);
                break;
            case R.id.kill:
                ID = id_info.getText().toString();
                Game.kill(ID);
                break;
            case R.id.save:
                ID = id_info.getText().toString();
                Game.save(ID);
                break;
            case R.id.poison:
                ID = id_info.getText().toString();
                Game.poison(ID);
                break;
            default:
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(!isChecked){
            buttonView.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
        }
        if(isChecked){ buttonView.setBackgroundTintList(ColorStateList.valueOf(Color.YELLOW));}

    }
}
