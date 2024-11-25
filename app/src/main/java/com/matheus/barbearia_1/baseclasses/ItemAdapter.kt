package com.matheus.barbearia_1.baseclasses

import android.content.Context
import android.content.Intent
import android.location.Location
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.matheus.barbearia_1.R
import androidx.navigation.Navigation
import android.os.Bundle

class StoreAdapter(
    private val context: Context,
    private val storeList: List<Item>,
    private val userLocation: Location?
) : RecyclerView.Adapter<StoreAdapter.StoreViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoreViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
        return StoreViewHolder(view)
    }

    override fun onBindViewHolder(holder: StoreViewHolder, position: Int) {
        val store = storeList[position]
        holder.storeNameTextView.text = store.name
        //holder.storeEmailTextView.text = store.email
        holder.storeEnderecoTextView.text = store.endereco
        holder.storeTelefoneTextView.text = store.telefone
        holder.storeRatingBarTextView.text = String.format("%.1f", store.rating)
        holder.storeDistanceTextView.text = getDistanceToStore(store)


        // Visibilidade da distância
        if (holder.storeDistanceTextView.text.isEmpty()) {
            holder.storeDistanceTextView.visibility = View.GONE
        } else {
            holder.storeDistanceTextView.visibility = View.VISIBLE
        }

        // Ajusta a visibilidade do ícone do mapa
        if (store.endereco.isEmpty()) {
            holder.mapIconImageView.visibility = View.GONE
        } else {
            holder.mapIconImageView.visibility = View.VISIBLE
        }

        holder.itemView.setOnClickListener {
            val bundle = Bundle().apply {
                putString("storeId", store.storeId)
                putString("storeName", store.name)
                putString("storeEmail", store.email)
                putString("storeEndereco", store.endereco)
                putString("storeTelefone", store.telefone)
                putString("storeImageUrl", store.imageUrl)
                putString("storeRating", store.rating.toString())

            }

            // Navegação para o fragment de detalhes da loja
            val navController = Navigation.findNavController(holder.itemView)
            navController.navigate(R.id.action_homeFragment_to_storeDetailFragment, bundle)
        }

        // Carrega a imagem da loja com Glide
        Glide.with(holder.storeImageView.context)
            .load(store.imageUrl)
            .circleCrop()
            .placeholder(R.drawable.ic_carregando)
            .error(R.drawable.ic_carregando)
            .into(holder.storeImageView)

        // Ação de abrir mapa com o Google Maps
        holder.mapIconImageView.setOnClickListener {
            val endereco = holder.storeEnderecoTextView.text.toString()
            val gmmIntentUri = Uri.parse("geo:0,0?q=${Uri.encode(endereco)}")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            if (mapIntent.resolveActivity(it.context.packageManager) != null) {
                it.context.startActivity(mapIntent)
            }
        }

        /*holder.wazeIconImageView.setOnClickListener {
            val endereco = holder.storeEnderecoTextView.text.toString()
            val wazeUri = Uri.parse("https://waze.com/ul?q=${Uri.encode(endereco)}")
            val wazeIntent = Intent(Intent.ACTION_VIEW, wazeUri)
            wazeIntent.setPackage("com.waze")
            if (wazeIntent.resolveActivity(it.context.packageManager) != null) {
                it.context.startActivity(wazeIntent)
            } else {
                Toast.makeText(it.context, "Waze não está instalado", Toast.LENGTH_SHORT).show()
            }
        }*/
    }

    // Método para calcular a distância entre a localização do usuário e a loja
    private fun getDistanceToStore(store: Item): String {
        if (userLocation == null) {
            return ""
        }
        val userLocation = Location("").apply {
            latitude = userLocation.latitude
            longitude = userLocation.longitude
        }
        if (store.latitude.toInt() == 0 && store.longitude.toInt() == 0) {
            return ""
        }
        val storeLocation = Location("").apply {
            latitude = store.latitude
            longitude = store.longitude
        }
        val distanceInMeters = userLocation.distanceTo(storeLocation)
        val distanceInKm = distanceInMeters / 1000
        return String.format("%.2f km", distanceInKm)
    }

    override fun getItemCount(): Int {
        return storeList.size
    }

    class StoreViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val storeImageView: ImageView = itemView.findViewById(R.id.storeImageView)
        val storeNameTextView: TextView = itemView.findViewById(R.id.storeNameTextView)
        val storeRatingBarTextView: TextView = itemView.findViewById(R.id.storeRatingBar)
        val storeEnderecoTextView: TextView = itemView.findViewById(R.id.storeEnderecoTextView)
        val storeTelefoneTextView: TextView = itemView.findViewById(R.id.storeTelefoneTextView)
        val storeDistanceTextView: TextView = itemView.findViewById(R.id.storeDistanceTextView)
        val mapIconImageView: ImageView = itemView.findViewById(R.id.mapIconImageView)
        // val wazeIconImageView: ImageView = itemView.findViewById(R.id.wazeIconImageView)
    }
}
