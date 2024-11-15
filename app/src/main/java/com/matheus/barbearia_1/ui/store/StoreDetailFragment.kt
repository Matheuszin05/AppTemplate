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
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.matheus.barbearia_1.R
import com.matheus.barbearia_1.baseclasses.Item
import com.matheus.barbearia_1.ui.agendamento.ActivityAgendamento

class StoreDetailFragment : Fragment() {

    private lateinit var storeNameTextView: TextView
    private lateinit var storeAddressTextView: TextView
    private lateinit var storeDescriptionTextView: TextView
    private lateinit var storeRatingBar: TextView // Usando TextView para Rating
    private lateinit var storeImageView: ImageView
    private lateinit var storeTelefoneTextView: TextView // Para mostrar o telefone
    private lateinit var agendarButton: Button
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

        // Inicializando as views com as novas IDs
        storeNameTextView = view.findViewById(R.id.storeNameTextView)
        storeAddressTextView = view.findViewById(R.id.storeAddressTextView)
        storeDescriptionTextView = view.findViewById(R.id.storeDescriptionTextView)
        storeRatingBar = view.findViewById(R.id.storeRatingBar) // Referência ao Rating
        storeImageView = view.findViewById(R.id.storeImageView)
        storeTelefoneTextView = view.findViewById(R.id.storeTelefoneTextView)  // Referência ao telefone
        agendarButton = view.findViewById(R.id.Agendabutton)

        // Inicializa a referência do banco de dados Firebase
        database = FirebaseDatabase.getInstance().getReference("stores")

        // Obtendo os dados da loja dos argumentos
        val storeImageUrl = arguments?.getString("storeImageUrl")
        val storeName = arguments?.getString("storeName") ?: "Nome da loja não disponível"
        val storeAddress = arguments?.getString("storeEndereco") ?: "Endereço não disponível"
        val storeDescription = arguments?.getString("storeEmail") ?: "Descrição não disponível"
        val storeTelefone = arguments?.getString("storeTelefone") ?: "(00)1234-5678"  // Obtendo telefone

        // Define os valores nos componentes da UI
        storeNameTextView.text = storeName
        storeAddressTextView.text = storeAddress
        storeDescriptionTextView.text = storeDescription
        storeTelefoneTextView.text = storeTelefone  // Definindo o telefone

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
        database.child(storeId).get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {
                // Mapeia para a classe Item
                val item = snapshot.getValue(Item::class.java)

                // Exibe o rating, se disponível
                storeRatingBar.text = item?.rating?.toString() ?: "0.0"
            } else {
                storeRatingBar.text = "0.0" // Caso não exista o campo rating
            }
        }.addOnFailureListener { exception ->
            // Se falhar ao obter a avaliação, define como 0
            storeRatingBar.text = "0.0"
            Toast.makeText(context, "Falha ao carregar avaliação", Toast.LENGTH_SHORT).show()
        }

        // Configura o botão de agendamento para navegar para a ActivityAgendamento
        agendarButton.setOnClickListener {
            // Criando o Intent para navegar para a ActivityAgendamento
            val intent = Intent(activity, ActivityAgendamento::class.java)

            // Passando o storeId e outros dados necessários para a nova Activity (se necessário)
            intent.putExtra("storeId", storeId)

            // Inicia a Activity
            startActivity(intent)
        }
    }
}
