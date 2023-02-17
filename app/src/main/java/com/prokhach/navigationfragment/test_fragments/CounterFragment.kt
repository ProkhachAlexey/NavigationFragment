package com.prokhach.navigationfragment.test_fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.prokhach.navigationfragment.R
import com.prokhach.navigationfragment.databinding.FragmentCounterBinding

class CounterFragment : Fragment() {

    private lateinit var binding: FragmentCounterBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCounterBinding.inflate(inflater, container, false)

        with(binding) {
            counterTextView.text = getString(R.string.screen_label, getCounterValue())
            quoteTextView.text = getQuote()
            launchNextButton.setOnClickListener { launchNext() }
            goBackButton.setOnClickListener { goBack() }
        }

        return binding.root
    }

    private fun launchNext() {
        val fragment = newInstance(
            counterValue = (requireActivity() as HelloActivity).getScreenCount() + 1,
            quote = (requireActivity() as HelloActivity).createQuote()
        )
        parentFragmentManager
            .beginTransaction()
            .addToBackStack(null)
            .replace(R.id.fragmentContainerHello, fragment)
            .commit()
    }

    private fun goBack() {
        requireActivity().onBackPressed()
    }

    private fun getCounterValue(): Int = requireArguments().getInt(ARG_COUNTER_VALUE)

    private fun getQuote(): String =
        requireArguments().getString(ARG_QUOTE) ?: throw RuntimeException("Unknown quote")

    companion object {

        @JvmStatic
        private val ARG_COUNTER_VALUE = "ARG_COUNTER_VALUE"

        @JvmStatic
        private val ARG_QUOTE = "`ARG_QUOTE"

        @JvmStatic
        fun newInstance(counterValue: Int, quote: String): CounterFragment {
            val args = Bundle().apply {
                putInt(ARG_COUNTER_VALUE, counterValue)
                putString(ARG_QUOTE, quote)
            }
            val fragment = CounterFragment()
            fragment.arguments = args
            return fragment
        }
    }
}