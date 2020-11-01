package com.trapezoidlimited.groundforce.ui.dashboard.survey

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.trapezoidlimited.groundforce.R
import com.trapezoidlimited.groundforce.databinding.FragmentBeginSurveyBinding


/**
 * BeginSurvey Fragment
 */
class BeginSurveyFragment : Fragment() {

    private lateinit var beginSurveyBinding: FragmentBeginSurveyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        beginSurveyBinding = FragmentBeginSurveyBinding.inflate(inflater, container, false)

        beginSurveyBinding.beginSurveyFragmentBtn.setOnClickListener {
            findNavController().navigate(R.id.surveyQuestionsFragment)
        }

        return beginSurveyBinding.root
    }


}