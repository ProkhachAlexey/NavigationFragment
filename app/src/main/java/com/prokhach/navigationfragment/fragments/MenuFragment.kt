package com.prokhach.navigationfragment.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.prokhach.navigationfragment.databinding.FragmentMenuBinding
import com.prokhach.navigationfragment.fragments.contract.navigator
import com.prokhach.navigationfragment.model.Options

class MenuFragment : Fragment() {

    private lateinit var options: Options

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        options = savedInstanceState?.getParcelable(KEY_OPTIONS) ?: Options.DEFAULT
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentMenuBinding.inflate(inflater, container, false)

        navigator().listenResult(Options::class.java, viewLifecycleOwner) {
            this.options = it
        }

        with(binding) {
            openBoxButton.setOnClickListener { onOpenBoxPressed() }
            optionsButton.setOnClickListener { onOptionsPressed() }
            aboutButton.setOnClickListener { onAboutPressed() }
            exitButton.setOnClickListener { onExitPressed() }
        }

        return binding.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(KEY_OPTIONS, options)
    }

    private fun onOpenBoxPressed() {
        navigator().showBoxSelectionScreen(options)
    }

    private fun onOptionsPressed() {
        navigator().showOptionsScreen(options)
    }

    private fun onAboutPressed() {
        navigator().showAboutScreen()
    }

    private fun onExitPressed() {
        navigator().goBack()
    }

    companion object {

        @JvmStatic
        private val KEY_OPTIONS = "OPTIONS"
    }
}