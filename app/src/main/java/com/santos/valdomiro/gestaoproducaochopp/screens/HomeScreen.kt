package com.santos.valdomiro.gestaoproducaochopp.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.santos.valdomiro.gestaoproducaochopp.common.AppDrawer
import com.santos.valdomiro.gestaoproducaochopp.navigation.LocalNavController
import com.santos.valdomiro.gestaoproducaochopp.navigation.Route

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onOpenDrawer: () -> Unit
) {

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val navController = LocalNavController.current

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            AppDrawer(
                selectedRoute = "",
                onItemClick = {},
                onLogoutClick = {}
            )
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("HomeScreen") },
                    windowInsets = WindowInsets(0),
                    navigationIcon = {
                        IconButton(
                            onClick = onOpenDrawer
                        ) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Abrir menu"
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        navigationIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        actionIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                )
            },
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "HomeScreen",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = {
                        navController.navigate(Route.ListaBarrisRoute.route)
                    }
                ) {
                    Text("Lista de Barris")
                }
                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        navController.navigate(Route.ListaProdutosRoute.route)
                    }
                ) {
                    Text("Lista de Produtos")
                }
                Spacer(modifier = Modifier.height(16.dp))

            }
        }
    }
}