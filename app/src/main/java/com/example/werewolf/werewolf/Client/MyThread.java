package com.example.werewolf.werewolf.Client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import android.content.Context;
import android.os.Message;
import android.widget.Toast;

public class MyThread implements Runnable{

    public static Socket mSocketClient = null;//客户端Socket
    private static String IP;
    private static int Port;
    private static Context context;
    public MyThread(String IP, int Port, Context context){
        this.IP = IP;
        this.Port = Port;
        this.context = context;
    }

    @Override
    public void run() {
        try{
            //连接服务器
            mSocketClient = new Socket(IP, Port);
            ClientActivity.isConnecting = true;
            //根据是否连接成功来更改按键的显示状态
            Message connectSuccess = new Message();
            connectSuccess.what = 0;
            ClientActivity.mHandler.sendMessage(connectSuccess);
            //取得输入、输出流
            ReceiveMessage.bufferFromServer = new BufferedReader(new InputStreamReader(mSocketClient.getInputStream(), "UTF-8"));//读取服务端发来的消息
            SendMessage.bufferToServer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(mSocketClient.getOutputStream(), "UTF-8")),true);//发送消息给服务端
            Toast.makeText(context, "输入输出流获取成功" + " 已连接", Toast.LENGTH_SHORT).show();
        }catch(Exception e){
            ClientActivity.isConnecting = false;
            //根据是否连接成功来更改按键的显示状态
            Message connectSuccess = new Message();
            connectSuccess.what = 1;
            ClientActivity.mHandler.sendMessage(connectSuccess);
            return;
        }
        ReceiveMessage.receiveRole();//接收身份
    }
}
