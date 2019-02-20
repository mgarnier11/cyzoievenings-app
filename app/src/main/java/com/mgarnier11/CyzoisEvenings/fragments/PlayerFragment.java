package com.mgarnier11.CyzoisEvenings.fragments;


import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mgarnier11.CyzoisEvenings.R;
import com.mgarnier11.CyzoisEvenings.activitys.QuestionActivity;
import com.mgarnier11.CyzoisEvenings.models.Game;
import com.mgarnier11.CyzoisEvenings.models.Player;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlayerFragment extends Fragment {
    Game game;

    Player player;

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

        if (getArguments() != null) player  = (Player)getArguments().getSerializable("player");

        if (player == null) player = game.actualPlayer;

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

        textViewPlayerName.setText(player.name);

        Glide
                .with(getActivity().getApplicationContext())
                .load(player.getImageUrl())
                .centerCrop()
                .into(imageViewPlayerPhoto);


        imageViewPlayerPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((QuestionActivity)getActivity()).takePhoto();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
