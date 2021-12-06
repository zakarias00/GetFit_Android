package com.example.getfit.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.getfit.retrofit.RetrofitClient
import com.example.getfit.data.User
import com.example.getfit.databinding.FragmentLeaderboardBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LeaderboardFragment : Fragment() {

    private lateinit var binding:FragmentLeaderboardBinding
    private var retrofit = RetrofitClient.getfitAPI
    lateinit var adapter: ArrayAdapter<*>
    lateinit var listView: ListView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLeaderboardBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listView = binding.listView

        retrofit.getUsers().enqueue(object : Callback<List<User>> {

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
            }

            override fun onResponse(
                call: Call<List<User>>,
                response: Response<List<User>>
            ) {
               val userList = response.body()!!
                val list : ArrayList<String> = ArrayList<String>()
                for (u in userList) {
                    list.add(u.username.toString() + " level:" + u.level.toString() + " points:" + u.points.toString())
                }

               // if(list.size == userList.size) {
                    adapter= ArrayAdapter<String>(
                        context!!,
                        android.R.layout.simple_list_item_1, list
                    )

                    binding.listView.adapter = adapter
                            //as ListAdapter?
                binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String): Boolean {
                        if (list.contains(query)) {
                            adapter.filter.filter(query)
                        } else {
                            Toast.makeText(context, "No Match found", Toast.LENGTH_LONG).show()
                        }
                        return false
                    }
                    override fun onQueryTextChange(newText: String): Boolean {
                        adapter.filter.filter(newText)
                        return false
                    }
                })

                }
           // }

        })


    }

}