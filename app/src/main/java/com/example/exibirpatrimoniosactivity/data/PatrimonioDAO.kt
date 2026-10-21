package com.example.exibirpatrimoniosactivity.data

import androidx.room3.Dao
import androidx.room3.Delete
import androidx.room3.Insert
import androidx.room3.Query
import androidx.room3.Update

@Dao
interface PatrimonioDAO {

    @Insert
    suspend fun inserir(patrimonio: Patrimonio)

    @Update
    suspend fun atualizar(patrimonio: Patrimonio)

    @Delete
    suspend fun deletar(patrimonio: Patrimonio)

    @Query("SELECT * FROM patrimonios ORDER BY id DESC")
    suspend fun buscarPatrimonios(): List<Patrimonio>

}
