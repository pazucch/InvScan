package com.example.exibirpatrimoniosactivity.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.exibirpatrimoniosactivity.ui.components.InvScanBottomBar
import com.example.exibirpatrimoniosactivity.ui.components.InvScanTopBar
import com.example.exibirpatrimoniosactivity.ui.screens.ConfiguracoesScreen
import com.example.exibirpatrimoniosactivity.ui.screens.CriarItemScreen
import com.example.exibirpatrimoniosactivity.ui.screens.EscanearScreen
import com.example.exibirpatrimoniosactivity.ui.screens.InicioScreen
import com.example.exibirpatrimoniosactivity.ui.screens.LocaisScreen
import com.example.exibirpatrimoniosactivity.ui.screens.LocalDetalheScreen
import com.example.exibirpatrimoniosactivity.ui.screens.MenuScreen
import com.example.exibirpatrimoniosactivity.ui.screens.PatrimonioDetalheScreen
import com.example.exibirpatrimoniosactivity.ui.screens.PatrimoniosScreen
import com.example.exibirpatrimoniosactivity.ui.screens.SuporteScreen
import com.example.exibirpatrimoniosactivity.ui.screens.UsuarioScreen
import com.example.exibirpatrimoniosactivity.viewmodel.InvScanViewModel
import kotlinx.coroutines.launch

/** Rotas que são "raiz" de uma aba: nelas não aparece o botão de voltar. */
private val rotasPrincipais = setOf(Rotas.INICIO, Rotas.MENU, Rotas.PATRIMONIOS)

/** Diz a qual aba da barra inferior a tela atual pertence. */
private fun abaDaRota(rota: String?): String = when (rota) {
    null, Rotas.INICIO -> Rotas.INICIO
    Rotas.PATRIMONIOS, Rotas.PATRIMONIO_DETALHE, Rotas.CRIAR_ITEM -> Rotas.PATRIMONIOS
    else -> Rotas.MENU
}

/** Vai para a raiz de uma aba, sem empilhar telas repetidas. */
private fun NavHostController.irParaAba(rota: String) {
    navigate(rota) {
        popUpTo(Rotas.INICIO) { inclusive = (rota == Rotas.INICIO) }
        launchSingleTop = true
    }
}

@Composable
fun AppNavigation(
    vm: InvScanViewModel,
    navController: NavHostController = rememberNavController()
) {
    val entradaAtual by navController.currentBackStackEntryAsState()
    val rotaAtual = entradaAtual?.destination?.route

    val snackbarHostState = remember { SnackbarHostState() }
    val escopo = rememberCoroutineScope()
    val mostrarMensagem: (String) -> Unit = { mensagem ->
        escopo.launch {
            snackbarHostState.currentSnackbarData?.dismiss()
            snackbarHostState.showSnackbar(mensagem)
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            InvScanTopBar(
                mostrarVoltar = rotaAtual != null && rotaAtual !in rotasPrincipais,
                onVoltar = { navController.popBackStack() }
            )
        },
        bottomBar = {
            InvScanBottomBar(
                rotaDaAbaAtual = abaDaRota(rotaAtual),
                onSelecionar = { rota -> navController.irParaAba(rota) }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Rotas.INICIO,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Rotas.INICIO) {
                InicioScreen(
                    vm = vm,
                    onAbrirPatrimonio = { id -> navController.navigate(Rotas.patrimonioDetalhe(id)) },
                    onVerTodos = { navController.irParaAba(Rotas.PATRIMONIOS) },
                    onBuscarLocal = { navController.navigate(Rotas.LOCAIS) },
                    onEscanear = { navController.navigate(Rotas.ESCANEAR) }
                )
            }

            composable(Rotas.MENU) {
                MenuScreen(
                    onNavegar = { rota ->
                        if (rota in rotasPrincipais) navController.irParaAba(rota)
                        else navController.navigate(rota)
                    }
                )
            }

            composable(Rotas.PATRIMONIOS) {
                PatrimoniosScreen(
                    vm = vm,
                    onAbrirPatrimonio = { id -> navController.navigate(Rotas.patrimonioDetalhe(id)) },
                    onCriarItem = { navController.navigate(Rotas.CRIAR_ITEM) },
                    mostrarMensagem = mostrarMensagem
                )
            }

            composable(
                route = Rotas.PATRIMONIO_DETALHE,
                arguments = listOf(navArgument(Rotas.ARG_ID) { type = NavType.IntType })
            ) { entrada ->
                PatrimonioDetalheScreen(
                    vm = vm,
                    patrimonioId = entrada.arguments?.getInt(Rotas.ARG_ID),
                    onAbrirLocal = { id -> navController.navigate(Rotas.localDetalhe(id)) },
                    mostrarMensagem = mostrarMensagem
                )
            }

            composable(Rotas.CRIAR_ITEM) {
                CriarItemScreen(
                    vm = vm,
                    onItemCriado = { id ->
                        navController.navigate(Rotas.patrimonioDetalhe(id)) {
                            popUpTo(Rotas.CRIAR_ITEM) { inclusive = true }
                        }
                    },
                    mostrarMensagem = mostrarMensagem
                )
            }

            composable(Rotas.LOCAIS) {
                LocaisScreen(
                    vm = vm,
                    onAbrirLocal = { id -> navController.navigate(Rotas.localDetalhe(id)) },
                    mostrarMensagem = mostrarMensagem
                )
            }

            composable(
                route = Rotas.LOCAL_DETALHE,
                arguments = listOf(navArgument(Rotas.ARG_ID) { type = NavType.IntType })
            ) { entrada ->
                LocalDetalheScreen(
                    vm = vm,
                    localId = entrada.arguments?.getInt(Rotas.ARG_ID),
                    onAbrirPatrimonio = { id -> navController.navigate(Rotas.patrimonioDetalhe(id)) },
                    mostrarMensagem = mostrarMensagem
                )
            }

            composable(Rotas.ESCANEAR) {
                EscanearScreen(
                    vm = vm,
                    onEncontrado = { id ->
                        navController.navigate(Rotas.patrimonioDetalhe(id)) {
                            popUpTo(Rotas.ESCANEAR) { inclusive = true }
                        }
                    },
                    mostrarMensagem = mostrarMensagem
                )
            }

            composable(Rotas.USUARIO) {
                UsuarioScreen(vm = vm, mostrarMensagem = mostrarMensagem)
            }

            composable(Rotas.CONFIGURACOES) {
                ConfiguracoesScreen(
                    vm = vm,
                    onAbrirSuporte = { navController.navigate(Rotas.SUPORTE) }
                )
            }

            composable(Rotas.SUPORTE) {
                SuporteScreen(mostrarMensagem = mostrarMensagem)
            }
        }
    }
}
