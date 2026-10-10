package com.example.exibirpatrimoniosactivity.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.exibirpatrimoniosactivity.data.Local
import com.example.exibirpatrimoniosactivity.ui.theme.Dimens

/** Título de cada tela, com subtítulo e uma ação opcional à direita. */
@Composable
fun TituloTela(
    titulo: String,
    modifier: Modifier = Modifier,
    subtitulo: String? = null,
    acao: (@Composable () -> Unit)? = null
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = titulo,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
            if (subtitulo != null) {
                Text(
                    text = subtitulo,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        acao?.invoke()
    }
}

@Composable
fun SecaoTitulo(texto: String, modifier: Modifier = Modifier) {
    Text(
        text = texto,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onBackground,
        modifier = modifier
    )
}

/** Campo de texto padrão do app (cantos arredondados, ícone opcional). */
@Composable
fun FormTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    leadingIcon: ImageVector? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Next,
    minLines: Int = 1,
    isError: Boolean = false,
    supportingText: String? = null
) {
    val iconeInicial: (@Composable () -> Unit)? =
        if (leadingIcon != null) {
            { Icon(leadingIcon, contentDescription = null) }
        } else null

    val textoApoio: (@Composable () -> Unit)? =
        if (supportingText != null) {
            { Text(supportingText) }
        } else null

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth(),
        label = { Text(label) },
        leadingIcon = iconeInicial,
        supportingText = textoApoio,
        isError = isError,
        singleLine = minLines == 1,
        minLines = minLines,
        shape = RoundedCornerShape(Dimens.FieldRadius),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = imeAction)
    )
}

/** Campo de pesquisa em formato de pílula, com botão para limpar. */
@Composable
fun SearchField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier
) {
    val limpar: (@Composable () -> Unit)? =
        if (value.isNotEmpty()) {
            {
                IconButton(onClick = { onValueChange("") }) {
                    Icon(Icons.Default.Close, contentDescription = "Limpar pesquisa")
                }
            }
        } else null

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth(),
        placeholder = { Text(placeholder) },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Pesquisar") },
        trailingIcon = limpar,
        singleLine = true,
        shape = RoundedCornerShape(28.dp)
    )
}

/** Mostra um dado com título pequeno em cima e valor embaixo (somente leitura). */
@Composable
fun InfoCampo(
    rotulo: String,
    valor: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.small,
        color = MaterialTheme.colorScheme.surfaceContainer
    ) {
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)) {
            Text(
                text = rotulo,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = valor.ifBlank { "—" },
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

/** Menu suspenso para escolher o [Local] de um item. */
@Composable
fun SeletorLocal(
    locais: List<Local>,
    selecionadoId: Int?,
    onSelecionar: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var expandido by remember { mutableStateOf(false) }
    val nomeSelecionado = locais.firstOrNull { it.id == selecionadoId }?.nome ?: ""

    Box(modifier = modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = nomeSelecionado,
            onValueChange = {},
            readOnly = true,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Local") },
            leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null) },
            trailingIcon = { Icon(Icons.Default.ArrowDropDown, contentDescription = "Escolher local") },
            shape = RoundedCornerShape(Dimens.FieldRadius)
        )
        // Camada transparente que captura o toque (o campo somente leitura não repassa cliques)
        Box(
            modifier = Modifier
                .matchParentSize()
                .clickable(enabled = locais.isNotEmpty()) { expandido = true }
        )
        DropdownMenu(expanded = expandido, onDismissRequest = { expandido = false }) {
            locais.forEach { local ->
                DropdownMenuItem(
                    text = { Text(local.nome) },
                    onClick = {
                        onSelecionar(local.id)
                        expandido = false
                    }
                )
            }
        }
    }
}
