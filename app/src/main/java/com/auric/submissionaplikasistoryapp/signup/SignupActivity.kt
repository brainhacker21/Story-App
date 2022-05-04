package com.auric.submissionaplikasistoryapp.signup

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.auric.submissionaplikasistoryapp.R
import com.auric.submissionaplikasistoryapp.StoryAppRetrofitInstance
import com.auric.submissionaplikasistoryapp.databinding.ActivitySignupBinding
import com.auric.submissionaplikasistoryapp.model.UserRegisterResponse
import com.auric.submissionaplikasistoryapp.signin.SigninActivity
import com.auric.submissionaplikasistoryapp.story.MainActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignupActivity : AppCompatActivity() , View.OnClickListener{
    private lateinit var binding: ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.backtosign.setOnClickListener(this)
        binding.signupbutton.setOnClickListener(this)
        setupView()
        playAnimation()
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.signupbutton -> {
                if(validateCreateAccount()) {
                    requestCreateAccount()
                } else {
                    clearEditText()
                }
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(v.windowToken, 0)
            }

            R.id.backtosign ->{
                val intent = Intent(this@SignupActivity, SigninActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    private fun requestCreateAccount() {
        showLoading(true)
        val name = binding.nameEditTextLayout.text.toString().trim()
        val email = binding.emailEditTextLayout.text.toString().trim()
        val password = binding.passwordEditTextLayout.text.toString().trim()
        StoryAppRetrofitInstance.apiService()
            .createAccount(name, email, password)
            .enqueue(object: Callback<UserRegisterResponse> {
                override fun onResponse(
                    call: Call<UserRegisterResponse>,
                    response: Response<UserRegisterResponse>
                ) {
                    showLoading(false)
                    if(response.isSuccessful) {
                        showLoading(false)
                        Toast.makeText(this@SignupActivity, R.string.accountok, Toast.LENGTH_SHORT).show()
                        val mainIntent =
                            Intent(this@SignupActivity, SigninActivity::class.java)
                        showLoading(false)
                        startActivity(mainIntent)

                        finish()
                    }
                    else{
                        showLoading(false)
                        Toast.makeText(this@SignupActivity, R.string.serverfail, Toast.LENGTH_SHORT).show()
                        clearEditText()
                    }
                }

                override fun onFailure(call: Call<UserRegisterResponse>, t: Throwable) {
                    showLoading(false)
                    Toast.makeText(this@SignupActivity, R.string.accountfail, Toast.LENGTH_SHORT).show()
                    Toast.makeText(this@SignupActivity, R.string.serverfail, Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun clearEditText() {
        binding.nameEditTextLayout.text.clear()
        binding.emailEditTextLayout.text!!.clear()
        binding.passwordEditTextLayout.text!!.clear()
    }

    private fun validateCreateAccount(): Boolean {
        return if(binding.emailEditTextLayout.text!!.isNotEmpty()
            && binding.passwordEditTextLayout.text!!.isNotEmpty()
            && binding.nameEditTextLayout.text.isNotEmpty()
            && android.util.Patterns.EMAIL_ADDRESS.matcher(binding.emailEditTextLayout.text.toString()).matches()
            && binding.passwordEditTextLayout.text.toString().length <= 6) {
            true
        } else {
            Toast.makeText(this, R.string.datamust, Toast.LENGTH_SHORT).show()
            false
        }

    }
    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()
        val title = ObjectAnimator.ofFloat(binding.titlesignup, View.ALPHA, 1f).setDuration(250)
        val nameEditTextLayout = ObjectAnimator.ofFloat(binding.nameEditTextLayout, View.ALPHA, 1f).setDuration(250)
        val emailEditTextLayout = ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(250)
        val passwordEditTextLayout = ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(250)
        val signup = ObjectAnimator.ofFloat(binding.signupbutton, View.ALPHA, 1f).setDuration(250)
        val login = ObjectAnimator.ofFloat(binding.backtosign, View.ALPHA, 1f).setDuration(250)

        AnimatorSet().apply {
            playSequentially(
                title,
                nameEditTextLayout,
                emailEditTextLayout,
                passwordEditTextLayout,
                signup,
            login
            )
            startDelay = 500
        }.start()
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
    private fun showLoading(isLoading: Boolean) {
        if(isLoading) binding.progressBar.visibility = View.VISIBLE
        else binding.progressBar.visibility = View.GONE
    }
}