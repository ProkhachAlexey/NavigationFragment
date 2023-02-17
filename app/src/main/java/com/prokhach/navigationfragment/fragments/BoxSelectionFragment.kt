package com.prokhach.navigationfragment.fragments

import android.app.AlertDialog
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.prokhach.navigationfragment.R
import com.prokhach.navigationfragment.databinding.FragmentBoxSelectionBinding
import com.prokhach.navigationfragment.databinding.ItemBoxBinding
import com.prokhach.navigationfragment.fragments.contract.HasCustomTitle
import com.prokhach.navigationfragment.fragments.contract.navigator
import com.prokhach.navigationfragment.model.Options
import java.lang.Long.max
import kotlin.properties.Delegates
import kotlin.random.Random

class BoxSelectionFragment : Fragment(), HasCustomTitle {

    private lateinit var binding: FragmentBoxSelectionBinding
    private lateinit var options: Options
    private var timerStartTimerStamp by Delegates.notNull<Long>()
    private var boxIndex by Delegates.notNull<Int>()
    private var alreadyDone = false
    private var timerHandler: TimerHandler? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        options = arguments?.getParcelable(ARG_OPTIONS) ?:
        throw IllegalArgumentException("Can't launch BoxSelectionActivity without options")
        boxIndex = savedInstanceState?.getInt(KEY_INDEX) ?: Random.nextInt(options.boxCount)

        timerHandler = if (options.isTimerEnabled) {
            TimerHandler()
        } else {
            null
        }
        timerHandler?.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBoxSelectionBinding.inflate(inflater, container, false)
        createBoxes()
        return binding.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_INDEX, boxIndex)
        timerHandler?.onSaveInstanceState(outState)
    }

    override fun onStart() {
        super.onStart()
        timerHandler?.onStart()
    }

    override fun onStop() {
        super.onStop()
        timerHandler?.onStop()
    }

    override fun getTitleRes(): Int = R.string.select_box

    private fun createBoxes() {
        val boxBindings = (0 until options.boxCount).map { index ->
            val boxBinding = ItemBoxBinding.inflate(layoutInflater)
            with(boxBinding) {
                root.id = View.generateViewId()
                boxTitleTextView.text = getString(R.string.box_title, index + 1)
                root.setOnClickListener { view -> onBoxSelected(view) }
                root.tag = index
            }
            binding.root.addView(boxBinding.root)
            boxBinding
        }

        binding.flow.referencedIds = boxBindings.map { it.root.id }.toIntArray()
    }

    private fun onBoxSelected(view: View) {
        if (view.tag as Int == boxIndex) {
            alreadyDone = true
            navigator().showConfigurationsScreen()
        } else {
            Toast.makeText(context, R.string.empty_box, Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateTimerUi() {
        if (getRemainingSecond() >= 0) {
            binding.timerTextView.visibility = View.VISIBLE
            binding.timerTextView.text = getString(R.string.timer_value, getRemainingSecond())
        } else {
            binding.timerTextView.visibility = View.GONE
        }
    }

    private fun showTimerEndDialog() {
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.the_end))
            .setMessage(getString(R.string.timer_end_message))
            .setCancelable(false)
            .setPositiveButton(android.R.string.ok) { _, _ -> navigator().goBack() }
            .create()
        dialog.show()
    }

    private fun getRemainingSecond(): Long {
        val finishedAt = timerStartTimerStamp + TIMER_DURATION
        return max(0, (finishedAt - System.currentTimeMillis()) / 1000)
    }

    inner class TimerHandler {
        private var timer: CountDownTimer? = null

        fun onCreate(savedInstanceState: Bundle?) {
            timerStartTimerStamp =
                savedInstanceState?.getLong(KEY_START_TIMESTAMP) ?: System.currentTimeMillis()
            alreadyDone = savedInstanceState?.getBoolean(KEY_ALREADY_DONE) ?: false
        }

        fun onSaveInstanceState(outState: Bundle) {
            outState.putLong(KEY_START_TIMESTAMP, timerStartTimerStamp)
            outState.putBoolean(KEY_ALREADY_DONE, alreadyDone)
        }

        fun onStart() {
            if (alreadyDone) return
            timer = object : CountDownTimer(getRemainingSecond() * 1000, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    updateTimerUi()
                }

                override fun onFinish() {
                    updateTimerUi()
                    showTimerEndDialog()
                }
            }
            updateTimerUi()
            timer?.start()
        }

        fun onStop() {
            timer?.cancel()
            timer = null
        }
    }

    companion object {
        @JvmStatic
        private val ARG_OPTIONS = "EXTRA_OPTIONS"
        @JvmStatic
        private val KEY_INDEX = "KEY_INDEX"
        @JvmStatic
        private val KEY_START_TIMESTAMP = "KEY_START_TIMESTAMP"
        @JvmStatic
        private val KEY_ALREADY_DONE = "KEY_ALREADY_DONE"
        @JvmStatic
        private val TIMER_DURATION = 10_000L

        @JvmStatic
        fun newInstance(options: Options): BoxSelectionFragment {
            val args = Bundle().apply {
                putParcelable(ARG_OPTIONS, options)
            }
            val fragment = BoxSelectionFragment().apply {
                arguments = args
            }
            return fragment
        }
    }
}