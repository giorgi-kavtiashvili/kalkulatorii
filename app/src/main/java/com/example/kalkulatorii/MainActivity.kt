package com.example.kalkulatorii

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var workingsTextView: TextView
    private lateinit var resultsTextView: TextView
    private var addOperation = false
    private var addDecimal = true



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        resultsTextView = findViewById(R.id.resultsTextView)
        workingsTextView = findViewById(R.id.workingsTextView)
    }


    fun numberAction(view: View) {
        if (view is TextView) {
            if (view.text == ".") {
                if (addDecimal)
                    workingsTextView.append(view.text)
            } else {
                workingsTextView.append(view.text)
                addOperation = true
            }

        }

    }


    fun allclearaction(view: View) {
        workingsTextView.text = ""
        resultsTextView.text = ""
    }




    fun operatorAction(view: View) {
        if (view is TextView && addOperation) {
            workingsTextView.append(view.text)
            addOperation = false
            addDecimal = true
        }
    }

    fun deleteAction(view: View) {
        val length = workingsTextView.length()
        if (length > 0) {
            workingsTextView.text = workingsTextView.text.subSequence(0, length - 1)
        }
    }

    fun equalAction(view: View) {
        resultsTextView.text = calculateResults()

    }

    private fun calculateResults(): String {
        val digitsOperators = digitsOperators()
        if(digitsOperators.isEmpty()) return ""

        val timesDivision = timeDivisionCalculate(digitsOperators)
        if(timesDivision.isEmpty()) return ""

        val result = addSubtractCalculate(timesDivision)
        return result.toString()
    }

    private fun addSubtractCalculate(passedList: MutableList<Any>): Float {
        var result = passedList[0] as Float

        for(i in passedList.indices){
            if(passedList[i] is Char && i != passedList.lastIndex){
                val operator = passedList[i]
                val nextDigit = passedList[i + 1] as Float
                if (operator == '+')
                    result += nextDigit
                if (operator == '-')
                    result -= nextDigit
                if (operator == '*')
                    result *= nextDigit
                if (operator == '/')
                    result /= nextDigit
            }
        }

        return result

    }

    private fun timeDivisionCalculate(passedList: MutableList<Any>): MutableList<Any> {
        var list = passedList
        while (list.contains('*') || list.contains('/')){
            list = calcTimesDiv(list)
        }
        return list
    }

    private fun calcTimesDiv(passedList: MutableList<Any>): MutableList<Any> {
        val newList = mutableListOf<Any>()
        var restartIndex = passedList.size

        for (i in passedList.indices){
            if(passedList[i] is Char && i!=passedList.lastIndex && i < restartIndex  ){
                val operator = passedList[i]
                val prevDigit = passedList[i - 1] as Float
                val nextDigit = passedList[i + 1] as Float
                when(operator){
                    '*' -> {
                        newList.add(nextDigit * prevDigit)
                        restartIndex = i + 1
                    }
                    '/' -> {
                        newList.add(prevDigit / nextDigit)
                        restartIndex = i + 1
                    }
                    else -> {
                        newList.add(prevDigit)
                        newList.add(operator)
                    }
                }
            }
            if(i > restartIndex)
                newList.add(passedList[i])
        }
        return newList

    }

    private fun digitsOperators(): MutableList<Any> {
        val list = mutableListOf<Any>()
        var currentDigit = ""
        for(character in workingsTextView.text){
            if(character.isDigit() || character == '.')
                currentDigit += character
            else{
                list.add(currentDigit.toFloat())
                currentDigit = ""
                list.add(character)
            }
        }
        if(currentDigit != "")
            list.add(currentDigit.toFloat())
        return list
    }

}


