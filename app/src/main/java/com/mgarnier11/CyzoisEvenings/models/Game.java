package com.mgarnier11.CyzoisEvenings.models;

import android.util.Log;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import cz.msebera.android.httpclient.Header;

public class Game implements Serializable {
    //region Properties
    private static Game instance;

    public static Game getInstance() {
        if (instance == null) instance = new Game();
        return instance;
    }

    public static Game setInstance(Game nInstance) {
        instance = new Game(nInstance);

        for (Player p : instance.lstPlayers) {
            p.game = instance;
        }

        return instance;
    }


    public static transient String API_URL = "https://cyzoievenings-backend.herokuapp.com/";

    public static transient Random rnd = new Random();

    private static transient String apiErrorCode = "apiError";



    public List<Player> lstPlayers;

    public List<Player> lstPlayersOrder;

    public Player group;

    public Player actualPlayer;

    public Question actualQuestion;

    public Type actualType;

    public int nbDrinkMin;

    public int nbDrinkMax;

    public int maxDifficulty;


    private transient AsyncHttpClient asyncClient;

    //endregion

    //region Constructors
    public Game()
    {
        lstPlayers = new ArrayList<>();
        lstPlayersOrder = new ArrayList<>();
        setGroup();
        actualPlayer = null;
        actualQuestion = null;
        actualType = null;
        nbDrinkMin = 0;
        nbDrinkMax = 0;
        maxDifficulty = 0;
        asyncClient = new AsyncHttpClient();
    }

    public Game(Game oldGame)
    {
        lstPlayers = oldGame.lstPlayers;
        lstPlayersOrder = oldGame.lstPlayersOrder;
        group = oldGame.group;
        actualPlayer = oldGame.actualPlayer;
        actualQuestion = oldGame.actualQuestion;
        actualType = oldGame.actualType;
        nbDrinkMin = oldGame.nbDrinkMin;
        nbDrinkMax = oldGame.nbDrinkMax;
        maxDifficulty = oldGame.maxDifficulty;
        asyncClient = new AsyncHttpClient();
    }

    private void setGroup() {
        group = new Player(this);
        group.gender = -1;
        group.name = "Group";
    }
    //endregion

    public void checkPlayers() throws Exception{
        if(lstPlayers.size() >= 2) {
            for (Player p: lstPlayers) {
                if (p.name.isEmpty()) {
                    throw new Exception("playerNameWrong");
                }
            }
        } else {
            throw new Exception("notEnoughPlayers");
        }
    }

    public void setPlayerOrder(int nbTurns) {
        lstPlayersOrder = new ArrayList<>();

        int nbPlayerIteration = nbTurns / (lstPlayers.size() + 1);

        for (Player p:lstPlayers) {
            for (int i = 0; i < nbPlayerIteration; i++) {
                lstPlayersOrder.add(p);
            }
        }

        for (int i = 0; i < nbPlayerIteration; i++) {
            lstPlayersOrder.add(group);
        }

        Collections.shuffle(lstPlayersOrder);
    }

    public void nextTurn() throws GameFinishedException {
        try {
            getNextPlayer(true);
        }catch (IndexOutOfBoundsException e) {
            throw new GameFinishedException();
        } catch (Exception e) {
            Log.i("a", e.getMessage());
        }
    }

    public void getNextPlayer(boolean nextT){
        actualPlayer = lstPlayersOrder.get(0);
        lstPlayersOrder.remove(0);

        gameListener.onPlayerChanged(actualPlayer);

        if (nextT) getNextType(nextT);
    }

    public void getNextType(final boolean nextQ) {
        randomType((actualPlayer == group ? true : false) ,new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Type t = new Gson().fromJson(response.toString(), Type.class);

                actualType = t;

                gameListener.onTypeChanged(t);

                if (nextQ) getNextQuestion();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.i(apiErrorCode, responseString);
            }
        });
    }

    public void getNextQuestion() {
        randomQuestion(actualType.id, maxDifficulty, lstPlayers.size() - 1, actualPlayer.gender, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // Root JSON in response is an dictionary i.e { "data : [ ... ] }
                // Handle resulting parsed JSON response here
                Question q = new Gson().fromJson(response.toString(), Question.class);

                q.setPlayersToText(getRandomPlayers(actualPlayer, q.nbPlayers));

                actualQuestion = q;

                gameListener.onQuestionChanged(q);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.i(apiErrorCode, responseString);
            }
        });
    }

    public List<Player> getRandomPlayers(Player p, int nb) {
        List<Player> lst = new ArrayList<>(lstPlayers);
        lst.remove(p);

        List<Player> lstP = new ArrayList<>();

        for (int i = 0; i < nb; i++) {
            Player newPlayer = lst.get(Game.rnd.nextInt(lst.size()));
            lst.remove(newPlayer);

            lstP.add(newPlayer);
        }

        return lstP;
    }

    //region ApiCalls
    public void startServer() {
        apiGetCall("/", new JsonHttpResponseHandler());
    }

    public void randomType(boolean group, JsonHttpResponseHandler handler) {
        apiGetCall("types/randomWG/" + (group ? "1" : "0"), handler);
    }

    public void randomQuestion(int typeId, int maxDifficulty, int maxPlayers, int gender, JsonHttpResponseHandler handler) {
        apiGetCall("questions/WrandomTMDPG/" + String.valueOf(typeId) + "/" + String.valueOf(maxDifficulty) + "/" + String.valueOf(maxPlayers) + "/" + String.valueOf(gender), handler);
    }

    public void questionDone(Question q, JsonHttpResponseHandler handler) {
        apiGetCall("questions/done/" + q.id, handler);
    }

    private void apiGetCall(String uri, JsonHttpResponseHandler handler) {
        if (asyncClient == null) asyncClient = new AsyncHttpClient();

        asyncClient.get(Game.API_URL + uri, handler);
    }
    //endregion

    //region Events
    private transient GameEventListener gameListener;

    public void setEventListner(GameEventListener listener) {
        gameListener = listener;
    }

    public interface GameEventListener{
        void onQuestionChanged(Question newQuestion);
        void onTypeChanged(Type newType);
        void onPlayerChanged(Player newPlayer);
    }

    public static class GameConsumer implements GameEventListener {
        public void onQuestionChanged(Question newQuestion) {

        }

        public void onTypeChanged(Type newType) {

        }

        public void onPlayerChanged(Player newPlayer) {

        }
    }
    //endregion

    public class GameFinishedException extends Exception {
        public GameFinishedException() {

        }
    }
}

