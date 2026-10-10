package com.example.exibirpatrimoniosactivity.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.exibirpatrimoniosactivity.data.Local
import com.example.exibirpatrimoniosactivity.data.Patrimonio

/** Card padrão do app: fundo de superfície, borda suave e (opcionalmente) clicável. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InvScanCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    val cores = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    val elevacao = CardDefaults.cardElevation(defaultElevation = 2.dp)
    val borda = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))

    if (onClick != null) {
        Card(
            onClick = onClick,
            modifier = modifier,
            colors = cores,
            elevation = elevacao,
            border = borda,
            content = content
        )
    } else {
        Card(
            modifier = modifier,
            colors = cores,
            elevation = elevacao,
            border = borda,
            content = content
        )
    }
}

/** Ícone dentro de um círculo colorido. */
@Composable
fun IconeCircular(
    icone: ImageVector,
    descricao: String?,
    modifier: Modifier = Modifier,
    tamanho: Dp = 48.dp,
    container: Color = MaterialTheme.colorScheme.primaryContainer,
    conteudo: Color = MaterialTheme.colorScheme.onPrimaryContainer
) {
    Box(
        modifier = modifier
            .size(tamanho)
            .clip(CircleShape)
            .background(container),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icone,
            contentDescription = descricao,
            tint = conteudo,
            modifier = Modifier.size(tamanho * 0.5f)
        )
    }
}

/** Card de um patrimônio na lista: abre o detalhe, marca como conferido e remove. */
@Composable
fun PatrimonioCard(
    patrimonio: Patrimonio,
    nomeLocal: String,
    onClick: () -> Unit,
    onAlternarConferido: () -> Unit,
    onRemover: () -> Unit,
    modifier: Modifier = Modifier
) {
    InvScanCard(modifier = modifier.fillMaxWidth(), onClick = onClick) {
        Column(
            modifier = Modifier.padding(start = 16.dp, end = 8.dp, top = 16.dp, bottom = 4.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.padding(end = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                IconeCircular(patrimonio.tipo.icone, patrimonio.tipo.rotulo)
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = patrimonio.nome,
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "Patrimônio ${patrimonio.codigo}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.LocationOn,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = nomeLocal,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = patrimonio.conferido, onCheckedChange = { onAlternarConferido() })
                Text("Conferido", style = MaterialTheme.typography.bodyMedium)
                Spacer(Modifier.weight(1f))
                IconButton(onClick = onRemover) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Remover ${patrimonio.nome}",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

/** Card de um local na lista de locais. */
@Composable
fun LocalCard(
    local: Local,
    quantidadeItens: Int,
    onClick: () -> Unit,
    onRemover: () -> Unit,
    modifier: Modifier = Modifier
) {
    InvScanCard(modifier = modifier.fillMaxWidth(), onClick = onClick) {
        Row(
            modifier = Modifier.padding(start = 16.dp, top = 12.dp, bottom = 12.dp, end = 4.dp),
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
                Text(local.nome, style = MaterialTheme.typography.titleMedium)
                if (local.descricao.isNotBlank()) {
                    Text(
                        text = local.descricao,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Text(
                    text = if (quantidadeItens == 1) "1 item" else "$quantidadeItens itens",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
            IconButton(onClick = onRemover) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Remover ${local.nome}",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

/** Número em destaque com legenda (usado no resumo da tela inicial). */
@Composable
fun StatCard(
    valor: String,
    rotulo: String,
    icone: ImageVector,
    modifier: Modifier = Modifier
) {
    InvScanCard(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 14.dp, horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(icone, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Text(valor, style = MaterialTheme.typography.titleLarge)
            Text(
                text = rotulo,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

/** Botão grande em forma de card (ícone + título + detalhe opcional). */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AcaoGrande(
    titulo: String,
    icone: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    detalhe: String? = null
) {
    Card(
        onClick = onClick,
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 120.dp)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                icone,
                contentDescription = null,
                modifier = Modifier.size(44.dp),
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Spacer(Modifier.padding(top = 8.dp))
            Text(
                text = titulo,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                textAlign = TextAlign.Center
            )
            if (detalhe != null) {
                Text(
                    text = detalhe,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.75f),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

/** Card de uma funcionalidade na grade da tela Menu. */
@Composable
fun FuncionalidadeCard(
    titulo: String,
    descricao: String,
    icone: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    InvScanCard(modifier = modifier, onClick = onClick) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 150.dp)
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            IconeCircular(icone, null)
            Column {
                Text(titulo, style = MaterialTheme.typography.titleMedium)
                Text(
                    text = descricao,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun EstadoVazio(
    icone: ImageVector,
    texto: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            icone,
            contentDescription = null,
            modifier = Modifier.size(48.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
        )
        Text(
            text = texto,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun ConfirmarExclusaoDialog(
    titulo: String,
    mensagem: String,
    onConfirmar: () -> Unit,
    onCancelar: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onCancelar,
        title = { Text(titulo) },
        text = { Text(mensagem) },
        confirmButton = {
            TextButton(onClick = onConfirmar) {
                Text("Excluir", color = MaterialTheme.colorScheme.error)
            }
        },
        dismissButton = {
            TextButton(onClick = onCancelar) { Text("Cancelar") }
        }
    )
}
