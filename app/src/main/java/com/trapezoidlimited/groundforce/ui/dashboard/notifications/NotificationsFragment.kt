package com.trapezoidlimited.groundforce.ui.dashboard.notifications

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.trapezoidlimited.groundforce.EntryApplication
import com.trapezoidlimited.groundforce.R
import com.trapezoidlimited.groundforce.adapters.NotificationsAdapter
import com.trapezoidlimited.groundforce.api.ApiService
import com.trapezoidlimited.groundforce.api.MissionsApi
import com.trapezoidlimited.groundforce.api.Resource
import com.trapezoidlimited.groundforce.databinding.FragmentNotificationsBinding
import com.trapezoidlimited.groundforce.model.NotificationItem
import com.trapezoidlimited.groundforce.model.NotificationsHeader
import com.trapezoidlimited.groundforce.model.NotificationsItem
import com.trapezoidlimited.groundforce.repository.AuthRepositoryImpl
import com.trapezoidlimited.groundforce.utils.DividerItemDecoration
import com.trapezoidlimited.groundforce.utils.checkItem
import com.trapezoidlimited.groundforce.utils.handleApiError
import com.trapezoidlimited.groundforce.viewmodel.AuthViewModel
import com.trapezoidlimited.groundforce.viewmodel.ViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_dashboard.*
import retrofit2.Retrofit
import javax.inject.Inject


/**
 * Notificationc Fragment
 */

@AndroidEntryPoint
class NotificationsFragment : Fragment() {

    @Inject
    lateinit var loginApiServiceService: ApiService

    @Inject
    lateinit var missionsApi: MissionsApi

    @Inject
    lateinit var retrofit: Retrofit

    private lateinit var notificationsList: MutableList<NotificationItem>

    private lateinit var viewModel: AuthViewModel

    private lateinit var binding: FragmentNotificationsBinding

    private val adapter = NotificationsAdapter(mutableListOf())

    private val roomViewModel by lazy { EntryApplication.roomViewModel(this) }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentNotificationsBinding.inflate(inflater, container, false)

        val repository = AuthRepositoryImpl(loginApiServiceService, missionsApi)
        val factory = ViewModelFactory(repository, requireContext())
        viewModel = ViewModelProvider(this, factory).get(AuthViewModel::class.java)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = binding.notificationsFragmentRecyclerNewNotificationsRv

        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = adapter
        val heightInPixels = resources.getDimensionPixelSize(R.dimen.list_item_divider_height)
        context?.let {
            recyclerView.addItemDecoration(
                DividerItemDecoration(
                    ContextCompat.getColor(it, R.color.colorDividerDialogVerification),
                    heightInPixels
                )
            )
        }
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)



        viewModel.getAllNotifications(1)

        viewModel.getAllNotificationsResponse.observe(viewLifecycleOwner, {
            when (it) {
                is Resource.Success -> {
                    val notificationResponseResultList = it.value.data?.data

                    if (notificationResponseResultList != null) {

                        for (notificationResponseResult in notificationResponseResultList) {

                            val notification = notificationResponseResult.notifications

                            val notificationItem = NotificationItem(
                                image = R.drawable.bolt,
                                message = notification,
                                date = "13 Sept, 2020 10:30 AM",
                                isNew = "New Notification"
                            )

                            notificationsList.add(notificationItem)
                        }

                    }

                }

                is Resource.Failure -> {

                    handleApiError(roomViewModel, requireActivity(), it, retrofit, requireView())

                }
            }
        })





        // Navigate to Home on Back Press
        requireActivity().onBackPressedDispatcher.addCallback {
            if (findNavController().currentDestination?.id == R.id.notificationsFragment) {
                activity?.dashboardActivity_bnv?.checkItem(R.id.agentDashboard_home)
                val action =
                    NotificationsFragmentDirections.actionNotificationsFragmentToAgentDashboardFragment()
                findNavController().navigate(action)
            } else {
                findNavController().popBackStack()
            }
        }
    }


    private fun returnNotifications(num: Int): List<NotificationsItem> {
        val notifications = mutableListOf<NotificationsItem>()
        val isNew = mutableListOf("New Notification", "Older Notifications")
        val notification = mutableListOf<NotificationItem>()

        val notificationOne = NotificationItem(
            image = R.drawable.bolt,
            message = "Hello Trooper Kehinde, a credit transaction of NGN100.00 just occurred in your account. Login to review. If there are any complaints or inquiries, please feel free to contact us.",
            date = "13 Sept, 2020 10:30 AM",
            isNew = "New Notification"
        )
        val notificationTwo = NotificationItem(
            image = R.drawable.bolt,
            message = "Hello Trooper, here's to a happy and fulfilling holiday this season. Have fun. Cheers!",
            date = "13 Sept, 2020 10:30 AM",
            isNew = "New Notification"
        )
        val notificationFour = NotificationItem(
            image = R.drawable.bolt,
            message = "Hello Trooper Kehinde, a credit transaction of NGN100.00 just occurred in your account. Login to review. If there are any complaints or inquiries, please feel free to contact us.",
            date = "13 Sept, 2020 10:30 AM",
            isNew = "Older Notifications"
        )
        val notificationThree = NotificationItem(
            image = R.drawable.bolt,
            message = "Hello Trooper, here's to a happy and fulfilling holiday this season. Have fun. Cheers!",
            date = "13 Sept, 2020 10:30 AM",
            isNew = "Older Notifications"
        )

        for (nu in 0..num) {

            when (nu % 4) {
                0 -> notification.add(notificationOne)
                1 -> notification.add(notificationTwo)
                2 -> notification.add(notificationThree)
                else -> notification.add(notificationFour)
            }
        }

        Log.d("CHECKINGSNotification", "$notification")
        isNew.let {
            for (new in isNew) {
                notifications.add(NotificationsItem.withHeader(NotificationsHeader(new)))
                val filtered = notification.filter { it.isNew == new }
                    .map { NotificationsItem.withMessage(it) }
                Log.d("CHECKINGSFiltered", "$filtered")
                notifications.addAll(filtered)
            }
        }

        Log.d("CHECKINGSMethos", "$notifications")

        return notifications
    }

    override fun onResume() {
        super.onResume()

        activity?.let {
            val lists = returnNotifications(5)

            Log.d("CHECKINGSLIST", "$lists")
            lists.let {
                adapter.updateNotifications(it)
            }
        }

        //adapter.updateNotifications()


    }


}