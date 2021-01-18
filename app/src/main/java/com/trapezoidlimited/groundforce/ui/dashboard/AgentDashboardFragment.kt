package com.trapezoidlimited.groundforce.ui.dashboard

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.trapezoidlimited.groundforce.EntryApplication
import com.trapezoidlimited.groundforce.R
import com.trapezoidlimited.groundforce.api.ApiService
import com.trapezoidlimited.groundforce.api.MissionsApi
import com.trapezoidlimited.groundforce.api.Resource
import com.trapezoidlimited.groundforce.databinding.FragmentAgentDashboardBinding
import com.trapezoidlimited.groundforce.repository.AuthRepositoryImpl
import com.trapezoidlimited.groundforce.utils.*
import com.trapezoidlimited.groundforce.viewmodel.AuthViewModel
import com.trapezoidlimited.groundforce.viewmodel.ViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Retrofit
import javax.inject.Inject
import com.trapezoidlimited.groundforce.room.RoomAgent
import com.trapezoidlimited.groundforce.viewmodel.MissionsViewModel
import java.text.SimpleDateFormat
import java.util.*


@AndroidEntryPoint
class AgentDashboardFragment : Fragment() {

    @Inject
    lateinit var loginApiServiceService: ApiService

    @Inject
    lateinit var missionsApi: MissionsApi

    @Inject
    lateinit var retrofit: Retrofit

    private var _binding: FragmentAgentDashboardBinding? = null
    private val binding get() = _binding!!
    private val roomViewModel by lazy { EntryApplication.roomViewModel(this) }
    private lateinit var dashBoardCard: CardView

    private lateinit var viewModel: AuthViewModel
    private lateinit var mViewModel: MissionsViewModel

    private lateinit var incompleteUserDetailsConstraintLayout: ConstraintLayout
    private lateinit var userId: String
    private lateinit var isVerified: String
    private var missionCompleted = 0
    private var surveyCompleted = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (_binding == null) {
            _binding = FragmentAgentDashboardBinding.inflate(inflater, container, false)
            /** setting toolbar text **/
            binding.dashboardToolBarLy.toolbarTitle.text = getString(R.string.home_title_str)
        }

        /** initializing views **/
        incompleteUserDetailsConstraintLayout = binding.fragmentAgentDashboardIncompleteProfileCl

//        /** Checking and logging user out if user has no authorization **/
//
//        logOut(roomViewModel)

        val repository = AuthRepositoryImpl(loginApiServiceService, missionsApi)
        val factory = ViewModelFactory(repository, requireContext())

        viewModel = ViewModelProvider(this, factory).get(AuthViewModel::class.java)
        mViewModel = ViewModelProvider(this, factory).get(MissionsViewModel::class.java)


        userId = loadFromSharedPreference(requireActivity(), USERID)


        mViewModel.getMissions(userId, "verified", "1")
        viewModel.getSurvey(userId, "completed", 1)

        roomViewModel.readAgent()

        /** Checking is user object is saved in Room
         *
         * Make network if user object in room is empty **/

        roomViewModel.agentObjectA.observe(viewLifecycleOwner, {
            if (it.isNotEmpty()) {

                val name = it[it.lastIndex].firstName

                saveToSharedPreference(requireActivity(), FIRSTNAME, name)

                binding.fragmentAgentDashboardCl.visibility = View.VISIBLE
                binding.fragmentAgentDashboardLl.visibility = View.GONE

            } else {

                binding.fragmentAgentDashboardCl.visibility = View.GONE
                binding.fragmentAgentDashboardLl.visibility = View.VISIBLE
                viewModel.getUser(userId)
            }

        })

        // Inflate the layout for this fragment
        return binding.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        /** Setting date  **/

        val sdf = SimpleDateFormat("MMM d, yyyy", Locale.US)
        val formattedDate = sdf.format(Calendar.getInstance().time)

        binding.agentDashFragmentDateTv.text = formattedDate


        dashBoardCard = binding.agentDashboardFragmentSummaryContainerCv
        isVerified = loadFromSharedPreference(requireActivity(), IS_VERIFIED)

        /** Checking if User is verified */

        checkUserVerified(isVerified)



        binding.fragmentAgentDashboardMissionsButtonIb.setOnClickListener {
            DataListener.currentItem = MISSION
            findNavController().navigate(R.id.action_agentDashboardFragment_to_tasksFragment)
        }

        binding.fragmentAgentDashboardSurveysButtonIb.setOnClickListener {
            findNavController().navigate(R.id.action_agentDashboardFragment_to_surveyListFragment2)
        }

        /** setting mutableLiveData value controlling where the tab will start on the mission fragment  */

        binding.fragmentAgentDashboardActiveButtonIb.setOnClickListener {
            DataListener.currentItem = ONGOING
            findNavController().navigate(R.id.action_agentDashboardFragment_to_tasksFragment)
        }

