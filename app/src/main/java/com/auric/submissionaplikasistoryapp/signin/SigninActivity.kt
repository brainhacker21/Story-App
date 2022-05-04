package com.auric.submissionaplikasistoryapp.signin

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.auric.submissionaplikasistoryapp.Api.UserModel
import com.auric.submissionaplikasistoryapp.Api.UserPreference
import com.auric.submissionaplikasistoryapp.R
import com.auric.submissionaplikasistoryapp.StoryAppRetrofitInstance
import com.auric.submissionaplikasistoryapp.story.StoryViewModelFactory
import com.auric.submissionaplikasistoryapp.databinding.ActivitySigninBinding
import com.auric.submissionaplikasistoryapp.model.UserLoginResponse
import com.auric.submissionaplikasistoryapp.story.MainActivity
import com.auric.submissionaplikasistoryapp.signup.SignupActivity
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
class SigninActivity: AppCompatActivity(), View.OnClickListener {
    private lateinit var signinViewModel: SigninViewModel
    private lateinit var binding: ActivitySigninBinding
    private lateinit var user: UserModel
    private lateinit var userPreference: UserPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySigninBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userPreference = UserPreference.getInstance(dataStore)

        binding.signinbutton.setOnClickListener(this)
        binding.signupbutton.setOnClickListener(this)

        setupView()
        playAnimation()
        setupViewModel()
    }

    private fun setupViewModel() {
        signinViewModel = ViewModelProvider(
            this,
            StoryViewModelFactory(UserPreference.getInstance(dataStore))
        )[SigninViewModel::class.java]

        signinViewModel.getUser().observe(this) { user ->
            if(user!=null){ this.user=user}
        }

    }

    private fun login() {
        showLoading(true)
        val email = binding.emailEditTextLayout.text.toString().trim()
        val password = binding.passwordEditTextLayout.text.toString().trim()
            StoryAppRetrofitInstance.apiService()
                .getLoginUser(email, password)
                .enqueue(object : Callback<UserLoginResponse>
                {
                    override fun onResponse(
                        call: Call<UserLoginResponse>,
                        response: Response<UserLoginResponse>
                    ) {
                        showLoading(false)
                        if (response.isSuccessful) {
                            showLoading(false)
                            response.body()?.loginResult?.apply {
                                val userPreference =
                                    UserPreference.getInstance(dataStore)
                                lifecycleScope.launch {
                                    userPreference.saveUser(UserModel(
                                        name,
                                        email,
                                        password,
                                        token,
                                        login = true))
                                }
                            }
                            val mainIntent =
                                Intent(this@SigninActivity, MainActivity::class.java)
                            showLoading(false)
                            startActivity(mainIntent)
                            finish()
                        } else {
                            showLoading(false)
                            Toast.makeText(this@SigninActivity,
                                R.string.invalid,
                                Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<UserLoginResponse>, t: Throwable) {
                        showLoading(false)
                        Toast.makeText(this@SigninActivity,
                            R.string.invalid,
                            Toast.LENGTH_SHORT).show()
                    }
                })
        }

    private fun validateLoginAccount(): Boolean {
        return if (binding.emailEditTextLayout.text!!.isNotEmpty()
            && binding.passwordEditTextLayout.text!!.isNotEmpty()
            && android.util.Patterns.EMAIL_ADDRESS.matcher(binding.emailEditTextLayout.text.toString())
                .matches()
            && binding.passwordEditTextLayout.text.toString().length <= 6) {
            true
        } else {
            showLoading(false)
            Toast.makeText(this, R.string.datamust, Toast.LENGTH_SHORT).show()
            false
        }
    }
        override fun onClick(view: View?) {
            when (view?.id) {
                R.id.signinbutton -> {
                    if (validateLoginAccount()) {
                        login()}
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(view.windowToken, 0)
                }
                R.id.signupbutton -> {
                    val intent = Intent(this@SigninActivity, SignupActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }

        private fun setupView() {
            @Suppress("DEPRECATION")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                window.insetsController?.hide(WindowInsets.Type.statusBars())
            } else {
                window.setFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN
                )
            }
            supportActionBar?.hide()
        }


        private fun playAnimation() {
            ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
                duration = 6000
                repeatCount = ObjectAnimator.INFINITE
                repeatMode = ObjectAnimator.REVERSE
            }.start()

            val title = ObjectAnimator.ofFloat(binding.titlesignin, View.ALPHA, 1f).setDuration(250)
            val message =
                ObjectAnimator.ofFloat(binding.messagesignin, View.ALPHA, 1f).setDuration(250)
            val emailEditTextLayout =
                ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(250)
            val passwordEditTextLayout =
                ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f)
                    .setDuration(250)
            val login =
                ObjectAnimator.ofFloat(binding.signinbutton, View.ALPHA, 1f).setDuration(250)
            val signup =
                ObjectAnimator.ofFloat(binding.signupbutton, View.ALPHA, 1f).setDuration(250)

            AnimatorSet().apply {
                playSequentially(title,
                    message,
                    emailEditTextLayout,
                    passwordEditTextLayout,
                    login,
                    signup)
                startDelay = 500
            }.start()
        }

        private fun showLoading(isLoading: Boolean) {
            if (isLoading) binding.loginProgressBar.visibility = View.VISIBLE
            else binding.loginProgressBar.visibility = View.GONE
        }
    }
