package com.example.werewolf.werewolf.Server;



import android.content.Context;
import android.os.Message;
import android.widget.Toast;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Role {
    //玩家属性
    private boolean alive;
    private int Id;
    private String role;
    //类公有成员
    public static int playerNumber;
    public static int seerNumber;
    public static int villagerNumber;
    public static int guardNumber;
    public static int wolfmanNumber;
    public static int witchNumber;
    public static List<String> roles = new ArrayList();

    public Role(int id, String Role){
        alive = true;
        Id = id;
        role = Role;
    }

    public String returnRole() {
        return role;
    }

    public void makeDead() {
        alive = false;
    }

    public void makeAlive() {
        alive = true;
    }

    //分配身份
    public static void roleDistribution(int playerNum) {
        playerNumber = playerNum;//初始化玩家人数
        if (playerNumber >= 6) {
            seerNumber = 0;
            villagerNumber = 3;
            guardNumber = 0;
            wolfmanNumber = 2;
            witchNumber = 1;
            for (int i = 0; i < 3; i++) roles.add("普通村民");
            for (int i = 1; i < 2; i++) roles.add("狼人");
            roles.add("wit");
        }
        if (playerNumber >= 7) {
            villagerNumber++;
            roles.add("普通村民");
        }
        if (playerNumber >= 8) {
            villagerNumber++;
            roles.add("普通村民");
        }
        if (playerNumber >= 9) {
            seerNumber++;
            roles.add("预言家");
        }
        if (playerNumber >= 10) {
            wolfmanNumber++;
            roles.add("狼人");
        }
        if(playerNumber>=11){
            guardNumber++;
            roles.add("守卫");
        }
        if(playerNumber>=12){
            villagerNumber++;
            roles.add("普通村民");
        }

        Collections.shuffle(roles);

        Message receiveMeg = new Message();
        receiveMeg.what = 1;
        ServerActivity.serverMessage = "身份分配完成^^";
        ServerActivity.mHandler.sendMessage(receiveMeg);
    }

    //给每个客户端发送对应的身份
    public static void sendRole(Context context){
        int num = ServerActivity.mList.size();
        String Role = "";
        for(int index = 0; index < num; index++) {
            Socket mSocket = ServerActivity.mList.get(index);
            Role = roles.get(index);//获得对应玩家的身份
            PrintWriter bufferToClient = null;
            try {
                bufferToClient = new PrintWriter(mSocket.getOutputStream(),true);
                bufferToClient.print(Role);
                bufferToClient.flush();
            } catch (IOException e) {
                Toast.makeText(context, "身份发送异常：" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
