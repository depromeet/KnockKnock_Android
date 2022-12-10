package com.depromeet.knockknock.ui.home

import androidx.core.app.NotificationManagerCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.depromeet.knockknock.R
import com.depromeet.knockknock.base.BaseFragment
import com.depromeet.knockknock.databinding.FragmentHomeBinding
import com.depromeet.knockknock.ui.home.adapter.HomeRecentAdapter
import com.depromeet.knockknock.ui.home.bottom.AlarmMoreType
import com.depromeet.knockknock.ui.home.bottom.BottomAlarmMore
import com.depromeet.knockknock.ui.home.bottom.BottomHomeRoom
import com.depromeet.knockknock.ui.home.model.Alarm
import com.depromeet.knockknock.util.KnockKnockIntent
import com.depromeet.knockknock.util.permission.PermissionManagerImpl
import com.depromeet.knockknock.util.permission.PermissionRequester
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.security.Permissions


@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>(R.layout.fragment_home) {

    private val TAG = "HomeFragment"

    override val layoutResourceId: Int
        get() = R.layout.fragment_home

    override val viewModel : HomeViewModel by viewModels()

    private val permissionManager = PermissionManagerImpl(this)
//    private val notificationPermissionRequest: PermissionRequester = permissionManager.forPermission(Permissions.PostNotification)
//        .onGranted { setFragmentResult(
//            KnockKnockIntent.RESULT_KEY_POST_NOTIFICATION_PERMISSION_GRANTED,
//            bundleOf(KnockKnockIntent.RESULT_KEY_POST_NOTIFICATION_PERMISSION_GRANTED to true)
//        ) }
//        .subscribe(this)

    override fun initStartView() {
        binding.apply {
            this.viewmodel = viewModel
            this.lifecycleOwner = viewLifecycleOwner
        }
        exception = viewModel.errorEvent
//        initNotificationPermission()
        initAdapter()
    }

    override fun initDataBinding() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.navigationHandler.collectLatest {
                when(it) {
                    is HomeNavigationAction.NavigateToNotification -> navigate(HomeFragmentDirections.actionHomeFragmentToNotificationFragment())
                    is HomeNavigationAction.NavigateToCreatePush -> initRoomBottomSheet()
                    is HomeNavigationAction.NavigateToRoom -> {}
                    is HomeNavigationAction.NavigateToRecentAlarm -> {}
                    is HomeNavigationAction.NavigateToAlarmReaction -> {}
                    is HomeNavigationAction.NavigateToSearchRoom -> {}
                    is HomeNavigationAction.NavigateToCreateRoom -> {}
                    is HomeNavigationAction.NavigateToRecentAlarmMore -> {
                        initAlarmMoreBottomSheet(roomId = it.alarmId)
                    }
                }
            }
        }
    }

    override fun initAfterBinding() {
    }

    private fun initAdapter() {
        binding.recentRecycle.adapter = HomeRecentAdapter(viewModel)
    }

    private fun initRoomBottomSheet() {
        val dialog = BottomHomeRoom(
            roomList = viewModel.roomList.value,
            eventListener = viewModel
        )
        dialog.show(childFragmentManager, TAG)
    }

    private fun initAlarmMoreBottomSheet(roomId: Int) {
        val dialog = BottomAlarmMore {
            when(it) {
                is AlarmMoreType.Copy -> {}
                is AlarmMoreType.Save -> {}
                is AlarmMoreType.Delete -> {}
                is AlarmMoreType.Declare -> {}
                is AlarmMoreType.Report -> {}
            }
        }
        dialog.show(childFragmentManager, TAG)
    }

//    private fun initNotificationPermission() {
//        lifecycleScope.launch {
//            if(NotificationManagerCompat.from(requireContext()).areNotificationsEnabled().not()) {
//                notificationPermissionRequest.request()
//            }
//        }
//    }
}
