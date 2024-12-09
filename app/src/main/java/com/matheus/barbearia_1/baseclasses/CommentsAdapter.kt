package com.matheus.barbearia_1.baseclasses

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.matheus.barbearia_1.R

// Adapter para exibir comentários em um RecyclerView
class CommentsAdapter : RecyclerView.Adapter<CommentsAdapter.CommentViewHolder>() {

    // Lista de comentários
    private var commentsList: List<Comment> = mutableListOf()

    // ViewHolder para o comentário
    class CommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val comentarioText: TextView = itemView.findViewById(R.id.commentMessageTextView)
        val userProfileImage: ImageView = itemView.findViewById(R.id.userProfileImageView)
        val userNameText: TextView = itemView.findViewById(R.id.commentUserTextView)
        val storeRatingBar: TextView = itemView.findViewById(R.id.storeRatingBar)
    }

    // Cria o ViewHolder e o associa à view de item
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_comment, parent, false)
        return CommentViewHolder(itemView)
    }

    // Associa os dados do comentário ao ViewHolder
    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val currentComment = commentsList[position]

        // Define o texto do comentário
        holder.comentarioText.text = currentComment.comentario

        // Define o nome do usuário
        holder.userNameText.text = currentComment.userName

        // Carrega a imagem do perfil usando Glide
        Glide.with(holder.userProfileImage.context)
            .load(currentComment.imageUrl)
            .circleCrop()// URL da imagem do perfil
            .into(holder.userProfileImage)

        val serviceRating = currentComment.serviceRating  // Assume que serviceRating é uma propriedade do Comment

        // Aqui estamos formatando a avaliação como uma String, você pode ajustá-la conforme necessário
        holder.storeRatingBar.text = "$serviceRating"
    }

    // Retorna o número de comentários na lista
    override fun getItemCount(): Int {
        return commentsList.size
    }

    // Atualiza a lista de comentários
    fun submitList(newCommentsList: List<Comment>) {
        commentsList = newCommentsList
        notifyDataSetChanged()
    }
}
