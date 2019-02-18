package com.mgarnier11.CyzoisEvenings.fragments;


import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mgarnier11.CyzoisEvenings.R;
import com.mgarnier11.CyzoisEvenings.models.Game;
import com.mgarnier11.CyzoisEvenings.models.GameOld;
import com.mgarnier11.CyzoisEvenings.models.Player;
import com.mgarnier11.CyzoisEvenings.models.Question;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlayerFragment extends Fragment {
    Game game;

    TextView textViewPlayerName;

    ImageView imageViewPlayerPhoto;

    public PlayerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        game = Game.getInstance();

        return inflater.inflate(R.layout.fragment_player, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        updateUi();
    }

    public void updateUi() {
        textViewPlayerName = getActivity().findViewById(R.id.fragment_player_textViewPlayerName);

        imageViewPlayerPhoto = getActivity().findViewById(R.id.fragment_player_imageViewPhoto);

        textViewPlayerName.setText(game.actualPlayer.name);

        imageViewPlayerPhoto.setImageBitmap(BitmapFactory.decodeFile(game.actualPlayer.imageUrl));
    }

}
