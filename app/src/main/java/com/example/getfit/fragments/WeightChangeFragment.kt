package com.example.getfit.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import com.example.getfit.databinding.FragmentWeightChangeBinding

class WeightChangeFragment : DialogFragment() {

    internal lateinit var context: Context
    private lateinit var binding : FragmentWeightChangeBinding

    companion object {
        const val TAG = "DataChangeFragment"
    }

  /*  override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parentFragmentManager.setFragmentResultListener("id"){ key, bundle ->
            val weight = binding.etWeight
        }
        id = arguments?.getString("ID").toString().toInt()
    }
*/
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {


        binding = FragmentWeightChangeBinding.inflate(requireActivity().layoutInflater)

        binding.btnOk.setOnClickListener{
            val w = binding.etWeight.text
            if(w.isEmpty()) {
                binding.etWeight.error = "Please fill the field!"
                binding.etWeight.requestFocus()
                return@setOnClickListener

            }

            else{
                parentFragmentManager.setFragmentResult("wKey", bundleOf("weight" to w.toString()))
                dismiss()

            }
        }

        return AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .create()
    }

}