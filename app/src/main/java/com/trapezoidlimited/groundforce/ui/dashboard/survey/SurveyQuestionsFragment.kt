package com.trapezoidlimited.groundforce.ui.dashboard.survey

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.trapezoidlimited.groundforce.R
import com.trapezoidlimited.groundforce.databinding.FragmentSurveyQuestionsBinding


/**
 * Survey questions fragment
 */
class SurveyQuestionsFragment : Fragment() {


    private lateinit var surveyQuestionsBinding: FragmentSurveyQuestionsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        surveyQuestionsBinding = FragmentSurveyQuestionsBinding.inflate(inflater, container, false)


        surveyQuestionsBinding.surveyQuestionsFragmentBackImageImg.setOnClickListener {
            findNavController().popBackStack()
        }

        return surveyQuestionsBinding.root
    }


}