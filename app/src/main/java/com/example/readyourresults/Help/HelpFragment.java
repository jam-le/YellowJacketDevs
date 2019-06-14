package com.example.readyourresults.Help;


import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.readyourresults.R;

public class HelpFragment extends Fragment {
    //Recycler view may be better if there are many questions

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_help, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        final Button question_01 = getView().findViewById(R.id.button_question_1);
        final Button question_02 = getView().findViewById(R.id.button_question_2);
        final Button question_03 = getView().findViewById(R.id.button_question_3);
        final Button question_04 = getView().findViewById(R.id.button_question_4);
        final Button question_05 = getView().findViewById(R.id.button_question_5);

        final Drawable right_arrow = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_help_question_arrow, null);
        final Drawable down_arrow = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_down_arrow, null);

        //links
        TextView infoBrochureLink = getView().findViewById(R.id.info_brochure_link);
        infoBrochureLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment infoBrochureFragment = new InfoBrochureFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.content_main, infoBrochureFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        question_01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LinearLayout answerbox = getView().findViewById(R.id.textview_answer_1);
                if (answerbox.getVisibility() == View.GONE) {
                    answerbox.setVisibility(View.VISIBLE);
                    question_01.setCompoundDrawablesWithIntrinsicBounds(null, null, down_arrow, null);
                } else {
                    answerbox.setVisibility(View.GONE);
                    question_01.setCompoundDrawablesWithIntrinsicBounds(null, null, right_arrow, null);
                }
            }
        });

        question_02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView answerbox = getView().findViewById(R.id.textview_answer_2);
                if (answerbox.getVisibility() == View.GONE) {
                    answerbox.setVisibility(View.VISIBLE);
                    question_02.setCompoundDrawablesWithIntrinsicBounds(null, null, down_arrow, null);
                } else {
                    answerbox.setVisibility(View.GONE);
                    question_02.setCompoundDrawablesWithIntrinsicBounds(null, null, right_arrow, null);
                }
            }
        });

        question_03.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView answerbox = getView().findViewById(R.id.textview_answer_3);
                if (answerbox.getVisibility() == View.GONE) {
                    answerbox.setVisibility(View.VISIBLE);
                    question_03.setCompoundDrawablesWithIntrinsicBounds(null, null, down_arrow, null);
                } else {
                    answerbox.setVisibility(View.GONE);
                    question_03.setCompoundDrawablesWithIntrinsicBounds(null, null, right_arrow, null);
                }
            }
        });

        question_04.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView answerbox = getView().findViewById(R.id.textview_answer_4);
                if (answerbox.getVisibility() == View.GONE) {
                    answerbox.setVisibility(View.VISIBLE);
                    question_04.setCompoundDrawablesWithIntrinsicBounds(null, null, down_arrow, null);
                } else {
                    answerbox.setVisibility(View.GONE);
                    question_04.setCompoundDrawablesWithIntrinsicBounds(null, null, right_arrow, null);
                }
            }
        });

        final TextView instiFaq = getView().findViewById(R.id.textview_answer_5);
        instiFaq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment instiFaqFragment = new InstiFaqFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.content_main, instiFaqFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
        question_05.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (instiFaq.getVisibility() == View.GONE) {
                    instiFaq.setVisibility(View.VISIBLE);
                    question_05.setCompoundDrawablesWithIntrinsicBounds(null, null, down_arrow, null);
                } else {
                    instiFaq.setVisibility(View.GONE);
                    question_05.setCompoundDrawablesWithIntrinsicBounds(null, null, right_arrow, null);
                }
            }
        });

    }
}
