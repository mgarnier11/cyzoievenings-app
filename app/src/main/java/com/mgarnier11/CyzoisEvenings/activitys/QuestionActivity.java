package com.mgarnier11.CyzoisEvenings.activitys;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceManager;
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

public class QuestionActivity extends AppCompatActivity {
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
                    .commit();
        }
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.activity_question_questionFragmentContainer, fragment)
                .commit();
    }

    public void showPlayerFragment(Fragment fragment) {
        if (getSupportFragmentManager().findFragmentById(R.id.activity_question_playerFragmentContainer) != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .remove(getSupportFragmentManager().findFragmentById(R.id.activity_question_playerFragmentContainer))
                    .commit();
        }
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.activity_question_playerFragmentContainer, fragment)
                .commit();
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
        editor.putString("game", json);
        editor.apply();

        //moveTaskToBack(true);
    }

}