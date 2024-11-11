package com.matheus.barbearia_1.ui.store

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.matheus.barbearia_1.R

class StoreDetailFragment : Fragment() {

    private lateinit var storeNameTextView: TextView
    private lateinit var storeAddressTextView: TextView
    private lateinit var storeDescriptionTextView: TextView
    private lateinit var storeRatingBar: RatingBar
    private lateinit var storeImageView: ImageView
    private lateinit var database: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_store_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Obtendo o storeId dos argumentos
        val storeId = arguments?.getString("storeId")

        // Verifica se o storeId está disponível
        if (storeId == null) {
            // Exibe uma mensagem de erro e retorna
            Toast.makeText(context, "ID da loja não disponível", Toast.LENGTH_SHORT).show()
            return
        }

        // Inicializa as views
        storeNameTextView = view.findViewById(R.id.storeNameTextView)
        storeAddressTextView = view.findViewById(R.id.storeAddressTextView)
        storeDescriptionTextView = view.findViewById(R.id.storeDescriptionTextView)
        storeRatingBar = view.findViewById(R.id.storeRatingBar)
        storeImageView = view.findViewById(R.id.storeImageView)

        // Inicializa a referência do banco de dados Firebase
        database = FirebaseDatabase.getInstance().getReference("stores")

        // Obtendo os dados da loja dos argumentos
        val storeImageUrl = arguments?.getString("storeImageUrl")
        val storeName = arguments?.getString("storeName") ?: "Nome da loja não disponível"
        val storeAddress = arguments?.getString("storeEndereco") ?: "Endereço não disponível"
        val storeDescription = arguments?.getString("storeEmail") ?: "Descrição não disponível"

        // Define os valores nos componentes da UI
        storeNameTextView.text = storeName
        storeAddressTextView.text = storeAddress
        storeDescriptionTextView.text = storeDescription

        // Carrega a imagem da loja usando Glide
        storeImageUrl?.let {
            Glide.with(storeImageView.context)
                .load(storeImageUrl)
                .into(storeImageView)

        } ?: run {
            // Define uma imagem padrão se a URL da imagem não for fornecida
            storeImageView.setImageResource(R.drawable.default_image)
        }

        // Obtém e exibe a avaliação da loja do Firebase
        database.child(storeId).child("rating").get().addOnSuccessListener { snapshot ->
            val initialRating = snapshot.getValue(Float::class.java) ?: 0.0f
            storeRatingBar.rating = initialRating
        }.addOnFailureListener { exception ->
            // Se falhar ao obter a avaliação, define como 0
            storeRatingBar.rating = 0.0f
        }

        // Configura a ação para atualizar a avaliação no Firebase quando o usuário mudar a classificação
        storeRatingBar.setOnRatingBarChangeListener { _, rating, _ ->
            database.child(storeId).child("rating").setValue(rating)
        }
    }
}
