package com.example.werewolf.werewolf.Client;

import android.os.Message;

import java.io.BufferedReader;

public class ReceiveMessage {

    private static String content;//服务端发送来的身份内容
    public static BufferedReader bufferFromServer = null;

    public static void receiveRole() {
        while(ClientActivity.isConnecting && MyThread.mSocketClient != null) {
            try {
                if((content = bufferFromServer.readLine()) != null) {
                    ClientActivity.serverMessage = content;
                    Message receiveMes = new Message();
                    receiveMes.what = 2;
                    ClientActivity.mHandler.sendMessage(receiveMes);
                }
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
}
