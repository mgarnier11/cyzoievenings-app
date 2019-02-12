package com.mgarnier11.CyzoisEvenings.activitys;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mgarnier11.CyzoisEvenings.R;
import com.mgarnier11.CyzoisEvenings.fragments.AskQuestionFragment;
import com.mgarnier11.CyzoisEvenings.fragments.GroupQuestionFragment;
import com.mgarnier11.CyzoisEvenings.fragments.LoadingFragment;
import com.mgarnier11.CyzoisEvenings.fragments.ResultQuestionFragment;
import com.mgarnier11.CyzoisEvenings.models.Game;
import com.mgarnier11.CyzoisEvenings.models.Player;
import com.mgarnier11.CyzoisEvenings.models.Question;
import com.mgarnier11.CyzoisEvenings.models.Type;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class QuestionActivity extends AppCompatActivity {
    Game game;
    Question chosedQuestion;
    Player chosedPlayer;

    int rndDrinks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);


        game = Game.getInstance();

        nextQuestion(false);
    }

    public void showFragment(Fragment fragment) {
        if(getSupportFragmentManager().findFragmentById(R.id.activity_question_activityQuestionFragmentContainer) != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .remove(getSupportFragmentManager().findFragmentById(R.id.activity_question_activityQuestionFragmentContainer))
                    .commit();
        }
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.activity_question_activityQuestionFragmentContainer, fragment)
                .commit();
    }

    public void nextQuestion(boolean isRetrying) {
        showFragment(new LoadingFragment());

        if (game.currentTurn  == game.getNbTurns()) {
            Intent intent = new Intent(this, EndGameActivity.class);

            startActivity(intent);
            finish();
        } else {
            if (!isRetrying) {
                chosedPlayer = game.getNextPlayer();

                game.currentTurn++;
            }

            getTypeThenQuestion();
        }
    }

    public void getQuestion(Type t) {
        game.randomQuestionByMaxDifficultyAndTypeIdAndMaxPlayersAndGender(t.id, game.difficultyMax, game.lstPlayers.size() - 1, chosedPlayer.gender, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // Root JSON in response is an dictionary i.e { "data : [ ... ] }
                // Handle resulting parsed JSON response here
                Question q = new Gson().fromJson(response.toString(), Question.class);

                q.setPlayersToText(game.getLstRandomPlayersDifferent(chosedPlayer, q.nbPlayers));

                game.lastQuestion = game.currentQuestion;
                chosedQuestion =  game.currentQuestion = q;

                rndDrinks = q.getRndDrinks(game.nbDrinkMin, game.nbDrinkMax);

                if (!chosedQuestion.type.value.contains("groupe")) {
                    chosedPlayer.nbQuestions++;
                }

                showQuestion(null, null);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);

                Toast.makeText(QuestionActivity.this, responseString + " Retrying", Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void getTypeThenQuestion() {
        game.randomTypeW(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Type t = new Gson().fromJson(response.toString(), Type.class);

                getQuestion(t);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);

                Toast.makeText(QuestionActivity.this, responseString + " Retrying", Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void showQuestion(Question q, Player p) {
        Fragment fragment;
        Bundle bundle = new Bundle();
        if (p != null & q != null) {
            if (q.type.value.contains("groupe")) {
                fragment = new GroupQuestionFragment();
                bundle.putInt("rndDrinks", rndDrinks);
            } else {
                fragment = new AskQuestionFragment();
            }
            bundle.putSerializable("question", q);
            bundle.putSerializable("player", p);
        } else {
            if (chosedQuestion.type.value.contains("groupe")) {
                fragment = new GroupQuestionFragment();
                bundle.putInt("rndDrinks", rndDrinks);
            } else {
                fragment = new AskQuestionFragment();
            }
            bundle.putSerializable("question", chosedQuestion);
            bundle.putSerializable("player", chosedPlayer);
        }
        fragment.setArguments(bundle);

        showFragment(fragment);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        moveTaskToBack(true);
    }

}
