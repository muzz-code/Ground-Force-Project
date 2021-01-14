package com.trapezoidlimited.groundforce.ui.dashboard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.trapezoidlimited.groundforce.R
import com.trapezoidlimited.groundforce.adapters.PaymentHistoryRecyclerAdapter
import com.trapezoidlimited.groundforce.databinding.FragmentPaymentHistorySurveyBinding
import com.trapezoidlimited.groundforce.utils.DummyData


class PaymentHistorySurvey : Fragment() {

    private var _binding: FragmentPaymentHistorySurveyBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: PaymentHistoryRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        _binding = FragmentPaymentHistorySurveyBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        adapter = PaymentHistoryRecyclerAdapter(mutableListOf())


        binding.fragmentPaymentHistorySurveyRecyclerView.adapter = adapter
        binding.fragmentPaymentHistorySurveyRecyclerView.layoutManager =
            LinearLayoutManager(requireContext())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
