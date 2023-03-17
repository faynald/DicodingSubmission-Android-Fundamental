package com.farhanrv.githubuser.ui.detail

import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.farhanrv.githubuser.R
import com.farhanrv.githubuser.data.network.response.UserItem
import com.farhanrv.githubuser.databinding.ActivityDetailBinding
import com.farhanrv.githubuser.ui.ViewModelFactory
import com.farhanrv.githubuser.ui.detail.follow.SectionsPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity() {

    private lateinit var viewModel: DetailViewModel
    private lateinit var binding: ActivityDetailBinding

    private var favoriteState: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = obtainViewModel(this)

        val intentData = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(EXTRA_USER, UserItem::class.java)
        } else {
            intent.getParcelableExtra(EXTRA_USER)
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (intentData != null) {
            viewModel.isLoading().observe(this) { setLoading(it) }
            intentData.login?.let {
                supportActionBar?.title = it
                setupDetail(it)
                setTab(it)
                viewModel.ifExist(it).observe(this@DetailActivity) { state ->
                    favoriteState = state
                    if (state) {
                        binding.fabFavorite.setImageResource(R.drawable.ic_favorite)
                    } else {
                        binding.fabFavorite.setImageResource(R.drawable.ic_favorite_null)
                    }
                }
                binding.fabFavorite.setOnClickListener {
                    if (favoriteState) {
                        viewModel.delete(intentData)
                    } else {
                        viewModel.insert(intentData)
                    }
                }
            }
        }
    }

    private fun setTab(loginName: String) {
        val bundle = Bundle()
        bundle.putString(EXTRA_USER, loginName)
        binding.viewPager.adapter = SectionsPagerAdapter(this, bundle)
        binding.viewPager.offscreenPageLimit = 2
        val tabTitle = resources.getStringArray(R.array.tab_title)
        TabLayoutMediator(
            binding.tabLayout, binding.viewPager
        ) { tab, position ->
            tab.text = tabTitle[position]
        }.attach()
    }

    private fun setupDetail(loginName: String) {
        viewModel.setUserDetail(loginName)
        viewModel.getUser().observe(this@DetailActivity) { dataResult ->
            dataResult?.let { data ->
                with(binding) {
                    Glide.with(this@DetailActivity)
                        .load(data.avatarUrl)
                        .circleCrop()
                        .into(imgDetailPhoto)
                    tvDetailName.text = data.name
                    tvDetailUsername.text = data.login
                    tvDetailRepository.text = data.publicRepos.toString()
                    tvDetailFollowing.text = data.following.toString()
                    tvDetailFollowers.text = data.followers.toString()
                }
            }
        }
    }

    private fun setLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.INVISIBLE
    }

    private fun obtainViewModel(activity: AppCompatActivity): DetailViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[DetailViewModel::class.java]
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressedDispatcher.onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        const val EXTRA_USER = "EXTRA_USER"
    }
}