package com.example.getfit.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.getfit.R
import com.example.getfit.retrofit.RetrofitClient
import com.example.getfit.data.Activity
import com.example.getfit.databinding.FragmentHistoryBinding
import com.example.getfit.data.Sport
import com.example.getfit.data.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class HistoryFragment : Fragment() {

    private lateinit var binding: FragmentHistoryBinding
    private var retrofit = RetrofitClient.getfitAPI
    private lateinit var history: List<Activity>
    private lateinit var sports: List<Sport>
    private val newActivityFragment = AddNewActivityFragment()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val user = arguments?.getSerializable("user") as User
        val uId = user.id
       // var sports:List<Sport> = listOf()
         retrofit.getSports().enqueue(
            object: Callback<List<Sport>>{
                override fun onResponse(call: Call<List<Sport>>, response: Response<List<Sport>>) {
                    if(response.code() == 200){
                        sports = response.body()!!
                    }
                    else{
                        Toast.makeText(context, "Error code: " + response.code().toString(), Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<List<Sport>>, t: Throwable) {
                    Toast.makeText(context, t.message, Toast.LENGTH_LONG).show()
                }
            }
        )

        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            resources.getStringArray(R.array.sports_array)
        )
        //adapter.add("All")
        binding.spinnerHistory.adapter = adapter
        binding.spinnerHistory.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (binding.spinnerHistory.selectedItem == "All") {
                    retrofit.getActivityByUserId(uId!!).enqueue(
                        object : Callback<List<Activity>> {
                            override fun onResponse(
                                call: Call<List<Activity>>,
                                response: Response<List<Activity>>
                            ) {
                                if (response.code() == 200) {
                                    history = response.body()!!
                                    val list: ArrayList<String> = ArrayList<String>()
                                    if (::sports.isInitialized) {
                                        for (s in history) {
                                            list.add(sports[s.sportId!!.toInt() - 1].type.toString() + " ")
                                            list.add(" Date: " + parseDateFromTimeStamp(s.date!!) + "\t\t" + s.distance.toString() + " km\t\t" + s.time.toString() + " min\t\t" + s.kcal.toString() + " kcal\r")
                                            //  list.add(System.lineSeparator())
                                        }
                                    }
                                    val adapter: Adapter = ArrayAdapter<String>(
                                        context!!,
                                        android.R.layout.simple_list_item_1, list
                                    )

                                    binding.listView.adapter = adapter as ListAdapter?
                                }
                            }

                            override fun onFailure(call: Call<List<Activity>>, t: Throwable) {
                                Toast.makeText(context, t.message, Toast.LENGTH_LONG).show()

                            }

                        })
                } else {
                    retrofit.getActivityByUserId(uId!!).enqueue(
                        object : Callback<List<Activity>> {
                            override fun onResponse(
                                call: Call<List<Activity>>,
                                response: Response<List<Activity>>
                            ) {
                                if (response.code() == 200) {
                                    history = response.body()!!
                                    val list: ArrayList<String> = ArrayList<String>()
                                    if (::sports.isInitialized) {
                                        for (s in history) {
                                            if (sports[s.sportId!!.toInt() - 1].type == resources.getStringArray(
                                                    R.array.sports_array
                                                )[binding.spinnerHistory.selectedItemPosition]
                                            ) {
                                                list.add(sports[s.sportId!!.toInt() - 1].type.toString() + " ")
                                                list.add(" Date: " + parseDateFromTimeStamp(s.date!!) + "\t\t" + s.distance.toString() + " km\t\t" + s.time.toString() + " min\t\t" + s.kcal.toString() + " kcal\r")
                                                //  list.add(System.lineSeparator())
                                            }
                                        }
                                    }
                                    val adapter: Adapter = ArrayAdapter<String>(
                                        context!!,
                                        android.R.layout.simple_list_item_1, list
                                    )

                                    binding.listView.adapter = adapter as ListAdapter?
                                }
                            }

                            override fun onFailure(call: Call<List<Activity>>, t: Throwable) {
                                Toast.makeText(context, t.message, Toast.LENGTH_LONG).show()

                            }

                        })
                }
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }



        binding.fBtnHistory
            .setOnClickListener{
            val arguments = Bundle()
            arguments.putString("userId", uId.toString())
            newActivityFragment.arguments = arguments
            val transaction = activity?.supportFragmentManager!!.beginTransaction()
            transaction.replace(R.id.fragment_container, newActivityFragment)
            transaction.commit()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
      //  var sportsList : List<Sport> = listOf()

        binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root

    }

    fun parseDateFromTimeStamp(timesStampDate: Long): String{
        val format = SimpleDateFormat("yyyy-MM-dd")
        val date = Date(timesStampDate)
        return format.format(date)
    }

}