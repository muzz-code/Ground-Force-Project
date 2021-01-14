package com.trapezoidlimited.groundforce.ui.dashboard.extras

import android.os.Bundle
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.trapezoidlimited.groundforce.R
import com.trapezoidlimited.groundforce.adapters.FAQAdapter
import com.trapezoidlimited.groundforce.databinding.FragmentFAQBinding
import com.trapezoidlimited.groundforce.model.FAQ

class FAQFragment : Fragment() {

    private var _binding: FragmentFAQBinding? = null
    private val binding
        get() = _binding!!
    private lateinit var adapter: FAQAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var faqList: MutableList<FAQ>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentFAQBinding.inflate(inflater, container, false)

        binding.fragmentFAQIct.toolbarFragment.setNavigationIcon(R.drawable.ic_arrow_back)
        binding.fragmentFAQIct.toolbarTitle.text = "FAQ"

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.fragmentFAQIct.toolbarFragment.setNavigationOnClickListener {
            findNavController().popBackStack()
        }


        /** Setting up clickable text */

        val callToActionText = getString(R.string.call_to_action_reach_us_str)

        val callToActionSpannableText = SpannableString(getString(R.string.call_to_action_reach_us_str))

        val clickableSpan: ClickableSpan = object : ClickableSpan(){
            override fun onClick(view: View) {
               findNavController().navigate(R.id.callCentreFragment)
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.color = ContextCompat.getColor(requireContext(), R.color.colorPrimary)
                ds.isUnderlineText = false
            }
        }

        callToActionSpannableText.setSpan(clickableSpan, 25, callToActionText.length -1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE )
        binding.fragmentFAQReachUsTv.text = callToActionSpannableText
        binding.fragmentFAQReachUsTv.movementMethod = LinkMovementMethod.getInstance()

        recyclerView = binding.fragmentFAQRecyclerView

        /** Setting up recyclerview list */

        initializeFAQList()
        setUpRecyclerView()

    }

    private fun initializeFAQList(){
        faqList = mutableListOf(
            FAQ(
                "What is GroundForce?",
                getString(R.string.faq_dummy_str)
            ),
            FAQ(
                "What is GroundForce?",
                getString(R.string.faq_dummy_str)
            ),
            FAQ(
                "What is GroundForce?",
                getString(R.string.faq_dummy_str)
            ),
            FAQ(
                "What is GroundForce?",
                getString(R.string.faq_dummy_str)
            ),
            FAQ(
                "What is GroundForce?",
                getString(R.string.faq_dummy_str)
            ),
            FAQ(
                "What is GroundForce?",
                getString(R.string.faq_dummy_str)
            ),
            FAQ(
                "What is GroundForce?",
                getString(R.string.faq_dummy_str)
            )
        )
    }

    private fun setUpRecyclerView() {
        adapter = FAQAdapter(faqList)

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}