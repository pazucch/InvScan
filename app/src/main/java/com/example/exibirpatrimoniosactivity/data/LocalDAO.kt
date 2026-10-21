package com.example.exibirpatrimoniosactivity.data

import androidx.room3.Dao
import androidx.room3.Delete
import androidx.room3.Insert
import androidx.room3.Query
import androidx.room3.Update

@Dao
interface LocalDAO {

    @Insert
    suspend fun inserir(local: Local)

    @Update
    suspend fun atualizar(local: Local)

    @Delete
    suspend fun deletar(local: Local)

    @Query("SELECT * FROM locais")
    suspend fun buscarLocais(): List<Local>

}
