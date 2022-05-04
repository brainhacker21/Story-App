package com.auric.submissionaplikasistoryapp.story

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.auric.submissionaplikasistoryapp.Api.UserPreference
import com.auric.submissionaplikasistoryapp.R
import com.auric.submissionaplikasistoryapp.databinding.ActivityMainBinding
import com.auric.submissionaplikasistoryapp.maps.MapsActivity
import com.auric.submissionaplikasistoryapp.signin.SigninActivity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var preferences: UserPreference

    private val storyAdapter: StoryAdapter by lazy { StoryAdapter() }
    private lateinit var viewModel: StoryViewModel
    private lateinit var storyprefModel: StoryPreferenceModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Story App"

        preferences = UserPreference.getInstance(dataStore)
        val usermodel = runBlocking{ preferences.getUser().first() }
        val token = usermodel?.token

        binding.rvstory.layoutManager = LinearLayoutManager(this)
        viewModel = ViewModelProvider(this)[StoryViewModel::class.java]
        if (token != null) {
            searchListItem(token)
        }
        binding.rvstory.adapter = storyAdapter

        storyprefModel = ViewModelProvider(
            this,
            StoryViewModelFactory(UserPreference.getInstance(dataStore))
        )[StoryPreferenceModel::class.java]

        storyprefModel.getUser().observe(this) { user ->
            if (user!=null) {
                user.login
                binding.nameTextView.text = getString(R.string.greeting, user.name)
            } else {
                startActivity(Intent(this, SigninActivity::class.java))
                Intent(this,MainActivity::class.java)
                finish()
            }
        }
        viewModel.getListStoryItem().observe(this) {
            if (it != null) {
                storyAdapter.setData(it)
                showLoading(false)
            }
        }

        binding.fabadd.setOnClickListener {
            val intent = Intent(this@MainActivity, AddstoryActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.logout -> {
                lifecycleScope.launch{
                    preferences.logout()

                }
                val loginIntent = Intent(this, SigninActivity::class.java)
                startActivity(loginIntent)
                finish()
                return true
            }
            R.id.language -> {
                startActivity(Intent(android.provider.Settings.ACTION_LOCALE_SETTINGS))
            }
            R.id.menu_maps -> {
                startActivity(Intent(this, MapsActivity::class.java))
                true
            }
        }
        return true
    }

    private fun searchListItem(token: String) {
        showLoading(false)
        viewModel.getListStoryItem(token)
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) binding.storiesProgressBar.visibility = View.VISIBLE
        else binding.storiesProgressBar.visibility = View.GONE
    }
}