package com.kontrol.app.ui.screens

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
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
import com.kontrol.app.data.TaskDto
import com.kontrol.app.data.TaskRequest
import com.kontrol.app.ui.components.GradientHeaderScreen
import kotlinx.coroutines.launch
import java.time.LocalDate

private val api get() = ApiFactory.api

/** Tela de Rotina: não existia no app antigo (ícone sem tela associada); segue a mesma linguagem visual. */
@Composable
fun RoutineScreen() {
    var tarefas by remember { mutableStateOf<List<TaskDto>>(emptyList()) }
    var novaTarefa by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    fun recarregar() {
        scope.launch { tarefas = api.tasks().body() ?: emptyList() }
    }
    LaunchedEffect(Unit) { recarregar() }

    GradientHeaderScreen(title = "Rotina") {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = novaTarefa,
                onValueChange = { novaTarefa = it },
                label = { Text("Nova tarefa") },
                modifier = Modifier.weight(1f),
            )
            IconButton(onClick = {
                if (novaTarefa.isNotBlank()) {
                    scope.launch {
                        api.createTask(TaskRequest(novaTarefa, null, LocalDate.now().toString()))
                        novaTarefa = ""
                        recarregar()
                    }
                }
            }) {
                Icon(Icons.Default.Add, contentDescription = "Adicionar tarefa")
            }
        }
        Spacer(Modifier.height(12.dp))
        LazyColumn {
            items(tarefas) { tarefa ->
                ListItem(
                    headlineContent = { Text(tarefa.title, fontWeight = FontWeight.SemiBold) },
                    supportingContent = { Text(tarefa.description ?: tarefa.date) },
                    leadingContent = {
                        Checkbox(
                            checked = tarefa.completed,
                            onCheckedChange = {
                                scope.launch {
                                    api.updateTask(
                                        tarefa.id,
                                        TaskRequest(tarefa.title, tarefa.description, tarefa.date, tarefa.priority, !tarefa.completed),
                                    )
                                    recarregar()
                                }
                            },
                        )
                    },
                )
            }
        }
    }
}
