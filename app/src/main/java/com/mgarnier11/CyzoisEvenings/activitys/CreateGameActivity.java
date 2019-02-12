package com.mgarnier11.CyzoisEvenings.activitys;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mgarnier11.CyzoisEvenings.R;
import com.mgarnier11.CyzoisEvenings.adapters.PlayersAdapter;
import com.mgarnier11.CyzoisEvenings.models.Game;
import com.mgarnier11.CyzoisEvenings.models.Player;

import com.appyvet.materialrangebar.RangeBar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CreateGameActivity extends AppCompatActivity {
    public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 0;

    private Game game;

    private PlayersAdapter playersAdapter;

    private RangeBar rangeBarNbDrinks;
    private RangeBar rangeBarDiffMax;

    private EditText editTextNbTurns;

    private TextView textViewMaxDrinks;

    private Camera camera;
    private int cameraId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        game = Game.getInstance();

        setContentView(R.layout.activity_create_game);

        //getWindow().getDecorView().setBackground(R.style.);

        textViewMaxDrinks = findViewById(R.id.activity_create_game_textViewMaxDrinks);
        textViewMaxDrinks.setText(getResources().getString(R.string.textViewMaxDrinksText, (int)(10 * 1.5 * 1.5)));

        rangeBarDiffMax = findViewById(R.id.activity_create_game_seekBarDifficulty);
        rangeBarNbDrinks = findViewById(R.id.activity_create_game_seekBarDrinks);
        rangeBarNbDrinks.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex, int rightPinIndex, String leftPinValue, String rightPinValue) {
                game.nbDrinkMin = Integer.parseInt(rangeBarNbDrinks.getLeftPinValue());
                game.nbDrinkMax = Integer.parseInt(rangeBarNbDrinks.getRightPinValue());

                textViewMaxDrinks.setText(getResources().getString(R.string.textViewMaxDrinksText, (int)(game.nbDrinkMax * 1.5 * 1.5)));
            }
        });

        editTextNbTurns = findViewById(R.id.activity_create_game_editTextNbTurns);

        RecyclerView recyclerViewPlayers = findViewById(R.id.activity_create_game_recyclerViewPlayers);
        recyclerViewPlayers.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewPlayers.setLayoutManager(layoutManager);

        playersAdapter = new PlayersAdapter(game.lstPlayers, this);

        recyclerViewPlayers.setAdapter(playersAdapter);
    }

    public void onAddPlayerClick(View v) {
        game.lstPlayers.add(new Player(game));

        playersAdapter.notifyDataSetChanged();
    }

    public void onStartClick(View v) {

        if (!game.checkPlayers()) {
            Toast.makeText(this, R.string.toastNotEnoughPlayers,Toast.LENGTH_SHORT).show();
        } else {
            game.nbDrinkMin = Integer.parseInt(rangeBarNbDrinks.getLeftPinValue());
            game.nbDrinkMax = Integer.parseInt(rangeBarNbDrinks.getRightPinValue());
            game.difficultyMax = Integer.parseInt(rangeBarDiffMax.getRightPinValue());

            String nbTurns = editTextNbTurns.getText().toString();
            if (nbTurns.isEmpty()) nbTurns = editTextNbTurns.getHint().toString();

            game.setNbTurns(Integer.parseInt(nbTurns));

            Intent intent = new Intent(this, QuestionActivity.class);

            startActivity(intent);
            finish();
        }

    }
}
