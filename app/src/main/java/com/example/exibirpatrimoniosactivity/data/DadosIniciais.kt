package com.example.exibirpatrimoniosactivity.data

/** Dados de exemplo (só existem na memória). O item 1 veio da tela original; o resto é fictício. */
object DadosIniciais {

    val locais = listOf(
        Local(1, "Armazém A", "Equipamentos de TI em uso e em reserva"),
        Local(2, "Armazém B", "Reserva técnica e peças"),
        Local(3, "Armazém C", "Estações de trabalho")
    )

    val patrimonios = listOf(
        Patrimonio(
            id = 1,
            codigo = "2-004321",
            nome = "Notebook Dell i7",
            serie = "190843792432",
            descricao = "Notebook Dell i7 de 13º Geração com 16 gb ram, 1tb de armazenamento e uma rtx 4050 com mais 8gb ram.",
            tipo = TipoPatrimonio.NOTEBOOK,
            localId = 1,
            prateleira = "Corredor 3, Seção D, Nível 2",
            conferido = true
        ),
        Patrimonio(
            id = 2,
            codigo = "2-004322",
            nome = "Computador Desktop",
            serie = "190843792433",
            descricao = "Computador desktop com 16 gb de RAM e SSD de 512 gb.",
            tipo = TipoPatrimonio.COMPUTADOR,
            localId = 1,
            prateleira = "Corredor 4, Seção B, Nível 1"
        ),
        Patrimonio(
            id = 3,
            codigo = "2-004323",
            nome = "Notebook Lenovo i5",
            serie = "190843792434",
            descricao = "Notebook Lenovo i5 com 8 gb de RAM e SSD de 256 gb.",
            tipo = TipoPatrimonio.NOTEBOOK,
            localId = 2,
            prateleira = "Corredor 1, Seção A, Nível 3",
            conferido = true
        ),
        Patrimonio(
            id = 4,
            codigo = "2-004324",
            nome = "Notebook Acer Ryzen 5",
            serie = "190843792435",
            descricao = "Notebook Acer Ryzen 5 com 16 gb de RAM e SSD de 512 gb.",
            tipo = TipoPatrimonio.NOTEBOOK,
            localId = 2,
            prateleira = "Corredor 2, Seção C, Nível 2"
        ),
        Patrimonio(
            id = 5,
            codigo = "2-004325",
            nome = "Computador All-in-One",
            serie = "190843792436",
            descricao = "All-in-one com tela de 24 polegadas, 8 gb de RAM e SSD de 256 gb.",
            tipo = TipoPatrimonio.COMPUTADOR,
            localId = 3,
            prateleira = "Corredor 1, Seção E, Nível 1"
        )
    )
}
