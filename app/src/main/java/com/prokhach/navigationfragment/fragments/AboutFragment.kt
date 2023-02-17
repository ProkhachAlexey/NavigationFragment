package com.prokhach.navigationfragment.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.prokhach.navigationfragment.R
import com.prokhach.navigationfragment.databinding.FragmentAboutBinding
import com.prokhach.navigationfragment.fragments.contract.HasCustomTitle
import com.prokhach.navigationfragment.fragments.contract.navigator

class AboutFragment : Fragment(), HasCustomTitle {

    private lateinit var binding: FragmentAboutBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAboutBinding.inflate(inflater, container, false)

        binding.okButton.setOnClickListener { navigator().goBack() }

        return binding.root
    }

    override fun getTitleRes(): Int = R.string.about
}