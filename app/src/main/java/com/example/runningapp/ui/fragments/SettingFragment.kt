package com.example.runningapp.ui.fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.runningapp.R
import com.example.runningapp.databinding.FragmentRunBinding
import com.example.runningapp.databinding.FragmentSettingBinding
import com.example.runningapp.others.Constants.KEY_NAME
import com.example.runningapp.others.Constants.KEY_WEIGHT
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SettingFragment : Fragment(R.layout.fragment_setting) {

    private lateinit var binding: FragmentSettingBinding

    @Inject
    lateinit var sharedPref: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadFieldsFromSharedPref()
        binding.btnApplyChanges.setOnClickListener {
            val success = applyChangesToSharedPref()
            if(success){
                Snackbar.make(view,"Saved changes",Snackbar.LENGTH_LONG).show()
            }else{
                Snackbar.make(view,"Please fill out all the fields",Snackbar.LENGTH_LONG).show()
            }
        }

    }

    private fun loadFieldsFromSharedPref(){
        val name = sharedPref.getString(KEY_NAME,"")
        val weight = sharedPref.getFloat(KEY_WEIGHT,80f)
        binding.etName.setText(name)
        binding.etWeight.setText(weight.toString())

    }

    private fun applyChangesToSharedPref():Boolean{
        val nameText = binding.etName.text.toString()
        val weightText = binding.etWeight.text.toString()
        if(nameText.isEmpty() || weightText.isEmpty()){
            return false
        }
        sharedPref.edit()
            .putString(KEY_NAME,nameText)
            .putFloat(KEY_WEIGHT,weightText.toFloat())
            .apply()

        val toolbarText = "Let's go $nameText"
        requireActivity().findViewById<TextView>(R.id.tvToolbarTitle).text = toolbarText
        return true

    }

}