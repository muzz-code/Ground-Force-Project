package com.trapezoidlimited.groundforce.ui.dashboard.mission

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ImageSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.trapezoidlimited.groundforce.R
import com.trapezoidlimited.groundforce.adapters.mission.MissionAdapter
import com.trapezoidlimited.groundforce.adapters.mission.OnMissionItemClickListener
import com.trapezoidlimited.groundforce.databinding.FragmentMissionBinding
import com.trapezoidlimited.groundforce.model.mission.MissionItem
import com.trapezoidlimited.groundforce.utils.DummyData


class MissionFragment : Fragment(), OnMissionItemClickListener {

    private var _binding: FragmentMissionBinding? = null
    private val binding get() = _binding!!
    private var locationTitlesList = DummyData.missionLocationData()
    private var adapter: MissionAdapter = MissionAdapter(
        mutableListOf(),
        this
    )


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment


        _binding= FragmentMissionBinding.inflate(inflater, container, false)

        return binding.root

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        /**
         * Mission location data will be coming from the backend to populate the recyclerview list
         *
         * For now: here includes some dummy data*/




        adapter.setMyList(locationTitlesList)

        binding.fragmentMissionRv.adapter = adapter
        binding.fragmentMissionRv.layoutManager = LinearLayoutManager(this.context)


    }



    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }


    /**
     * Method handles the actions onclick of the accept btn */

    override fun onAcceptClick(mission: MissionItem, position: Int) {

        /** setting the indicator  on the onGoing tab onclick of the accept btn */

        val image: Drawable? = ResourcesCompat.getDrawable(resources, R.drawable.custom_tab_indicator_active, null)
        image?.setBounds(0, 0, image.intrinsicWidth, image.intrinsicHeight)

        val sb = SpannableString("Ongoing" + "     ")

        val imageSpan = ImageSpan(image!!, ImageSpan.ALIGN_CENTER)
        sb.setSpan(
            imageSpan,
            sb.length - 2,
            sb.length - 1,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

//        requireActivity().mission_ongoing_tl?.getTabAt(1)?.text = SpannableString(sb)



        /** Onclick of the accept btn, the location data at that position will be removed from the Mission's list in the backend
         * And then, this location data is added to the ongoing list.
         * For now, this logic here just removes from the dummy list of the locations data */
        locationTitlesList.removeAt(position)

        adapter.notifyItemRemoved(position)
    }

    /**
     * Method handles the actions onclick of the delete btn */

    override fun onDeclineClick(mission: MissionItem, position: Int) {

        /** Onclick of the decline btn, the location data is removed from the Mission's list
         * For now, this logic here just removes at that position from the dummy list of the locations data*/

        locationTitlesList.removeAt(position)

        adapter.notifyItemRemoved(position)
    }

}