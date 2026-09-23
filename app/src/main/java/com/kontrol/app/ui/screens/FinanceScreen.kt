package com.kontrol.app.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.align
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.weight
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.kontrol.app.data.TransactionDto
import com.kontrol.app.data.TransactionRequest
import com.kontrol.app.ui.components.GradientHeaderScreen
import com.kontrol.app.ui.components.MonthNavigator
import com.kontrol.app.ui.components.SummaryTile
import com.kontrol.app.ui.theme.AzulEscuroKontrol
import com.kontrol.app.ui.theme.VerdeKontrol
import com.kontrol.app.ui.theme.VermelhoKontrol
import com.kontrol.app.ui.theme.kontrolDespesaGradient
import com.kontrol.app.ui.theme.kontrolNeutroGradient
import com.kontrol.app.ui.theme.kontrolReceitaGradient
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

private val api get() = ApiFactory.api

/** Tela de Finanças: reproduz fragment_financas.xml — total do mês, receitas, despesas e lançamentos. */
@Composable
fun FinanceScreen() {
    var mes by remember { mutableStateOf(YearMonth.now()) }
    var lancamentos by remember { mutableStateOf<List<TransactionDto>>(emptyList()) }
    var menuAberto by remember { mutableStateOf(false) }
    var dialogoTipo by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    fun recarregar() {
        scope.launch { lancamentos = api.transactions().body() ?: emptyList() }
    }

    LaunchedEffect(Unit) { recarregar() }

    val doMes = lancamentos.filter {
        val data = runCatching { LocalDate.parse(it.date) }.getOrNull()
        data != null && YearMonth.from(data) == mes
    }
    val receitas = doMes.filter { it.type == "INCOME" }.sumOf { it.amount }
    val despesas = doMes.filter { it.type == "EXPENSE" }.sumOf { it.amount }

    Box(modifier = Modifier.fillMaxSize()) {
        GradientHeaderScreen(
            title = "Finanças Mensais",
            headerExtra = {
                MonthNavigator(
                    label = nomeMes(mes),
                    onPrevious = { mes = mes.minusMonths(1) },
                    onNext = { mes = mes.plusMonths(1) },
                    modifier = Modifier.padding(top = 8.dp),
                )
            },
        ) {
            SummaryTile(
                label = "Total do mês",
                value = moeda(receitas - despesas),
                background = kontrolNeutroGradient,
                contentColor = AzulEscuroKontrol,
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                SummaryTile(
                    label = "Receitas",
                    value = moeda(receitas),
                    background = kontrolReceitaGradient,
                    icon = { Icon(Icons.Default.ArrowUpward, contentDescription = null, tint = androidx.compose.ui.graphics.Color.White) },
                    modifier = Modifier.weight(1f),
                )
                SummaryTile(
                    label = "Despesas",
                    value = moeda(despesas),
                    background = kontrolDespesaGradient,
                    icon = { Icon(Icons.Default.ArrowDownward, contentDescription = null, tint = androidx.compose.ui.graphics.Color.White) },
                    modifier = Modifier.weight(1f),
                )
            }
            Spacer(Modifier.height(16.dp))
            Text("Lançamentos do mês", fontWeight = FontWeight.Bold)
            LazyColumn(contentPadding = PaddingValues(vertical = 8.dp)) {
                items(doMes) { t ->
                    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                        ListItem(
                            headlineContent = { Text(t.description ?: if (t.type == "INCOME") "Receita" else "Despesa") },
                            supportingContent = { Text(t.date) },
                            trailingContent = {
                                Text(
                                    (if (t.type == "EXPENSE") "- " else "+ ") + moeda(t.amount),
                                    fontWeight = FontWeight.Bold,
                                    color = if (t.type == "EXPENSE") VermelhoKontrol else VerdeKontrol,
                                )
                            },
                        )
                    }
                }
            }
        }

        // Menu flutuante expansível: equivalente ao FloatingActionMenu do app antigo
        // (menu_despesa em vermelho, menu_receita em verde).
        Column(
            horizontalAlignment = Alignment.End,
            modifier = Modifier.align(Alignment.BottomEnd).padding(20.dp),
        ) {
            AnimatedVisibility(visible = menuAberto) {
                Column(horizontalAlignment = Alignment.End) {
                    MiniFab(texto = "Despesa", cor = VermelhoKontrol, icone = Icons.Default.ArrowDownward) {
                        menuAberto = false; dialogoTipo = "EXPENSE"
                    }
                    Spacer(Modifier.height(10.dp))
                    MiniFab(texto = "Receita", cor = VerdeKontrol, icone = Icons.Default.ArrowUpward) {
                        menuAberto = false; dialogoTipo = "INCOME"
                    }
                    Spacer(Modifier.height(10.dp))
                }
            }
            Button(
                onClick = { menuAberto = !menuAberto },
                shape = androidx.compose.foundation.shape.CircleShape,
                colors = ButtonDefaults.buttonColors(containerColor = AzulEscuroKontrol),
                contentPadding = PaddingValues(16.dp),
            ) {
                Icon(if (menuAberto) Icons.Default.Close else Icons.Default.Add, contentDescription = "Nova movimentação")
            }
        }
    }

    dialogoTipo?.let { tipo ->
        TransactionDialog(tipo = tipo, onDone = { dialogoTipo = null; recarregar() })
    }
}

@Composable
private fun MiniFab(texto: String, cor: androidx.compose.ui.graphics.Color, icone: androidx.compose.ui.graphics.vector.ImageVector, onClick: () -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Card(modifier = Modifier.padding(end = 8.dp)) {
            Text(texto, modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp))
        }
        Button(
            onClick = onClick,
            shape = androidx.compose.foundation.shape.CircleShape,
            colors = ButtonDefaults.buttonColors(containerColor = cor),
            contentPadding = PaddingValues(12.dp),
        ) {
            Icon(icone, contentDescription = texto, tint = androidx.compose.ui.graphics.Color.White)
        }
    }
}

@Composable
private fun TransactionDialog(tipo: String, onDone: () -> Unit) {
    var valor by remember { mutableStateOf("") }
    var descricao by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    AlertDialog(
        onDismissRequest = onDone,
        title = { Text(if (tipo == "EXPENSE") "Nova despesa" else "Nova receita") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(valor, { valor = it }, label = { Text("Valor") })
                OutlinedTextField(descricao, { descricao = it }, label = { Text("Descrição") })
            }
        },
        confirmButton = {
            Button(onClick = {
                val v = valor.replace(",", ".").toDoubleOrNull() ?: 0.0
                scope.launch {
                    api.createTransaction(TransactionRequest(tipo, v, descricao.ifBlank { null }, LocalDate.now().toString(), null, null))
                    onDone()
                }
            }) { Text("Salvar") }
        },
        dismissButton = { TextButton(onDone) { Text("Cancelar") } },
    )
}

private fun nomeMes(mes: YearMonth): String {
    val nome = mes.month.getDisplayName(TextStyle.FULL, Locale("pt", "BR")).replaceFirstChar { it.uppercase() }
    return "$nome ${mes.year}"
}

private fun moeda(v: Double) = java.text.NumberFormat.getCurrencyInstance(Locale("pt", "BR")).format(v)
