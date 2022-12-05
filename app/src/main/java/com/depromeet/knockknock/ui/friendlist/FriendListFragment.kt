package com.depromeet.knockknock.ui.friendlist

import android.annotation.SuppressLint
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.depromeet.knockknock.R
import com.depromeet.knockknock.base.AlertDialogModel
import com.depromeet.knockknock.base.BaseFragment
import com.depromeet.knockknock.base.DefaultYellowAlertDialog
import com.depromeet.knockknock.databinding.FragmentFriendListBinding
import com.depromeet.knockknock.ui.friendlist.adapter.FriendListAdapter
import com.depromeet.knockknock.ui.friendlist.bottom.BottomFriendMore
import com.depromeet.knockknock.ui.friendlist.bottom.FriendMoreType
import com.depromeet.knockknock.util.customOnFocusChangeListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest


@AndroidEntryPoint
class FriendListFragment : BaseFragment<FragmentFriendListBinding, FriendListViewModel>(R.layout.fragment_friend_list) {

    private val TAG = "FriendListFragment"

    override val layoutResourceId: Int
        get() = R.layout.fragment_friend_list

    override val viewModel : FriendListViewModel by viewModels()
    private val navController by lazy { findNavController() }

    override fun initStartView() {
        binding.apply {
            this.viewmodel = viewModel
            this.lifecycleOwner = viewLifecycleOwner
        }
        exception = viewModel.errorEvent
        initToolbar()
        initAdapter()
        binding.searchEditText.customOnFocusChangeListener(requireContext())
    }

    override fun initDataBinding() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.navigationHandler.collectLatest {
                when(it) {
                    is FriendListNavigationAction.NavigateToLink -> {}
                    is FriendListNavigationAction.NavigateToFriendMore -> { moreFriendPopUp(userIdx = it.userIdx) }
                }
            }
        }
    }

    override fun initAfterBinding() {
    }

    private fun initToolbar() {
        with(binding.toolbar) {
            this.title = getString(R.string.friend_list_title)
            // 뒤로가기 버튼
            this.setNavigationIcon(R.drawable.ic_allow_back)
            this.setNavigationOnClickListener { navController.popBackStack() }
        }
    }

    private fun moreFriendPopUp(userIdx: Int) {
        val dialog: BottomFriendMore = BottomFriendMore {
            when(it) {
                is FriendMoreType.Black -> {}
                is FriendMoreType.Delete -> {}
            }
        }
        dialog.show(requireActivity().supportFragmentManager, TAG)
    }

    private fun initAdapter() {
        binding.friendRecycler.adapter = FriendListAdapter(viewModel)
    }
}
