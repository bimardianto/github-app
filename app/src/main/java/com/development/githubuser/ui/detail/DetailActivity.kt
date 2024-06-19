package com.development.githubuser.ui.detail

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import coil.load
import android.content.res.ColorStateList
import android.view.Menu
import android.view.MenuItem
import coil.transform.CircleCropTransformation
import com.development.githubuser.R
import androidx.core.content.ContextCompat
import com.development.githubuser.data.local.room.DbModule
import com.development.githubuser.databinding.ActivityDetailBinding
import com.development.githubuser.data.remote.response.DetailUserResponse
import com.development.githubuser.data.remote.response.ItemsItem
import com.development.githubuser.utils.MessageCallback
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import androidx.annotation.ColorRes
import com.development.githubuser.ui.settings.SettingsActivity
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private val viewModel by viewModels<DetailViewModel> {
        DetailViewModel.Factory(DbModule(this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val item = intent.getParcelableExtra<ItemsItem>(item)
        val username = item?.login ?: ""

        viewModel.resultDetailUser.observe(this) {
            when (it) {
                is MessageCallback.Success<*> -> {
                    val user = it.data as DetailUserResponse
                    binding.image.load(user.avatarUrl) {
                        transformations(CircleCropTransformation())
                    }
                    binding.name.text = user.name
                    binding.username.text = user.login
                    val followers = "${user.followers}"
                    binding.countFollowers.text = followers
                    val following = "${user.following}"
                    binding.countFollowing.text = following
                }

                is MessageCallback.Error -> {
                    Toast.makeText(this, it.exception.message.toString(), Toast.LENGTH_SHORT).show()
                }

                is MessageCallback.Loading -> {
                    binding.progressBar.isVisible = it.isLoading
                }
            }
        }

        val fragments = mutableListOf(
            FollowFragment.newInstance(FollowFragment.FOLLOWERS),
            FollowFragment.newInstance(FollowFragment.FOLLOWING)
        )

        val tittleFragments = listOf(
            getString(R.string.followers), getString(R.string.following)
        )

        val adapter = DetailAdapter(this, fragments)
        binding.viewpager.adapter = adapter

        TabLayoutMediator(binding.tab, binding.viewpager) { tab, position ->
            tab.text = tittleFragments[position]
        }.attach()

        binding.tab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab?.position == 0) {
                    viewModel.getFollowers(username)
                } else {
                    viewModel.getFollowing(username)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })

        binding.btnFavorite.setOnClickListener {
            viewModel.setFavorite(item)
        }

        viewModel.resultFavoriteSuccess.observe(this) {
            binding.btnFavorite.changeIconColor(R.color.red)
        }

        viewModel.resultFavoriteDelete.observe(this) {
            binding.btnFavorite.changeIconColor(R.color.white)
        }

    }

    override fun onResume() {
        super.onResume()
        val item = intent.getParcelableExtra<ItemsItem>("item")
        val username = item?.login.orEmpty()
        viewModel.getFollowers(username)
        viewModel.getDetailUser(username)
        viewModel.findFavorite(item?.id ?: 0) {
            binding.btnFavorite.changeIconColor(R.color.red)
        }
    }

    private fun FloatingActionButton.changeIconColor(@ColorRes color: Int) {
        imageTintList = ColorStateList.valueOf(ContextCompat.getColor(this.context, color))
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detail_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    companion object {
        private const val item = "item"
    }

}
