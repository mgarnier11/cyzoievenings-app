package com.mgarnier11.CyzoisEvenings.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.mgarnier11.CyzoisEvenings.R;
import com.mgarnier11.CyzoisEvenings.activitys.QuestionActivity;
import com.mgarnier11.CyzoisEvenings.models.Game;

/**
 * A simple {@link Fragment} subclass.
 */
public class QuestionSingleFragment extends Fragment {

    TextView textViewQuestion;
    TextView textViewDifficulty;

    Button buttonDone;
    Button buttonNotDone;

    Game game;

    public QuestionSingleFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        game = Game.getInstance();
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_question_single, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        updateUi();
    }

    public void updateUi() {
        textViewQuestion = getActivity().findViewById(R.id.fragment_question_textViewQuestion);
        textViewDifficulty = getActivity().findViewById(R.id.fragment_question_textViewDifficulty);

        buttonDone = getActivity().findViewById(R.id.fragment_question_buttonDone);
        buttonNotDone = getActivity().findViewById(R.id.fragment_question_buttonNotDone);

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

        textViewQuestion.setText(game.actualQuestion.text);

        textViewDifficulty.setText(game.actualQuestion.getDifficultyStars());
    }

    public void buttonDoneOnClick(View v) {
        game.questionDone(game.actualQuestion, new JsonHttpResponseHandler());

        game.actualPlayer.nbQuestions++;
        game.actualPlayer.nbDone++;

        ((QuestionActivity)getActivity()).nextQuestion();
    }

    public void buttonNotDoneOnClick(View v) {

        ResultQuestionFragment fragment = new ResultQuestionFragment();

        ((QuestionActivity)getActivity()).showQuestionFragment(fragment);
    }

}
