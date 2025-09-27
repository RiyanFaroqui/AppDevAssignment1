package com.example.assignment1

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import com.google.android.material.button.MaterialButton
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import kotlin.math.pow

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        //Adjust the view padding to account for the system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // Gather user input from the text boxes
        val loanAmountInput = findViewById<EditText>(R.id.loanAmount)
        val interestRateInput = findViewById<EditText>(R.id.interestRate)
        val loanTermInput = findViewById<EditText>(R.id.loanTerm)
        val emiResultTextView = findViewById<TextView>(R.id.emiResult)
        val nextButton = findViewById<MaterialButton>(R.id.nextButton)

        // Add a TextWatcher to the EditText fields, which will update the UI and button state
        val watcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // This call updates the UI and enables/disables the button
                updateEmiDisplayAndButtonState(loanAmountInput, interestRateInput, loanTermInput, emiResultTextView, nextButton)
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }
        // Attach the watcher to the EditText fields
        loanAmountInput.addTextChangedListener(watcher)
        interestRateInput.addTextChangedListener(watcher)
        loanTermInput.addTextChangedListener(watcher) // Corrected from loanTerm

        nextButton.setOnClickListener {
            // Get the raw text values again for calculation when button is clicked
            // This ensures we use the latest values if TextWatcher didn't perfectly align
            val loanAmountText = loanAmountInput.text.toString()
            val interestRateText = interestRateInput.text.toString()
            val loanTermText = loanTermInput.text.toString()

            // Perform calculation again, but this time for the intent
            // You can call a more focused calculation function here
            val emiValue = calculateEmiForIntent(loanAmountText, interestRateText, loanTermText)

            if (emiValue != null && nextButton.isEnabled) { // Check if EMI is valid and button is enabled
                val intent = Intent(this, MainActivity2::class.java)
                intent.putExtra("EMI", emiValue)   // Send EMI value to the next activity
                startActivity(intent)
            }
        }
        nextButton.isEnabled = false // Initially disable
    }

    // Function to update UI and button state, called by TextWatcher
    private fun updateEmiDisplayAndButtonState(
        loanAmountEditText: EditText,
        interestRateEditText: EditText,
        loanTermEditText: EditText,
        emiResultTextView: TextView,
        nextButtonToUpdate: MaterialButton
    ) {
        // Gather user input from the text boxes
        val loanText = loanAmountEditText.text.toString()
        val rateText = interestRateEditText.text.toString()
        val termText = loanTermEditText.text.toString()

        // Check if any of the fields are empty
        if (loanText.isEmpty() || rateText.isEmpty() || termText.isEmpty()) {
            emiResultTextView.visibility = TextView.GONE
            nextButtonToUpdate.isEnabled = false
            return
        }

        try {
            // Convert input to numbers
            val principal = loanText.toDouble()
            val annualRate = rateText.toDouble()
            val loanTermYears = termText.toDouble()

            // Basic input validation-- Ensure they're positive and integers
            if (principal <= 0 || annualRate < 0 || loanTermYears <= 0) {
                emiResultTextView.text = "Invalid input" // Tell the user to check input
                emiResultTextView.visibility = TextView.VISIBLE
                nextButtonToUpdate.isEnabled = false
                return
            }
            // Convert loan term to months
            val loanTermMonths = (loanTermYears * 12).toInt()
            if (loanTermMonths <= 0) {
                emiResultTextView.text = "Invalid loan term" // Tell the user to check input
                emiResultTextView.visibility = TextView.VISIBLE
                nextButtonToUpdate.isEnabled = false
                return
            }
            // Calculate EMI
            val monthlyRate = annualRate / 12 / 100
            val emi = if (monthlyRate == 0.0) {
                principal / loanTermMonths
            } else {
                (principal * monthlyRate * (1 + monthlyRate).pow(loanTermMonths)) /
                        ((1 + monthlyRate).pow(loanTermMonths) - 1)
            }
            // Format and display the result
            val emiRounded = "%.2f".format(emi)
            emiResultTextView.text = "Monthly EMI: $emiRounded"
            emiResultTextView.visibility = TextView.VISIBLE
            nextButtonToUpdate.isEnabled = true

          // error checking
        } catch (e: NumberFormatException) {
            emiResultTextView.text = "Enter valid numbers"
            emiResultTextView.visibility = TextView.VISIBLE
            nextButtonToUpdate.isEnabled = false
        } catch (e: Exception) {
            emiResultTextView.text = "Error"
            emiResultTextView.visibility = TextView.VISIBLE
            nextButtonToUpdate.isEnabled = false
        }
    }
    // New function specifically for calculating EMI value for the Intent
    private fun calculateEmiForIntent(loanAmountStr: String, interestRateStr: String, loanTermStr: String): Double? {
        return try {
            // Convert input to numbers
            val principal = loanAmountStr.toDouble()
            val annualRate = interestRateStr.toDouble()
            val loanTermYears = loanTermStr.toInt()

            // Basic input validation-- Ensure they're positive and integers
            if (principal <= 0 || annualRate < 0 || loanTermYears <= 0) {
                return null
            }
            // Convert loan term to months
            val loanTermMonths = (loanTermYears * 12).toInt()
            if (loanTermMonths <= 0) {
                return null
            }
            // Calculate EMI
            val monthlyRate = annualRate / 12 / 100
            if (monthlyRate == 0.0) {
                if (loanTermMonths > 0) principal / loanTermMonths else 0.0
            } else {
                (principal * monthlyRate * (1 + monthlyRate).pow(loanTermMonths)) /
                        ((1 + monthlyRate).pow(loanTermMonths) - 1)
            }
          // error checking
        } catch (e: NumberFormatException) {
            null
        } catch (e: Exception) {
            null
        }
    }
}
