package com.mgarnier11.CyzoisEvenings.activitys;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mgarnier11.CyzoisEvenings.MainActivity;
import com.mgarnier11.CyzoisEvenings.R;
import com.mgarnier11.CyzoisEvenings.adapters.PlayersAdapter;
import com.mgarnier11.CyzoisEvenings.models.Game;
import com.mgarnier11.CyzoisEvenings.models.Player;

import com.appyvet.materialrangebar.RangeBar;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CreateGameActivity extends AppCompatActivity {
    private static final int REQUEST_CAPTURE_IMAGE = 100;

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
        //textViewMaxDrinks.setText(getResources().getString(R.string.textViewMaxDrinksText, (int)(10 * 1.5 * 1.5)));

        buttonPhoto = findViewById(R.id.activity_create_game_buttonPhoto);

        rangeBarDiffMax = findViewById(R.id.activity_create_game_seekBarDifficulty);
        rangeBarNbDrinks = findViewById(R.id.activity_create_game_seekBarDrinks);
        rangeBarNbDrinks.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex, int rightPinIndex, String leftPinValue, String rightPinValue) {
                game.nbDrinkMin = Integer.parseInt(rangeBarNbDrinks.getLeftPinValue());
                game.nbDrinkMax = Integer.parseInt(rangeBarNbDrinks.getRightPinValue());

                //textViewMaxDrinks.setText(getResources().getString(R.string.textViewMaxDrinksText, (int)(game.nbDrinkMax * 1.5 * 1.5)));
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
    //<editor-fold desc="onActRes">
/*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CAPTURE_IMAGE &&
                resultCode == RESULT_OK) {
            Bitmap thumbImage = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(game.groupImageUrl.toString()),
                    64, 64);

            Drawable d = new BitmapDrawable(getResources(), thumbImage);

            buttonPhoto.setBackground(d);
        }

    }
*/
//</editor-fold>

    public void onAddPlayerClick(View v) {
        game.lstPlayers.add(new Player(game));

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
            Toast.makeText(this, MainActivity.getStringIdentifier(this, e.getMessage()), Toast.LENGTH_SHORT).show();
        }
    }

    public void onPhotoClick(View v) {
        Intent pictureIntent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);
        if (pictureIntent.resolveActivity(getPackageManager()) != null) {
            //Create a file to store the image
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Toast.makeText(this, R.string.savePhotoError, Toast.LENGTH_SHORT);
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this, "com.mgarnier11.CyzoisEvenings.provider", photoFile);
                game.group.imageUrl = photoFile.toString();
                pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(pictureIntent, REQUEST_CAPTURE_IMAGE);
            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp =
                new SimpleDateFormat("yyyyMMdd_HHmmss",
                        Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        return image;
    }
}
