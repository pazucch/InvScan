package com.example.exibirpatrimoniosactivity.ui.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.exibirpatrimoniosactivity.data.Patrimonio
import com.example.exibirpatrimoniosactivity.ui.components.CarrosselImagens
import com.example.exibirpatrimoniosactivity.ui.components.EstadoVazio
import com.example.exibirpatrimoniosactivity.ui.components.FormTextField
import com.example.exibirpatrimoniosactivity.ui.components.IconeCircular
import com.example.exibirpatrimoniosactivity.ui.components.InfoCampo
import com.example.exibirpatrimoniosactivity.ui.components.InvScanCard
import com.example.exibirpatrimoniosactivity.ui.components.SecaoTitulo
import com.example.exibirpatrimoniosactivity.ui.components.SeletorLocal
import com.example.exibirpatrimoniosactivity.ui.components.TituloTela
import com.example.exibirpatrimoniosactivity.ui.theme.Dimens
import com.example.exibirpatrimoniosactivity.viewmodel.InvScanViewModel

@Composable
fun PatrimonioDetalheScreen(
    vm: InvScanViewModel,
    patrimonioId: Int?,
    onAbrirLocal: (Int) -> Unit,
    mostrarMensagem: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val patrimonio = patrimonioId?.let { vm.buscarPatrimonio(it) }

    if (patrimonio == null) {
        EstadoVazio(
            icone = Icons.Default.Inventory2,
            texto = "Este item não existe mais.",
            modifier = modifier
        )
    } else {
        DetalheConteudo(vm, patrimonio, onAbrirLocal, mostrarMensagem, modifier)
    }
}

/**
 * Detalhe do item. Vai além de só mostrar os dados:
 *  1) o item pode ser EDITADO ali mesmo (campos, fotos, local);
 *  2) mostra dados do LOCAL (outra lista) e abre o detalhe do local (navegação secundária);
 *  3) permite marcar o item como conferido.
 */
