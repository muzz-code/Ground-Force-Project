package com.trapezoidlimited.groundforce.ui.dashboard.mission

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.trapezoidlimited.groundforce.adapters.mission.OngoingAdapter
import com.trapezoidlimited.groundforce.adapters.mission.OngoingItemClickListener
import com.trapezoidlimited.groundforce.databinding.FragmentOngoingBinding
import com.trapezoidlimited.groundforce.model.mission.OngoingItem
import com.trapezoidlimited.groundforce.utils.DummyData


class OngoingFragment : Fragment(), OngoingItemClickListener {

    private var _binding: FragmentOngoingBinding? = null
    private val binding get() = _binding!!

    private var locationTitlesList = DummyData.ongoingLocationData()
    private var adapter: OngoingAdapter = OngoingAdapter(
        mutableListOf<OngoingItem>(),
        this
    )


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding= FragmentOngoingBinding.inflate(inflater, container, false)

        return binding.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        /**
         * Ongoing location data will be coming from the backend to populate the recyclerview list
         *
         * For now: here includes some dummy data*/


        adapter.setMyList(locationTitlesList)

        binding.fragmentOngoingRv.adapter = adapter
        binding.fragmentOngoingRv.layoutManager = LinearLayoutManager(this.context)



    }


    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }

    /**
     * This method will handle the action onclick of the verify button */

    override fun onVerifyBtnClick(ongoing: OngoingItem, position: Int) {

        /** navigate to the verification page */

//        Intent(requireContext(), MissionReportActivity::class.java).also {
//            startActivity(it)
//        }

    }
}