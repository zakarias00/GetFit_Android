package com.example.getfit.fragments

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import com.example.getfit.MainActivity
import com.example.getfit.R
import com.example.getfit.retrofit.RetrofitClient
import com.example.getfit.data.User
import com.example.getfit.databinding.FragmentHomeBinding
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*


class HomeFragment : Fragment() {

    private val retrofit = RetrofitClient.getfitAPI
    private lateinit var userToUpdate: User
    private var newWeight: Int = 0
    private var newHeight: Int = 0

    private lateinit var binding: FragmentHomeBinding
    private lateinit var hFragment : HeightChangeFragment
    private lateinit var wFragment: WeightChangeFragment

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
      }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        userToUpdate = arguments?.getSerializable("user") as User
        childFragmentManager.setFragmentResultListener("wKey", this) { key, bundle ->
            newWeight = bundle.getString("weight").toString().toInt()
            binding.weightInputText.placeholderText = "$newWeight kg"

            userToUpdate.weight = newWeight

            retrofit.updateUser(userToUpdate.id!!, userToUpdate).enqueue(
                object : Callback<ResponseBody?> {

                    override fun onResponse(
                        call: Call<ResponseBody?>,
                        response: Response<ResponseBody?>
                    ) {
                        if (response.code() != 200) {
                            Toast.makeText(
                                requireContext(),
                                "Error code:" + response.code().toString(),
                                Toast.LENGTH_LONG
                            ).show()
                        }

                    }

                    override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                        Toast.makeText(
                            context,
                            t.message,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
                )
            }


        childFragmentManager.setFragmentResultListener("requestKey", this) { key, bundle ->
            newHeight = bundle.getString("height").toString().toInt()

            binding.heightInputText.placeholderText = "$newHeight cm"
            retrofit.updateUser(userToUpdate.id!!, userToUpdate).enqueue(
            object : Callback<ResponseBody?> {

                override fun onResponse(
                    call: Call<ResponseBody?>,
                    response: Response<ResponseBody?>
                ) {
                    if(response.code()!=200){
                       Toast.makeText(
                            requireContext(),
                            "Error code:" + response.code().toString(),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                    Toast.makeText(
                        context,
                        t.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
           )
    }
}


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val level = userToUpdate.level.toString()
        val points = userToUpdate.points.toString()

        userToUpdate = arguments?.getSerializable("user") as User
        binding.usernameTextView.text = userToUpdate.username
        binding.heightInputText.placeholderText = userToUpdate.height.toString() + " cm"
        binding.weightInputText.placeholderText = userToUpdate.weight.toString() + " kg"
        binding.tvBirthDateInput.text = parseDateFromTimeStamp(userToUpdate.birthdate!!)
        binding.levelTextView.text = "$level level"
        binding.pointsTextView.text = "$points points"


        if(userToUpdate.gender!!)
            binding.tvGenderInput.text = "Female"
        else
            binding.tvGenderInput.text = "Male"

        if(userToUpdate.points != null) {

            val nr = points.toInt() - level.toInt() * 10
            val percentage = ((nr.toDouble()/10.0) * 100).toInt()
            setMotivationalText(binding.tvMotivation, percentage)


            binding.progressBar.max = 10

            val curr = points.toInt() - level.toInt() * 10

            binding.progressPercentage.text = ((curr.toDouble() / binding.progressBar.max.toDouble()) * 100).toInt().toString() + "%"
            ObjectAnimator.ofInt(binding.progressBar, "progress", curr)
                .setDuration(2000)
                .start()
           }

        else{
            binding.progressBar.max = 10
            val currProgress = 0
            ObjectAnimator.ofInt(binding.progressBar, "progress", currProgress)
                .setDuration(2000)
                .start()

            binding.progressPercentage.text = "0%"
            setMotivationalText(binding.tvMotivation, 0)
        }

        var frame = activity?.findViewById<FrameLayout>(R.id.fragment_container)

        binding.btnChange.setOnClickListener {
            hFragment = HeightChangeFragment()

            HeightChangeFragment().show(
                childFragmentManager, "HeightChangeFragment"
            )
            hFragment.showsDialog

            childFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, hFragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)

        }

        binding.btnChange2.setOnClickListener{
            wFragment = WeightChangeFragment()

            WeightChangeFragment().show(
                childFragmentManager, "WeightChangeFragment"
            )
            wFragment.showsDialog

            childFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, wFragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        }

        binding.btnLogOut.setOnClickListener{
            val intent = Intent(activity, MainActivity::class.java)
            activity!!.startActivity(intent)
            activity!!.finish()
        }
    }

    private fun parseDateFromTimeStamp(timesStampDate: Long): String{
        val format = SimpleDateFormat("yyyy-MM-dd")
        val date = Date(timesStampDate)
        return format.format(date)
    }

    private fun setMotivationalText(t:TextView, percentage:Int){
        if(percentage == 0){
            t.text = "It's time to start training!"
        }
        else if(percentage <= 25){
            t.text = "Good start, keep going!"
        }
        else if(percentage in 26..49){
            t.text = "Good job!"
        }
        else if(percentage == 50){
            t.text = "You are halfway there!"
        }
        else if(percentage in 51..75){
            t.text = "You are almost there!"
        }
        else if(percentage in 76..99){
            t.text = "Last effort to reach your goal!"
        }
        else {
            t.text = "You reached your goal. Well done!"
        }
    }

   /* private fun countPercentage(max:Int, curr:Int) : Int{

    }*/

}


