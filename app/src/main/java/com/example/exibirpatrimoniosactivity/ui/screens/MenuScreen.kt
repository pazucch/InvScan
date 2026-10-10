package com.example.exibirpatrimoniosactivity.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.HeadsetMic
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.exibirpatrimoniosactivity.navigation.Rotas
import com.example.exibirpatrimoniosactivity.ui.components.FuncionalidadeCard
import com.example.exibirpatrimoniosactivity.ui.components.TituloTela
import com.example.exibirpatrimoniosactivity.ui.theme.Dimens

private data class Funcionalidade(
    val titulo: String,
    val descricao: String,
    val icone: ImageVector,
    val rota: String
)

private val funcionalidades = listOf(
    Funcionalidade("Início", "Resumo e busca rápida", Icons.Default.Home, Rotas.INICIO),
    Funcionalidade("Patrimônios", "Lista de todos os itens", Icons.Default.Inventory2, Rotas.PATRIMONIOS),
    Funcionalidade("Criar item", "Cadastrar um novo patrimônio", Icons.Default.AddCircle, Rotas.CRIAR_ITEM),
    Funcionalidade("Locais", "Onde cada item está guardado", Icons.Default.LocationOn, Rotas.LOCAIS),
    Funcionalidade("Escanear código", "Encontrar item pela etiqueta", Icons.Default.QrCodeScanner, Rotas.ESCANEAR),
    Funcionalidade("Usuário", "Seu perfil e conquistas", Icons.Default.Person, Rotas.USUARIO),
    Funcionalidade("Configurações", "Tema, texto e alertas", Icons.Default.Settings, Rotas.CONFIGURACOES),
    Funcionalidade("Suporte", "Ajuda e contato", Icons.Default.HeadsetMic, Rotas.SUPORTE)
)

/** Grade com todas as funcionalidades do app (botão da esquerda da barra inferior). */
@Composable
fun MenuScreen(
    onNavegar: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(Dimens.ScreenPadding),
        verticalArrangement = Arrangement.spacedBy(Dimens.ItemSpacing),
        horizontalArrangement = Arrangement.spacedBy(Dimens.ItemSpacing)
    ) {
        item(span = { GridItemSpan(maxLineSpan) }) {
            TituloTela(
                titulo = "Menu",
                subtitulo = "Todas as funcionalidades do InvScan"
            )
        }
        items(funcionalidades) { funcionalidade ->
            FuncionalidadeCard(
                titulo = funcionalidade.titulo,
                descricao = funcionalidade.descricao,
                icone = funcionalidade.icone,
                onClick = { onNavegar(funcionalidade.rota) }
            )
        }
    }
}
