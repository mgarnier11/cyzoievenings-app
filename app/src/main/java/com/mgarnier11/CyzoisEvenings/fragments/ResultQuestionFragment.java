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

import com.mgarnier11.CyzoisEvenings.R;
import com.mgarnier11.CyzoisEvenings.activitys.QuestionActivity;
import com.mgarnier11.CyzoisEvenings.models.Game;

/**
 * A simple {@link Fragment} subclass.
 */
public class ResultQuestionFragment extends Fragment {
    Game game;

    TextView textViewNbDrinks;

    Button buttonNextQuestion;

    public ResultQuestionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_result_question, container, false);

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        game = Game.getInstance();

        updateUi();
    }

    private void updateUi() {
        textViewNbDrinks = getActivity().findViewById(R.id.fragment_result_question_textViewNbDrinks);

        textViewNbDrinks.setText(getResources().getString(R.string.drinksStr, game.actualQuestion.getRndDrinks(game.nbDrinkMin, game.nbDrinkMax)));

        buttonNextQuestion = getActivity().findViewById(R.id.fragment_result_question_buttonNextQuestion);

        buttonNextQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonNextQuestionOnClick(v);
            }
        });
    }

    public void buttonNextQuestionOnClick(View v) {
        game.actualPlayer.nbDrinked += game.actualQuestion.nbDrinks;

        ((QuestionActivity)getActivity()).nextQuestion();
    }
}
