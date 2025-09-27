package com.example.assignment1

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.progressindicator.LinearProgressIndicator
import android.widget.TextView

class MainActivity3 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main3)
        // Adjust the view padding to account for the system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // Update the progress bar
        val progressBar = findViewById<LinearProgressIndicator>(R.id.progressBar)
        progressBar.progress = 100  // Page 3
        // Retrieve the EMI value passed from the previous activities
        val emi = intent.getDoubleExtra("EMI", 0.0)
        val income = intent.getDoubleExtra("Income", 0.0)
        val expenses = intent.getDoubleExtra("Expenses", 0.0)
        // Calculate and display the remaining balance
        val remaining = income - (emi + expenses)
        val resultText = findViewById<TextView>(R.id.resultText)
        // Display the result. Adjust wording according to the balance.
        if (remaining >= 0) {
            resultText.text = "Remaining Savings: %.2f".format(remaining)
        } else {
            resultText.text = "Deficit: %.2f".format(-remaining)
        }
    }
}
