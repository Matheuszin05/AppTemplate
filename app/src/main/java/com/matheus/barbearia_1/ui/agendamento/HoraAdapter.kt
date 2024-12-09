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

    fun resetHorariosIndisponiveis (){
        horariosIndisponiveis = emptySet()
    }

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

        // Atualiza a aparência do botão com base no horário e sua disponibilidade
        updateButtonAppearance(holder, horario, position)

        holder.horaButton.setOnClickListener {
            val previousPosition = selectedPosition
            selectedPosition = holder.adapterPosition

            // Notificar as mudanças de posição para indicar seleção
            notifyItemChanged(previousPosition)
            notifyItemChanged(selectedPosition)

            onTimeSelected(horario)
        }
    }

    override fun getItemCount(): Int = horarios.size

    private fun updateButtonAppearance(holder: HoraViewHolder, horario: String, position: Int) {
        // Verifica se o horário está na lista de horários indisponíveis
        if (horariosIndisponiveis.contains(horario)) {
            holder.horaButton.isEnabled = false
            holder.horaButton.setBackgroundResource(R.drawable.button_hora_selector) // Usando o mesmo fundo
            holder.horaButton.setTextColor(Color.DKGRAY)  // Texto cinza escuro para indicar indisponibilidade
        } else {
            // Se o horário estiver disponível, ajusta o fundo e a cor do texto
            holder.horaButton.isEnabled = true
            holder.horaButton.setBackgroundResource(R.drawable.button_hora_selector) // Fundo do botão
            holder.horaButton.setTextColor(
                if (position == selectedPosition) Color.BLACK else Color.WHITE // Cor do texto para selecionado
            )
        }
    }

    inner class HoraViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val horaButton: Button = itemView.findViewById(R.id.horaButton)
    }
}
