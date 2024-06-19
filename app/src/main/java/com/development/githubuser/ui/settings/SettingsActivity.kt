package com.development.githubuser.ui.settings

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import com.development.githubuser.R
import com.development.githubuser.data.preferences.SettingsPreferences
import com.development.githubuser.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    private val viewModel by viewModels<SettingsViewModel> {
        SettingsViewModel.Factory(SettingsPreferences(this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.switchTheme.setOnCheckedChangeListener { _, isChecked ->
            viewModel.saveTheme(isChecked)
        }

    }

    override fun onResume() {
        super.onResume()

        viewModel.getTheme().observe(this) { isDarkMode ->
            val themeText = if (isDarkMode) getString(R.string.dark_mode) else getString(R.string.light_mode)
            val nightMode = if (isDarkMode) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO

            binding.switchTheme.text = themeText
            AppCompatDelegate.setDefaultNightMode(nightMode)
            binding.switchTheme.isChecked = isDarkMode
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}