package com.example.getfit.fragments

import android.animation.ObjectAnimator
import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import com.example.getfit.R
import com.example.getfit.retrofit.RetrofitClient
import com.example.getfit.data.Activity
import com.example.getfit.data.Goal
import com.example.getfit.data.User
import com.example.getfit.databinding.FragmentGoalsBinding
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


class GoalsFragment : Fragment() {

    private lateinit var binding: FragmentGoalsBinding
    private lateinit var user: User
    private var retrofit = RetrofitClient.getfitAPI
    private lateinit var nGFragment: GoalChangeFragment

    private var found = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        user = arguments?.getSerializable("user") as User

        childFragmentManager.setFragmentResultListener("requestKey", this) { key, bundle ->
            val newGoal = bundle.getString("newGoal").toString().toInt()

            retrofit.getGoals().enqueue(
                object : Callback<List<Goal>> {
                    override fun onResponse(call: Call<List<Goal>>, response: Response<List<Goal>>) {
                        val goalList = response.body()!!
                        lateinit var goalToUpdate: Goal
                        for (g in goalList) {
                            if (g.userId == user.id) {
                                goalToUpdate = g
                                found = true
                            }
                        }
                        if (found) {
                            goalToUpdate?.amount = newGoal
                            retrofit.updateGoal(goalToUpdate?.id.toString().toInt(), goalToUpdate!!)
                                .enqueue(
                                    object : Callback<ResponseBody?> {
                                        override fun onResponse(
                                            call: Call<ResponseBody?>,
                                            response: Response<ResponseBody?>
                                        ) {
                                            binding.resultTextView.text =
                                                goalToUpdate.currentAmount.toString() + "/" + goalToUpdate.amount.toString() + " active days completed"
                                            binding.progressBar2.max = goalToUpdate.amount.toString().toInt()
                                        }

                                        override fun onFailure(
                                            call: Call<ResponseBody?>,
                                            t: Throwable
                                        ) {
                                            Toast.makeText(context, t.message, Toast.LENGTH_LONG)
                                                .show()
                                        }

                                    }
                                )
                        }

                        else{
                            val currentTime = Calendar.getInstance()
                            val year = currentTime.get(Calendar.YEAR)
                            val month = currentTime.get(Calendar.MONTH)
                            val day = currentTime.get(Calendar.DAY_OF_MONTH)

                            lateinit var newgoal: Goal

                            if(binding.tvWeekGoal.text.isNotEmpty()) {

                                var date = binding.tvWeekGoal.text.toString().split("/")
                                val calendarG = GregorianCalendar(date[2].toInt()+1, date[1].toInt()+1, date[0].toInt()+1)
                                var selectedDate = calendarG.timeInMillis
                                newgoal = Goal(newGoal, selectedDate, user.id)
                            }
                            else{
                                val calendarG = GregorianCalendar(year+1, month+1,day+1)
                                val currentDate = calendarG.timeInMillis

                                newgoal = Goal(newGoal, currentDate, user.id)
                            }

                            retrofit.createGoal(newgoal).enqueue(
                                object : Callback<ResponseBody?>{
                                    override fun onResponse(
                                        call: Call<ResponseBody?>,
                                        response: Response<ResponseBody?>
                                    ) {
                                        binding.resultTextView.text = "0 /"+ newgoal.amount+" active days completed"
                                        binding.progressBar2.max = newgoal.amount.toString().toInt()
                                        val currProgress = 0


                                        ObjectAnimator.ofInt(binding.progressBar2, "progress", currProgress)
                                            .setDuration(2000)
                                            .start()
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
                    }

                    override fun onFailure(call: Call<List<Goal>>, t: Throwable) {
                        Toast.makeText(context, t.message, Toast.LENGTH_LONG).show()
                    }

                }
            )
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        user = arguments?.getSerializable("user") as User
        var selectedDate : Long? = null

        val currentTime = Calendar.getInstance()
        val year = currentTime.get(Calendar.YEAR)
        val month = currentTime.get(Calendar.MONTH)
        val day = currentTime.get(Calendar.DAY_OF_MONTH)

        val datePicker = DatePickerDialog(requireContext(),
            { view, year, month, dayOfMonth ->
                binding.tvWeekGoal.text = String.format("%d/%d/%d", dayOfMonth, month + 1, year)
                val calendarG = GregorianCalendar(year+1, month+1, dayOfMonth+1)
                selectedDate = calendarG.timeInMillis
            },
            year, month, day)

        binding.btnChooseDateGoal.setOnClickListener{
            datePicker.show()
        }

        val calendarG = GregorianCalendar(year+1, month+1,day+1)
        val currentDate = calendarG.timeInMillis

        retrofit.getGoalsForWeek(currentDate, user.id!!).enqueue(
            object : Callback<List<Goal>> {
                override fun onResponse(
                    call: Call<List<Goal>>,
                    response: Response<List<Goal>>
                ) {
                   if(response.code()==200){
                       val goal = response.body()!!
                       binding.resultTextView.text = goal[0].currentAmount.toString()+"/"+ goal[0].amount.toString()+" active days completed"
                       binding.progressBar2.max = goal[0].amount.toString().toInt()
                       val currProgress = goal[0].currentAmount.toString().toInt()


                       ObjectAnimator.ofInt(binding.progressBar2, "progress", currProgress)
                           .setDuration(2000)
                           .start()

                   }
                   else if (response.code() == 404){
                       binding.resultTextView.text =  "0/0"
                       binding.progressBar2.max = 0
                       val currProgress = 0


                       ObjectAnimator.ofInt(binding.progressBar2, "progress", currProgress)
                           .setDuration(2000)
                           .start()

                   }
                    else {
                       Toast.makeText(context, "Error code: "+ response.code().toString(), Toast.LENGTH_LONG).show()
                   }
                }

                override fun onFailure(call: Call<List<Goal>>, t: Throwable) {
                    Toast.makeText(context, t.message, Toast.LENGTH_LONG).show()
                }
            }
        )

        retrofit.getActivitiesForWeek(currentDate, user.id!!).enqueue(
            object : Callback<List<Activity>> {
                override fun onResponse(
                    call: Call<List<Activity>>,
                    response: Response<List<Activity>>
                ) {
                    if(response.code() == 200){
                        var timeCnt = 0.0
                        var distCnt = 0.0
                        var calCnt = 0.0

                        val activityList = response.body()!!
                        for(i in activityList){
                            timeCnt += i.time!!
                            distCnt += i.distance!!
                            calCnt += i.kcal!!
                        }

                        binding.hoursTextView2.text = "$timeCnt minutes"
                        binding.kmTextView2.text = "$distCnt kilometers"
                        binding.tvKcalInput.text = "$calCnt calories"

                    }
                    else {
                        Toast.makeText(context, "Error code: "+ response.code().toString(), Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<List<Activity>>, t: Throwable) {
                    Toast.makeText(context, t.message, Toast.LENGTH_LONG).show()
                }
            }
        )

        if(binding.tvWeekGoal.text.isNotEmpty()) {
            retrofit.getGoalsForWeek(selectedDate!!, user.id!!).enqueue(
                object : Callback<List<Goal>> {
                    override fun onResponse(
                        call: Call<List<Goal>>,
                        response: Response<List<Goal>>
                    ) {
                        if(response.code()==200){
                            val goal = response.body()!!
                            binding.resultTextView.text = goal[0].currentAmount.toString()+"/"+ goal[0].amount.toString()+" active days completed"
                            binding.progressBar2.max = goal[0].amount.toString().toInt()
                            val currProgress = goal[0].currentAmount.toString().toInt()


                            ObjectAnimator.ofInt(binding.progressBar2, "progress", currProgress)
                                .setDuration(2000)
                                .start()

                        }
                        else if (response.code() == 404){
                            binding.resultTextView.text =  "0/0"
                            binding.progressBar2.max = 0
                            val currProgress = 0


                            ObjectAnimator.ofInt(binding.progressBar2, "progress", currProgress)
                                .setDuration(2000)
                                .start()

                        }
                        else {
                            Toast.makeText(context, "Error code: "+ response.code().toString(), Toast.LENGTH_LONG).show()
                        }
                    }

                    override fun onFailure(call: Call<List<Goal>>, t: Throwable) {
                        Toast.makeText(context, t.message, Toast.LENGTH_LONG).show()
                    }

                }
            )

            retrofit.getActivitiesForWeek(selectedDate!!, user.id!!).enqueue(
                object : Callback<List<Activity>> {
                    override fun onResponse(
                        call: Call<List<Activity>>,
                        response: Response<List<Activity>>
                    ) {
                        if(response.code() == 200){
                            var timeCnt = 0.0
                            var distCnt = 0.0
                            var calCnt = 0.0

                            val activityList = response.body()!!
                            for(i in activityList){
                                timeCnt += i.time!!
                                distCnt += i.distance!!
                                calCnt += i.kcal!!
                            }

                            binding.hoursTextView2.text = "$timeCnt minutes"
                            binding.kmTextView2.text = "$distCnt kilometers"
                            binding.tvKcalInput.text = "$calCnt calories"

                        }
                        else {
                            Toast.makeText(context, "Error code: "+ response.code().toString(), Toast.LENGTH_LONG).show()
                        }
                    }

                    override fun onFailure(call: Call<List<Activity>>, t: Throwable) {
                        Toast.makeText(context, t.message, Toast.LENGTH_LONG).show()
                    }
                }
            )
        }

        binding.btnGoalSet.setOnClickListener{
            nGFragment = GoalChangeFragment()

            GoalChangeFragment().show(
                childFragmentManager, "GoalChangeFragment"
            )
            nGFragment.showsDialog

            childFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, nGFragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)

        }
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGoalsBinding.inflate(inflater, container, false)
        return binding.root
    }
}

