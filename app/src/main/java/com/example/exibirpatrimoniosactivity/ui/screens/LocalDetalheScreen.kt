package com.example.exibirpatrimoniosactivity.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.exibirpatrimoniosactivity.data.Local
import com.example.exibirpatrimoniosactivity.ui.components.EstadoVazio
import com.example.exibirpatrimoniosactivity.ui.components.FormTextField
import com.example.exibirpatrimoniosactivity.ui.components.IconeCircular
import com.example.exibirpatrimoniosactivity.ui.components.InvScanCard
import com.example.exibirpatrimoniosactivity.ui.components.SecaoTitulo
import com.example.exibirpatrimoniosactivity.ui.components.TituloTela
import com.example.exibirpatrimoniosactivity.ui.theme.Dimens
import com.example.exibirpatrimoniosactivity.viewmodel.InvScanViewModel

@Composable
fun LocalDetalheScreen(
    vm: InvScanViewModel,
    localId: Int?,
    onAbrirPatrimonio: (Int) -> Unit,
    mostrarMensagem: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val local = localId?.let { vm.buscarLocal(it) }

    if (local == null) {
        EstadoVazio(Icons.Default.LocationOn, "Este local não existe mais.", modifier)
    } else {
        LocalConteudo(vm, local, onAbrirPatrimonio, mostrarMensagem, modifier)
    }
}

/**
 * Detalhe do local: edita o local ali mesmo, mostra a barra de progresso da conferência
 * e lista os itens que estão nele (cada um abre o detalhe do item).
 */
@Composable
private fun LocalConteudo(
    vm: InvScanViewModel,
    local: Local,
    onAbrirPatrimonio: (Int) -> Unit,
    mostrarMensagem: (String) -> Unit,
    modifier: Modifier
) {
    var editando by rememberSaveable { mutableStateOf(false) }
    var nome by rememberSaveable(local, editando) { mutableStateOf(local.nome) }
    var descricao by rememberSaveable(local, editando) { mutableStateOf(local.descricao) }

    val itens = vm.patrimoniosDoLocal(local.id)
    val conferidos = itens.count { it.conferido }
    val fracao = if (itens.isEmpty()) 0f else conferidos / itens.size.toFloat()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(Dimens.ScreenPadding),
        verticalArrangement = Arrangement.spacedBy(Dimens.SectionSpacing)
    ) {
        TituloTela(
            titulo = "Local",
            subtitulo = if (editando) "Editando — salve para aplicar" else null,
            acao = {
                IconButton(onClick = { editando = !editando }) {
                    Icon(
                        imageVector = if (editando) Icons.Default.Close else Icons.Default.Edit,
                        contentDescription = if (editando) "Cancelar edição" else "Editar local"
                    )
                }
            }
        )

        InvScanCard(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(Dimens.ItemSpacing)
            ) {
                if (editando) {
                    FormTextField(
                        value = nome,
                        onValueChange = { nome = it },
                        label = "Nome do local",
                        leadingIcon = Icons.Default.LocationOn,
                        isError = nome.isBlank()
                    )
                    FormTextField(
                        value = descricao,
                        onValueChange = { descricao = it },
                        label = "Descrição",
                        minLines = 2
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(Dimens.ItemSpacing)) {
                        OutlinedButton(
                            onClick = { editando = false },
                            modifier = Modifier.weight(1f)
                        ) { Text("Cancelar") }
                        Button(
                            onClick = {
                                vm.atualizarLocal(local.copy(nome = nome.trim(), descricao = descricao.trim()))
                                editando = false
                                mostrarMensagem("Local atualizado")
                            },
                            enabled = nome.isNotBlank(),
                            modifier = Modifier.weight(1f)
                        ) { Text("Salvar") }
                    }
                } else {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        IconeCircular(
                            icone = Icons.Default.LocationOn,
                            descricao = null,
                            container = MaterialTheme.colorScheme.secondaryContainer,
                            conteudo = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                        Column {
                            Text(local.nome, style = MaterialTheme.typography.titleLarge)
                            Text(
                                text = local.descricao.ifBlank { "Sem descrição" },
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }

        // Progresso da conferência (dado calculado a partir dos itens)
        InvScanCard(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Conferência: $conferidos de ${itens.size}",
                    style = MaterialTheme.typography.titleMedium
                )
                LinearProgressIndicator(
                    progress = { fracao },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        // Itens do local
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            SecaoTitulo("Itens neste local")
            if (itens.isEmpty()) {
                EstadoVazio(Icons.Default.Inventory2, "Nenhum item neste local ainda.")
            }
            itens.forEach { patrimonio ->
                InvScanCard(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { onAbrirPatrimonio(patrimonio.id) }
                ) {
                    ListItem(
                        headlineContent = { Text(patrimonio.nome) },
                        supportingContent = {
                            Text("Patrimônio ${patrimonio.codigo}")
                        },
                        leadingContent = {
                            Icon(patrimonio.tipo.icone, contentDescription = patrimonio.tipo.rotulo)
                        },
                        trailingContent = {
                            if (patrimonio.conferido) {
                                Icon(
                                    Icons.Default.CheckCircle,
                                    contentDescription = "Conferido",
                                    tint = MaterialTheme.colorScheme.secondary
                                )
                            }
                        },
                        colors = ListItemDefaults.colors(containerColor = Color.Transparent)
                    )
                }
            }
        }
    }
}
