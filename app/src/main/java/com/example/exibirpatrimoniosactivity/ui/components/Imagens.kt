package com.example.exibirpatrimoniosactivity.ui.components

import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/** Carrega e mostra uma imagem a partir de uma Uri (sem biblioteca externa). */
@Composable
fun UriImage(
    uri: String,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop
) {
    val context = LocalContext.current
    val bitmap by produceState<ImageBitmap?>(null, uri) {
        value = withContext(Dispatchers.IO) {
            try {
                val origem = Uri.parse(uri)
                val medidas = BitmapFactory.Options().apply { inJustDecodeBounds = true }
                context.contentResolver.openInputStream(origem)?.use {
                    BitmapFactory.decodeStream(it, null, medidas)
                }
                var reducao = 1
                while (medidas.outWidth / reducao > 1600 || medidas.outHeight / reducao > 1600) {
                    reducao *= 2
                }
                val opcoes = BitmapFactory.Options().apply { inSampleSize = reducao }
                context.contentResolver.openInputStream(origem)?.use {
                    BitmapFactory.decodeStream(it, null, opcoes)
                }?.asImageBitmap()
            } catch (e: Exception) {
                null
            }
        }
    }

    val imagem = bitmap
    if (imagem != null) {
        Image(
            bitmap = imagem,
            contentDescription = "Foto do item",
            modifier = modifier,
            contentScale = contentScale
        )
    } else {
        Box(modifier = modifier, contentAlignment = Alignment.Center) {
            Icon(Icons.Default.Image, contentDescription = null)
        }
    }
}

/**
 * Carrossel de fotos com setas « » dos dois lados (como no wireframe).
 * Se [onRemoverAtual] for informado, aparece um botão para remover a foto exibida.
 */
@Composable
fun CarrosselImagens(
    imagens: List<String>,
    modifier: Modifier = Modifier,
    onRemoverAtual: ((Int) -> Unit)? = null
) {
    var indice by rememberSaveable { mutableIntStateOf(0) }
    val atual = indice.coerceIn(0, (imagens.size - 1).coerceAtLeast(0))

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = { indice = if (atual > 0) atual - 1 else imagens.lastIndex },
            enabled = imagens.size > 1
        ) {
            Icon(Icons.Default.ChevronLeft, contentDescription = "Foto anterior")
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .aspectRatio(4f / 3f)
                .clip(MaterialTheme.shapes.medium)
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            if (imagens.isEmpty()) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.Default.Image,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "Sem fotos",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                UriImage(uri = imagens[atual], modifier = Modifier.fillMaxSize())
                Text(
                    text = "${atual + 1}/${imagens.size}",
                    color = Color.White,
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(8.dp)
                        .background(Color.Black.copy(alpha = 0.55f), CircleShape)
                        .padding(horizontal = 10.dp, vertical = 2.dp)
                )
                if (onRemoverAtual != null) {
                    IconButton(
                        onClick = { onRemoverAtual(atual) },
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(4.dp)
                            .background(Color.Black.copy(alpha = 0.55f), CircleShape)
                            .size(32.dp)
                    ) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "Remover foto",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }

        IconButton(
            onClick = { indice = if (atual < imagens.lastIndex) atual + 1 else 0 },
            enabled = imagens.size > 1
        ) {
            Icon(Icons.Default.ChevronRight, contentDescription = "Próxima foto")
        }
    }
}
