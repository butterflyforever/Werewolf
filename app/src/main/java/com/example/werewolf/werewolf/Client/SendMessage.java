package com.example.werewolf.werewolf.Client;

import android.content.Context;
import android.widget.Toast;

import java.io.PrintWriter;
import java.net.Socket;

public class SendMessage {
    public static PrintWriter bufferToServer = null;

    public static void sendMessage(String message, Context context){
        //if (ClientActivity.isConnecting && MyThread.mSocketClient != null)
        if(true){
            try {
                bufferToServer.print(message);//发送给服务器
                bufferToServer.flush();
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(context, "发送异常：" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } else Toast.makeText(context, "发送失败！", Toast.LENGTH_SHORT).show();
    }

    //判断客户端是否在线
    public static boolean isConnected(Socket socket){
        try{
            socket.sendUrgentData(0xFF);
            return true;
        }catch(Exception e){
            return false;
        }
    }
}
