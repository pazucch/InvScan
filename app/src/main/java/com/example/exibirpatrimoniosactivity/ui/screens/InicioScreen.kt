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
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.FactCheck
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.exibirpatrimoniosactivity.ui.components.AcaoGrande
import com.example.exibirpatrimoniosactivity.ui.components.InvScanCard
import com.example.exibirpatrimoniosactivity.ui.components.SearchField
import com.example.exibirpatrimoniosactivity.ui.components.StatCard
import com.example.exibirpatrimoniosactivity.ui.theme.Dimens
import com.example.exibirpatrimoniosactivity.viewmodel.InvScanViewModel

@Composable
fun InicioScreen(
    vm: InvScanViewModel,
    onAbrirPatrimonio: (Int) -> Unit,
    onVerTodos: () -> Unit,
    onBuscarLocal: () -> Unit,
    onEscanear: () -> Unit,
    modifier: Modifier = Modifier
) {
    var busca by rememberSaveable { mutableStateOf("") }
    val resultados = vm.pesquisar(busca)
    val nome = vm.perfil.nome.ifBlank { "usuário" }
    val conferidos = vm.patrimonios.count { it.conferido }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(Dimens.ScreenPadding),
        verticalArrangement = Arrangement.spacedBy(Dimens.SectionSpacing)
    ) {
        // Saudação
        Column {
            Text(
                text = "Olá, $nome!",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "Seja bem-vindo!",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // Resumo
        Row(horizontalArrangement = Arrangement.spacedBy(Dimens.ItemSpacing)) {
            StatCard(vm.patrimonios.size.toString(), "Itens", Icons.Default.Inventory2, Modifier.weight(1f))
            StatCard("$conferidos/${vm.patrimonios.size}", "Conferidos", Icons.Default.FactCheck, Modifier.weight(1f))
            StatCard(vm.locais.size.toString(), "Locais", Icons.Default.LocationOn, Modifier.weight(1f))
        }

        // Pesquisa + resultados
        SearchField(
            value = busca,
            onValueChange = { busca = it },
            placeholder = "Pesquisar patrimônio"
        )

        InvScanCard(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(vertical = 8.dp)) {
                if (resultados.isEmpty()) {
                    Text(
                        text = "Nenhum item encontrado",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(16.dp)
                    )
                }
                resultados.take(3).forEach { patrimonio ->
                    ListItem(
                        headlineContent = { Text(patrimonio.nome) },
                        supportingContent = { Text("Patrimônio ${patrimonio.codigo}") },
                        leadingContent = { Icon(patrimonio.tipo.icone, contentDescription = patrimonio.tipo.rotulo) },
                        trailingContent = { Icon(Icons.Default.ChevronRight, contentDescription = null) },
                        colors = ListItemDefaults.colors(containerColor = Color.Transparent),
                        modifier = Modifier.clickable { onAbrirPatrimonio(patrimonio.id) }
                    )
                }
                TextButton(
                    onClick = onVerTodos,
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(horizontal = 8.dp)
                ) {
                    Text("Ver todos (${vm.patrimonios.size})")
                }
            }
        }

        // Ações rápidas (botões inferiores do wireframe)
        Row(horizontalArrangement = Arrangement.spacedBy(Dimens.ItemSpacing)) {
            AcaoGrande(
                titulo = "Buscar por local",
                icone = Icons.Default.LocationOn,
                onClick = onBuscarLocal,
                modifier = Modifier.weight(1f)
            )
            AcaoGrande(
                titulo = "Escanear código",
                icone = Icons.Default.QrCodeScanner,
                onClick = onEscanear,
                modifier = Modifier.weight(1f)
            )
        }
    }
}
