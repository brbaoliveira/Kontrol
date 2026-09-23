package com.kontrol.app.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kontrol.app.ui.theme.AzulEscuroKontrol
import com.kontrol.app.ui.theme.AzulKontrol
import com.kontrol.app.ui.theme.CantoPadrao
import com.kontrol.app.ui.theme.Cinza
import com.kontrol.app.ui.theme.CinzaClaro
import com.kontrol.app.ui.theme.PainelTopoArredondado
import com.kontrol.app.ui.theme.VermelhoClaroKontrol
import com.kontrol.app.ui.theme.kontrolBackgroundGradientVertical
import com.kontrol.app.ui.theme.kontrolButtonGradient
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.ui.res.painterResource
import com.kontrol.app.R
import com.kontrol.app.ui.theme.kontrolBackgroundGradientHorizontal

/** Fundo com o gradiente característico do Kontrol, ocupando a tela toda. */
@Composable
fun GradientBackground(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit = {},
) {
    Box(
        modifier = modifier.fillMaxSize().background(kontrolBackgroundGradientHorizontal),
        content = content,
    )
}

/** Botão primário com o mesmo gradiente e cantos de 10dp do bt_gradient_kontrol.xml original. */
@Composable
fun KontrolButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(CantoPadrao)
            .background(if (enabled) kontrolButtonGradient else Brush.verticalGradient(listOf(CinzaClaro, CinzaClaro)))
            .clickable(enabled = enabled, onClick = onClick)
            .padding(vertical = 14.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(text, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
    }
}

/** Campo outlined com o mesmo raio de 16dp e cor azul_kontrol dos TextInputLayout originais. */
@Composable
fun KontrolTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    leadingIcon: ImageVector? = null,
    isPassword: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text,
) {
    var visible by remember { mutableStateOf(!isPassword) }
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        singleLine = true,
        leadingIcon = leadingIcon?.let { icon -> { Icon(icon, contentDescription = null, tint = AzulKontrol) } },
        trailingIcon = if (isPassword) {
            {
                IconButton(onClick = { visible = !visible }) {
                    Icon(
                        if (visible) Icons.Default.ChevronRight else Icons.Default.ChevronLeft,
                        contentDescription = if (visible) "Ocultar senha" else "Mostrar senha",
                    )
                }
            }
        } else null,
        visualTransformation = if (isPassword && !visible) {
            PasswordVisualTransformation()
        } else {
            VisualTransformation.None
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = if (isPassword) KeyboardType.Password else keyboardType,
        ),
        shape = RoundedCornerShape(16.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = AzulKontrol,
            focusedLabelColor = AzulKontrol,
            cursorColor = AzulKontrol,
        ),
        modifier = modifier.fillMaxWidth(),
    )
}

/** Faixa de aviso usada em erros de login/cadastro (borda_erro.xml). */
@Composable
fun WarningBanner(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = VermelhoClaroKontrol,
) {
    Surface(
        color = color.copy(alpha = 0.12f),
        contentColor = color,
        shape = RoundedCornerShape(10.dp),
        modifier = modifier.fillMaxWidth(),
    ) {
        Text(text, modifier = Modifier.padding(12.dp), fontSize = 13.sp)
    }
}

/** Navegador de mês com setas dentro de uma "pill" com borda, igual ao componente do Financeiro/Ponto originais. */
@Composable
fun MonthNavigator(
    label: String,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        shape = RoundedCornerShape(24.dp),
        border = BorderStroke(1.dp, CinzaClaro),
        color = Color.Transparent,
        modifier = modifier,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(horizontal = 4.dp)) {
            IconButton(onClick = onPrevious) {
                Icon(Icons.Default.ChevronLeft, contentDescription = "Mês anterior", tint = AzulEscuroKontrol)
            }
            Text(label, fontWeight = FontWeight.Bold, color = AzulEscuroKontrol)
            IconButton(onClick = onNext) {
                Icon(Icons.Default.ChevronRight, contentDescription = "Próximo mês", tint = AzulEscuroKontrol)
            }
        }
    }
}

