package com.example.getfit.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import com.example.getfit.databinding.FragmentHeightChangeBinding

class HeightChangeFragment :  DialogFragment() {

    internal lateinit var context:Context

    private lateinit var binding : FragmentHeightChangeBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = FragmentHeightChangeBinding.inflate(requireActivity().layoutInflater)

        binding.btnOk.setOnClickListener{
            val h = binding.etHeight.text
            if(h.isEmpty()) {
                binding.etHeight.error = "Please fill the field!"
                binding.etHeight.requestFocus()
                return@setOnClickListener
            }
            else {
                parentFragmentManager.setFragmentResult("requestKey", bundleOf("height" to h.toString()))
                dismiss()
            }
        }

        return AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .create()
    }

}