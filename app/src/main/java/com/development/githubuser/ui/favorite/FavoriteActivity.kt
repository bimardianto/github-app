package com.development.githubuser.ui.favorite

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.development.githubuser.R
import com.development.githubuser.data.local.room.DbModule
import com.development.githubuser.databinding.ActivityFavoriteBinding
import com.development.githubuser.ui.detail.DetailActivity
import com.development.githubuser.ui.main.MainAdapter
import com.development.githubuser.ui.settings.SettingsActivity

class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var progressBar: ProgressBar
    private val adapter by lazy {
        MainAdapter {
            Intent(this, DetailActivity::class.java).apply {
                putExtra(item, it)
                startActivity(this)
            }

        }
    }
    private val viewModel by viewModels<FavoriteViewModel> {
        FavoriteViewModel.Factory(DbModule(this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // back icon
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        progressBar = binding.progressBar
        binding.rvFavorite.layoutManager = LinearLayoutManager(this)
        binding.rvFavorite.adapter = adapter

    }

    override fun onResume() {
        super.onResume()
        progressBar.visibility = View.VISIBLE

        viewModel.getFavoriteUser().observe(this) { userList ->
            progressBar.visibility = View.GONE

            if (userList.isEmpty()) {
                binding.imgNoUsersFavoriteFound.visibility = View.VISIBLE
                binding.tvNoUsersFavoriteFound.visibility = View.VISIBLE
                binding.rvFavorite.visibility = View.GONE
            } else {
                binding.imgNoUsersFavoriteFound.visibility = View.GONE
                binding.tvNoUsersFavoriteFound.visibility = View.GONE
                binding.rvFavorite.visibility = View.VISIBLE
                adapter.setData(userList)
            }

        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detail_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
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