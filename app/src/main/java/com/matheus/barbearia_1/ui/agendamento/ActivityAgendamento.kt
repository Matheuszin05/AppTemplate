package com.matheus.barbearia_1.ui.agendamento

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase
import com.matheus.barbearia_1.MainActivity
import com.matheus.barbearia_1.R
import com.matheus.barbearia_1.ui.avaliacao.AvaliacaoActivity


class ActivityAgendamento : AppCompatActivity() {

    private var selectedDate: String = ""
    private var selectedTime: String = ""
    private var storeId: String = ""  // Identificador da loja

    private val database = FirebaseDatabase.getInstance().reference
    private val agendamentosRef = database.child("agendamentos")  // Referência para o nó de agendamentos

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agendamento)

        // Obtendo o storeId da loja selecionada (passado de outra atividade)
        storeId = intent.getStringExtra("storeId") ?: ""

        // CalendarView
        val calendarView = findViewById<android.widget.CalendarView>(R.id.calendarView)
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            selectedDate = "$dayOfMonth/${month + 1}/$year"
            Toast.makeText(this, "Data Selecionada: $selectedDate", Toast.LENGTH_SHORT).show()

            // Carregar os horários disponíveis para a data selecionada
            loadIndisponiveis(selectedDate)
        }

        // RecyclerView para horários
        val timeRecyclerView = findViewById<RecyclerView>(R.id.timeRecyclerView)
        timeRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        // Adapter com os horários
        val horarios = generateHorarios()
        val adapter = HoraAdapter(horarios) { time ->
            selectedTime = time
            Toast.makeText(this, "Hora Selecionada: $selectedTime", Toast.LENGTH_SHORT).show()
        }
        timeRecyclerView.adapter = adapter

        // Botão de confirmação
        val confirmButton = findViewById<Button>(R.id.confirm_agendamento_button)
        confirmButton.setOnClickListener {
            if (selectedDate.isNotEmpty() && selectedTime.isNotEmpty()) {
                // Salvar o agendamento no Firebase
                MainActivity.usuarioLogado?.displayName?.let { it1 ->
                    saveAgendamento(storeId, selectedDate, selectedTime, it1)
                }

                // Exibir mensagem de confirmação
                Toast.makeText(this, "Agendamento confirmado para $selectedDate às $selectedTime", Toast.LENGTH_SHORT).show()

                // Retornar à tela anterior (Stores)
                finish()
            } else {
                Toast.makeText(this, "Selecione uma data e um horário", Toast.LENGTH_SHORT).show()
            }
        }
    }
// Exibir popup para avaliação
    //showRatingPopup()
    /*private fun showRatingPopup() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Avaliação do Serviço")
        builder.setMessage("Você gostaria de avaliar o serviço que acabou de agendar?")

        // Botão "Avaliar Agora"
        builder.setPositiveButton("Avaliar Agora") { dialog, _ ->

            // Navegar para a tela de avaliação
            val intent = Intent(this, AvaliacaoActivity::class.java)
            startActivity(intent)
            dialog.dismiss()
        }

        // Botão "Depois"
        builder.setNegativeButton("Depois") { dialog, _ ->
            dialog.dismiss()
            Toast.makeText(this, "Você pode avaliar mais tarde!", Toast.LENGTH_SHORT).show()
        }

        // Exibir o diálogo
        val dialog = builder.create()
        dialog.show()
    }*/

    private fun generateHorarios(): List<String> {
        val horarios = mutableListOf<String>()
        var hour = 9 // Começando às 9:00
        var minute = 0

        while (hour < 18) { // Até 17:30
            horarios.add(String.format("%02d:%02d", hour, minute))
            minute += 30
            if (minute == 60) {
                minute = 0
                hour++
            }
        }
        return horarios
    }

    private fun saveAgendamento(storeId: String, date: String, time: String, nomeUsuario: String) {
        val agendamento = Agendamento(date, time, nomeUsuario)

        // Dividindo a data em partes para estruturar a chave hierárquica
        val dateParts = date.split("/")
        val day = dateParts[0]
        val month = dateParts[1]
        val year = dateParts[2]

        // Salva o agendamento na chave específica para cada loja, data e hora
        agendamentosRef.child(storeId).child(year).child(month).child(day).child(time).setValue(agendamento)
    }

    private fun loadIndisponiveis(date: String) {
        val dateParts = date.split("/")
        val day = dateParts[0]
        val month = dateParts[1]
        val year = dateParts[2]
        val adapter = (findViewById<RecyclerView>(R.id.timeRecyclerView).adapter as HoraAdapter).resetHorariosIndisponiveis()

        agendamentosRef.child(storeId).child(year).child(month).child(day).get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {
                val horariosIndisponiveis = snapshot.children.mapNotNull { it.key }
                val adapter = (findViewById<RecyclerView>(R.id.timeRecyclerView).adapter as HoraAdapter)
                adapter.setHorariosIndisponiveis(horariosIndisponiveis.toSet())
            }
        }
    }

    data class Agendamento(val date: String, val time: String, val nomeUsuario: String)
}
