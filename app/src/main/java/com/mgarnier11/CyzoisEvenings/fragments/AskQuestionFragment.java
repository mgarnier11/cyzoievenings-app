package com.mgarnier11.CyzoisEvenings.fragments;


import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.mgarnier11.CyzoisEvenings.R;
import com.mgarnier11.CyzoisEvenings.activitys.QuestionActivity;
import com.mgarnier11.CyzoisEvenings.models.Game;
import com.mgarnier11.CyzoisEvenings.models.Player;
import com.mgarnier11.CyzoisEvenings.models.Question;

/**
 * A simple {@link Fragment} subclass.
 */
public class AskQuestionFragment extends Fragment {

    TextView textViewQuestion;
    TextView textViewDifficulty;
    TextView textViewPlayerName;

    ImageView imageViewPlayerPhoto;

    Button buttonDone;
    Button buttonNotDone;

    Question chosedQuestion;
    Player chosedPlayer;
    Game game;

    public AskQuestionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v;

        game = Game.getInstance();

        chosedQuestion = (Question)getArguments().getSerializable("question");
        chosedPlayer = (Player) getArguments().getSerializable("player");

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            v = inflater.inflate(R.layout.fragment_ask_question_horizontal, container, false);
        } else {
            v = inflater.inflate(R.layout.fragment_ask_question_vertical, container, false);
        }

        return v;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        ((QuestionActivity)getActivity()).showQuestion(chosedQuestion, chosedPlayer);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        updateUi();
    }

    public void updateUi() {
        textViewQuestion = getActivity().findViewById(R.id.fragment_ask_question_textViewQuestion);
        textViewDifficulty = getActivity().findViewById(R.id.fragment_ask_question_textViewDifficulty);
        textViewPlayerName = getActivity().findViewById(R.id.fragment_ask_question_textViewPlayerName);

        imageViewPlayerPhoto = getActivity().findViewById(R.id.fragment_ask_question_imageViewPhoto);

        buttonDone = getActivity().findViewById(R.id.fragment_ask_question_buttonDone);
        buttonNotDone = getActivity().findViewById(R.id.fragment_ask_question_buttonNotDone);

        buttonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonDoneOnClick(v);
            }
        });
        buttonNotDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonNotDoneOnClick(v);
            }
        });

        textViewQuestion.setText(chosedQuestion.text);
        textViewPlayerName.setText(chosedPlayer.name);

        textViewDifficulty.setText(chosedQuestion.getDifficultyStars());

        imageViewPlayerPhoto.setImageBitmap(BitmapFactory.decodeFile(chosedPlayer.imageUrl));
    }

    public void buttonDoneOnClick(View v) {
        if (!chosedQuestion.type.value.contains("groupe")) {
            chosedPlayer.nbDone++;
        }

        game.questionDone(chosedQuestion, new JsonHttpResponseHandler());

        ((QuestionActivity)getActivity()).nextQuestion(false);
    }

    public void buttonNotDoneOnClick(View v) {
        ResultQuestionFragment fragment = new ResultQuestionFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("question", chosedQuestion);
        bundle.putSerializable("player", chosedPlayer);
        fragment.setArguments(bundle);

        ((QuestionActivity)getActivity()).showFragment(fragment);
    }
}