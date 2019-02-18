package com.mgarnier11.CyzoisEvenings.models;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;


public class GameOld implements Serializable {
    private static GameOld instance;

    public static GameOld getInstance() {
        if (instance == null) instance = new GameOld();
        return instance;
    }

    public static GameOld setInstance(GameOld nInstance) {
        instance = new GameOld(nInstance);

        return instance;
    }

    public transient static String API_URL = "https://cyzoievenings-backend.herokuapp.com/";

    private transient AsyncHttpClient asyncClient;

    public transient static Random rnd = new Random();

    private List<Player> lstPlayersOrder;

    public int nbDrinkMin;

    public int nbDrinkMax;

    public String groupImageUrl;

    private int nbTurns;

    public int getNbTurns() {
        return nbTurns;
    }

    public void setNbTurns(int nbTurns) {
        lstPlayersOrder = new ArrayList<>();

        int nbPlayerIteration = nbTurns / lstPlayers.size();

        for (Player p:lstPlayers) {
            for (int i = 0; i < nbPlayerIteration; i++) {
                lstPlayersOrder.add(p);
            }
        }

        Collections.shuffle(lstPlayersOrder);

        this.nbTurns = nbTurns;
    }

    public int currentTurn;

    public int difficultyMax;

    public List<Player> lstPlayers;

    public Question lastQuestion;

    public Question currentQuestion;


    public GameOld() {
        this.nbDrinkMin = 0;
        this.nbDrinkMax = 0;
        this.nbTurns = 0;
        this.difficultyMax = 0;
        this.currentTurn = 0;
        this.lastQuestion = null;
        this.currentQuestion = null;
        this.lstPlayers = new ArrayList<>();
        this.asyncClient = new AsyncHttpClient();
        GameOld.rnd = new Random();
    }

    public GameOld(GameOld oldGameOld) {
        this.nbDrinkMin = oldGameOld.nbDrinkMin;
        this.nbDrinkMax = oldGameOld.nbDrinkMax;
        this.nbTurns = oldGameOld.nbTurns;
        this.difficultyMax = oldGameOld.difficultyMax;
        this.currentTurn = oldGameOld.currentTurn;
        this.lastQuestion = oldGameOld.lastQuestion;
        this.currentQuestion = oldGameOld.currentQuestion;
        this.lstPlayers = oldGameOld.lstPlayers;
        this.asyncClient = new AsyncHttpClient();
        GameOld.rnd = new Random();
    }

    public void deletePlayer(Player player) {
        lstPlayers.remove(player);
    }


    public void randomTypeW(JsonHttpResponseHandler handler) {
        apiGetCall("types/randomW", handler);
    }

    public void randomQuestion(JsonHttpResponseHandler handler) {
        apiGetCall("questions/random", handler);
    }

    public void randomQuestionByMaxDifficultyAndTypeIdAndMaxPlayersAndGender(int typeId, int maxDifficulty, int maxPlayers, int gender, JsonHttpResponseHandler handler) {
        apiGetCall("questions/WrandomTMDPG/" + String.valueOf(typeId) + "/" + String.valueOf(maxDifficulty) + "/" + String.valueOf(maxPlayers) + "/" + String.valueOf(gender), handler);
    }

    public void questionDone(Question q, JsonHttpResponseHandler handler) {
        apiGetCall("questions/done/" + q.id, handler);
    }

    public void startServer() {
        apiGetCall("/", new JsonHttpResponseHandler());
    }

    public Player getRandomPlayer() {
        return lstPlayers.get(GameOld.rnd.nextInt(lstPlayers.size()));
    }

    public Player getNextPlayer(){

        try {
            return lstPlayersOrder.get(currentTurn);
        } catch (Exception e) {
            return getRandomPlayer();
        }
    }

    public Player reformatTakingPlayer() {
        for (Player p: lstPlayers) {
            if (p.imageUrl.substring(0, 7) == "taking:") p.imageUrl = p.imageUrl.substring(7);
        }
        return null;
    }

    public List<Player> getLstRandomPlayersDifferent(Player p, int nb) {
        List<Player> lst = new ArrayList<>(lstPlayers);
        lst.remove(p);

        List<Player> lstP = new ArrayList<>();

        for (int i = 0; i < nb; i++) {
            Player newPlayer = lst.get(GameOld.rnd.nextInt(lst.size()));
            lst.remove(newPlayer);

            lstP.add(newPlayer);
        }

        return lstP;
    }

    public boolean checkPlayers() {
        boolean ret = true;
        if(lstPlayers.size() >= 2) {
            for (Player p: lstPlayers) {
                if (p.name.isEmpty()) ret = false;
            }
        } else return false;
        return ret;
    }

    private void apiGetCall(String uri, JsonHttpResponseHandler handler) {
        if (asyncClient == null) asyncClient = new AsyncHttpClient();

        asyncClient.get(GameOld.API_URL + uri, handler);
    }
}
