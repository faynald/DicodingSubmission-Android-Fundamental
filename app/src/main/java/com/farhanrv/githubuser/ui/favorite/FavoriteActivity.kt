package com.farhanrv.githubuser.ui.favorite

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.farhanrv.githubuser.R
import com.farhanrv.githubuser.databinding.ActivityFavoriteBinding
import com.farhanrv.githubuser.ui.ViewModelFactory
import com.farhanrv.githubuser.ui.main.UserListAdapter

class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var adapter: UserListAdapter
    private lateinit var viewModel: FavoriteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.favorite)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        adapter = UserListAdapter()
        binding.rvUser.layoutManager = LinearLayoutManager(this)
        binding.rvUser.adapter = adapter
        binding.rvUser.setHasFixedSize(true)

        viewModel = obtainViewModel(this@FavoriteActivity)
        viewModel.getFavorites().observe(this) { favoriteList ->
            if (favoriteList != null && favoriteList.isNotEmpty()) {
                binding.emptyFavorite.visibility = View.GONE
                binding.rvUser.visibility = View.VISIBLE
                adapter.submitList(favoriteList)
            } else {
                binding.rvUser.visibility = View.GONE
                binding.emptyFavorite.visibility = View.VISIBLE
            }
        }

    }

    private fun obtainViewModel(activity: AppCompatActivity): FavoriteViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[FavoriteViewModel::class.java]
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressedDispatcher.onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}