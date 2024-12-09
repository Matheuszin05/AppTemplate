package com.matheus.barbearia_1.baseclasses

data class Comment(
    val serviceRating: Float = 0f,      // Avaliação do serviço
    val employeeRating: Float = 0f,    // Avaliação do funcionário
    val comentario: String = "",       // Comentário do usuário
    val userId: String = "",           // ID do usuário
    val userName: String = "",         // Nome do usuário
    val userEmail: String = "",         // Email do usuário
    val imageUrl: String = ""
)
