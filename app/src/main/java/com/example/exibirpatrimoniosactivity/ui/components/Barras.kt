package com.example.exibirpatrimoniosactivity.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.exibirpatrimoniosactivity.navigation.Rotas
import com.example.exibirpatrimoniosactivity.ui.theme.InvScanGreen
import com.example.exibirpatrimoniosactivity.ui.theme.InvScanNavy
import com.example.exibirpatrimoniosactivity.ui.theme.InvScanYellow

/** TopAppBar padrão: logo "InvScan", mensagem de boas-vindas e botão de voltar quando preciso. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InvScanTopBar(
    mostrarVoltar: Boolean,
    onVoltar: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        modifier = modifier,
        title = {
            Text(
                text = buildAnnotatedString {
                    withStyle(SpanStyle(color = Color.White)) { append("Inv") }
                    withStyle(SpanStyle(color = InvScanGreen)) { append("Scan") }
                },
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold
            )
        },
        navigationIcon = {
            if (mostrarVoltar) {
                IconButton(onClick = onVoltar) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                }
            }
        },
        actions = {
            Text(
                text = "Seja bem-vindo!",
                color = InvScanYellow,
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.padding(end = 16.dp)
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = InvScanNavy,
            titleContentColor = Color.White,
            navigationIconContentColor = Color.White,
            actionIconContentColor = Color.White
        )
    )
}

/** As três abas da barra inferior, na ordem do wireframe. */
enum class AbaInferior(val rota: String, val rotulo: String, val icone: ImageVector) {
    MENU(Rotas.MENU, "Menu", Icons.Default.Menu),
    INICIO(Rotas.INICIO, "Início", Icons.Default.Home),
    ITENS(Rotas.PATRIMONIOS, "Itens", Icons.Default.Apps)
}

@Composable
fun InvScanBottomBar(
    rotaDaAbaAtual: String,
    onSelecionar: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier,
        containerColor = InvScanNavy,
        contentColor = Color.White
    ) {
        AbaInferior.entries.forEach { aba ->
            NavigationBarItem(
                selected = aba.rota == rotaDaAbaAtual,
                onClick = { onSelecionar(aba.rota) },
                icon = { Icon(aba.icone, contentDescription = aba.rotulo) },
                label = { Text(aba.rotulo) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.White,
                    selectedTextColor = Color.White,
                    unselectedIconColor = Color.White.copy(alpha = 0.65f),
                    unselectedTextColor = Color.White.copy(alpha = 0.65f),
                    indicatorColor = Color.White.copy(alpha = 0.18f)
                )
            )
        }
    }
}
