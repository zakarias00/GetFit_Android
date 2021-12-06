package com.example.getfit.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import com.example.getfit.R
import com.example.getfit.databinding.FragmentGoalChangeBinding
import com.example.getfit.databinding.FragmentHeightChangeBinding


class GoalChangeFragment : DialogFragment() {
    private lateinit var binding : FragmentGoalChangeBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = FragmentGoalChangeBinding.inflate(requireActivity().layoutInflater)

        binding.btnSetGoal.setOnClickListener{
            val newGoal = binding.etNewGoal.text
            if(newGoal.isEmpty() || (newGoal.toString().toInt() > 7) || (newGoal.toString().toInt() < 1)) {
                binding.etNewGoal.error = "Please fill the field, and enter a valid goal number: between 1 and 7!"
                binding.etNewGoal.requestFocus()
                return@setOnClickListener
            }
            else {
                parentFragmentManager.setFragmentResult("requestKey", bundleOf("newGoal" to newGoal.toString()))
                dismiss()
            }
        }

        return AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .create()
    }

}