@Composable
private fun DetalheConteudo(
    vm: InvScanViewModel,
    patrimonio: Patrimonio,
    onAbrirLocal: (Int) -> Unit,
    mostrarMensagem: (String) -> Unit,
    modifier: Modifier
) {
    var editando by rememberSaveable { mutableStateOf(false) }

    // Rascunhos: recomeçam do valor atual do item sempre que entra/sai do modo de edição
    var nome by rememberSaveable(patrimonio, editando) { mutableStateOf(patrimonio.nome) }
    var serie by rememberSaveable(patrimonio, editando) { mutableStateOf(patrimonio.serie) }
    var descricao by rememberSaveable(patrimonio, editando) { mutableStateOf(patrimonio.descricao) }
    var prateleira by rememberSaveable(patrimonio, editando) { mutableStateOf(patrimonio.prateleira) }
    var localId by rememberSaveable(patrimonio, editando) { mutableStateOf(patrimonio.localId) }
    var imagens by remember(patrimonio, editando) { mutableStateOf(patrimonio.imagens) }

    val seletorFotos = rememberLauncherForActivityResult(
        ActivityResultContracts.PickMultipleVisualMedia(5)
    ) { uris ->
        imagens = imagens + uris.map { it.toString() }
    }
    val aoRemoverFoto: (Int) -> Unit = { indice ->
        imagens = imagens.filterIndexed { i, _ -> i != indice }
    }

    val local = vm.buscarLocal(patrimonio.localId)
    val outrosNoLocal = vm.patrimonios.count {
        it.localId == patrimonio.localId && it.id != patrimonio.id
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(Dimens.ScreenPadding),
        verticalArrangement = Arrangement.spacedBy(Dimens.SectionSpacing)
    ) {
        TituloTela(
            titulo = "Item Patrimoniado",
            subtitulo = if (editando) "Editando — salve para aplicar" else "Patrimônio ${patrimonio.codigo}",
            acao = {
                IconButton(onClick = { editando = !editando }) {
                    Icon(
                        imageVector = if (editando) Icons.Default.Close else Icons.Default.Edit,
                        contentDescription = if (editando) "Cancelar edição" else "Editar item"
                    )
                }
            }
        )

        // Fotos
        CarrosselImagens(
            imagens = if (editando) imagens else patrimonio.imagens,
            onRemoverAtual = aoRemoverFoto.takeIf { editando }
        )
        if (editando) {
            OutlinedButton(
                onClick = {
                    seletorFotos.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.AddPhotoAlternate, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Adicionar fotos")
            }
        } else {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = patrimonio.conferido,
                    onCheckedChange = { vm.alternarConferido(patrimonio.id) }
                )
                Text("Item conferido", style = MaterialTheme.typography.bodyLarge)
            }
        }

        // Identificação
        Column(verticalArrangement = Arrangement.spacedBy(Dimens.ItemSpacing)) {
            InfoCampo("PATRIMÔNIO", patrimonio.codigo)
            if (editando) {
                FormTextField(
                    value = nome,
                    onValueChange = { nome = it },
                    label = "Item",
                    leadingIcon = patrimonio.tipo.icone,
                    isError = nome.isBlank(),
                    supportingText = if (nome.isBlank()) "Informe o nome do item" else null
                )
                FormTextField(
                    value = serie,
                    onValueChange = { serie = it.filter(Char::isDigit) },
                    label = "Série",
                    keyboardType = KeyboardType.Number
                )
            } else {
                InfoCampo("ITEM", patrimonio.nome)
                InfoCampo("SÉRIE", patrimonio.serie)
            }
        }

        // Descrição
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            SecaoTitulo("Descrição")
            if (editando) {
                FormTextField(
                    value = descricao,
                    onValueChange = { descricao = it },
                    label = "Descrição",
                    minLines = 4
                )
            } else {
                InvScanCard(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = patrimonio.descricao.ifBlank { "Sem descrição." },
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }

        // Localização (dados vindos da lista de locais)
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            SecaoTitulo("Localização")
            if (editando) {
                SeletorLocal(
                    locais = vm.locais,
                    selecionadoId = localId,
                    onSelecionar = { localId = it }
                )
                FormTextField(
                    value = prateleira,
                    onValueChange = { prateleira = it },
                    label = "Prateleira / posição",
                    leadingIcon = Icons.Default.LocationOn
                )
            } else {
                InvScanCard(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { onAbrirLocal(patrimonio.localId) }
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        IconeCircular(
                            icone = Icons.Default.LocationOn,
                            descricao = null,
                            container = MaterialTheme.colorScheme.secondaryContainer,
                            conteudo = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = local?.nome ?: "Sem local",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                text = patrimonio.prateleira.ifBlank { "Posição não informada" },
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = when (outrosNoLocal) {
                                    0 -> "Único item neste local"
                                    1 -> "Mais 1 item neste local"
                                    else -> "Mais $outrosNoLocal itens neste local"
                                },
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }
                        Icon(Icons.Default.ChevronRight, contentDescription = "Abrir local")
                    }
                }
            }
        }

        if (editando) {
            Row(horizontalArrangement = Arrangement.spacedBy(Dimens.ItemSpacing)) {
                OutlinedButton(
                    onClick = { editando = false },
                    modifier = Modifier.weight(1f)
                ) { Text("Cancelar") }

                Button(
                    onClick = {
                        vm.atualizarPatrimonio(
                            patrimonio.copy(
                                nome = nome.trim(),
                                serie = serie,
                                descricao = descricao.trim(),
                                prateleira = prateleira.trim(),
                                localId = localId,
                                imagens = imagens
                            )
                        )
                        editando = false
                        mostrarMensagem("Alterações salvas")
                    },
                    enabled = nome.isNotBlank(),
                    modifier = Modifier.weight(1f)
                ) { Text("Salvar") }
            }
        }
    }
}
