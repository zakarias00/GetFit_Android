package com.example.getfit

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.getfit.retrofit.RetrofitClient
import com.example.getfit.data.User
import com.example.getfit.databinding.ActivityProfileBinding
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.Serializable
import java.util.*
import kotlin.properties.Delegates

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private var retrofit = RetrofitClient.getfitAPI
    private var gender = true
    private var birthdate by Delegates.notNull<Long>()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            resources.getStringArray(R.array.genders)
        )
        binding.spinnerGender.adapter = adapter
        binding.spinnerGender.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }

        val currentTime = Calendar.getInstance()
        val year = currentTime.get(Calendar.YEAR)
        val month = currentTime.get(Calendar.MONTH)
        val day = currentTime.get(Calendar.DAY_OF_MONTH)

        val datePicker = DatePickerDialog(this,
            { view, year, month, dayOfMonth ->
                binding.tvChosenDate.text = String.format("%d/%d/%d", dayOfMonth, month + 1, year)
                val calendarG = GregorianCalendar(year+1, month+1, dayOfMonth+1)
                birthdate = calendarG.timeInMillis
            },
            year, month, day)

        binding.btnChooseDate.setOnClickListener{
                datePicker.show()
            }

        binding.btnSubmit.setOnClickListener {

            val weight = binding.evWeight.text.toString().trim()
            val height = binding.evHeight.text.toString().trim()


            if(binding.spinnerGender.selectedItemPosition != 0)
                 gender = false

            if (weight.isEmpty()) {
                binding.evWeight.error = "Weight is required!"
                binding.evWeight.requestFocus()
                return@setOnClickListener
            }

            if (height.isEmpty()) {
                binding.evHeight.error = "Height is required!"
                binding.evHeight.requestFocus()
                return@setOnClickListener
            }

            if(binding.tvChosenDate.text.isEmpty()){
                binding.tvChosenDate.error = "Please pick a date!"
                binding.tvChosenDate.requestFocus()
                return@setOnClickListener
            }

            val newUser = User(
                intent.getStringExtra("name"),
                intent.getStringExtra("username"),
                intent.getStringExtra("email"),
                intent.getStringExtra("password"),
                birthdate,
                height.toInt(),
                weight.toInt(),
                gender
            )

            retrofit.createUser(newUser).enqueue(object : Callback<ResponseBody?> {
                override fun onResponse(
                    call: Call<ResponseBody?>,
                    response: Response<ResponseBody?>
                ) {
                    if (response.code() == 200) {

                        Toast.makeText(
                            applicationContext,
                            "Successful registration!",
                            Toast.LENGTH_LONG
                        ).show()

                        binding.evHeight.text.clear()
                        binding.evWeight.text.clear()

                        val intent = Intent(this@ProfileActivity, HomeActivity::class.java)
                        intent.putExtra("loggedUser", newUser as Serializable)
                        startActivity(intent)

                    }
                    else{
                        Toast.makeText(
                            applicationContext,
                            "Error code:" + response.code().toString(),
                            Toast.LENGTH_LONG
                        ).show()

                        binding.evHeight.text.clear()
                        binding.evWeight.text.clear()

                    }
                }

                override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                    Toast.makeText(
                        applicationContext,
                        t.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
        }

        binding.btnBackProfile.setOnClickListener{
            startActivity(Intent(this@ProfileActivity, SignUpActivity::class.java))
        }


    }
}