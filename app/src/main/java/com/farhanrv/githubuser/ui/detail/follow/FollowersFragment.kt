package com.farhanrv.githubuser.ui.detail.follow

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.farhanrv.githubuser.R
import com.farhanrv.githubuser.databinding.FragmentFollowBinding
import com.farhanrv.githubuser.ui.detail.DetailActivity

class FollowersFragment : Fragment(R.layout.fragment_follow) {
    private val binding: FragmentFollowBinding by viewBinding()
    private lateinit var loginName: String
    private lateinit var viewModel: FollowersViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[FollowersViewModel::class.java]

        if (arguments != null) {
            loginName = arguments?.getString(DetailActivity.EXTRA_USER).toString()
            viewModel.setFollowerUser(loginName)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { setLoading(it) }

        val adapter = FollowAdapter()
        binding.rvFragment.layoutManager = LinearLayoutManager(activity)
        binding.rvFragment.adapter = adapter
        binding.rvFragment.setHasFixedSize(true)

        viewModel.getFollowers().observe(viewLifecycleOwner) {
            it?.let { list ->
                adapter.submitList(list)
            }
        }
    }

    private fun setLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.INVISIBLE
    }
}