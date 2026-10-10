package com.example.exibirpatrimoniosactivity.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.exibirpatrimoniosactivity.data.Local
import com.example.exibirpatrimoniosactivity.ui.components.ConfirmarExclusaoDialog
import com.example.exibirpatrimoniosactivity.ui.components.FormTextField
import com.example.exibirpatrimoniosactivity.ui.components.InvScanCard
import com.example.exibirpatrimoniosactivity.ui.components.LocalCard
import com.example.exibirpatrimoniosactivity.ui.components.TituloTela
import com.example.exibirpatrimoniosactivity.ui.theme.Dimens
import com.example.exibirpatrimoniosactivity.viewmodel.InvScanViewModel

/** Segunda lista do app: locais. Tem formulário para adicionar e botão para remover. */
@Composable
fun LocaisScreen(
    vm: InvScanViewModel,
    onAbrirLocal: (Int) -> Unit,
    mostrarMensagem: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var nome by rememberSaveable { mutableStateOf("") }
    var descricao by rememberSaveable { mutableStateOf("") }
    var paraExcluir by remember { mutableStateOf<Local?>(null) }

    fun remover(local: Local) {
        if (vm.removerLocal(local.id)) mostrarMensagem("\"${local.nome}\" removido")
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(Dimens.ScreenPadding),
        verticalArrangement = Arrangement.spacedBy(Dimens.ItemSpacing)
    ) {
        item {
            TituloTela(
                titulo = "Locais",
                subtitulo = "${vm.locais.size} locais cadastrados"
            )
        }

        item {
            InvScanCard(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(Dimens.ItemSpacing)
                ) {
                    Text("Novo local", style = MaterialTheme.typography.titleMedium)
                    FormTextField(
                        value = nome,
                        onValueChange = { nome = it },
                        label = "Nome do local",
                        leadingIcon = Icons.Default.LocationOn
                    )
                    FormTextField(
                        value = descricao,
                        onValueChange = { descricao = it },
                        label = "Descrição",
                        leadingIcon = Icons.Default.Edit
                    )
                    Button(
                        onClick = {
                            vm.adicionarLocal(nome, descricao)
                            nome = ""
                            descricao = ""
                            mostrarMensagem("Local adicionado")
                        },
                        enabled = nome.isNotBlank(),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text("Adicionar local")
                    }
                }
            }
        }

        items(vm.locais, key = { it.id }) { local ->
            val quantidade = vm.patrimoniosDoLocal(local.id).size
            LocalCard(
                local = local,
                quantidadeItens = quantidade,
                onClick = { onAbrirLocal(local.id) },
                onRemover = {
                    when {
                        quantidade > 0 -> mostrarMensagem(
                            "Mova ou remova os $quantidade itens deste local antes de excluí-lo."
                        )
                        vm.config.confirmarExclusao -> paraExcluir = local
                        else -> remover(local)
                    }
                }
            )
        }
    }

    paraExcluir?.let { local ->
        ConfirmarExclusaoDialog(
            titulo = "Excluir local?",
            mensagem = "\"${local.nome}\" será removido.",
            onConfirmar = {
                remover(local)
                paraExcluir = null
            },
            onCancelar = { paraExcluir = null }
        )
    }
}
