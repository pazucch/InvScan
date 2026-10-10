package com.example.exibirpatrimoniosactivity.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Surface
import com.example.exibirpatrimoniosactivity.ui.components.TituloTela
import com.example.exibirpatrimoniosactivity.ui.theme.Dimens
import com.example.exibirpatrimoniosactivity.viewmodel.InvScanViewModel

/**
 * Busca um item pelo código da etiqueta.
 * Ainda não lê pela câmera: o código é digitado (leitura por câmera fica como evolução).
 */
@Composable
fun EscanearScreen(
    vm: InvScanViewModel,
    onEncontrado: (Int) -> Unit,
    mostrarMensagem: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var codigo by rememberSaveable { mutableStateOf("") }

    fun buscar() {
        val encontrado = vm.buscarPorCodigo(codigo)
        if (encontrado != null) onEncontrado(encontrado.id)
        else mostrarMensagem("Nenhum patrimônio com o código \"${codigo.trim()}\".")
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(Dimens.ScreenPadding),
        verticalArrangement = Arrangement.spacedBy(Dimens.SectionSpacing),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TituloTela(
            titulo = "Escanear código",
            subtitulo = "Encontre um item pela etiqueta"
        )

        Surface(
            shape = MaterialTheme.shapes.large,
            color = MaterialTheme.colorScheme.primaryContainer
        ) {
            Icon(
                imageVector = Icons.Default.QrCodeScanner,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier
                    .padding(40.dp)
                    .size(96.dp)
            )
        }

        Text(
            text = "A leitura pela câmera ainda não está disponível. Digite o código impresso na etiqueta do item (exemplo: 2-004321).",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        OutlinedTextField(
            value = codigo,
            onValueChange = { codigo = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Código do patrimônio") },
            leadingIcon = { Icon(Icons.Default.QrCode, contentDescription = null) },
            singleLine = true,
            shape = RoundedCornerShape(Dimens.FieldRadius),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = { if (codigo.isNotBlank()) buscar() })
        )

        Button(
            onClick = { buscar() },
            enabled = codigo.isNotBlank(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.Search, contentDescription = null)
            Text("  Buscar item")
        }
    }
}