/**
 * Bloco colorido com rótulo e valor — equivalente aos cards bt_verde/bt_vermelho/bt_cinza
 * usados nos resumos de Finanças e Ponto.
 */
@Composable
fun SummaryTile(
    label: String,
    value: String,
    background: Brush,
    modifier: Modifier = Modifier,
    contentColor: Color = Color.White,
    icon: (@Composable () -> Unit)? = null,
) {
    Box(
        modifier = modifier
            .clip(CantoPadrao)
            .background(background)
            .padding(14.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            icon?.invoke()
            Column {
                Text(label, color = contentColor, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                Text(value, color = contentColor, fontSize = 19.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

/**
 * Estrutura de tela com cabeçalho em gradiente (título grande em branco) e conteúdo dentro
 * de um painel branco com o topo arredondado — reproduz o CollapsingToolbarLayout usado em
 * Finanças e Ponto no app original.
 */
/*@Composable
fun GradientHeaderScreen(
    title: String,
    modifier: Modifier = Modifier,
    headerExtra: @Composable () -> Unit = {},
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(modifier = modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(kontrolBackgroundGradient),
        ) {
            Column(modifier = Modifier.padding(start = 24.dp, bottom = 16.dp).align(Alignment.BottomStart)) {
                Text(title, color = Color.White, fontSize = 30.sp, fontWeight = FontWeight.Bold)
                headerExtra()
            }
        }
        Surface(
            shape = PainelTopoArredondado,
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .offset(y = (-24).dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp, vertical = 24.dp),
                content = content,
            )
        }
    }
}*/

@Composable
fun GradientHeaderScreen(
    title: String,
    modifier: Modifier = Modifier,
    headerExtra: @Composable () -> Unit = {},
    content: @Composable ColumnScope.() -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(kontrolBackgroundGradientHorizontal)
    ) {

        Column(
            modifier = Modifier.fillMaxSize()
        ) {

            // -----------------------------------------
            // CABEÇALHO
            // -----------------------------------------

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 24.dp,
                        end = 24.dp,
                        top = 20.dp,
                        bottom = 36.dp
                    )
            ) {

                // Aqui futuramente podemos colocar o logo
                // Image(...)
                Image(
                    painter = painterResource(id = R.drawable.logo_degrade),
                    contentDescription = "Logo Kontrol",
                    modifier = Modifier
                        .size(60.dp)
                        .padding(bottom = 20.dp)
                )

                Text(
                    text = title,
                    color = Color.White,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold
                )

                headerExtra()
            }

            // -----------------------------------------
            // PAINEL BRANCO
            // -----------------------------------------

            Surface(
                shape = PainelTopoArredondado,
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)

            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            horizontal = 20.dp,
                            vertical = 24.dp
                        ),
                    content = content,
                )
            }
        }
    }
}

/** Um item da navegação inferior: ícone apenas, sem rótulo — igual ao bottom_navigation.xml original. */
@Composable
private fun RowScope.KontrolNavItem(
    icon: ImageVector,
    contentDescription: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .weight(1f)
            .size(56.dp)
            .clip(CircleShape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            icon,
            contentDescription = contentDescription,
            tint = if (selected) AzulEscuroKontrol else Cinza,
            modifier = Modifier.size(26.dp),
        )
    }
}

/**
 * Barra de navegação inferior branca, com os cantos inferiores arredondados e ícones sem
 * texto — reproduz bottom_navigation.xml + fundo_branco_baixo.xml + nav_item_color.xml.
 */
@Composable
fun KontrolBottomBar(
    items: List<Triple<String, ImageVector, String>>,
    selected: String,
    onSelect: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        shape = com.kontrol.app.ui.theme.BarraInferiorArredondada,
        color = Color.White,
        shadowElevation = 0.dp,
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding(),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            items.forEach { (route, icon, description) ->
                KontrolNavItem(
                    icon = icon,
                    contentDescription = description,
                    selected = selected == route,
                    onClick = { onSelect(route) },
                )
            }
        }
    }
}
