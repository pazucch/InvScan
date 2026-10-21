package com.example.exibirpatrimoniosactivity.data

import androidx.room3.Entity
import androidx.room3.PrimaryKey

/** Data class 2: local onde os patrimônios ficam guardados. Também é a tabela "locais". */
@Entity(tableName = "locais")
data class Local(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nome: String,
    val descricao: String
)
