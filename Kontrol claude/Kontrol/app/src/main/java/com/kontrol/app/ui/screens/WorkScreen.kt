package com.kontrol.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.FreeBreakfast
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.Logout
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.kontrol.app.data.ApiFactory
import com.kontrol.app.data.WorkRecordDto
import com.kontrol.app.data.WorkRecordRequest
import com.kontrol.app.ui.components.GradientHeaderScreen
import com.kontrol.app.ui.components.KontrolButton
import com.kontrol.app.ui.components.MonthNavigator
import com.kontrol.app.ui.components.SummaryTile
import com.kontrol.app.ui.theme.AzulClaroKontrol
import com.kontrol.app.ui.theme.AzulEscuroKontrol
import com.kontrol.app.ui.theme.VerdeEscuroKontrol
import com.kontrol.app.ui.theme.kontrolDespesaGradient
import com.kontrol.app.ui.theme.kontrolReceitaGradient
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

private val api get() = ApiFactory.api
private val fmtHora = DateTimeFormatter.ofPattern("HH:mm")

/** Tela de Ponto: reproduz fragment_folha_ponto.xml — marcações do dia, totais e histórico do mês. */
@Composable
fun WorkScreen() {
    val hoje = LocalDate.now().toString()
    var registro by remember { mutableStateOf<WorkRecordDto?>(null) }
    var historico by remember { mutableStateOf<List<WorkRecordDto>>(emptyList()) }
    var mes by remember { mutableStateOf(YearMonth.now()) }
    var agora by remember { mutableStateOf(LocalTime.now().format(fmtHora)) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) { registro = api.work(hoje).body() }
    LaunchedEffect(Unit) { historico = api.workHistory().body() ?: emptyList() }
    LaunchedEffect(Unit) {
        while (true) {
            agora = LocalTime.now().format(fmtHora)
            delay(1000)
        }
    }

    val doMes = historico.filter {
        val data = runCatching { LocalDate.parse(it.date) }.getOrNull()
        data != null && YearMonth.from(data) == mes
    }
    val totalMinutos = doMes.sumOf { it.workedMinutes }

    GradientHeaderScreen(
        title = "Marcação de Ponto",
        headerExtra = {
            MonthNavigator(
                label = nomeMes(mes),
                onPrevious = { mes = mes.minusMonths(1) },
                onNext = { mes = mes.plusMonths(1) },
                modifier = Modifier.padding(top = 8.dp),
            )
        },
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
            HoraTile("Entrada", registro?.entryTime, Icons.Default.Login, AzulClaroKontrol, Modifier.weight(1f))
            HoraTile("Almoço", registro?.breakStart, Icons.Default.FreeBreakfast, AzulClaroKontrol, Modifier.weight(1f))
            HoraTile("Volta", registro?.breakEnd, Icons.Default.FreeBreakfast, VerdeEscuroKontrol, Modifier.weight(1f))
            HoraTile("Saída", registro?.exitTime, Icons.Default.Logout, VerdeEscuroKontrol, Modifier.weight(1f))
        }
        Spacer(Modifier.height(16.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            SummaryTile(
                label = "Horas feitas",
                value = minutosParaTexto(registro?.workedMinutes ?: 0),
                background = kontrolReceitaGradient,
                icon = { Icon(Icons.Default.AccessTime, contentDescription = null, tint = androidx.compose.ui.graphics.Color.White) },
                modifier = Modifier.weight(1f),
            )
            SummaryTile(
                label = "Meta do dia",
                value = minutosParaTexto(registro?.expectedMinutes ?: 480),
                background = kontrolDespesaGradient,
                icon = { Icon(Icons.Default.AccessTime, contentDescription = null, tint = androidx.compose.ui.graphics.Color.White) },
                modifier = Modifier.weight(1f),
            )
        }
        Spacer(Modifier.height(16.dp))
        KontrolButton(
            text = "Registrar ponto • $agora",
            onClick = {
                scope.launch {
                    val r = registro
                    val request = when {
                        r == null -> WorkRecordRequest(hoje, agora, null, null, null)
                        r.breakStart == null -> WorkRecordRequest(hoje, r.entryTime, agora, null, null)
                        r.breakEnd == null -> WorkRecordRequest(hoje, r.entryTime, r.breakStart, agora, null)
                        r.exitTime == null -> WorkRecordRequest(hoje, r.entryTime, r.breakStart, r.breakEnd, agora)
                        else -> null
                    } ?: return@launch
                    registro = if (r == null) api.createWork(request).body() else api.updateWork(r.id, request).body()
                    historico = api.workHistory().body() ?: emptyList()
                }
            },
        )
        Spacer(Modifier.height(20.dp))
        Text("Histórico do mês", fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        Card {
            Column {
                LazyColumn(modifier = Modifier.height((doMes.size.coerceAtMost(6) * 64).dp)) {
                    items(doMes) { r ->
                        ListItem(
                            headlineContent = { Text(r.date) },
                            trailingContent = { Text(minutosParaTexto(r.workedMinutes)) },
                        )
                    }
                }
                Divider()
                ListItem(
                    headlineContent = { Text("Total de horas", fontWeight = FontWeight.Bold) },
                    trailingContent = { Text(minutosParaTexto(totalMinutos), fontWeight = FontWeight.Bold, color = AzulEscuroKontrol) },
                )
            }
        }
    }
}

@Composable
private fun HoraTile(
    titulo: String,
    horario: String?,
    icone: androidx.compose.ui.graphics.vector.ImageVector,
    cor: androidx.compose.ui.graphics.Color,
    modifier: Modifier = Modifier,
) {
    Card(modifier = modifier) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(10.dp),
        ) {
            Icon(icone, contentDescription = titulo, tint = cor)
            Text(titulo, fontWeight = FontWeight.SemiBold, color = cor)
            Text(horario ?: "--:--", fontWeight = FontWeight.Bold)
        }
    }
}

private fun nomeMes(mes: YearMonth): String {
    val nome = mes.month.getDisplayName(TextStyle.FULL, Locale("pt", "BR")).replaceFirstChar { it.uppercase() }
    return "$nome ${mes.year}"
}

private fun minutosParaTexto(m: Int): String = "${m / 60}h ${m % 60}min"
