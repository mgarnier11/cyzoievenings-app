package com.mgarnier11.CyzoisEvenings.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
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
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PlayersAdapter extends RecyclerView.Adapter<PlayersAdapter.PlayerViewHolder>
{
    // Liste d'objets métier :
    private List<Player> listePlayers;
    private Activity parent;
    private Context context;
    // Constructeur :
    public PlayersAdapter(List<Player> listePlayers, Activity parent)
    {
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
        final Player player = listePlayers.get(position);

        if (player.gender == 0) holder.buttonPlayerGender.setText(R.string.buttonGenderMaleText);
        else if (player.gender == 1) holder.buttonPlayerGender.setText(R.string.buttonGenderFemaleText);
        else holder.buttonPlayerGender.setText(R.string.errorText);

        holder.editTextPlayerName.setText(player.name);
    }
    @Override
    public int getItemCount()
    {
        return listePlayers.size();
    }

    public class PlayerViewHolder extends RecyclerView.ViewHolder
    {
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

            editTextPlayerName = itemView.findViewById(R.id.recycler_item_player_editTextPlayerName);
            buttonPlayerGender = itemView.findViewById(R.id.recycler_item_player_buttonGender);
            buttonDeletePlayer = itemView.findViewById(R.id.recycler_item_player_buttonDeletePlayer);
            buttonPlayerPhoto= itemView.findViewById(R.id.recycler_item_player_buttonPhoto);

            buttonDeletePlayer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Player p = listePlayers.get(getAdapterPosition());

                    p.game.lstPlayers.remove(p);

                    notifyDataSetChanged();
                }
            });

            buttonPlayerGender.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Player p = listePlayers.get(getAdapterPosition());

                    if (p.gender == 0) p.gender = 1;
                    else p.gender = 0;

                    p.game.lstPlayers.set(getAdapterPosition(), p);

                    notifyDataSetChanged();
                }
            });

            buttonPlayerPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Player p = listePlayers.get(getAdapterPosition());

                    //Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    //parent.startActivityForResult(intent, CreateGameActivity.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);

                    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                    String imageFileName = timeStamp + ".jpg";
                    String pictureImagePath = getDir().getAbsolutePath() + File.separator + imageFileName;
                    File file = new File(pictureImagePath);
                    p.imageUrl = pictureImagePath;
                    Uri outputFileUri = Uri.fromFile(file);
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
                    parent.startActivityForResult(cameraIntent, 1);
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
        }

        private File getDir() {
            File sdDir = new File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) +
                            File.separator +
                            "CyzoiEveningsPhotos");
            boolean success = true;
            if (!sdDir.exists()) {
                success = sdDir.mkdirs();
            }
            return sdDir;
        }
    }
}