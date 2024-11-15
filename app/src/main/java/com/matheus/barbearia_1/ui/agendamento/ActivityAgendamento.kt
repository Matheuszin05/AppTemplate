package com.matheus.barbearia_1.ui.agendamento

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.matheus.barbearia_1.R
import com.matheus.barbearia_1.ui.agendamento.HoraAdapter

class ActivityAgendamento : AppCompatActivity() {

    private var selectedDate: String = ""
    private var selectedTime: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agendamento)

        // CalendarView
        val calendarView = findViewById<android.widget.CalendarView>(R.id.calendarView)
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            selectedDate = "$dayOfMonth/${month + 1}/$year"
            Toast.makeText(this, "Data Selecionada: $selectedDate", Toast.LENGTH_SHORT).show()
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

        // Exibir o ID da loja se disponível

        // Botão de confirmação
        val confirmButton = findViewById<Button>(R.id.confirm_button)
        confirmButton.setOnClickListener {
            if (selectedDate.isNotEmpty() && selectedTime.isNotEmpty()) {
                Toast.makeText(this, "Agendamento Confirmado", Toast.LENGTH_SHORT).show()

                // Volta para a tela anterior
                finish()
            } else {
                Toast.makeText(this, "Selecione uma data e um horário", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Função para gerar uma lista de horários
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
}
