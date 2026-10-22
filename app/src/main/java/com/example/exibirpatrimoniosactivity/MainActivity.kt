package com.example.exibirpatrimoniosactivity

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.exibirpatrimoniosactivity.navigation.AppNavigation
import com.example.exibirpatrimoniosactivity.ui.theme.ExibirPatrimoniosActivityTheme
import com.example.exibirpatrimoniosactivity.viewmodel.InvScanViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // As barras do app são sempre azul-marinho, então os ícones do sistema ficam claros.
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.dark(Color.TRANSPARENT)
        )

        setContent {
            val vm: InvScanViewModel = viewModel()
            ExibirPatrimoniosActivityTheme(
                temaEscuro = vm.config.temaEscuro,
                textoGrande = vm.config.textoGrande
            ) {
                AppNavigation(vm)
            }
        }
    }
}
