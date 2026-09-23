package com.kontrol.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.align
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kontrol.app.ui.components.GradientBackground
import com.kontrol.app.ui.components.KontrolButton
import com.kontrol.app.ui.components.KontrolTextField
import com.kontrol.app.ui.components.WarningBanner
import com.kontrol.app.ui.theme.AzulEscuroKontrol
import com.kontrol.app.ui.theme.CinzaClaro
import com.kontrol.app.ui.theme.PainelArredondado
import kotlinx.coroutines.launch

/**
 * Tela de autenticação, reproduzindo o layout de activity_login.xml / activity_cadastro.xml
 * do app antigo: fundo em gradiente, título de boas-vindas em branco e um painel branco com
 * cantos bem arredondados contendo o formulário.
 *
 * O app antigo usava um campo "Usuário" separado do e-mail; o backend atual autentica só por
 * e-mail e senha, então o formulário foi simplificado para o que a API realmente aceita.
 */
@Composable
fun AuthScreen(
    onLogin: suspend (email: String, senha: String) -> Unit,
    onRegister: suspend (email: String, senha: String, nome: String) -> Unit,
) {
    var isRegister by remember { mutableStateOf(false) }
    var nome by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var erro by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    GradientBackground {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                "KONTROL",
                color = androidx.compose.ui.graphics.Color.White,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
            )
            Text(
                if (isRegister) "Seja bem-vindo!" else "Bem-vindo de volta!",
                color = androidx.compose.ui.graphics.Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 16.dp),
            )
            Text(
                if (isRegister) "Faça seu cadastro e acesse sua conta" else "Faça login para acessar sua conta",
                color = CinzaClaro,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 16.dp, top = 4.dp),
            )

            Surface(
                shape = PainelArredondado,
                color = androidx.compose.ui.graphics.Color.White,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                ) {
                    erro?.let { WarningBanner(it) }

                    if (isRegister) {
                        KontrolTextField(
                            value = nome,
                            onValueChange = { nome = it },
                            label = "Nome",
                            leadingIcon = Icons.Default.Person,
                        )
                    }
                    KontrolTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = "E-mail",
                        leadingIcon = Icons.Default.Email,
                        keyboardType = androidx.compose.ui.text.input.KeyboardType.Email,
                    )
                    KontrolTextField(
                        value = senha,
                        onValueChange = { senha = it },
                        label = "Senha",
                        leadingIcon = Icons.Default.Lock,
                        isPassword = true,
                    )

                    KontrolButton(
                        text = if (isRegister) "Cadastrar" else "Entrar",
                        onClick = {
                            erro = null
                            scope.launch {
                                try {
                                    if (isRegister) onRegister(email, senha, nome) else onLogin(email, senha)
                                } catch (e: Exception) {
                                    erro = if (isRegister) {
                                        "Não foi possível concluir o cadastro. Verifique os dados e tente novamente."
                                    } else {
                                        "Usuário ou senha inválidos, tente novamente!"
                                    }
                                }
                            }
                        },
                    )

                    TextButton(
                        onClick = { isRegister = !isRegister; erro = null },
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text(
                            if (isRegister) "Já tem cadastro? Clique aqui para fazer login!" else "Não tem cadastro? Clique aqui e faça seu cadastro!",
                            color = AzulEscuroKontrol,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                        )
                    }
                }
            }
        }
    }
}
