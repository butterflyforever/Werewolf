package com.example.werewolf.werewolf;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.werewolf.werewolf.Client.ClientActivity;
import com.example.werewolf.werewolf.Server.ServerActivity;

public class StartGame extends AppCompatActivity {
    private Button host;
    private Button player;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_game);
        //获取控件
        host = (Button) findViewById(R.id.host);
        player = (Button) findViewById(R.id.player);
        //监听按键
        host.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent(StartGame.this, ServerActivity.class);
                startActivity(intent1);
            }
        });
        player.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent(StartGame.this, ClientActivity.class);
                startActivity(intent1);
            }
        });


    }
}
