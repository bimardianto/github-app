package com.development.githubuser.ui.main

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.development.githubuser.R
import com.development.githubuser.data.preferences.SettingsPreferences
import com.development.githubuser.databinding.ActivityMainBinding
import com.development.githubuser.data.remote.response.ItemsItem
import com.development.githubuser.ui.detail.DetailActivity
import com.development.githubuser.ui.favorite.FavoriteActivity
import com.development.githubuser.ui.settings.SettingsActivity
import com.development.githubuser.utils.MessageCallback

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<MainViewModel> {
        MainViewModel.Factory(SettingsPreferences(this))
    }
    private val adapter by lazy {
        MainAdapter {
            Intent(this, DetailActivity::class.java).apply {
                putExtra(item, it)
                startActivity(this)
            }

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getTheme().observe(this) {
            if (it) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.adapter = adapter

        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView.editText.setOnEditorActionListener { textView, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    val query = textView.text.toString().trim()
                    if (query.isNotEmpty()) {
                        viewModel.getUser(query)
                        val imm =
                            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.hideSoftInputFromWindow(textView.windowToken, 0)
                        searchView.hide()
                    }
                    true
                } else {
                    false
                }
            }
        }

        viewModel.resultUser.observe(this) {
            when (it) {
                is MessageCallback.Success<*> -> {
                    adapter.setData(it.data as MutableList<ItemsItem>)
                }

                is MessageCallback.Error -> {
                    Toast.makeText(this, it.exception.message.toString(), Toast.LENGTH_SHORT).show()
                }

                is MessageCallback.Loading -> {
                    binding.progressBar.isVisible = it.isLoading
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getUser()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.favoriteMenu -> {
                Intent(this, FavoriteActivity::class.java).apply {
                    startActivity(this)
                }
            }

            R.id.settingsMenu -> {
                Intent(this, SettingsActivity::class.java).apply {
                    startActivity(this)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        private const val item = "item"
    }

}