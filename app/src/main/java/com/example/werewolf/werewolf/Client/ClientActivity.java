package com.example.werewolf.werewolf.Client;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.werewolf.werewolf.R;

public class ClientActivity extends Activity implements OnClickListener,CompoundButton.OnCheckedChangeListener {
    //定义属性
    private String serverIP = "";//服务器IP地址
    private int port = 12345;//服务器端口号
    private Thread mThreadClient = null;//创建Socket连接的子线程
    private String sendContent;//发送的消息
    public static boolean isConnecting = false;//连接状态标识位
    public static String clientMessage = "";//客户端发送的消息
    public static String serverMessage = "";//客户端接收的消息
    //定义控件
    private static Button connect;
    private EditText id_info, ipAddress, port_num;
    private static TextView role_info;//身份框
    private Button send;
    private ToggleButton tb1,tb2,tb3,tb4,tb5,tb6,tb7,tb8,
            tb9,tb10,tb11,tb12,tb13,tb14,tb15,tb16;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);
        //获取控件
        connect = (Button)findViewById(R.id.connect);
        send = (Button)findViewById(R.id.send);
        id_info = (EditText)findViewById(R.id.id_info);
        ipAddress = (EditText)findViewById(R.id.ipAddress);
        port_num = (EditText)findViewById(R.id.port_num);
        role_info = (TextView) findViewById(R.id.role_info);
        tb1= (ToggleButton) findViewById(R.id.togglebutton1);
        tb2= (ToggleButton) findViewById(R.id.togglebutton2);
        tb3= (ToggleButton) findViewById(R.id.togglebutton3);
        tb4= (ToggleButton) findViewById(R.id.togglebutton4);
        tb5= (ToggleButton) findViewById(R.id.togglebutton5);
        tb6= (ToggleButton) findViewById(R.id.togglebutton6);
        tb7= (ToggleButton) findViewById(R.id.togglebutton7);
        tb8= (ToggleButton) findViewById(R.id.togglebutton8);
        tb9= (ToggleButton) findViewById(R.id.togglebutton9);
        tb10= (ToggleButton) findViewById(R.id.togglebutton10);
        tb11= (ToggleButton) findViewById(R.id.togglebutton11);
        tb12= (ToggleButton) findViewById(R.id.togglebutton12);
        tb13= (ToggleButton) findViewById(R.id.togglebutton13);
        tb14= (ToggleButton) findViewById(R.id.togglebutton14);
        tb15= (ToggleButton) findViewById(R.id.togglebutton15);
        tb16= (ToggleButton) findViewById(R.id.togglebutton16);
        //控件监听
        connect.setOnClickListener(this);
        send.setOnClickListener(this);
        tb1.setOnCheckedChangeListener(this);
        tb2.setOnCheckedChangeListener(this);
        tb3.setOnCheckedChangeListener(this);
        tb4.setOnCheckedChangeListener(this);
        tb5.setOnCheckedChangeListener(this);
        tb6.setOnCheckedChangeListener(this);
        tb7.setOnCheckedChangeListener(this);
        tb8.setOnCheckedChangeListener(this);
        tb9.setOnCheckedChangeListener(this);
        tb10.setOnCheckedChangeListener(this);
        tb11.setOnCheckedChangeListener(this);
        tb12.setOnCheckedChangeListener(this);
        tb13.setOnCheckedChangeListener(this);
        tb14.setOnCheckedChangeListener(this);
        tb15.setOnCheckedChangeListener(this);
        tb16.setOnCheckedChangeListener(this);
    }

    //连接服务端
    private void connectServer(String IP,int Port) {
        if(!isConnecting) { //如果没有连上服务端
            MyThread myThread = new MyThread(IP, Port, ClientActivity.this);
            mThreadClient = new Thread(myThread);
            mThreadClient.start();
        }
        else Toast.makeText(ClientActivity.this, "已连上服务端", Toast.LENGTH_SHORT).show();
    }

    //根据接收到的信息更新UI
    public static Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //服务线程传递过来消息，更改按键状态
            if(msg.what == 0) {
                connect.setText("断开");
                //connect.setBackgroundResource(R.drawable.connect2); //R.drawable.connect2显示connect按键按下
            }else if(msg.what == 1) {
                connect.setText("连接");
                //connect.setBackgroundResource(R.drawable.connect1);//R.drawable.connect1显示connect按键未被按下
            }
            //服务线程传递过来消息，更改身份框
            else if(msg.what == 2) {
                switch (serverMessage) {
                    case "vil": role_info.setText("村民"); break;
                    case "see": role_info.setText("预言家"); break;
                    case "wol": role_info.setText("狼人"); break;
                    case "gua": role_info.setText("守卫"); break;
                    case "wit": role_info.setText("女巫"); break;
                    default: role_info.setText("角色分配错误"); break;
                }
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.connect:
                serverIP = ipAddress.getText().toString();
                String str_port = port_num.getText().toString();
                if(serverIP.length() > 0 && str_port.length() > 0) {
                    port = Integer.parseInt(str_port);
                    connectServer(serverIP, port);
                }
                break;
            case R.id.send:
                sendContent = id_info.getText().toString();//取得编辑框中我们输入的内容
                if(sendContent.length() != 0) {
                    if(SendMessage.isConnected(MyThread.mSocketClient)){
                        //Toast.makeText(ClientActivity.this, sendContent, Toast.LENGTH_SHORT).show();
                        SendMessage.sendMessage(sendContent, ClientActivity.this);
                        id_info.setText("");//点击发送之后清空输入框
                    }else {
                        Toast.makeText(ClientActivity.this, "服务端已断开", Toast.LENGTH_SHORT).show();
                        connect.setText("连接");
                        //connect.setBackgroundResource(R.drawable.connect1);
                        isConnecting = false;
                    }
                }else {
                    Toast.makeText(ClientActivity.this, "发送内容为空，请重新输入！", Toast.LENGTH_SHORT).show();
                }
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
        if(isChecked){ buttonView.setBackgroundTintList(ColorStateList.valueOf(0xFF8C69));}

    }
}
