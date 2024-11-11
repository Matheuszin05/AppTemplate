package com.matheus.barbearia_1.ui.avaliacao

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.matheus.barbearia_1.R

class AvaliacaoActivity : AppCompatActivity() {

    private lateinit var ratingBar: RatingBar
    private lateinit var submitButton: Button
    private lateinit var commentEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_avaliacao)

        // Corrigindo a referência do RatingBar
        ratingBar = findViewById(R.id.ratingBarService) // ID correto
        submitButton = findViewById(R.id.buttonSubmitRating)
        commentEditText = findViewById(R.id.editTextComment)

        // Configurando a ação do botão de envio
        submitButton.setOnClickListener {
            val rating = ratingBar.rating // Captura a avaliação
            val comment = commentEditText.text.toString() // Captura o comentário

            if (comment.isNotBlank()) {
                // Exibe a avaliação e o comentário
                Toast.makeText(this, "Sua avaliação: $rating estrelas\nComentário: $comment", Toast.LENGTH_LONG).show()
            } else {
                // Informa ao usuário para inserir um comentário
                Toast.makeText(this, "Por favor, insira um comentário.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
