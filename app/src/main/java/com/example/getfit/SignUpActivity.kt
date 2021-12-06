package com.example.getfit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.getfit.retrofit.RetrofitClient
import com.example.getfit.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private var retrofit = RetrofitClient.getfitAPI

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnNext.setOnClickListener {

            val name = binding.personName.text.toString().trim()
            val userName = binding.userName.text.toString().trim()
            val email = binding.emailAddress.text.toString().trim()
            val password = binding.password.text.toString().trim()

            if (name.isEmpty()) {
                binding.personName.error = "Name is required!"
                binding.personName.requestFocus()
                return@setOnClickListener
            }

            if (userName.isEmpty()) {
                binding.userName.error = "Username is required!"
                binding.userName.requestFocus()
                return@setOnClickListener
            }

            if (email.isEmpty()) {
                binding.emailAddress.error = "Email is required!"
                binding.emailAddress.requestFocus()
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                binding.password.error = "Password is required!"
                binding.password.requestFocus()
                return@setOnClickListener
            }

            val intent = Intent(this@SignUpActivity, ProfileActivity::class.java)
            with(intent) {
                putExtra("username", userName)
                putExtra("name", name)
                putExtra("email", email)
                putExtra("password", password)
                startActivity(this)
            }
        }

        binding.btnBack.setOnClickListener{
            startActivity(Intent(this@SignUpActivity, MainActivity::class.java))
        }
    }
}
