package com.kontrol.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kontrol.app.data.ApiFactory
import com.kontrol.app.data.DashboardDto
import com.kontrol.app.ui.components.GradientHeaderScreen
import com.kontrol.app.ui.components.SummaryTile
import com.kontrol.app.ui.theme.AzulEscuroKontrol
import com.kontrol.app.ui.theme.kontrolAmareloGradient
import com.kontrol.app.ui.theme.kontrolAzulGradient
import com.kontrol.app.ui.theme.kontrolNeutroGradient
import com.kontrol.app.ui.theme.kontrolReceitaGradient
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

/** Tela inicial: no app antigo era um fragment em branco (TODO); aqui reaproveita a linguagem visual do Kontrol. */
@Composable
fun HomeScreen() {
    var dashboard by remember { mutableStateOf<DashboardDto?>(null) }
    LaunchedEffect(Unit) { dashboard = ApiFactory.api.dashboard().body() }

    val hoje = remember {
        val nomeMes = LocalDate.now().month.getDisplayName(TextStyle.FULL, Locale("pt", "BR"))
        "${LocalDate.now().dayOfMonth} de $nomeMes"
    }

    GradientHeaderScreen(title = "Olá!", headerExtra = { Text(hoje, color = androidx.compose.ui.graphics.Color.White) }) {
        val d = dashboard
        if (d == null) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        } else {
            SummaryTile(
                label = "Saldo atual",
                value = moeda(d.finance.balance),
                background = kontrolReceitaGradient,
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(Modifier.height(12.dp))
            SummaryTile(
                label = "Horas hoje",
                value = "${d.work.workedMinutes / 60}h ${d.work.workedMinutes % 60}min",
                background = kontrolAzulGradient,
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(Modifier.height(12.dp))
            SummaryTile(
                label = "Tarefas concluídas",
                value = "${d.routine.completedTasks}/${d.routine.totalTasks}",
                background = kontrolNeutroGradient,
                contentColor = AzulEscuroKontrol,
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(Modifier.height(12.dp))
            SummaryTile(
                label = "Itens pendentes no mercado",
                value = d.shopping.pendingItems.toString(),
                background = kontrolAmareloGradient,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

private fun moeda(v: Double) = java.text.NumberFormat.getCurrencyInstance(Locale("pt", "BR")).format(v)
