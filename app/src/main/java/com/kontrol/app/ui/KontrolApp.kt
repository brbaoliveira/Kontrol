package com.kontrol.app.ui

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.kontrol.app.data.ApiFactory
import com.kontrol.app.data.LoginRequest
import com.kontrol.app.data.RegisterRequest
import com.kontrol.app.data.SessionManager
import com.kontrol.app.ui.components.KontrolBottomBar
import com.kontrol.app.ui.screens.AuthScreen
import com.kontrol.app.ui.screens.FinanceScreen
import com.kontrol.app.ui.screens.HomeScreen
import com.kontrol.app.ui.screens.ProfileScreen
import com.kontrol.app.ui.screens.RoutineScreen
import com.kontrol.app.ui.screens.ShoppingScreen
import com.kontrol.app.ui.screens.WorkScreen
import kotlinx.coroutines.launch

/**
 * Ordem e ícones da navegação inferior — reproduz exatamente os 6 itens de
 * bottom_navigation.xml / menu_bottom_navigation.xml do app antigo:
 * Início, Finanças, Pontos, Listas, Rotina, Perfil, todos sem rótulo de texto.
 */
private val bottomNavItems = listOf(
    Triple("home", Icons.Default.Home, "Início"),
    Triple("finance", Icons.Default.AccountBalanceWallet, "Finanças"),
    Triple("work", Icons.Default.AccessTime, "Pontos"),
    Triple("shopping", Icons.Default.ShoppingCart, "Listas"),
    Triple("routine", Icons.Default.Checklist, "Rotina"),
    Triple("profile", Icons.Default.Person, "Perfil"),
)

@Composable
fun KontrolApp(context: Context) {
    val session = remember { SessionManager(context) }
    val scope = rememberCoroutineScope()
    var token by remember { mutableStateOf<String?>(null) }
    var carregando by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        token = session.token()
        ApiFactory.tokenProvider = { token }
        carregando = false
    }

    if (carregando) return

    if (token == null) {
        AuthScreen(
            onLogin = { email, senha ->
                val resposta = ApiFactory.api.login(LoginRequest(email, senha))
                if (!resposta.isSuccessful) error("Credenciais inválidas")
                val novoToken = resposta.body()!!.token
                token = novoToken
                session.save(novoToken)
            },
            onRegister = { email, senha, nome ->
                val resposta = ApiFactory.api.register(RegisterRequest(nome, email, senha))
                if (!resposta.isSuccessful) error("Não foi possível concluir o cadastro")
                val novoToken = resposta.body()!!.token
                token = novoToken
                session.save(novoToken)
            },
        )
    } else {
        MainShell(onLogout = {
            scope.launch { session.clear() }
            token = null
        })
    }
}

@Composable
private fun MainShell(onLogout: () -> Unit) {
    val nav = rememberNavController()
    val destinoAtual by nav.currentBackStackEntryAsState()
    val rotaAtual = destinoAtual?.destination?.route ?: "home"

    Scaffold(
        containerColor = Color.White,
        bottomBar = {
            Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                KontrolBottomBar(
                    items = bottomNavItems,
                    selected = rotaAtual,
                    onSelect = { rota ->
                        nav.navigate(rota) {
                            popUpTo("home")
                            launchSingleTop = true
                        }
                    },
                )
            }
        },
    ) { padding ->
        NavHost(nav, startDestination = "home", modifier = Modifier.padding(padding).fillMaxSize()) {
            composable("home") { HomeScreen() }
            composable("finance") { FinanceScreen() }
            composable("work") { WorkScreen() }
            composable("shopping") { ShoppingScreen() }
            composable("routine") { RoutineScreen() }
            composable("profile") { ProfileScreen(onLogout = onLogout) }
        }
    }
}
