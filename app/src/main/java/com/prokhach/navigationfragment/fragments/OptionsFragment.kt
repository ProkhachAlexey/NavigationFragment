package com.prokhach.navigationfragment.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.prokhach.navigationfragment.R
import com.prokhach.navigationfragment.databinding.FragmentOptionsBinding
import com.prokhach.navigationfragment.fragments.contract.CustomAction
import com.prokhach.navigationfragment.fragments.contract.HasCustomAction
import com.prokhach.navigationfragment.fragments.contract.HasCustomTitle
import com.prokhach.navigationfragment.fragments.contract.navigator
import com.prokhach.navigationfragment.model.Options

class OptionsFragment : Fragment(), HasCustomAction, HasCustomTitle {

    private lateinit var binding: FragmentOptionsBinding

    private lateinit var options: Options

    private lateinit var boxCounterItems: List<BoxCountItem>
    private lateinit var myAdapter: ArrayAdapter<BoxCountItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        options =
            savedInstanceState?.getParcelable<Options>(KEY_OPTIONS) ?:
            arguments?.getParcelable(ARG_OPTIONS) ?:
            throw IllegalArgumentException("You need to specify options to launch this fragment")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOptionsBinding.inflate(inflater, container, false)

        setupSpinner()
        setupCheckBox()
        updateUi()

        with(binding) {
            cancelButton.setOnClickListener { onCancelPressed() }
            confirmButton.setOnClickListener { onConfirmPressed() }
        }

        return binding.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(KEY_OPTIONS, options)
    }

    override fun getTitleRes(): Int = R.string.options

    override fun getCustomAction(): CustomAction {
        return CustomAction(
            iconRes = R.drawable.ic_done,
            textRes = R.string.done,
            onCustomAction = {
                onConfirmPressed()
            }
        )
    }

    private fun setupSpinner() {
        boxCounterItems = (1..6).map { BoxCountItem(it, resources.getQuantityString(R.plurals.boxes, it, it)) }
        myAdapter = ArrayAdapter(
            requireContext(),
            R.layout.item_spinner,
            boxCounterItems
        )
        myAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1)

        binding.boxCountSpinner.adapter = myAdapter
        binding.boxCountSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val count = boxCounterItems[position].count
                options = options.copy(boxCount = count)
            }
        }
    }

    private fun setupCheckBox() {
        binding.enableTimerCheckBox.setOnClickListener {
            options = options.copy(isTimerEnabled =  binding.enableTimerCheckBox.isChecked)
        }
    }

    private fun updateUi() {
        binding.enableTimerCheckBox.isChecked = options.isTimerEnabled

        val currentIndex = boxCounterItems.indexOfFirst { it.count == options.boxCount }
        binding.boxCountSpinner.setSelection(currentIndex)
    }

    private fun onCancelPressed() {
        navigator().goBack()
    }

    private fun onConfirmPressed() {
        navigator().publishResult(options)
        navigator().goBack()
    }

    companion object {
        @JvmStatic
        private val ARG_OPTIONS = "ARG_OPTIONS"
        @JvmStatic
        private val KEY_OPTIONS = "KEY_OPTIONS"

        @JvmStatic
        fun newInstance(options: Options): OptionsFragment {
            val args = Bundle()
            args.putParcelable(ARG_OPTIONS, options)
            val fragment = OptionsFragment()
            fragment.arguments = args
            return fragment
        }
    }

    class BoxCountItem(
        val count: Int,
        private val optionTitle: String
    ) {
        override fun toString(): String {
            return optionTitle
        }
    }
}