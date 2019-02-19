package com.mgarnier11.CyzoisEvenings.activitys;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mgarnier11.CyzoisEvenings.R;
import com.mgarnier11.CyzoisEvenings.adapters.PlayersAdapter;
import com.mgarnier11.CyzoisEvenings.models.Game;
import com.mgarnier11.CyzoisEvenings.models.Player;

import com.appyvet.materialrangebar.RangeBar;
import com.mgarnier11.CyzoisEvenings.models.Utils;

public class CreateGameActivity extends AppCompatActivity {
    private final static int IMAGE_RESULT = 200;

    private Game game;

    private PlayersAdapter playersAdapter;

    private Button buttonPhoto;

    private RangeBar rangeBarNbDrinks;
    private RangeBar rangeBarDiffMax;

    private EditText editTextNbTurns;

    private TextView textViewMaxDrinks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        game = Game.getInstance();

        game.startServer();

        setContentView(R.layout.activity_create_game);

        textViewMaxDrinks = findViewById(R.id.activity_create_game_textViewMaxDrinks);
        textViewMaxDrinks.setText(getResources().getString(R.string.textViewMaxDrinksText, (int)(10 * 1.5 * 1.5)));

        buttonPhoto = findViewById(R.id.activity_create_game_buttonPhoto);

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

        game.group.setEventListner(new Player.PlayerConsumer() {
            @Override
            public void onImageUrlChanged(String newImageUrl) {
                super.onImageUrlChanged(newImageUrl);

                Bitmap thumbImage = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(newImageUrl),
                        64, 64);

                Drawable d = new BitmapDrawable(getResources(), thumbImage);

                buttonPhoto.setBackground(d);
            }
        });

        RecyclerView recyclerViewPlayers = findViewById(R.id.activity_create_game_recyclerViewPlayers);
        recyclerViewPlayers.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewPlayers.setLayoutManager(layoutManager);

        playersAdapter = new PlayersAdapter(game.lstPlayers, this);

        recyclerViewPlayers.setAdapter(playersAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == IMAGE_RESULT) {
                game.actualPlayer.setImageUrl(Utils.getImageFromFilePath(this, data));
            }
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    public void onAddPlayerClick(View v) {
        game.lstPlayers.add(new Player(game));

        game.actualPlayer = game.lstPlayers.get(game.lstPlayers.size() - 1);

        playersAdapter.notifyDataSetChanged();
    }

    public void onStartClick(View v) {
        try {
            game.checkPlayers();

            game.nbDrinkMin = Integer.parseInt(rangeBarNbDrinks.getLeftPinValue());
            game.nbDrinkMax = Integer.parseInt(rangeBarNbDrinks.getRightPinValue());
            game.maxDifficulty = Integer.parseInt(rangeBarDiffMax.getRightPinValue());

            String nbTurns = editTextNbTurns.getText().toString();
            if (nbTurns.isEmpty()) nbTurns = editTextNbTurns.getHint().toString();

            game.setPlayerOrder(Integer.parseInt(nbTurns));

            Intent intent = new Intent(this, QuestionActivity.class);

            startActivity(intent);
            finish();

        } catch (Exception e) {
            Toast.makeText(this, Utils.getStringIdentifier(this, e.getMessage()), Toast.LENGTH_SHORT).show();
        }
    }

    public void onPhotoClick(View v) {
        game.actualPlayer = game.group;

        takePhoto();
    }

    public void takePhoto() {
        startActivityForResult(Utils.getPickImageChooserIntent(this), IMAGE_RESULT);
    }
}