        binding.fragmentAgentDashboardSeeDetailsButton.setOnClickListener {
            findNavController().navigate(R.id.action_agentDashboardFragment_to_paymentHistory)
        }

        binding.fragmentAgentViewDetailsTv.setOnClickListener {
            // Setting currentItem value for mission-survey-history viewpager
            DataListener.msCurrentItem = MISSIONCOMPLETED
            findNavController().navigate(R.id.action_agentDashboardFragment_to_historyFragment)
        }

        binding.fragmentAgentSurveyViewDetailsTv.setOnClickListener {
            // Setting currentItem value for mission-survey-history viewpager
            DataListener.msCurrentItem = SURVEYCOMPLETED
            findNavController().navigate(R.id.action_agentDashboardFragment_to_historyFragment)
        }

        mViewModel.getMissionResponse.observe(viewLifecycleOwner, {

            when(it) {
                is Resource.Success -> {
                    val missionCompleted = it.value.data?.data?.size

                    if (it.value.data?.data != null) {
                        binding.fragmentAgentDashboardMissionCompletedTv.text = "$missionCompleted"
                    }

                }
                is Resource.Failure -> {
                    println(it.errorCode.toString())
                }
            }
        })

        viewModel.getSurveyResponse.observe(viewLifecycleOwner, {

            when(it) {
                is Resource.Success -> {
                    val surveyCompleted = it.value.data?.userSurveyToReturn?.size

                    if (it.value.data?.userSurveyToReturn != null) {
                        binding.fragmentAgentDashboardHistoryCompletedTv.text = "$surveyCompleted"
                    }

                }
                is Resource.Failure -> {
                    println(it.errorCode.toString())
                }
            }

        })


        viewModel.getUserDetailsResponse.observe(viewLifecycleOwner, { response ->

            when (response) {
                is Resource.Success -> {

                    binding.fragmentAgentDashboardLl.visibility = View.GONE

                    val firstName = response.value.data?.firstName.toString()

                    val lastName = response.value.data?.lastName

                    val avatarUrl = response.value.data?.avatarUrl

                    val isVerified = response.value.data?.isVerified.toString()

                    val bank = response.value.data?.bankName.toString()

                    val accountNum = response.value.data?.accountNumber.toString()


                    val isLocationVerified = response.value.data?.isLocationVerified.toString()

                    if (avatarUrl != null) {
                        saveToSharedPreference(requireActivity(), AVATAR_URL, avatarUrl)
                    }

                    if (lastName != null) {
                        saveToSharedPreference(requireActivity(), LASTNAME, lastName)
                    }

                    saveToSharedPreference(requireActivity(), FIRSTNAME, firstName)

                    saveToSharedPreference(requireActivity(), IS_VERIFIED, isVerified)

                    saveToSharedPreference(requireActivity(), IS_LOCATION_VERIFIED, isLocationVerified)

                    saveToSharedPreference(requireActivity(), BANKNAME, bank)

                    saveToSharedPreference(requireActivity(), ACCOUNTNUMBER, accountNum)


                    /** Checking if User is verified */

                    checkUserVerified(isVerified)


                    binding.fragmentAgentDashboardCl.visibility = View.VISIBLE

                    /** Adding Agent Details to Room Database */

                    Toast.makeText(
                        requireContext(),
                        response.value.data?.firstName!!,
                        Toast.LENGTH_SHORT
                    )
                        .show()


                    val roomAgent = RoomAgent(
                        agentId = 1,
                        id = response.value.data?.id!!,
                        lastName = response.value.data.lastName,
                        firstName = response.value.data.firstName,
                        email = response.value.data.email,
                        residentialAddress = response.value.data.residentialAddress,
                        dob = response.value.data.dob,
                        gender = response.value.data.gender,
                    )

                    roomViewModel.addAgent(roomAgent)


                }
                is Resource.Failure -> {
                    binding.fragmentAgentDashboardLl.visibility = View.GONE


                    handleApiError(
                        roomViewModel,
                        requireActivity(),
                        response,
                        retrofit,
                        requireView()
                    )

                }
            }
        })


        /** Setting firstName  **/

        val name = loadFromSharedPreference(requireActivity(), FIRSTNAME)

        binding.agentDashboardFragmentNameTv.text = "Hello $name"


        binding.agentDashboardUpdateNowBtn.setOnClickListener {
            findNavController().navigate(R.id.uploadImageFragment)
        }

        // Navigate to Home on Back Press
        requireActivity().onBackPressedDispatcher.addCallback {
            if (findNavController().currentDestination?.id == R.id.agentDashboardFragment) {
                activity?.finish()
            } else {
                findNavController().popBackStack()
            }
        }


    }

    private fun checkUserVerified(isVerified: String) {

        if (isVerified == "true") {
            Log.d("IS_VERIFIED", "RUNS")
            setInVisibility(incompleteUserDetailsConstraintLayout)
        } else {
            setVisibility(incompleteUserDetailsConstraintLayout)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}