package com.example.exibirpatrimoniosactivity.ui.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.exibirpatrimoniosactivity.data.TipoPatrimonio
import com.example.exibirpatrimoniosactivity.ui.components.AcaoGrande
import com.example.exibirpatrimoniosactivity.ui.components.CarrosselImagens
import com.example.exibirpatrimoniosactivity.ui.components.FormTextField
import com.example.exibirpatrimoniosactivity.ui.components.SeletorLocal
import com.example.exibirpatrimoniosactivity.ui.components.TituloTela
import com.example.exibirpatrimoniosactivity.ui.theme.Dimens
import com.example.exibirpatrimoniosactivity.viewmodel.InvScanViewModel

/** Formulário para cadastrar um novo item (tela "Criar Item" do wireframe). */
@Composable
fun CriarItemScreen(
    vm: InvScanViewModel,
    onItemCriado: (Int) -> Unit,
    mostrarMensagem: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var nome by rememberSaveable { mutableStateOf("") }
    var tipo by rememberSaveable { mutableStateOf(TipoPatrimonio.NOTEBOOK) }
    var descricao by rememberSaveable { mutableStateOf("") }
    var serie by rememberSaveable { mutableStateOf("") }
    var codigo by rememberSaveable { mutableStateOf("") }
    var prateleira by rememberSaveable { mutableStateOf("") }
    var localId by rememberSaveable { mutableStateOf<Int?>(vm.locais.firstOrNull()?.id) }
    var imagens by remember { mutableStateOf<List<String>>(emptyList()) }
    var dialogoCodigo by remember { mutableStateOf(false) }

    val seletorFotos = rememberLauncherForActivityResult(
        ActivityResultContracts.PickMultipleVisualMedia(5)
    ) { uris ->
        imagens = imagens + uris.map { it.toString() }
    }
    val aoRemoverFoto: (Int) -> Unit = { indice ->
        imagens = imagens.filterIndexed { i, _ -> i != indice }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(Dimens.ScreenPadding),
        verticalArrangement = Arrangement.spacedBy(Dimens.ItemSpacing)
    ) {
        TituloTela(
            titulo = "Criar Item",
            subtitulo = "Cadastre um novo patrimônio"
        )

        FormTextField(
            value = nome,
            onValueChange = { nome = it },
            label = "Nome do item",
            leadingIcon = Icons.Default.Inventory2
        )

        // Tipo do item
        Row(
            modifier = Modifier.horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TipoPatrimonio.entries.forEach { opcao ->
                FilterChip(
                    selected = tipo == opcao,
                    onClick = { tipo = opcao },
                    label = { Text(opcao.rotulo) },
                    leadingIcon = {
                        Icon(opcao.icone, contentDescription = null, modifier = Modifier.size(18.dp))
                    }
                )
            }
        }

        FormTextField(
            value = descricao,
            onValueChange = { descricao = it },
            label = "Descrição",
            leadingIcon = Icons.Default.Edit,
            minLines = 3
        )

        FormTextField(
            value = serie,
            onValueChange = { serie = it.filter(Char::isDigit) },
            label = "Número de série",
            keyboardType = KeyboardType.Number
        )

        SeletorLocal(
            locais = vm.locais,
            selecionadoId = localId,
            onSelecionar = { localId = it }
        )
        if (vm.locais.isEmpty()) {
            Text(
                text = "Cadastre um local em Menu > Locais antes de criar itens.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error
            )
        }

        FormTextField(
            value = prateleira,
            onValueChange = { prateleira = it },
            label = "Prateleira / posição",
            leadingIcon = Icons.Default.LocationOn
        )

        // Botões de foto e código (parte de baixo do wireframe)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(Dimens.ItemSpacing)
        ) {
            AcaoGrande(
                titulo = "Adicionar fotos",
                detalhe = when (imagens.size) {
                    0 -> "Nenhuma foto"
                    1 -> "1 foto"
                    else -> "${imagens.size} fotos"
                },
                icone = Icons.Default.AddPhotoAlternate,
                onClick = {
                    seletorFotos.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                },
                modifier = Modifier.weight(1f)
            )
            AcaoGrande(
                titulo = "Código",
                detalhe = codigo.ifBlank { "Automático" },
                icone = Icons.Default.QrCodeScanner,
                onClick = { dialogoCodigo = true },
                modifier = Modifier.weight(1f)
            )
        }

        if (imagens.isNotEmpty()) {
            CarrosselImagens(imagens = imagens, onRemoverAtual = aoRemoverFoto)
        }

        Button(
            onClick = {
                val local = localId
                if (local != null) {
                    val novoId = vm.adicionarPatrimonio(
                        nome = nome,
                        tipo = tipo,
                        descricao = descricao,
                        serie = serie,
                        codigo = codigo,
                        localId = local,
                        prateleira = prateleira,
                        imagens = imagens
                    )
                    mostrarMensagem("Item criado!")
                    onItemCriado(novoId)
                }
            },
            enabled = nome.isNotBlank() && localId != null,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .padding(top = 0.dp)
        ) {
            Text("CRIAR ITEM", style = MaterialTheme.typography.labelLarge)
        }
    }

    if (dialogoCodigo) {
        var rascunho by remember { mutableStateOf(codigo) }
        val duplicado = rascunho.isNotBlank() && vm.codigoExiste(rascunho)

        AlertDialog(
            onDismissRequest = { dialogoCodigo = false },
            title = { Text("Código do patrimônio") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Digite o código da etiqueta. Se deixar em branco, será gerado ${vm.proximoCodigo()}.")
                    FormTextField(
                        value = rascunho,
                        onValueChange = { rascunho = it },
                        label = "Código",
                        leadingIcon = Icons.Default.QrCode,
                        isError = duplicado,
                        supportingText = if (duplicado) "Já existe um item com este código" else null
                    )
                }
            },
            confirmButton = {
                TextButton(
                    enabled = !duplicado,
                    onClick = {
                        codigo = rascunho.trim()
                        dialogoCodigo = false
                    }
                ) { Text("Confirmar") }
            },
            dismissButton = {
                TextButton(onClick = { dialogoCodigo = false }) { Text("Cancelar") }
            }
        )
    }
}
