package com.example.exibirpatrimoniosactivity.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Accessibility
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.HeadsetMic
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.exibirpatrimoniosactivity.ui.components.IconeCircular
import com.example.exibirpatrimoniosactivity.ui.components.InvScanCard
import com.example.exibirpatrimoniosactivity.ui.components.TituloTela
import com.example.exibirpatrimoniosactivity.ui.theme.Dimens
import com.example.exibirpatrimoniosactivity.viewmodel.InvScanViewModel

@Composable
fun ConfiguracoesScreen(
    vm: InvScanViewModel,
    onAbrirSuporte: () -> Unit,
    modifier: Modifier = Modifier
) {
    val config = vm.config

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(Dimens.ScreenPadding),
        verticalArrangement = Arrangement.spacedBy(Dimens.SectionSpacing)
    ) {
        TituloTela(titulo = "Configurações", subtitulo = "Personalize o InvScan")

        InvScanCard(modifier = Modifier.fillMaxWidth()) {
            Column {
                LinhaConfig(
                    icone = Icons.Default.Settings,
                    titulo = "Confirmar exclusões",
                    descricao = "Pedir confirmação antes de remover itens e locais",
                    onClick = { vm.atualizarConfig(config.copy(confirmarExclusao = !config.confirmarExclusao)) }
                ) {
                    Switch(
                        checked = config.confirmarExclusao,
                        onCheckedChange = { vm.atualizarConfig(config.copy(confirmarExclusao = it)) }
                    )
                }
                HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))

                LinhaConfig(
                    icone = Icons.Default.DarkMode,
                    titulo = "Tema escuro",
                    descricao = "Usa cores escuras em todo o app",
                    onClick = { vm.atualizarConfig(config.copy(temaEscuro = !config.temaEscuro)) }
                ) {
                    Switch(
                        checked = config.temaEscuro,
                        onCheckedChange = { vm.atualizarConfig(config.copy(temaEscuro = it)) }
                    )
                }
                HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))

                LinhaConfig(
                    icone = Icons.Default.Accessibility,
                    titulo = "Texto grande",
                    descricao = "Aumenta o tamanho das letras",
                    onClick = { vm.atualizarConfig(config.copy(textoGrande = !config.textoGrande)) }
                ) {
                    Switch(
                        checked = config.textoGrande,
                        onCheckedChange = { vm.atualizarConfig(config.copy(textoGrande = it)) }
                    )
                }
                HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))

                LinhaConfig(
                    icone = Icons.Default.HeadsetMic,
                    titulo = "Suporte",
                    descricao = "Dúvidas frequentes e contato",
                    onClick = onAbrirSuporte
                ) {
                    Icon(Icons.Default.ChevronRight, contentDescription = "Abrir suporte")
                }
            }
        }
    }
}

@Composable
private fun LinhaConfig(
    icone: ImageVector,
    titulo: String,
    descricao: String,
    onClick: () -> Unit,
    acaoFinal: @Composable () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        IconeCircular(icone, null)
        Column(modifier = Modifier.weight(1f)) {
            Text(titulo, style = MaterialTheme.typography.titleMedium)
            Text(
                text = descricao,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        acaoFinal()
    }
}
