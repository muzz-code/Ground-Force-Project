package com.trapezoidlimited.groundforce.ui.dashboard.extras

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.navigation.fragment.findNavController
import com.trapezoidlimited.groundforce.R
import com.trapezoidlimited.groundforce.databinding.FragmentCallCentreBinding
import java.util.*

class CallCentreFragment : Fragment() {

    private var _binding: FragmentCallCentreBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        _binding = FragmentCallCentreBinding.inflate(inflater, container, false)

        binding.fragmentCallCentreIct.toolbarFragment.setNavigationIcon(R.drawable.ic_arrow_back)
        binding.fragmentCallCentreIct.toolbarTitle.text = getString(R.string.help_fragment_title_str)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        /** set navigation to go to the home screen **/

        binding.fragmentCallCentreIct.toolbarFragment.setNavigationOnClickListener {
            findNavController().navigate(R.id.agentDashboardFragment)
        }

        /** set navigation to go to the home screen onBackPressed **/

        requireActivity().onBackPressedDispatcher.addCallback{
            if (findNavController().currentDestination?.id == R.id.callCentreFragment) {
                findNavController().navigate(R.id.agentDashboardFragment)
            } else {
                findNavController().popBackStack()
            }
        }



        val phone = binding.fragmentCallPhoneOne.text.toString()
        val phoneTwo = binding.fragmentCallPhoneTwo.text.toString()
        val phoneThree = binding.fragmentCallPhoneThree.text.toString()


        /** Checking time within working hours 8am - 5pm, and activating click to call */

        val time = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        val minute = Calendar.getInstance().get(Calendar.MINUTE)


        if (!( (time in 8..16 && minute in 0..59) || ( time == 17 && minute == 0) )) {

            binding.fragmentCallAvailableStr.text = getString(R.string.not_available_str)
            binding.fragmentCallAvailableStr.setTextColor(resources.getColor(R.color.colorRed))

        } else {
            binding.fragmentCallAvailableStr.text = getString(R.string.we_re_available_now_str)

            binding.fragmentCallPhoneOne.setOnClickListener {
                val intent = Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null))
                startActivity(intent)
            }

            binding.fragmentCallPhoneTwo.setOnClickListener {
                val intent = Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneTwo, null))
                startActivity(intent)
            }

            binding.fragmentCallPhoneThree.setOnClickListener {
                val intent = Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneThree, null))
                startActivity(intent)
            }
        }



    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}