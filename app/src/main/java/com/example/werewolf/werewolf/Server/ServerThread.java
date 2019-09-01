package com.example.werewolf.werewolf.Server;

import android.os.Message;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;

public class ServerThread implements Runnable{
    public static ServerSocket serverSocket = null;//服务Socket
    public static final int PORT = 12345;//服务端PORT

    @Override
    public void run() {
        try{
            /*创建TCP服务端
			--如果把参数 port 设为 0, 表示由操作系统来为服务器分配一个任意可用的端口
			--如果主机只有一个IP 地址, 那么默认情况下, 服务器程序就与该IP地址绑定*/
            serverSocket = new ServerSocket(PORT);//绑定serverSocket
            ServerActivity.serverMessage = "服务创建成功!\n请玩家连接：" + ServerActivity.IP + " : " + PORT;
            Message msg = new Message();
            msg.what = 2;
            ServerActivity.mHandler.sendMessage(msg);
            //创建线程池
            ServerActivity.myExecutorService = Executors.newCachedThreadPool();
            Socket client;//客户端socket
            while(ServerActivity.serverRunning){
                client = serverSocket.accept();
                ServerActivity.mList.add(client);
                int num = ServerActivity.mList.size() - 1;
                //发送消息到mandler
                Message clientOnline = new Message();
                clientOnline.what = 0;
                ServerActivity.serverMessage = "玩家 " + num + " 加入";
                ServerActivity.mHandler.sendMessage(clientOnline);
                //开启新线程
                ServerActivity.myExecutorService.execute(new CommunicationThread(client, num));
            }
        }catch (Exception e) {
            Message msg = new Message();
            msg.what = 0;
            ServerActivity.serverMessage = "服务异常:" + e.getMessage() + e.toString();//消息换行
            ServerActivity.mHandler.sendMessage(msg);
            return;
        }
    }
}
