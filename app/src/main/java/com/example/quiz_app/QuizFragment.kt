package com.example.quiz_app

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import java.io.BufferedReader
import java.io.InputStreamReader
import com.example.quiz_app.databinding.QuizFragmentBinding


class QuizFragment : Fragment() {

    private var _binding: QuizFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var questions: Array<String>
    private lateinit var options: Array<Array<String>>
    private lateinit var correctAnswers: Array<Int>
    private var currentQuestionIndex = 0
    private var score = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = QuizFragmentBinding.inflate(inflater, container, false)
        loadQuestions()
        return binding.root
    }

    private fun loadQuestions() {
        val inputStream = resources.openRawResource(R.raw.questions)
        val reader = BufferedReader(InputStreamReader(inputStream))
        val allLines = reader.readLines().shuffled().take(5)  // Shuffle and take the first 5

        questions = Array(allLines.size) { "" }
        options = Array(allLines.size) { arrayOf("", "", "") }
        correctAnswers = Array(allLines.size) { 0 }

        try {
            allLines.forEachIndexed { index, line ->
                val parts = line.split("|")
                if (parts.size < 5) throw IllegalArgumentException("Incorrect line format at index $index: $line")
                questions[index] = parts[0]
                options[index] = arrayOf(parts[1], parts[2], parts[3])
                correctAnswers[index] = parts[4].toInt()
            }
        } catch (e: Exception) {
            Log.e("QuizFragment", "Error reading questions", e)
            Toast.makeText(context, "Error loading questions: ${e.message}", Toast.LENGTH_LONG).show()
        } finally {
            reader.close()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // hide quiz components and Set up the Ready button
        hideQuizComponents()
        binding.readyButton.setOnClickListener {
            loadQuestions()  // Load and shuffle questions
            displayQuestion()  // Start displaying the first question
            showQuizComponents()  // Make quiz elements visible
            it.visibility = View.GONE  // Hide the Ready button
        }

        binding.option1Button.setOnClickListener { checkAnswer(0) }
        binding.option2Button.setOnClickListener { checkAnswer(1) }
        binding.option3Button.setOnClickListener { checkAnswer(2) }
        binding.restartButton.setOnClickListener { restartQuiz() }
    }

    private fun hideQuizComponents() {
        binding.questionText.visibility = View.GONE
        binding.option1Button.visibility = View.GONE
        binding.option2Button.visibility = View.GONE
        binding.option3Button.visibility = View.GONE
        binding.restartButton.visibility = View.GONE
    }

    private fun showQuizComponents() {
        binding.questionText.visibility = View.VISIBLE
        binding.option1Button.visibility = View.VISIBLE
        binding.option2Button.visibility = View.VISIBLE
        binding.option3Button.visibility = View.VISIBLE
        binding.restartButton.visibility = View.VISIBLE
    }


    private fun correctButtonColors(buttonIndex: Int) {
        when (buttonIndex) {
            0 -> binding.option1Button.setBackgroundColor(Color.GREEN)
            1 -> binding.option2Button.setBackgroundColor(Color.GREEN)
            2 -> binding.option3Button.setBackgroundColor(Color.GREEN)
        }
    }

    private fun wrongButtonColors(buttonIndex: Int) {
        when (buttonIndex) {
            0 -> binding.option1Button.setBackgroundColor(Color.RED)
            1 -> binding.option2Button.setBackgroundColor(Color.RED)
            2 -> binding.option3Button.setBackgroundColor(Color.RED)
        }
    }

    private fun resetButtonColors() {
        binding.option1Button.setBackgroundColor(Color.rgb(50 ,59 ,96))
        binding.option2Button.setBackgroundColor(Color.rgb(50 ,59 ,96))
        binding.option3Button.setBackgroundColor(Color.rgb(50 ,59 ,96))

    }

    private fun showResults() {
        Toast.makeText(context, "Your score: $score out of ${questions.size}", Toast.LENGTH_LONG).show()
        binding.restartButton.isEnabled = true
    }

    private fun displayQuestion() {
        binding.questionText.text = questions[currentQuestionIndex]
        binding.option1Button.text = options[currentQuestionIndex][0]
        binding.option2Button.text = options[currentQuestionIndex][1]
        binding.option3Button.text = options[currentQuestionIndex][2]
        resetButtonColors()
    }
    private fun checkAnswer(selectedAnswerIndex: Int) {
        val correctAnswerIndex = correctAnswers[currentQuestionIndex]

        if (selectedAnswerIndex == correctAnswerIndex) {
            score++
            correctButtonColors(selectedAnswerIndex)
        } else {
            wrongButtonColors(selectedAnswerIndex)
            correctButtonColors(correctAnswerIndex)
        }

        if (currentQuestionIndex < questions.size - 1) {
            currentQuestionIndex++
            binding.questionText.postDelayed({ displayQuestion() }, 1000)
        } else {
            showResults()
        }
    }

    private fun restartQuiz() {
        currentQuestionIndex = 0
        score = 0
        hideQuizComponents()
        binding.readyButton.visibility = View.VISIBLE  // Show the Ready button again
        binding.readyButton.isEnabled = true  // Ensure the Ready button is enabled
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}