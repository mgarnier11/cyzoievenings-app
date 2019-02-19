package com.mgarnier11.CyzoisEvenings.models;

import java.io.Serializable;
import java.util.List;

public class Player implements Serializable {
    public String name;

    public int nbDrinked;

    public int nbQuestions;

    public int nbDone;

    public int gender;

    private String _imageUrl;

    public String getImageUrl() {
        return _imageUrl;
    }

    public void setImageUrl(String _imageUrl) {
        playerListener.onImageUrlChanged(_imageUrl);
        this._imageUrl = _imageUrl;
    }

    public transient Game game;

    public Player(Game game) {
        this.name = "";
        this.nbDrinked = 0;
        this.nbQuestions = 0;
        this.nbDone = 0;
        this.gender = 0;
        this._imageUrl = "";
        this.game = game;
    }

    public static Player getPlayerByGenderFromLst(List<Player> lst, int gender) {
        if (gender == 0 | gender == 1) {
            for (Player p : lst) {
                if (p.gender == gender) return p;
            }
            return null;
        } else {
            return lst.get(0);
        }
    }

    public int getplayerPoints() {
        return ((nbDrinked * 2) + nbQuestions + nbDone);
    }

    //region Events
    private transient PlayerEventListener playerListener;

    public void setEventListner(PlayerEventListener listener) {
        playerListener = listener;
    }

    public interface PlayerEventListener{
        void onImageUrlChanged(String newImageUrl);
    }

    public static class PlayerConsumer implements Player.PlayerEventListener {
        public void onImageUrlChanged(String newImageUrl) {

        }
    }
    //endregion
}
