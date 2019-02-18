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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.mgarnier11.CyzoisEvenings.R;
import com.mgarnier11.CyzoisEvenings.activitys.QuestionActivity;
import com.mgarnier11.CyzoisEvenings.models.GameOld;
import com.mgarnier11.CyzoisEvenings.models.Player;
import com.mgarnier11.CyzoisEvenings.models.Question;

/**
 * A simple {@link Fragment} subclass.
 */
public class GroupQuestionFragment extends Fragment {


    TextView textViewQuestion;
    TextView textViewDifficulty;
    TextView textViewDrinks;

    Button buttonContinue;

    ImageView imageViewGroupPhoto;

    Question chosedQuestion;
    Player chosedPlayer;

    int rndDrinks;

    GameOld gameOld;

    public GroupQuestionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v;

        gameOld = GameOld.getInstance();

        chosedQuestion = (Question)getArguments().getSerializable("question");
        chosedPlayer = (Player) getArguments().getSerializable("player");

        rndDrinks = getArguments().getInt("rndDrinks");

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            v = inflater.inflate(R.layout.fragment_group_question_horizontal, container, false);
        } else {
            v = inflater.inflate(R.layout.fragment_group_question_vertical, container, false);
        }

        return v;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        //((QuestionActivity)getActivity()).showQuestion(chosedQuestion, chosedPlayer);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        updateUi();
    }

    public void updateUi() {
        textViewQuestion = getActivity().findViewById(R.id.fragment_group_question_textViewQuestion);
        textViewDifficulty = getActivity().findViewById(R.id.fragment_group_question_textViewDifficulty);
        textViewDrinks = getActivity().findViewById(R.id.fragment_group_question_textViewDrinks);

        buttonContinue = getActivity().findViewById(R.id.fragment_group_question_buttonContinue);

        imageViewGroupPhoto = getActivity().findViewById(R.id.fragment_group_question_imageViewPhoto);

        buttonContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonContinueOnClick(v);
            }
        });

        textViewQuestion.setText(chosedQuestion.text);
        textViewDrinks.setText(getResources().getString(R.string.drinksStr, rndDrinks));
        textViewDifficulty.setText(chosedQuestion.getDifficultyStars());

        imageViewGroupPhoto.setImageBitmap(BitmapFactory.decodeFile(gameOld.groupImageUrl));

    }

    public void buttonContinueOnClick(View v) {
        gameOld.questionDone(chosedQuestion, new JsonHttpResponseHandler());

        //((QuestionActivity)getActivity()).nextQuestion(false);
    }
}
