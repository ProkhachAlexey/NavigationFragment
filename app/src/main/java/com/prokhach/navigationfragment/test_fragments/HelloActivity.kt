package com.prokhach.navigationfragment.test_fragments

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.javafaker.Faker
import com.prokhach.navigationfragment.R
import com.prokhach.navigationfragment.databinding.ActivityHelloBinding

class HelloActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHelloBinding

    private val faker = Faker.instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHelloBinding.inflate(layoutInflater).also { setContentView(it.root) }

        if (savedInstanceState == null) {
            val fragment = CounterFragment.newInstance(
                counterValue = 1,
                quote = createQuote()
            )
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragmentContainerHello, fragment)
                .commit()
        }
    }

    fun createQuote(): String {
        return faker.harryPotter().quote()
    }

    fun getScreenCount(): Int {
        return supportFragmentManager.backStackEntryCount + 1
    }
}