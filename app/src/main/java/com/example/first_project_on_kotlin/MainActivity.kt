package com.example.first_project_on_kotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    private lateinit var button: Button
    private lateinit var noteButton: Button
    private lateinit var newCopyNote: Button
    private lateinit var printButton:Button
    private var counter = 0
    private val note = Note("Антон", "Петров", 3)
    private val copyNote = note.copy(SecondName = "Иванов", age = 110)
    private var array=Array(10) { i -> i * i }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        button = findViewById(R.id.hello_button)
        noteButton = findViewById(R.id.note_button)
        newCopyNote = findViewById(R.id.copy_note_button)
        printButton = findViewById(R.id.print_array_button)
        buttonOnClickListener(button)
        noteButtonOnClickListener(noteButton)
        copyButtonClickListener(newCopyNote)
        printArrayButtonOnClickListener(printButton)

    }

    private fun buttonOnClickListener(button: Button) {
        button.setOnClickListener {
            if (button.isEnabled) {
                counter++
                Toast.makeText(this, "Количество нажатий:$counter", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun printArrayButtonOnClickListener(button: Button){
        button.setOnClickListener {
            for ((index, value) in array.withIndex()) {
                println("Значение индекса $index равно $value")
            }
        }
    }

    private fun noteButtonOnClickListener(button: Button) {
        button.setOnClickListener {
            Toast.makeText(this, "Имя: " + note.firstName
                    + " Фамилия: " + note.SecondName
                    + " Возраст: " + note.age,
                    Toast.LENGTH_SHORT).show()
        }
    }

    private fun copyButtonClickListener(button: Button) {
        button.setOnClickListener {
            Toast.makeText(this, "Имя: " + copyNote.firstName
                    + " Фамилия: " + copyNote.SecondName
                    + " Возраст: " + copyNote.age,
                    Toast.LENGTH_SHORT).show()
        }
    }

}

data class Note(val firstName: String, val SecondName: String, val age: Int)

