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

import org.w3c.dom.Text;

/**
 * A simple {@link Fragment} subclass.
 */
public class QuestionGroupFragment extends Fragment {

    TextView textViewQuestion;
    TextView textViewDifficulty;
    TextView textViewDrinks;

    Button buttonContinue;

    Game game;

    public QuestionGroupFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        game = Game.getInstance();
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_question_group, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        updateUi();
    }

    public void updateUi() {
        textViewQuestion = getActivity().findViewById(R.id.fragment_question_textViewQuestion);
        textViewDifficulty = getActivity().findViewById(R.id.fragment_question_textViewDifficulty);
        textViewDrinks = getActivity().findViewById(R.id.fragment_question_textViewDrinks);

        buttonContinue = getActivity().findViewById(R.id.fragment_question_buttonContinue);

        buttonContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonContinueOnClick(v);
            }
        });

        textViewQuestion.setText(game.actualQuestion.text);
        textViewDrinks.setText(getResources().getString(R.string.drinksStr, game.actualQuestion.getRndDrinks(game.nbDrinkMin, game.nbDrinkMax)));
        textViewDifficulty.setText(game.actualQuestion.getDifficultyStars());
    }

    public void buttonContinueOnClick(View v) {
        game.questionDone(game.actualQuestion, new JsonHttpResponseHandler());

        game.group.nbQuestions++;
        game.group.nbDone++;
        game.group.nbDrinked += game.actualQuestion.nbDrinks;

        ((QuestionActivity)getActivity()).nextQuestion();
    }

}
