package com.matheus.barbearia_1.ui.avaliacao

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.matheus.barbearia_1.R

class AvaliacaoActivity : AppCompatActivity() {

    private lateinit var ratingBarService: RatingBar
    private lateinit var ratingBarEmployee: RatingBar
    private lateinit var comentarioEditText: EditText
    private lateinit var submitButton: Button
    private lateinit var storeId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_avaliacao)

        // Inicializando views
        ratingBarService = findViewById(R.id.ratingBarService)
        ratingBarEmployee = findViewById(R.id.ratingBarEmployee)
        comentarioEditText = findViewById(R.id.editTextComment)
        submitButton = findViewById(R.id.buttonSubmitRating)

        // Obtendo o storeId da intenção
        storeId = intent.getStringExtra("storeId") ?: ""

        // Configurando o botão de envio
        submitButton.setOnClickListener {
            val serviceRating = ratingBarService.rating
            val employeeRating = ratingBarEmployee.rating
            val comentario = comentarioEditText.text.toString().trim()

            if (validateFields(serviceRating, employeeRating, comentario)) {
                submitButton.isEnabled = false // Evitar múltiplos cliques

                val currentUser = FirebaseAuth.getInstance().currentUser
                if (currentUser != null) {
                    val userId = currentUser.uid
                    val userName = currentUser.displayName ?: "Usuário Anônimo"
                    val userEmail = currentUser.email ?: "Email não disponível"

                    val database = FirebaseDatabase.getInstance().getReference("reviews").child(storeId)
                    val review = mapOf(
                        "serviceRating" to serviceRating,
                        "employeeRating" to employeeRating,
                        "comentario" to comentario,
                        "userId" to userId,
                        "userName" to userName,
                        "userEmail" to userEmail
                    )

                    database.push().setValue(review).addOnCompleteListener {
                        submitButton.isEnabled = true // Reativar botão

                        if (it.isSuccessful) {
                            Toast.makeText(this, "Avaliação enviada com sucesso!", Toast.LENGTH_SHORT).show()
                            finish() // Finaliza a atividade
                        } else {
                            Toast.makeText(this, "Erro ao enviar avaliação.", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(this, "Usuário não autenticado.", Toast.LENGTH_SHORT).show()
                    submitButton.isEnabled = true // Reativar botão
                }
            }
        }
    }

    private fun validateFields(serviceRating: Float, employeeRating: Float, comentario: String): Boolean {
        if (serviceRating == 0f && employeeRating == 0f) {
            Toast.makeText(this, "Por favor, insira uma avaliação.", Toast.LENGTH_SHORT).show()
            return false
        }

        if (comentario.isEmpty()) {
            Toast.makeText(this, "Por favor, adicione um comentário.", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }
}
