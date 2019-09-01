package com.example.werewolf.werewolf.Server;

import android.os.Message;

import java.util.ArrayList;
import java.util.List;

public class Game {
    public static List<Role> players;
    public static List<String> votes;
    public static int guarded;
    public static boolean cure;
    public static boolean poison;

    //初始化游戏相关数据
    public static void initializeInfo() {
        guarded = -1;
        cure = true;
        poison = false;
        players = new ArrayList<Role>();
        //初始化玩家身份列表
        for(int i = 0; i < Role.playerNumber; i++){
            if(Role.roles.get(i).equals("普通村民")) {
                players.add(i,new Role(i,"普通村民"));
            }
            else if(Role.roles.get(i).equals("狼人")){
                players.add(i,new Role(i,"狼人"));
            }
            else if(Role.roles.get(i).equals("预言家")){
                players.add(i,new Role(i,"预言家"));
            }
            else if(Role.roles.get(i).equals("女巫")){
                players.add(i,new Role(i,"女巫"));
            }
            else if(Role.roles.get(i).equals("守卫")){
                players.add(i,new Role(i,"守卫"));
            }
        }
        //初始化投票信息
        for(int i = 0; i < Role.playerNumber; i++) {
            votes.add("0");
        }
    }

    //处理投票信息，返回编号
    public static int vote() {
        int max = 0;
        for(int i = 1; i < Role.playerNumber; i++){
            if(Integer.parseInt(votes.get(i)) > Integer.parseInt(votes.get(max))) max = i;
        }
        return max;
    }

    //接受编号，杀死玩家
    public static void kill(String ID){
        int id = Integer.parseInt(ID);
        if(guarded != id) {//如果此玩家未被守卫
            switch (Role.roles.get(id)) {
                case "普通村民":
                    (players.get(id)).makeDead();
                    Role.villagerNumber--;
                    break;
                case "狼人":
                    (players.get(id)).makeDead();
                    Role.wolfmanNumber--;
                    break;
                case "女巫":
                    (players.get(id)).makeDead();
                    Role.witchNumber--;
                    break;
                case "守卫":
                    (players.get(id)).makeDead();
                    Role.guardNumber--;
                    guarded = -1;
                    break;
                case "预言家":
                    (players.get(id)).makeDead();
                    Role.seerNumber--;
                    break;
            }
            Message gameMeg = new Message();
            gameMeg.what = 5;
            ServerActivity.serverMessage = "玩家 " + id + " " + (players.get(id)).returnRole() + " 被狼人杀死";
            ServerActivity.mHandler.sendMessage(gameMeg);
        }
    }

    //接受编号，守卫玩家
    public static void guard(String ID){
        int id = Integer.parseInt(ID);
        for(int i = 0; i < Role.playerNumber; i++)
            if(Role.roles.get(i).equals("守卫")){
                guarded = id;
                Message gameMeg = new Message();
                gameMeg.what = 5;
                ServerActivity.serverMessage = "玩家 " + id + " " + (players.get(id)).returnRole() + " 被守卫";
                ServerActivity.mHandler.sendMessage(gameMeg);
                break;
            }
    }

    //接受编号，对玩家使用解药
    public static void save(String ID){
        int id = Integer.parseInt(ID);
        if(cure) {
            (players.get(id)).makeAlive();
            cure = false;
            Message gameMeg = new Message();
            gameMeg.what = 5;
            ServerActivity.serverMessage = "女巫对玩家 " + id + " " + (players.get(id)).returnRole() + " 使用解药";
            ServerActivity.mHandler.sendMessage(gameMeg);
        }
    }

    //接受编号，对玩家使用毒药
    public static void poison(String ID){
        int id = Integer.parseInt(ID);
        if(poison) {
            (players.get(id)).makeDead();
            poison = false;
            Message gameMeg = new Message();
            gameMeg.what = 5;
            ServerActivity.serverMessage = "女巫对玩家 " + id + " " + (players.get(id)).returnRole() + " 使用毒药";
            ServerActivity.mHandler.sendMessage(gameMeg);
        }
    }
}
