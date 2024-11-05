package com.matheus.barbearia_1.ui.store

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.fragment.app.Fragment
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

        storeNameTextView = view.findViewById(R.id.storeNameTextView)
        storeAddressTextView = view.findViewById(R.id.storeAddressTextView)
        storeDescriptionTextView = view.findViewById(R.id.storeDescriptionTextView)
        storeImageView = view.findViewById(R.id.storeImageView)
        storeRatingBar = view.findViewById(R.id.storeRatingBar)

        // Inicializa o Firebase Database
        database = FirebaseDatabase.getInstance().getReference("stores")

        // Obtém os dados da loja a partir dos argumentos
        val storeId = arguments?.getString("storeId") ?: return
        val storeName = arguments?.getString("storeName") ?: "Nome da loja não disponível"
        val storeAddress = arguments?.getString("storeEndereco") ?: "Endereço não disponível"
        val storeDescription = arguments?.getString("storeEmail") ?: "Descrição não disponível"

        storeNameTextView.text = storeName
        storeAddressTextView.text = storeAddress
        storeDescriptionTextView.text = storeDescription

        // Carrega a avaliação inicial da loja
        database.child(storeId).child("rating").get().addOnSuccessListener { snapshot ->
            val initialRating = snapshot.getValue(Float::class.java) ?: 0.0f
            storeRatingBar.rating = initialRating
        }.addOnFailureListener { exception ->
            // Lida com a falha de leitura
            storeRatingBar.rating = 0.0f // ou qualquer valor padrão que você queira
        }

        // Configura o listener para salvar a avaliação no Firebase quando o usuário alterar a nota
        storeRatingBar.setOnRatingBarChangeListener { _, rating, _ ->
            database.child(storeId).child("rating").setValue(rating)
        }
    }
}
