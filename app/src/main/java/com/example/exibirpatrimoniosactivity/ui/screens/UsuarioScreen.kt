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
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.exibirpatrimoniosactivity.ui.components.FormTextField
import com.example.exibirpatrimoniosactivity.ui.components.IconeCircular
import com.example.exibirpatrimoniosactivity.ui.components.InvScanCard
import com.example.exibirpatrimoniosactivity.ui.components.SecaoTitulo
import com.example.exibirpatrimoniosactivity.ui.components.TituloTela
import com.example.exibirpatrimoniosactivity.ui.theme.Dimens
import com.example.exibirpatrimoniosactivity.viewmodel.InvScanViewModel

/** Perfil editável (cada letra digitada já atualiza o app) + conquistas calculadas pelo uso. */
@Composable
fun UsuarioScreen(
    vm: InvScanViewModel,
    mostrarMensagem: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val perfil = vm.perfil

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(Dimens.ScreenPadding),
        verticalArrangement = Arrangement.spacedBy(Dimens.SectionSpacing)
    ) {
        TituloTela(titulo = "Usuário", subtitulo = "Seus dados e conquistas")

        InvScanCard(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(Dimens.ItemSpacing),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                IconeCircular(Icons.Default.Person, "Foto do perfil", tamanho = 88.dp)
                FormTextField(
                    value = perfil.nome,
                    onValueChange = { vm.atualizarPerfil(perfil.copy(nome = it)) },
                    label = "Nome",
                    leadingIcon = Icons.Default.Person
                )
                FormTextField(
                    value = perfil.email,
                    onValueChange = { vm.atualizarPerfil(perfil.copy(email = it)) },
                    label = "E-mail",
                    leadingIcon = Icons.Default.Email,
                    keyboardType = KeyboardType.Email
                )
                FormTextField(
                    value = perfil.telefone,
                    onValueChange = { vm.atualizarPerfil(perfil.copy(telefone = it)) },
                    label = "Telefone",
                    leadingIcon = Icons.Default.Phone,
                    keyboardType = KeyboardType.Phone
                )
            }
        }

        InvScanCard(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(Dimens.ItemSpacing)
            ) {
                SecaoTitulo("Sobre você")
                FormTextField(
                    value = perfil.descricao,
                    onValueChange = { vm.atualizarPerfil(perfil.copy(descricao = it)) },
                    label = "Descrição",
                    leadingIcon = Icons.Default.Edit,
                    minLines = 3
                )
            }
        }

        InvScanCard(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(Dimens.ItemSpacing)
            ) {
                SecaoTitulo("Localização")
                FormTextField(
                    value = perfil.endereco,
                    onValueChange = { vm.atualizarPerfil(perfil.copy(endereco = it)) },
                    label = "Endereço",
                    leadingIcon = Icons.Default.LocationOn
                )
                FormTextField(
                    value = perfil.cidade,
                    onValueChange = { vm.atualizarPerfil(perfil.copy(cidade = it)) },
                    label = "Cidade",
                    leadingIcon = Icons.Default.LocationOn
                )
            }
        }

        // Conquistas (os quatro selos do wireframe)
        Column(verticalArrangement = Arrangement.spacedBy(Dimens.ItemSpacing)) {
            SecaoTitulo("Conquistas")
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                vm.conquistas().forEach { conquista ->
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .clip(MaterialTheme.shapes.small)
                            .clickable {
                                val situacao = if (conquista.desbloqueada) "Desbloqueada!" else "Ainda bloqueada."
                                mostrarMensagem("${conquista.titulo}: ${conquista.descricao} $situacao")
                            }
                            .padding(vertical = 8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        IconeCircular(
                            icone = Icons.Default.WorkspacePremium,
                            descricao = conquista.titulo,
                            tamanho = 56.dp,
                            container = if (conquista.desbloqueada) MaterialTheme.colorScheme.secondaryContainer
                            else MaterialTheme.colorScheme.surfaceVariant,
                            conteudo = if (conquista.desbloqueada) MaterialTheme.colorScheme.secondary
                            else MaterialTheme.colorScheme.outline
                        )
                        Text(
                            text = conquista.titulo,
                            style = MaterialTheme.typography.labelMedium,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.alpha(if (conquista.desbloqueada) 1f else 0.6f)
                        )
                    }
                }
            }
        }
    }
}
