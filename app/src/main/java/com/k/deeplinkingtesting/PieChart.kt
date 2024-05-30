package com.k.deeplinkingtesting

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.intrusoft.scatter.ChartData
import kotlin.math.pow


class PieChart : AppCompatActivity() {
    private var loanAmount: EditText? = null
    private var interestRate:EditText? = null
    private var loanTenure:EditText? = null
    private lateinit var calculateButton: Button
    private var emiResult: TextView? = null
    private  var pieChart : com.intrusoft.scatter.PieChart? = null
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_pie_chart)


        loanAmount = findViewById(R.id.loanAmount);
        interestRate = findViewById(R.id.interestRate);
        loanTenure = findViewById(R.id.loanTenure);
        calculateButton = findViewById(R.id.calculateButton);
        emiResult = findViewById(R.id.emiResult);
        pieChart = findViewById(R.id.pie_chart);

        calculateButton.setOnClickListener {

            calculateEMI()
        }




    }
    private fun calculateEMI() {
        val principal = loanAmount!!.text.toString().toDouble()
        val annualInterestRate = interestRate!!.text.toString().toDouble()
        val tenureYears = loanTenure!!.text.toString().toInt()
        val monthlyInterestRate = annualInterestRate / 12 / 100
        val numberOfMonths = tenureYears * 12
        val emi: Double =
            principal * monthlyInterestRate * (1 + monthlyInterestRate).pow(numberOfMonths.toDouble()) /
                    ((1 + monthlyInterestRate).pow(numberOfMonths.toDouble()) - 1)
        emiResult!!.text = String.format("Monthly EMI: %.2f", emi)
        val totalPayment = emi * numberOfMonths
        val totalInterest = totalPayment - principal
        showPieChart(principal, totalInterest,totalPayment)
    }

    private fun showPieChart(principal: Double, interest: Double,totalPayment : Double) {
        val data: MutableList<ChartData> = ArrayList()
        data.add(ChartData("Principal", principal.toFloat())) //ARGS-> (display text, percentage)

        data.add(ChartData("totalInterest", interest.toFloat()))
        data.add(ChartData("totalPayment",totalPayment.toFloat() ))
        data.add(ChartData("Fourth", 10f))
        pieChart!!.setChartData(data)
    }
}