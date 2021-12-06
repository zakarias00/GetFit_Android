package com.example.getfit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.getfit.retrofit.RetrofitClient
import com.example.getfit.data.Sport
import com.example.getfit.databinding.ActivityAdminBinding
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdminActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminBinding
    private var retrofit = RetrofitClient.getfitAPI

    override fun onCreate(savedInstanceState: Bundle?) {

        binding = ActivityAdminBinding.inflate(layoutInflater)

        binding.btnOk.setOnClickListener{
            val kcal = binding.etKcal.text.toString().trim().toDouble()
            val type = binding.etType.text.toString().trim()
            val newSport = Sport(kcal, type)

            retrofit.createSport(newSport).enqueue(object: Callback<ResponseBody?> {
                override fun onResponse(
                    call: Call<ResponseBody?>,
                    response: Response<ResponseBody?>
                )   = if (response.code() == 200) {

                    Toast.makeText(
                        applicationContext,
                        "The new sport was created successfully!",
                        Toast.LENGTH_LONG
                    ).show()

                    startActivity(Intent(this@AdminActivity, MainActivity::class.java))
                }
                    else{
                    Toast.makeText(
                        applicationContext,
                        "Something went wrong!",
                        Toast.LENGTH_LONG
                    ).show()
                    }

                override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                    Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
                }

            })
        }

        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}