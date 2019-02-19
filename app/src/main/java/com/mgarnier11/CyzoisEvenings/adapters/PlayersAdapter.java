package com.mgarnier11.CyzoisEvenings.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.mgarnier11.CyzoisEvenings.R;
import com.mgarnier11.CyzoisEvenings.activitys.CreateGameActivity;
import com.mgarnier11.CyzoisEvenings.models.Game;
import com.mgarnier11.CyzoisEvenings.models.Player;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PlayersAdapter extends RecyclerView.Adapter<PlayersAdapter.PlayerViewHolder>
{
    private Game game;
    private List<Player> listePlayers;
    private CreateGameActivity parent;
    private Context context;
    // Constructeur :
    public PlayersAdapter(List<Player> listePlayers, CreateGameActivity parent)
    {
        this.game = Game.getInstance();

        this.listePlayers = listePlayers;
        this.parent = parent;
    }
    @Override
    public PlayerViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View viewPlayer = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_item_player, parent, false);
        return new PlayerViewHolder(viewPlayer);
    }
    @Override
    public void onBindViewHolder(final PlayerViewHolder holder, int position)
    {
        Player p = listePlayers.get(position);

        if (p.gender == 0) holder.buttonPlayerGender.setText(R.string.buttonGenderMaleText);
        else if (p.gender == 1) holder.buttonPlayerGender.setText(R.string.buttonGenderFemaleText);
        else holder.buttonPlayerGender.setText(R.string.errorText);

        holder.editTextPlayerName.setText(p.name);
    }
    @Override
    public int getItemCount()
    {
        return listePlayers.size();
    }

    public class PlayerViewHolder extends RecyclerView.ViewHolder
    {
        private static final int REQUEST_CAPTURE_IMAGE = 100;

        // TextView intitulé Player :
        public TextView editTextPlayerName;
        public Button buttonPlayerGender;
        public Button buttonDeletePlayer;
        public Button buttonPlayerPhoto;
        // Constructeur :
        public PlayerViewHolder(View itemView)
        {
            super(itemView);

            context = itemView.getContext();
            Log.i("ae", String.valueOf(getAdapterPosition()));

            final Player p = game.actualPlayer;

            editTextPlayerName = itemView.findViewById(R.id.recycler_item_player_editTextPlayerName);
            buttonPlayerGender = itemView.findViewById(R.id.recycler_item_player_buttonGender);
            buttonDeletePlayer = itemView.findViewById(R.id.recycler_item_player_buttonDeletePlayer);
            buttonPlayerPhoto= itemView.findViewById(R.id.recycler_item_player_buttonPhoto);

            buttonDeletePlayer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    p.game.lstPlayers.remove(p);

                    notifyDataSetChanged();
                }
            });

            buttonPlayerGender.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (p.gender == 0) p.gender = 1;
                    else p.gender = 0;

                    p.game.lstPlayers.set(getAdapterPosition(), p);

                    notifyDataSetChanged();
                }
            });

            buttonPlayerPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    game.actualPlayer = listePlayers.get(getAdapterPosition());

                    parent.takePhoto();
                }

            });

            editTextPlayerName.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    Player p = listePlayers.get(getAdapterPosition());

                    p.name = s.toString();
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            p.setEventListner(new Player.PlayerConsumer() {
                @Override
                public void onImageUrlChanged(String newImageUrl) {
                    super.onImageUrlChanged(newImageUrl);

                    Bitmap thumbImage = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(newImageUrl),
                            64, 64);

                    Drawable d = new BitmapDrawable(context.getResources(), thumbImage);

                    buttonPlayerPhoto.setBackground(d);
                }
            });
        }
    }
}