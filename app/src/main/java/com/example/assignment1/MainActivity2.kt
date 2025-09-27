package com.example.assignment1

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.content.Intent
import com.google.android.material.button.MaterialButton
import com.google.android.material.progressindicator.LinearProgressIndicator
import android.widget.EditText
class MainActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main2)
        // Adjust the view padding to account for the system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Retrieve the EMI value passed from the previous activity
        val emi = intent.getDoubleExtra("EMI", 0.0)

        // Initialize EditText views using findViewById
        val incomeInput = findViewById<EditText>(R.id.income)
        val expensesInput = findViewById<EditText>(R.id.expense)

        val nextButton = findViewById<MaterialButton>(R.id.nextButton)

        // Set a click listener for the next button
        nextButton.setOnClickListener {
            val incomeString = incomeInput.text.toString()
            val expensesString = expensesInput.text.toString()

            // Use toDoubleOrNull() to avoid crashes if input is not a number
            val income = incomeString.toDoubleOrNull()
            val expenses = expensesString.toDoubleOrNull()

            // Basic input validation
            var hasError = false
            if (income == null) {
                incomeInput.error = "Please enter a valid income"
                hasError = true
            } else if (income < 0) {
                incomeInput.error = "Income cannot be negative"
                hasError = true
            }

            if (expenses == null) {
                expensesInput.error = "Please enter valid expenses"
                hasError = true
            } else if (expenses < 0) {
                expensesInput.error = "Expenses cannot be negative"
                hasError = true
            }

            if (hasError) {
                return@setOnClickListener // Stops processing if there are errors
            }

            // If no errors, proceed to the next activity
            val intent = Intent(this, MainActivity3::class.java)
            intent.putExtra("EMI", emi)
            intent.putExtra("Income", income)
            intent.putExtra("Expenses", expenses)
            startActivity(intent)
        }
        // Update the progress bar
        val progressBar = findViewById<LinearProgressIndicator>(R.id.progressBar)
        progressBar.progress = 66  // Page 2
    }
}
