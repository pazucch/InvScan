package com.example.exibirpatrimoniosactivity.ui.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.exibirpatrimoniosactivity.ui.components.FormTextField
import com.example.exibirpatrimoniosactivity.ui.components.InvScanCard
import com.example.exibirpatrimoniosactivity.ui.components.SecaoTitulo
import com.example.exibirpatrimoniosactivity.ui.components.TituloTela
import com.example.exibirpatrimoniosactivity.ui.theme.Dimens

private val perguntasFrequentes = listOf(
    "Como cadastro um novo patrimônio?" to
        "Abra a aba Itens e toque em Novo item (ou use Menu > Criar item). Informe nome, local e, se quiser, fotos e o código da etiqueta.",
    "Para que serve o campo Conferido?" to
        "Marca que o item foi verificado fisicamente no inventário. O progresso aparece na tela inicial e em cada local.",
    "Por que não consigo excluir um local?" to
        "Um local só pode ser removido quando não tem itens. Mude os itens de local (editando cada um) ou remova-os antes.",
    "Meus dados ficam salvos?" to
        "Nesta versão os dados ficam só na memória do app e são perdidos quando ele é fechado."
)

@Composable
fun SuporteScreen(
    mostrarMensagem: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var assunto by rememberSaveable { mutableStateOf("") }
    var mensagem by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(Dimens.ScreenPadding),
        verticalArrangement = Arrangement.spacedBy(Dimens.ItemSpacing)
    ) {
        TituloTela(titulo = "Suporte", subtitulo = "Estamos aqui para ajudar")

        SecaoTitulo("Dúvidas frequentes")
        perguntasFrequentes.forEach { (pergunta, resposta) ->
            PerguntaFrequente(pergunta, resposta)
        }

        SecaoTitulo("Fale com a equipe", Modifier.padding(top = 8.dp))
        InvScanCard(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(Dimens.ItemSpacing)
            ) {
                FormTextField(
                    value = assunto,
                    onValueChange = { assunto = it },
                    label = "Assunto"
                )
                FormTextField(
                    value = mensagem,
                    onValueChange = { mensagem = it },
                    label = "Mensagem",
                    minLines = 4
                )
                Button(
                    onClick = {
                        assunto = ""
                        mensagem = ""
                        mostrarMensagem("Solicitação enviada! Retornaremos em breve.")
                    },
                    enabled = assunto.isNotBlank() && mensagem.isNotBlank(),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.AutoMirrored.Filled.Send, contentDescription = null)
                    Text("  Enviar solicitação")
                }
            }
        }
    }
}

@Composable
private fun PerguntaFrequente(pergunta: String, resposta: String) {
    var aberta by rememberSaveable(pergunta) { mutableStateOf(false) }

    InvScanCard(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(),
        onClick = { aberta = !aberta }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = pergunta,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = if (aberta) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = if (aberta) "Recolher" else "Expandir"
                )
            }
            if (aberta) {
                Text(
                    text = resposta,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}
