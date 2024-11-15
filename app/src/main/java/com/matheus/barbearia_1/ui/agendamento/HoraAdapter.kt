package com.matheus.barbearia_1.ui.agendamento

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.matheus.barbearia_1.R

class HoraAdapter(
    private val horarios: List<String>,
    private val onTimeSelected: (String) -> Unit
) : RecyclerView.Adapter<HoraAdapter.HoraViewHolder>() {

    private var selectedPosition = RecyclerView.NO_POSITION

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HoraViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_hora, parent, false)
        return HoraViewHolder(view)
    }

    override fun onBindViewHolder(holder: HoraViewHolder, position: Int) {
        val horario = horarios[position]
        holder.horaButton.text = horario

        // Configuração da cor de seleção
        holder.horaButton.setBackgroundColor(
            if (position == selectedPosition) Color.LTGRAY else Color.TRANSPARENT
        )

        holder.horaButton.setOnClickListener {
            val previousPosition = selectedPosition
            selectedPosition = holder.adapterPosition

            notifyItemChanged(previousPosition)
            notifyItemChanged(selectedPosition)

            onTimeSelected(horario)
        }
    }

    override fun getItemCount(): Int = horarios.size

    inner class HoraViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val horaButton: Button = itemView.findViewById(R.id.horaButton)
    }
}
