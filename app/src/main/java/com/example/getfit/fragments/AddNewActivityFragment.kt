package com.example.getfit.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.getfit.R
import com.example.getfit.retrofit.RetrofitClient
import com.example.getfit.data.Activity
import com.example.getfit.data.Sport
import com.example.getfit.databinding.FragmentAddNewActivityBinding
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate.*
import java.util.*
import kotlin.properties.Delegates

class AddNewActivityFragment : Fragment() {

    private lateinit var binding: FragmentAddNewActivityBinding
    private var retrofit = RetrofitClient.getfitAPI
    private lateinit var historyFragment: HistoryFragment

    private lateinit var sports: List<Sport>

     override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
      super.onViewCreated(view, savedInstanceState)

      val userId = arguments?.getString("userId").toString().toInt()

      var activityDate by Delegates.notNull<Long>()

      val adapter = ArrayAdapter(
          requireContext(),
          android.R.layout.simple_spinner_item,
          resources.getStringArray(R.array.sports_array)
      )
      binding.spinner.adapter = adapter
      binding.spinner.onItemSelectedListener = object :
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

      val newType = resources.getStringArray(R.array.sports_array)[binding.spinner.selectedItemPosition]
      val currentTime = Calendar.getInstance()
      val year = currentTime.get(Calendar.YEAR)
      val month = currentTime.get(Calendar.MONTH)
      val day = currentTime.get(Calendar.DAY_OF_MONTH)

      val datePicker = DatePickerDialog(requireContext(),
          { view, year, month, dayOfMonth ->
              binding.tvDatePicker.text = String.format("%d/%d/%d", dayOfMonth, month + 1, year)
              val calendarG = GregorianCalendar(year+1, month+1, dayOfMonth+1)
              activityDate = calendarG.timeInMillis
          },
          year, month, day)

      binding.btnChooseDateActivity.setOnClickListener{
          datePicker.show()
      }

      binding.btnOkGoal.setOnClickListener {
          val newDist = binding.etDist.text.toString().trim()
          val newDuration = binding.etDur.text.toString().trim()

          if(newDuration.isEmpty()) {
               binding.etDur.error = "Please fill the field!"
               binding.etDur.requestFocus()
               return@setOnClickListener
           }

          if(newDist.isEmpty()){
              binding.etDist.error = "Please fill the field!"
              binding.etDist.requestFocus()
              return@setOnClickListener
          }

          if(binding.tvDatePicker.text.isEmpty()){
              binding.tvDatePicker.error = "Please fill the field!"
              binding.tvDatePicker.requestFocus()
              return@setOnClickListener
          }

          retrofit.getSports().enqueue(
              object : Callback<List<Sport>> {
                  override fun onResponse(
                      call: Call<List<Sport>>,
                      response: Response<List<Sport>>
                  ) {
                      sports = response.body()!!

                      for(i in sports){
                          if(i.type == newType){
                              val newSportId = i.id
                              val newActivity = Activity(
                                  activityDate,
                                  newDist.toDouble(),
                                  newSportId,
                                  newDuration.toDouble(),
                                  userId)
                            /*  Log.d("TAG",  activityDate+
                                  newDist.toDouble(),
                                  newSportId,
                                  newDuration.toDouble())*/

                              retrofit.createActivity(newActivity).enqueue(
                                  object: Callback<ResponseBody?>{
                                      override fun onResponse(
                                          call: Call<ResponseBody?>,
                                          response: Response<ResponseBody?>
                                      ) {
                                          binding.etDur.text.clear()
                                          binding.etDist.text.clear()
                                          if(response.code()==200) {
                                              Toast.makeText(
                                                  requireContext(),
                                                  "New activity was added successfully!",
                                                  Toast.LENGTH_LONG
                                              ).show()
                                              Log.d("TAG", response.code().toString())
                                          }

                                          else{
                                              Toast.makeText(
                                                  requireContext(),
                                                  "Error code:" + response.code().toString(),
                                                  Toast.LENGTH_LONG
                                              ).show()
                                          }
                                      }

                                      override fun onFailure(
                                          call: Call<ResponseBody?>,
                                          t: Throwable
                                      ) {
                                          Toast.makeText(requireContext(), t.message, Toast.LENGTH_LONG).show()
                                      }

                                  }
                              )

                          }
                          break
                      }


                  }

                  override fun onFailure(call: Call<List<Sport>>, t: Throwable) {
                      Toast.makeText(requireContext(), t.message, Toast.LENGTH_LONG).show()
                  }

              }
          )
      }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddNewActivityBinding.inflate(inflater, container, false)
        return binding.root
    }

}