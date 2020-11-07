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

        /** setting toolbar text **/
        surveyQuestionsBinding.fragmentSurveyQuestToolbar.toolbarTitle.text = getString(R.string.one_of_ten_title_str)

        /** set navigation arrow from drawable **/
        surveyQuestionsBinding.fragmentSurveyQuestToolbar.toolbarFragment.setNavigationIcon(R.drawable.ic_arrow_back)



        return surveyQuestionsBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        /** set navigation to go to the previous screen on click of navigation arrow **/
        surveyQuestionsBinding.fragmentSurveyQuestToolbar.toolbarFragment.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }


}