package com.matheus.barbearia_1.ui.store

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.matheus.barbearia_1.R
import com.matheus.barbearia_1.baseclasses.Comment
import com.matheus.barbearia_1.baseclasses.Item
import com.matheus.barbearia_1.baseclasses.CommentsAdapter
import com.matheus.barbearia_1.ui.agendamento.ActivityAgendamento
import com.matheus.barbearia_1.ui.avaliacao.AvaliacaoActivity

class StoreDetailFragment : Fragment() {

    // Declaração das views
    private lateinit var storeNameTextView: TextView
    private lateinit var storeAddressTextView: TextView
    private lateinit var storeDescriptionTextView: TextView
    private lateinit var storeRatingBar: TextView
    private lateinit var storeImageView: ImageView
    private lateinit var storeTelefoneTextView: TextView
    private lateinit var agendarButton: Button
    private lateinit var avaliacaoButton: Button
    private lateinit var database: DatabaseReference


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_store_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inicializa as views
        initializeViews(view)

        // Obtém o storeId dos argumentos
        val storeId = arguments?.getString("storeId")
        if (storeId.isNullOrEmpty()) {
            Toast.makeText(context, "ID da loja não disponível", Toast.LENGTH_SHORT).show()
            return
        }

        // Configura os dados da loja
        configureStoreDetails(storeId)

        fetchStoreComments(storeId, view)

        // Configura ações dos botões
        setupButtonActions(storeId)
    }

     override fun onResume() {
        super.onResume()
        //fetchStoreComments
    }

    private fun initializeViews(view: View) {
        storeNameTextView = view.findViewById(R.id.storeNameTextView)
        storeAddressTextView = view.findViewById(R.id.storeAddressTextView)
        storeDescriptionTextView = view.findViewById(R.id.storeDescriptionTextView)
        storeRatingBar = view.findViewById(R.id.storeRatingBar)
        storeImageView = view.findViewById(R.id.storeImageView)
        storeTelefoneTextView = view.findViewById(R.id.storeTelefoneTextView)
        agendarButton = view.findViewById(R.id.Agendabutton)
        avaliacaoButton = view.findViewById(R.id.AvaliacaoActivity)
        database = FirebaseDatabase.getInstance().getReference("stores")
    }

    private fun configureStoreDetails(storeId: String) {
        val storeImageUrl = arguments?.getString("storeImageUrl")
        val storeName = arguments?.getString("storeName") ?: "Nome da loja não disponível"
        val storeAddress = arguments?.getString("storeEndereco") ?: "Endereço não disponível"
        val storeDescription = arguments?.getString("storeEmail") ?: "Descrição não disponível"
        val storeTelefone = arguments?.getString("storeTelefone") ?: ""
        val storeRating = arguments?.getString("storeRating") ?: ""

        storeNameTextView.text = storeName
        storeAddressTextView.text = storeAddress
        storeDescriptionTextView.text = storeDescription
        storeTelefoneTextView.text = storeTelefone
        storeRatingBar.text = storeRating

        // Configura imagem da loja com Glide
        storeImageUrl?.let {
            Glide.with(storeImageView.context)
                .load(it)
                .into(storeImageView)
        } ?: storeImageView.setImageResource(R.drawable.default_image)

        // Recupera a avaliação da loja do Firebase
        database.child(storeId).get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {
                val item = snapshot.getValue(Item::class.java)
                storeRatingBar.text = item?.rating?.toString() ?: "0.0"
            } else {
                storeRatingBar.text = "0.0"
            }
        }.addOnFailureListener {
            storeRatingBar.text = "0.0"
            Toast.makeText(context, "Falha ao carregar avaliação", Toast.LENGTH_SHORT).show()
        }
    }



    private fun setupButtonActions(storeId: String) {
        agendarButton.setOnClickListener {
            val intent = Intent(activity, ActivityAgendamento::class.java).apply {
                putExtra("storeId", storeId)
            }
            startActivity(intent)
        }

        avaliacaoButton.setOnClickListener {
            val intent = Intent(activity, AvaliacaoActivity::class.java).apply {
                putExtra("storeId", storeId)
            }
            startActivity(intent)
        }
    }

    private fun fetchStoreComments(storeId: String, view: View) {
        val commentsReference = FirebaseDatabase.getInstance().getReference("reviews").child(storeId)
        val commentsRecyclerView: RecyclerView = view.findViewById(R.id.commentsRecyclerView)
        val commentsAdapter = CommentsAdapter()

        commentsRecyclerView.layoutManager = LinearLayoutManager(context)
        commentsRecyclerView.adapter = commentsAdapter

        // Listener para obter atualizações em tempo real
        commentsReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val commentsList = mutableListOf<Comment>()
                var rating = 0.0;
                for (commentSnapshot in snapshot.children) {
                    // Mapeia o nó do Firebase para a classe Comment
                    val comment = commentSnapshot.getValue(Comment::class.java)
                    comment?.let { commentsList.add(it)
                        rating += it.serviceRating}
                }
                if (snapshot.children.count() > 0) {
                    rating = rating / snapshot.children.count()
                    storeRatingBar.text = (rating).toString()
                    updateRating(storeId, rating)
                }
                // Passa a lista de Comment para o adaptador
                commentsAdapter.submitList(commentsList.toList())
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Falha ao carregar comentários", Toast.LENGTH_SHORT).show()
            }
        })


    }

    private fun updateRating(storeId: String, newRating: Double) {
        // Recupera a avaliação atual da loja
        database.child(storeId).get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {

                // Atualiza os valores no Firebase
                database.child(storeId).child("rating").setValue(newRating)

                // Atualiza a interface com o novo valor
                storeRatingBar.text = String.format("%.1f", newRating)
            }
        }.addOnFailureListener {
            Toast.makeText(context, "Falha ao atualizar avaliação", Toast.LENGTH_SHORT).show()
        }
    }



}
