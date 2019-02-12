package com.mgarnier11.CyzoisEvenings;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.mgarnier11.CyzoisEvenings.activitys.CreateGameActivity;
import com.mgarnier11.CyzoisEvenings.activitys.QuestionActivity;
import com.mgarnier11.CyzoisEvenings.models.Game;
import com.mgarnier11.CyzoisEvenings.services.OnClearFromRecentService;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        startService(new Intent(getBaseContext(), OnClearFromRecentService.class));
    }

    public void buttonStartNewGameClick(View v) {
        startNewGameActivity();
    }

    public void buttonResumeGameClick(View v) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        Gson gson = new Gson();
        try {
            String json = preferences.getString("game", "");
            Game game = gson.fromJson(json, Game.class);
            if (game != null) {
                Game.setInstance(game);

                Intent intent = new Intent(this, QuestionActivity.class);
                startActivity(intent);
            } else {
                startNewGameActivity();
            }
        } catch (Exception e) {
            Log.i("debug", e.getMessage());

            startNewGameActivity();
        }
    }

    public void startNewGameActivity() {
        Intent intent = new Intent(this, CreateGameActivity.class);
        startActivity(intent);
    }
}
