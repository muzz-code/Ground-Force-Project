package com.trapezoidlimited.groundforce.ui.dashboard.survey

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.trapezoidlimited.groundforce.R
import com.trapezoidlimited.groundforce.adapters.SurveyRecyclerAdapter
import com.trapezoidlimited.groundforce.databinding.FragmentSurveyListBinding
import com.trapezoidlimited.groundforce.utils.DummyData

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
        /** setting toolbar text **/
        binding.fragmentSurveyListToolbar.toolbarTitle.text = getString(R.string.surveys_title_str)

        /** set navigation arrow from drawable **/
        binding.fragmentSurveyListToolbar.toolbarFragment.setNavigationIcon(R.drawable.ic_arrow_back)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)



        /** set navigation to go to the previous screen on click of navigation arrow **/
        binding.fragmentSurveyListToolbar.toolbarFragment.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        //initialize survey recycler view adapter and  Layout Manager
        binding.fragmentSurveyListRecyclerView.adapter = adapter
        binding.fragmentSurveyListRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun onAcceptClick(position: Int) {
        findNavController().navigate(R.id.beginSurveyFragment2)
    }

    override fun onDeleteClick(position: Int) {
        DummyData.surveyList.removeAt(position)
        adapter.notifyItemRemoved(position)
    }


}