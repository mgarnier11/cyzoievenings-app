package com.mgarnier11.CyzoisEvenings.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class Question implements Serializable {
    public String id;

    public Type type;

    public int difficulty;

    public  int nbPicked;

    public int nbDone;

    public int nbPlayers;

    public boolean hidden;

    public String text;

    public int typeId;

    public int gender;

    public Question() {
        this.id = "";
        this.typeId = 0;
        this.type = null;
        this.difficulty = 0;
        this.nbPicked = 0;
        this.nbDone = 0;
        this.nbPlayers = 0;
        this.hidden = false;
        this.gender = -1;
        this.text = "";
    }

    public void setPlayersToText(List<Player> lstPlayers) {
        text = text.replace("{F}", "");
        text = text.replace("{M}", "");

        String findM = "{PlayerM}";
        Pattern patternM = Pattern.compile("\\{PlayerM\\}");
        String findF = "{PlayerF}";
        Pattern patternF = Pattern.compile("\\{PlayerF\\}");
        String findP = "{Player}";
        Pattern patternP = Pattern.compile("\\{Player\\}");

        for (Integer i: getStrpos(text, findM)) {
            Player pM = Player.getPlayerByGenderFromLst(lstPlayers, 0);
            if (pM != null) {
                text = text.replaceFirst(patternM.toString(), pM.name);

                lstPlayers.remove(pM);
            }
        }

        for (Integer i: getStrpos(text, findF)) {
            Player pF = Player.getPlayerByGenderFromLst(lstPlayers, 1);
            if (pF != null) {
                text = text.replaceFirst(patternF.toString(), pF.name);

                lstPlayers.remove(pF);
            }
        }

        for (Integer i: getStrpos(text, findP)) {
            Player p = Player.getPlayerByGenderFromLst(lstPlayers, -1);

            text = text.replaceFirst(patternP.toString(), p.name);

            lstPlayers.remove(p);
        }

    }

    private List<Integer> getStrpos(String base, String search) {
        int lastIndex = 0;

        List<Integer> result = new ArrayList<>();
        while(lastIndex != -1) {

            lastIndex = base.indexOf(search,lastIndex);

            if(lastIndex != -1){
                result.add(lastIndex);
                lastIndex += 1;
            }
        }

        return result;
    }

    public int getRndDrinks(int min, int max) {
        float rnd = (float)Game.rnd.nextInt(max - min) + min;

        int eff = nbDone * 100 / nbPicked;

        return (int)((rnd * (1 + (float)difficulty/10)) * (1 + (float)eff/20));
    }

    public String getDifficultyStars() {
        String difficultyText = "";
        for (int i = 0; i< 5; i++) {
            if (i < difficulty) difficultyText += "★";
            else difficultyText += "☆";
        }
        return  difficultyText;
    }
}
