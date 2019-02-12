package com.mgarnier11.CyzoisEvenings.activitys;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.mgarnier11.CyzoisEvenings.MainActivity;
import com.mgarnier11.CyzoisEvenings.R;
import com.mgarnier11.CyzoisEvenings.adapters.PlayersAdapter;
import com.mgarnier11.CyzoisEvenings.adapters.PlayersEndAdapter;
import com.mgarnier11.CyzoisEvenings.models.Game;
import com.mgarnier11.CyzoisEvenings.models.Player;

import java.util.Collections;
import java.util.Comparator;

public class EndGameActivity extends AppCompatActivity {

    Game game;
    PlayersEndAdapter playersEndAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
/*
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();*/

        setContentView(R.layout.activity_end_game);

        game = Game.getInstance();

        RecyclerView recyclerViewPlayers = findViewById(R.id.activity_end_game_recyclerViewEndPlayers);
        recyclerViewPlayers.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewPlayers.setLayoutManager(layoutManager);

        Collections.sort(game.lstPlayers, new Comparator<Player>() {
            @Override
            public int compare(Player o1, Player o2) {
                return o1.getplayerPoints() - o2.getplayerPoints();
            }
        });

        playersEndAdapter = new PlayersEndAdapter(game.lstPlayers);
        recyclerViewPlayers.setAdapter(playersEndAdapter);
    }

    public void onButtonContinueClick(View v) {
        Game.setInstance(new Game());

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
