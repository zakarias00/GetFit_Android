package com.example.getfit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.getfit.retrofit.RetrofitClient
import com.example.getfit.data.User
import com.example.getfit.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.Serializable

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var retrofit = RetrofitClient.getfitAPI
    lateinit var loggedUser: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSingUp.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        binding.btnLogin.setOnClickListener {

            val email = binding.editTextEmailAddress.text.toString().trim()
            val password = binding.editTexPassword.text.toString().trim()

            if (email.isEmpty()) {
                binding.editTextEmailAddress.error = "Email is required!"
                binding.editTextEmailAddress.requestFocus()
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                binding.editTexPassword.error = "Password is required!"
                binding.editTexPassword.requestFocus()
                return@setOnClickListener
            }

              retrofit.getUsers().enqueue(object: Callback<List<User>> {

                  override fun onFailure(call: Call<List<User>>, t: Throwable) {
                      Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
                  }

                  override fun onResponse(
                      call: Call<List<User>>,
                      response: Response<List<User>>
                  ) = if (response.code() == 200) {

                      val userList = response.body()!!
                      var ok = false

                      for(u in userList) {
                          if (u.email.toString() == email && u.password.toString() == password) {
                              ok = true
                              loggedUser = u
                          }
                      }

                      if(ok) {
                          binding.editTexPassword.text.clear()
                          binding.editTextEmailAddress.text.clear()
                          Toast.makeText(
                              applicationContext,
                              "Successful login!",
                              Toast.LENGTH_LONG
                          ).show()

                          val intent2 = Intent(this@MainActivity, AdminActivity::class.java)
                          if (loggedUser.username.toString() == "admin") {
                              startActivity(intent2)

                          } else {

                              val intent = Intent(this@MainActivity, HomeActivity::class.java)
                              intent.putExtra("loggedUser", loggedUser as Serializable)
                              startActivity(intent)
                          }
                      }

                      else {
                          binding.editTexPassword.text.clear()
                          binding.editTextEmailAddress.text.clear()

                          Toast.makeText(
                                  applicationContext,
                                  "Unsuccessful login!",
                                  Toast.LENGTH_LONG
                              ).show()
                          }

                  } else {
                      Toast.makeText(
                          applicationContext,
                          "Connection problem!",
                          Toast.LENGTH_LONG
                      ).show()
                  }
              }
             )
        }

    }
}