package com.kontrol.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.weight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.kontrol.app.data.ApiFactory
import com.kontrol.app.data.NoteDto
import com.kontrol.app.data.NoteRequest
import com.kontrol.app.ui.components.GradientHeaderScreen
import com.kontrol.app.ui.theme.VermelhoKontrol
import kotlinx.coroutines.launch

private val api get() = ApiFactory.api

/**
 * Tela de Perfil — equivalente ao ícone "Perfil" do app antigo (nunca implementado; no código
 * original ele reabria por engano a tela de Ponto). Reúne as Notas do usuário e a saída da conta.
 */
@Composable
fun ProfileScreen(onLogout: () -> Unit) {
    var notas by remember { mutableStateOf<List<NoteDto>>(emptyList()) }
    var novaNota by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    fun recarregar() {
        scope.launch { notas = api.notes().body() ?: emptyList() }
    }
    LaunchedEffect(Unit) { recarregar() }

    GradientHeaderScreen(title = "Perfil") {
        Text("Minhas notas", fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        Button(onClick = { novaNota = true }, modifier = Modifier.fillMaxWidth()) {
            Text("+ Nova nota")
        }
        Spacer(Modifier.height(8.dp))
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(notas) { nota ->
                Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                    ListItem(
                        headlineContent = { Text(nota.title, fontWeight = FontWeight.SemiBold) },
                        supportingContent = { Text(nota.content, maxLines = 2) },
                    )
                }
            }
        }
        Spacer(Modifier.height(12.dp))
        TextButton(onClick = onLogout, modifier = Modifier.fillMaxWidth()) {
            Text("Sair da conta", color = VermelhoKontrol, fontWeight = FontWeight.Bold)
        }
    }

    if (novaNota) {
        var titulo by remember { mutableStateOf("") }
        var conteudo by remember { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = { novaNota = false },
            title = { Text("Nova nota") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(titulo, { titulo = it }, label = { Text("Título") })
                    OutlinedTextField(conteudo, { conteudo = it }, label = { Text("Conteúdo") })
                }
            },
            confirmButton = {
                Button(onClick = {
                    scope.launch {
                        api.createNote(NoteRequest(titulo, conteudo))
                        novaNota = false
                        recarregar()
                    }
                }) { Text("Salvar") }
            },
            dismissButton = { TextButton({ novaNota = false }) { Text("Cancelar") } },
        )
    }
}
