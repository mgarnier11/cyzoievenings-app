package com.mgarnier11.CyzoisEvenings.adapters;


import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.mgarnier11.CyzoisEvenings.R;
import com.mgarnier11.CyzoisEvenings.models.Player;

import java.util.List;

public class PlayersEndAdapter extends RecyclerView.Adapter<PlayersEndAdapter.PlayerEndViewHolder>
{
    // Liste d'objets métier :
    private List<Player> listePlayers;
    // Constructeur :
    public PlayersEndAdapter(List<Player> listePlayers)
    {
        this.listePlayers = listePlayers;
    }
    @Override
    public PlayerEndViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View viewPlayer = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_item_player_end, parent, false);
        return new PlayerEndViewHolder(viewPlayer);
    }
    @Override
    public void onBindViewHolder(final PlayerEndViewHolder holder, int position)
    {
        final Player player = listePlayers.get(position);

        holder.textViewPlayerName.setText(player.name);
        holder.textViewNbAsked.setText(String.valueOf(player.nbQuestions));
        holder.textViewNbDone.setText(String.valueOf(player.nbDone));
        holder.textViewPercentage.setText(String.valueOf(player.nbDone * 100 / player.nbQuestions));
        holder.textViewNbDrinked.setText(String.valueOf(player.nbDrinked));

    }
    @Override
    public int getItemCount()
    {
        return listePlayers.size();
    }

    public class PlayerEndViewHolder extends RecyclerView.ViewHolder
    {
        // TextView intitulé Player :
        public TextView textViewPlayerName;
        public TextView textViewNbAsked;
        public TextView textViewNbDone;
        public TextView textViewPercentage;
        public TextView textViewNbDrinked;
        // Constructeur :
        public PlayerEndViewHolder(View itemView)
        {
            super(itemView);
            textViewPlayerName = itemView.findViewById(R.id.recycler_item_player_end_textViewPlayerName);
            textViewNbAsked = itemView.findViewById(R.id.recycler_item_player_end_textViewNbAsked);
            textViewNbDone = itemView.findViewById(R.id.recycler_item_player_end_textViewNbDone);
            textViewPercentage = itemView.findViewById(R.id.recycler_item_player_end_textViewPercentage);
            textViewNbDrinked = itemView.findViewById(R.id.recycler_item_player_end_textViewNbDrunk);
        }
    }
}