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
    private var horariosIndisponiveis: Set<String> = emptySet() // Horários indisponíveis

    // Método para atualizar a lista de horários indisponíveis
    fun setHorariosIndisponiveis(indisponiveis: Set<String>) {
        horariosIndisponiveis = indisponiveis
        notifyDataSetChanged()  // Atualiza a lista para refletir os horários indisponíveis
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HoraViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_hora, parent, false)
        return HoraViewHolder(view)
    }

    override fun onBindViewHolder(holder: HoraViewHolder, position: Int) {
        val horario = horarios[position]
        holder.horaButton.text = horario

        // Se o horário estiver indisponível, desabilitar o botão e alterar a cor
        if (horariosIndisponiveis.contains(horario)) {
            holder.horaButton.isEnabled = false
            holder.horaButton.setBackgroundColor(Color.GRAY)  // Cor para horários indisponíveis
        } else {
            holder.horaButton.isEnabled = true
            holder.horaButton.setBackgroundColor(
                if (position == selectedPosition) Color.LTGRAY else Color.TRANSPARENT
            )

            holder.horaButton.setOnClickListener {
                val previousPosition = selectedPosition
                selectedPosition = holder.adapterPosition

                // Notificar as mudanças de posição para indicar seleção
                notifyItemChanged(previousPosition)
                notifyItemChanged(selectedPosition)

                onTimeSelected(horario)
            }
        }
    }

    override fun getItemCount(): Int = horarios.size

    inner class HoraViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val horaButton: Button = itemView.findViewById(R.id.horaButton)
    }
}
