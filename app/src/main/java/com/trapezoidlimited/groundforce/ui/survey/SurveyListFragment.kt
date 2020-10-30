package com.trapezoidlimited.groundforce.ui.survey

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.trapezoidlimited.groundforce.R
import com.trapezoidlimited.groundforce.adapters.SurveyRecyclerAdapter
import com.trapezoidlimited.groundforce.databinding.FragmentLoginBinding
import com.trapezoidlimited.groundforce.databinding.FragmentSurveyListBinding
import com.trapezoidlimited.groundforce.utils.DummyData
import kotlinx.android.synthetic.main.fragment_survey_list.*

class SurveyListFragment : Fragment(), SurveyRecyclerAdapter.OnSurveyClickListener {

    private var _binding: FragmentSurveyListBinding? = null

    private val binding get() = _binding!!

    private var adapter = SurveyRecyclerAdapter(DummyData.surveyList, this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSurveyListBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //initialize survey recycler view adapter and  Layout Manager
        binding.fragmentSurveyListRecyclerView.adapter = adapter
        binding.fragmentSurveyListRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    //TODO(Move to the Begin Survey )

    override fun onAcceptClick(position: Int) {
        DummyData.surveyList.removeAt(position)
        adapter.notifyItemRemoved(position)
    }

    override fun onDeleteClick(position: Int) {
        Toast.makeText(requireContext(), "Item $position clicked for Decline", Toast.LENGTH_SHORT)
            .show()
    }


}