package com.example.exibirpatrimoniosactivity.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.exibirpatrimoniosactivity.data.Patrimonio
import com.example.exibirpatrimoniosactivity.ui.components.ConfirmarExclusaoDialog
import com.example.exibirpatrimoniosactivity.ui.components.EstadoVazio
import com.example.exibirpatrimoniosactivity.ui.components.PatrimonioCard
import com.example.exibirpatrimoniosactivity.ui.components.SearchField
import com.example.exibirpatrimoniosactivity.ui.components.TituloTela
import com.example.exibirpatrimoniosactivity.ui.theme.Dimens
import com.example.exibirpatrimoniosactivity.viewmodel.InvScanViewModel

/** Lista de todos os itens patrimoniados (botão da direita da barra inferior). */
@Composable
fun PatrimoniosScreen(
    vm: InvScanViewModel,
    onAbrirPatrimonio: (Int) -> Unit,
    onCriarItem: () -> Unit,
    mostrarMensagem: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var busca by rememberSaveable { mutableStateOf("") }
    var paraExcluir by remember { mutableStateOf<Patrimonio?>(null) }
    val resultados = vm.pesquisar(busca)

    fun remover(patrimonio: Patrimonio) {
        vm.removerPatrimonio(patrimonio.id)
        mostrarMensagem("\"${patrimonio.nome}\" removido")
    }

    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = Dimens.ScreenPadding,
                end = Dimens.ScreenPadding,
                top = Dimens.ScreenPadding,
                bottom = 96.dp // espaço para o botão flutuante
            ),
            verticalArrangement = Arrangement.spacedBy(Dimens.ItemSpacing)
        ) {
            item {
                TituloTela(
                    titulo = "Patrimônios",
                    subtitulo = "${vm.patrimonios.size} itens cadastrados"
                )
            }
            item {
                SearchField(
                    value = busca,
                    onValueChange = { busca = it },
                    placeholder = "Pesquisar por nome, código ou série"
                )
            }
            if (resultados.isEmpty()) {
                item { EstadoVazio(Icons.Default.Inventory2, "Nenhum item encontrado") }
            }
            items(resultados, key = { it.id }) { patrimonio ->
                PatrimonioCard(
                    patrimonio = patrimonio,
                    nomeLocal = vm.nomeDoLocal(patrimonio.localId),
                    onClick = { onAbrirPatrimonio(patrimonio.id) },
                    onAlternarConferido = { vm.alternarConferido(patrimonio.id) },
                    onRemover = {
                        if (vm.config.confirmarExclusao) paraExcluir = patrimonio
                        else remover(patrimonio)
                    }
                )
            }
        }

        ExtendedFloatingActionButton(
            onClick = onCriarItem,
            icon = { Icon(Icons.Default.Add, contentDescription = null) },
            text = { Text("Novo item") },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        )
    }

    paraExcluir?.let { patrimonio ->
        ConfirmarExclusaoDialog(
            titulo = "Excluir item?",
            mensagem = "\"${patrimonio.nome}\" será removido da lista.",
            onConfirmar = {
                remover(patrimonio)
                paraExcluir = null
            },
            onCancelar = { paraExcluir = null }
        )
    }
}
