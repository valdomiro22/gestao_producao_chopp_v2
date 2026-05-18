//package com.santos.valdomiro.gestaoproducaochopp.homescreen.components
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.border
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material3.CircularProgressIndicator
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.collectAsState
//import androidx.compose.runtime.getValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
//import androidx.hilt.navigation.compose.hiltViewModel
//import com.santos.valdomiro.gestaodeproducaodechoppandroidstudio.common.components.ErroComponent
//import com.santos.valdomiro.gestaodeproducaodechoppandroidstudio.common.state.UiState
//import com.santos.valdomiro.gestaodeproducaodechoppandroidstudio.features.barril.presentation.screens.buscarbarril.BuscarBarrilViewModel
//import com.santos.valdomiro.gestaodeproducaodechoppandroidstudio.features.homescreen.LinhaChaveValor
//import java.text.DecimalFormat
//
//@Composable
//fun ControleDoBuffer(
//    onClick: () -> Unit,
//    quantidadePendente: Int,
//    tipoBarril: String,
//    barrilId: String,
////    buscarBarrilViewModel: GetO = hiltViewModel()
//) {
//    val barrilState by buscarBarrilViewModel.uiState.collectAsState()
//    val formatador = DecimalFormat("#.##")
//    val nivelMaxBuffer = 30
//
//    LaunchedEffect(Unit) {
//        buscarBarrilViewModel.buscarBarril(barrilId)
//    }
//
//    when {
//        barrilState.isSuccess -> {
//            val barril = (barrilState as? UiState.Success)?.data ?: run {
//                ErroComponent("Barril não encontrado")
//                return
//            }
//
//            val vlBarril = barril.volume
//            val vlNecessario = vlBarril * quantidadePendente / 100.0
//
//            Box {
//                Column(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(12.dp))
//                        .background(Color.White, RoundedCornerShape(12.dp))
//                        // O clickable deve vir antes do padding para a área de toque ser completa
//                        .clickable { onClick() }
//                        .padding(16.dp),
//                    horizontalAlignment = Alignment.CenterHorizontally,
//                    verticalArrangement = Arrangement.spacedBy(8.dp),
//                ) {
//                    LinhaChaveValor(
//                        chave = "Volume do Barril:",
//                        valor = barril.nome,
//                        modifier = Modifier.fillMaxWidth()
//                    )
//
//                    LinhaChaveValor(
//                        chave = "Volume necessário:",
//                        valor = "${formatador.format(vlNecessario)} hl",
//                        modifier = Modifier.fillMaxWidth()
//                    )
//
//                    if (vlNecessario >= nivelMaxBuffer) BufferOk() else AlertaBuffer()
//
//                }
//            }
//        }
//
//        barrilState.isLoading -> {
//            Box(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .padding(top = 16.dp), contentAlignment = Alignment.Center
//            ) {
//                CircularProgressIndicator()
//            }
//        }
//
//        barrilState.isError -> {
//            ErroComponent(
//                mensagem = (barrilState as? UiState.Error)?.message
//                    ?: "Erro desconhecido ao buscar produção"
//            )
//        }
//    }
//}
//
//@Composable
//private fun AlertaBuffer(
//    modifier: Modifier = Modifier
//) {
//    Box(
//        modifier = Modifier
//            .fillMaxWidth()
//            .background(Color(0xFFFFEBEE), RoundedCornerShape(8.dp))
//            .padding(vertical = 20.dp),
//        contentAlignment = Alignment.Center
//    ) {
//        Text(
//            text = "VERIFIQUE O BUFFER",
//            color = Color(0xFFB71C1C),
//            style = MaterialTheme.typography.bodySmall,
//            fontWeight = FontWeight.Bold
//        )
//    }
//}
//
//@Composable
//private fun BufferOk(
//    modifier: Modifier = Modifier
//) {
//    Box(
//        modifier = Modifier
//            .fillMaxWidth()
//            .background(Color(0xFFEBFFEB), RoundedCornerShape(8.dp))
//            .padding(vertical = 20.dp),
//        contentAlignment = Alignment.Center
//    ) {
//        Text(
//            text = "BUFFER OK",
//            color = Color(0xFF21A74E),
//            style = MaterialTheme.typography.bodySmall,
//            fontWeight = FontWeight.Bold
//        )
//    }
//}
