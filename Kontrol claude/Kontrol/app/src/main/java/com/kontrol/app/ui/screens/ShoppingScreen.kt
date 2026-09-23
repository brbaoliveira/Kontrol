package com.kontrol.app.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.kontrol.app.data.ShoppingItemRequest
import com.kontrol.app.data.ShoppingListDto
import com.kontrol.app.data.ShoppingListRequest
import com.kontrol.app.ui.components.GradientHeaderScreen
import kotlinx.coroutines.launch

private val api get() = ApiFactory.api

/** Tela de Mercado (equivalente ao ícone "Listas" do app antigo, nunca implementado). */
@Composable
fun ShoppingScreen() {
    var listas by remember { mutableStateOf<List<ShoppingListDto>>(emptyList()) }
    var novaLista by remember { mutableStateOf(false) }
    var listaAberta by remember { mutableStateOf<ShoppingListDto?>(null) }
    val scope = rememberCoroutineScope()

    fun recarregar() {
        scope.launch { listas = api.shopping().body() ?: emptyList() }
    }
    LaunchedEffect(Unit) { recarregar() }

    GradientHeaderScreen(title = "Mercado") {
        Button(onClick = { novaLista = true }, modifier = Modifier.fillMaxWidth()) {
            Icon(Icons.Default.Add, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text("Nova lista")
        }
        Spacer(Modifier.height(12.dp))
        LazyColumn {
            items(listas) { lista ->
                Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                    ListItem(
                        headlineContent = { Text(lista.name, fontWeight = FontWeight.SemiBold) },
                        supportingContent = { Text("${lista.items.count { !it.completed }} itens pendentes") },
                        modifier = Modifier.clickable { listaAberta = lista },
                    )
                }
            }
        }
    }

    if (novaLista) {
        var nome by remember { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = { novaLista = false },
            title = { Text("Nova lista") },
            text = { OutlinedTextField(nome, { nome = it }, label = { Text("Nome da lista") }) },
            confirmButton = {
                Button(onClick = {
                    scope.launch {
                        api.createShopping(ShoppingListRequest(nome))
                        novaLista = false
                        recarregar()
                    }
                }) { Text("Criar") }
            },
            dismissButton = { TextButton({ novaLista = false }) { Text("Cancelar") } },
        )
    }

    listaAberta?.let { lista ->
        ShoppingListDialog(lista = lista, onDismiss = { listaAberta = null; recarregar() })
    }
}

@Composable
private fun ShoppingListDialog(lista: ShoppingListDto, onDismiss: () -> Unit) {
    var novoItem by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(lista.name) },
        text = {
            androidx.compose.foundation.layout.Column {
                lista.items.forEach { item ->
                    Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                        Checkbox(
                            checked = item.completed,
                            onCheckedChange = {
                                scope.launch {
                                    api.updateItem(item.id, ShoppingItemRequest(item.name, item.quantity, !item.completed))
                                }
                            },
                        )
                        Text(item.name)
                    }
                }
                Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                    OutlinedTextField(novoItem, { novoItem = it }, label = { Text("Novo item") }, modifier = Modifier.weight(1f))
                    IconButton(onClick = {
                        if (novoItem.isNotBlank()) {
                            scope.launch {
                                api.createItem(lista.id, ShoppingItemRequest(novoItem, null))
                                novoItem = ""
                            }
                        }
                    }) { Icon(Icons.Default.Add, contentDescription = "Adicionar item") }
                }
            }
        },
        confirmButton = { TextButton(onDismiss) { Text("Fechar") } },
    )
}
