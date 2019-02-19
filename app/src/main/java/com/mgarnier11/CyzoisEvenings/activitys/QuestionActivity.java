package com.mgarnier11.CyzoisEvenings.activitys;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mgarnier11.CyzoisEvenings.R;
import com.mgarnier11.CyzoisEvenings.fragments.LoadingFragment;
import com.mgarnier11.CyzoisEvenings.fragments.PlayerFragment;
import com.mgarnier11.CyzoisEvenings.fragments.QuestionGroupFragment;
import com.mgarnier11.CyzoisEvenings.fragments.QuestionSingleFragment;
import com.mgarnier11.CyzoisEvenings.models.Game;
import com.mgarnier11.CyzoisEvenings.models.Player;
import com.mgarnier11.CyzoisEvenings.models.Question;
import com.mgarnier11.CyzoisEvenings.models.Type;
import com.mgarnier11.CyzoisEvenings.models.Utils;

public class QuestionActivity extends AppCompatActivity {
    private final static int IMAGE_RESULT = 200;

    Game game;

    ConstraintLayout layoutAct;
    ConstraintLayout layoutPlayer;
    ConstraintLayout layoutQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        game = Game.getInstance();

        layoutAct = findViewById(R.id.activity_question_activityQuestionContainer);
        layoutPlayer = findViewById(R.id.activity_question_playerFragmentContainer);
        layoutQuestion = findViewById(R.id.activity_question_questionFragmentContainer);

        game.setEventListner(new Game.GameConsumer() {
            @Override
            public void onPlayerChanged(Player newPlayer) {
                super.onPlayerChanged(newPlayer);

                showPlayer(newPlayer);
            }

            @Override
            public void onQuestionChanged(Question newQuestion) {
                super.onQuestionChanged(newQuestion);

                showQuestion(newQuestion);
            }

            @Override
            public void onTypeChanged(Type newType) {
                super.onTypeChanged(newType);
            }
        });

        Boolean newAct = true;

        if (savedInstanceState != null) newAct = savedInstanceState.getBoolean("new");

        if (newAct) nextQuestion();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("new", false);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == IMAGE_RESULT) {
                game.actualPlayer.setImageUrl(Utils.getImageFromFilePath(this, data));

                showPlayer(game.actualPlayer);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    public void showQuestion(Question q) {
        Fragment fragment;
        if (game.actualType.group) {
            fragment = new QuestionGroupFragment();
        } else {
            fragment = new QuestionSingleFragment();
        }

        showQuestionFragment(fragment);
    }

    public void showPlayer(Player p) {
        Fragment fragment = new PlayerFragment();

        showPlayerFragment(fragment);
    }

    public void showQuestionFragment(Fragment fragment) {
        if (getSupportFragmentManager().findFragmentById(R.id.activity_question_questionFragmentContainer) != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .remove(getSupportFragmentManager().findFragmentById(R.id.activity_question_questionFragmentContainer))
                    .commitAllowingStateLoss();
        }
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.activity_question_questionFragmentContainer, fragment)
                .commitAllowingStateLoss();
    }

    public void showPlayerFragment(Fragment fragment) {
        if (getSupportFragmentManager().findFragmentById(R.id.activity_question_playerFragmentContainer) != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .remove(getSupportFragmentManager().findFragmentById(R.id.activity_question_playerFragmentContainer))
                    .commitAllowingStateLoss();
        }
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.activity_question_playerFragmentContainer, fragment)
                .commitAllowingStateLoss();
    }

    public void nextQuestion() {
        showPlayerFragment(new LoadingFragment());
        showQuestionFragment(new LoadingFragment());

        try{
            game.nextTurn();
        } catch (Game.GameFinishedException e) {
            Intent intent = new Intent(this, EndGameActivity.class);

            startActivity(intent);
            finish();
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(Game.getInstance());
        editor.putString("gameSaved", json);
        editor.apply();
    }

    public void takePhoto() {
        startActivityForResult(Utils.getPickImageChooserIntent(this), IMAGE_RESULT);
    }
}