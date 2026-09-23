package com.kontrol.app.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val KontrolColorScheme = lightColorScheme(
    primary = AzulKontrol,
    onPrimary = Color.White,
    secondary = VerdeKontrol,
    onSecondary = Color.White,
    tertiary = AzulClaroKontrol,
    error = VermelhoClaroKontrol,
    background = Color.White,
    surface = Color.White,
    onSurface = CinzaMedio,
    onSurfaceVariant = Cinza,
)

@Composable
fun KontrolTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = KontrolColorScheme,
        typography = Typography(),
        content = content,
    )
}

/**
 * Gradiente de fundo do app (equivalente a drawable/bg_gradient_kontrol.xml):
 * verde -> azul -> azul escuro, de cima para baixo.
 */
val kontrolBackgroundGradientVertical: Brush
    get() = Brush.verticalGradient(colors = listOf(VerdeKontrol, AzulKontrol, AzulEscuroKontrol))
/**
 * Gradiente de fundo do app (equivalente a drawable/bg_gradient_kontrol.xml):
 * verde -> azul -> azul escuro, de cima para baixo.
 */
val kontrolBackgroundGradientHorizontal: Brush
    get() = Brush.horizontalGradient(colors = listOf(AzulEscuroKontrol, AzulKontrol, VerdeKontrol))

/** Gradiente dos botões primários (drawable/bt_gradient_kontrol.xml): mesmas cores, uso em botão. */
val kontrolButtonGradient: Brush
    get() = Brush.verticalGradient(colors = listOf(VerdeKontrol, AzulKontrol, AzulEscuroKontrol))

/** Diagonal usada nos cards de receita (drawable/bt_verde.xml). */
val kontrolReceitaGradient: Brush
    get() = Brush.linearGradient(colors = listOf(VerdeClaroKontrol, VerdeKontrol, VerdeEscuroKontrol))

/** Diagonal usada nos cards de despesa (drawable/bt_vermelho.xml). */
val kontrolDespesaGradient: Brush
    get() = Brush.linearGradient(colors = listOf(VermelhoClaroKontrol, VermelhoKontrol, VermelhoEscuroKontrol))

/** Cinza neutro usado no card "Total do mês" (drawable/bt_cinza.xml). */
val kontrolNeutroGradient: Brush
    get() = Brush.verticalGradient(colors = listOf(CinzaClaro, CinzaClaro, CinzaClarinho))

/** Diagonal azul usada nos cards de horas/tempo no dashboard e no Ponto. */
val kontrolAzulGradient: Brush
    get() = Brush.linearGradient(colors = listOf(AzulClaroKontrol, AzulKontrol, AzulEscuroKontrol))

/** Diagonal em tom de âmbar usada no card de Mercado no dashboard. */
val kontrolAmareloGradient: Brush
    get() = Brush.linearGradient(colors = listOf(Color(0xFFFFD54F), Amarelo, Color(0xFFC98A00)))

/** Painel branco com todos os cantos bem arredondados (drawable/fundo_branco.xml) — usado no login. */
val PainelArredondado = RoundedCornerShape(42.dp)

/** Painel branco com cantos superiores arredondados (drawable/fundo_branco_cima.xml) — cabeçalhos com gradiente. */
val PainelTopoArredondado = RoundedCornerShape(topStart = 42.dp, topEnd = 42.dp)

/** Barra branca com cantos inferiores arredondados (drawable/fundo_branco_baixo.xml) — navegação inferior. */
val BarraInferiorArredondada = RoundedCornerShape(bottomStart = 42.dp, bottomEnd = 42.dp)

/** Cantos dos cards/botões pequenos (10dp em quase todos os drawables bt_*). */
val CantoPadrao = RoundedCornerShape(10.dp)
