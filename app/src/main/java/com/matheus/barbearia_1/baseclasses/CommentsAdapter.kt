package com.matheus.barbearia_1.baseclasses

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.matheus.barbearia_1.R

// Adapter para exibir comentários em um RecyclerView
class CommentsAdapter : RecyclerView.Adapter<CommentsAdapter.CommentViewHolder>() {

    // Lista de comentários
    private var commentsList: List<Comment> = mutableListOf()

    // ViewHolder para o comentário
    class CommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //val userNameText: TextView = itemView.findViewById(R.id.userNameTextView)
        val comentarioText: TextView = itemView.findViewById(R.id.commentMessageTextView)
       // val serviceRatingBar: RatingBar = itemView.findViewById(R.id.serviceRatingBar)
      //  val employeeRatingBar: RatingBar = itemView.findViewById(R.id.employeeRatingBar)
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

       // holder.userNameText.text = currentComment.userName
        holder.comentarioText.text = currentComment.comentario
       // holder.serviceRatingBar.rating = currentComment.serviceRating
      //  holder.employeeRatingBar.rating = currentComment.employeeRating
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
