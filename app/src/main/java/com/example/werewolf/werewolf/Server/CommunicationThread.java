package com.example.werewolf.werewolf.Server;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import android.os.Message;
import android.widget.Toast;

import com.example.werewolf.werewolf.Client.SendMessage;

public class CommunicationThread implements Runnable{
    private Socket socket;
    private BufferedReader bufferFromClient = null;
    private PrintWriter bufferToCLient = null;
    private String msg = "";
    private String msgHead =  "";//消息标头
    private String msgRear =  "";//消息尾部
    private int threadNo = 0;//线程编号，与玩家编号对应


    public CommunicationThread(Socket socket, int num) {
        this.socket = socket;
        threadNo = num;
        try{
            //接受客服端数据BufferedReader对象
            bufferFromClient = new BufferedReader(new InputStreamReader((this.socket).getInputStream(), "UTF-8"));
            bufferToCLient = new PrintWriter(new BufferedWriter(new OutputStreamWriter((this.socket).getOutputStream(), "UTF-8")),true);//发送消息给服务端
            //
            Message msg = new Message();
            msg.what = 0;
            ServerActivity.serverMessage = "获取输入流对象成功\n线程" + threadNo + "成功创建";
            ServerActivity.mHandler.sendMessage(msg);
        }catch//(IOException e){e.printStackTrace();}
                (Exception e){
        Message msg = new Message();
        msg.what = 0;
        ServerActivity.serverMessage = "异常:" + e.getMessage() + e.toString();//消息换行
        ServerActivity.mHandler.sendMessage(msg);
        return;}
    }

    @Override
    public void run() {
        try{
            while(ServerActivity.serverRunning){
                if((msg = bufferFromClient.readLine()) != null){
                    int msgLength = msg.length();//获取服务端消息的长度
                    //如果是投票消息
                    if(msgLength > 4){
                        msgHead = msg.substring(0, 4);//获取消息标头
                        msgRear = msg.substring(4, msg.length());//去除投票信息的前缀“vote”,获得投票的编号
                    }
                    if(msgHead == "vote" || msgHead == "Vote") {
                        //Game.votes.get(threadNo) = msgRear; 对投票数组赋值
                        ServerActivity.clientMessage = "玩家 " + threadNo + " 投给玩家" + msgRear;
                    } else ServerActivity.clientMessage = msg;
                    Message receiveMeg = new Message();
                    receiveMeg.what = 3;
                    ServerActivity.mHandler.sendMessage(receiveMeg);
                } else {
                    Message receiveMeg = new Message();
                    receiveMeg.what = 3;
                    ServerActivity.mHandler.sendMessage(receiveMeg);
                }
            }
        }catch(Exception e){
            ServerActivity.serverMessage = "接收异常:" + e.getMessage() ;//消息换行
            Message exceptionMsg = new Message();
            exceptionMsg .what = 0;
            ServerActivity.mHandler.sendMessage(exceptionMsg);
            return;
        }
    }
}
