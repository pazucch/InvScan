package com.example.exibirpatrimoniosactivity.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DesktopWindows
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Laptop
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.room3.Entity
import androidx.room3.PrimaryKey

enum class TipoPatrimonio(val rotulo: String, val icone: ImageVector) {
    NOTEBOOK("Notebook", Icons.Default.Laptop),
    COMPUTADOR("Computador", Icons.Default.DesktopWindows),
    OUTRO("Outro", Icons.Default.Inventory2)
}

/** Data class 1: item patrimoniado. Ligado a um [Local] pelo [localId]. Também é a tabela "patrimonios". */
@Entity(tableName = "patrimonios")
data class Patrimonio(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val codigo: String,
    val nome: String,
    val serie: String,
    val descricao: String,
    val tipo: TipoPatrimonio,
    val localId: Int,
    val prateleira: String,
    val conferido: Boolean = false,
    val imagens: String = ""    // o banco só guarda texto
)

/** Lista de fotos para as telas usarem. */
val Patrimonio.listaImagens: List<String>
    get() = if (imagens.isEmpty()) emptyList() else imagens.split("\n")

/** Junta a lista de fotos no texto que vai para o banco. */
fun List<String>.paraTexto(): String = joinToString("\n")
