package com.example.exibirpatrimoniosactivity.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DesktopWindows
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Laptop
import androidx.compose.ui.graphics.vector.ImageVector

enum class TipoPatrimonio(val rotulo: String, val icone: ImageVector) {
    NOTEBOOK("Notebook", Icons.Default.Laptop),
    COMPUTADOR("Computador", Icons.Default.DesktopWindows),
    OUTRO("Outro", Icons.Default.Inventory2)
}

/** Data class 1: item patrimoniado. Ligado a um [Local] pelo [localId]. */
data class Patrimonio(
    val id: Int,
    val codigo: String,
    val nome: String,
    val serie: String,
    val descricao: String,
    val tipo: TipoPatrimonio,
    val localId: Int,
    val prateleira: String,
    val conferido: Boolean = false,
    val imagens: List<String> = emptyList()
)
