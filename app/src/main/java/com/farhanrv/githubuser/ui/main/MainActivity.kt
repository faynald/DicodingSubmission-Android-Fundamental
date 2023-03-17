package com.farhanrv.githubuser.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.farhanrv.githubuser.R
import com.farhanrv.githubuser.data.local.DarkModePreferences
import com.farhanrv.githubuser.databinding.ActivityMainBinding
import com.farhanrv.githubuser.ui.SettingFactory
import com.farhanrv.githubuser.ui.favorite.FavoriteActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "Settings")
class MainActivity : AppCompatActivity() {

    private lateinit var adapter: UserListAdapter
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private var workRunnable: Runnable = Runnable { }
    private val handler = Handler(Looper.getMainLooper())
    private var isDarkMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val preferences = DarkModePreferences.getInstance(dataStore)
        viewModel = ViewModelProvider(this, SettingFactory(preferences))[MainViewModel::class.java]

        viewModel.isDarkMode().observe(this) { isDarkMode ->
            this.isDarkMode = isDarkMode
            if (isDarkMode) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        adapter = UserListAdapter()
        binding.rvHome.layoutManager = LinearLayoutManager(this)
        binding.rvHome.adapter = adapter
        binding.rvHome.setHasFixedSize(true)

        binding.searchView.setOnQueryTextListener(object :
            OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    searchUser(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                handler.removeCallbacks(workRunnable)
                workRunnable = Runnable {
                    if (newText != null && newText.isNotEmpty()) {
                        binding.tvUserNotFound.visibility = View.GONE
                        binding.rvHome.visibility = View.VISIBLE
                        binding.tvBeginSearch.visibility = View.GONE
                        searchUser(newText)
                    } else {
                        binding.tvUserNotFound.visibility = View.GONE
                        binding.rvHome.visibility = View.GONE
                        binding.tvBeginSearch.visibility = View.VISIBLE
                    }
                }
                handler.postDelayed(workRunnable, 1000)
                return true
            }
        })

        viewModel.searchResult.observe(this@MainActivity) { data ->
            if (data.isNotEmpty()) {
                binding.tvUserNotFound.visibility = View.GONE
                adapter.submitList(data)
            } else {
                binding.rvHome.visibility = View.GONE
                binding.tvUserNotFound.visibility = View.VISIBLE
            }
        }
        viewModel.isLoading.observe(this) { setLoading(it) }
    }

    private fun searchUser(query: String) {
        viewModel.findUser(query)
    }

    private fun setLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.INVISIBLE
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_item, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_item_dark_mode -> {
                viewModel.changeTheme(!isDarkMode)
            }
            R.id.menu_item_favorite -> {
                val intent = Intent(this@MainActivity, FavoriteActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}