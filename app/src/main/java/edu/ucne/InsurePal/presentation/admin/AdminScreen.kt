package edu.ucne.InsurePal.presentation.admin
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import edu.ucne.InsurePal.presentation.home.uiModels.LifePolicyUi
import edu.ucne.InsurePal.presentation.home.uiModels.PolicyUiModel
import edu.ucne.InsurePal.presentation.home.uiModels.VehiclePolicyUi


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScreen(
    viewModel: AdminViewModel = hiltViewModel(),
    onLogout: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    if (state.isDetailVisible && state.selectedPolicy != null) {
        PolicyDetailDialog(
            policy = state.selectedPolicy!!,
            onDismiss = { viewModel.onEvent(AdminEvent.OnDismissDetail) }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Panel Administrativo", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                actions = {
                    IconButton(onClick = { viewModel.onEvent(AdminEvent.LoadDashboard) }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Recargar")
                    }
                    IconButton(onClick = {
                        viewModel.onEvent(AdminEvent.OnLogout)
                        onLogout()
                    }) {
                        Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = "Salir")
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (state.policies.isEmpty()) {
                EmptyState(modifier = Modifier.align(Alignment.Center))
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item { DashboardSummary(state.policies) }

                    item {
                        Text(
                            "Pólizas Recientes",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                        )
                    }

                    items(state.policies) { policy ->
                        PolicyCardItem(
                            policy = policy,
                            onClick = { viewModel.onEvent(AdminEvent.OnSelectPolicy(policy.id)) }
                        )
                    }
                }
            }

            // Error Snackbar/Text
            if (state.errorMessage != null) {
                Text(
                    text = state.errorMessage!!,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.BottomCenter).padding(16.dp)
                )
            }
        }
    }
}

@Composable
fun PolicyCardItem(policy: PolicyUiModel, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icono con fondo circular del color del tipo de seguro
            Surface(
                shape = CircleShape,
                color = policy.color.copy(alpha = 0.1f),
                modifier = Modifier.size(48.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(policy.icon, contentDescription = null, tint = policy.color)
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = policy.title,
                    style = MaterialTheme.typography.labelSmall,
                    color = policy.color,
                    fontWeight = FontWeight.Bold
                )

                // Generamos la descripción localmente según el tipo
                val description = when(policy) {
                    is VehiclePolicyUi -> "${policy.vehicleModel} - ${policy.plate}"
                    is LifePolicyUi -> "${policy.insuredName} - $${policy.coverageAmount}"
                    // Fallback para seguridad si agregas más tipos en el futuro
                    else -> policy.id
                }

                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
            }

            StatusBadge(status = policy.status)
        }
    }
}

@Composable
fun StatusBadge(status: String) {
    val color = if (status == "Activo") Color(0xFF4CAF50) else Color(0xFFFF9800)
    Surface(
        color = color.copy(alpha = 0.15f),
        shape = RoundedCornerShape(50),
    ) {
        Text(
            text = status,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelSmall,
            color = color,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun DashboardSummary(policies: List<PolicyUiModel>) {
    val vehiclesCount = policies.count { it is VehiclePolicyUi }
    val lifeCount = policies.count { it is LifePolicyUi }

    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        SummaryCard("Vehículos", vehiclesCount, Icons.Default.DirectionsCar, Color(0xFF2196F3), Modifier.weight(1f))
        SummaryCard("Vida", lifeCount, Icons.Default.Favorite, Color(0xFFE91E63), Modifier.weight(1f))
    }
}

@Composable
fun SummaryCard(title: String, count: Int, icon: ImageVector, color: Color, modifier: Modifier) {
    Card(modifier = modifier, colors = CardDefaults.cardColors(containerColor = Color.White), elevation = CardDefaults.cardElevation(2.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Icon(icon, contentDescription = null, tint = color)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = count.toString(), style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Text(text = title, style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
        }
    }
}

@Composable
fun EmptyState(modifier: Modifier = Modifier) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(Icons.Default.Search, contentDescription = null, modifier = Modifier.size(64.dp), tint = Color.LightGray)
        Text("No se encontraron registros", color = Color.Gray)
    }
}

@Composable
fun PolicyDetailDialog(policy: PolicyUiModel, onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                // Cabecera del Dialog
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(policy.icon, contentDescription = null, tint = policy.color)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Detalle ${policy.title}", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                }
                HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

                // Campos Comunes
                DetailRow("ID Ref:", policy.id)
                DetailRow("Estado:", policy.status)

                when (policy) {
                    is VehiclePolicyUi -> {
                        DetailRow("Vehículo:", policy.vehicleModel)
                        DetailRow("Placa:", policy.plate)
                    }
                    is LifePolicyUi -> {
                        DetailRow("Asegurado:", policy.insuredName)
                        DetailRow("Cobertura:", "$${policy.coverageAmount}")
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
                Button(onClick = onDismiss, modifier = Modifier.fillMaxWidth()) {
                    Text("Cerrar")
                }
            }
        }
    }
}

@Composable
fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontWeight = FontWeight.Bold, color = Color.Gray)
        Text(text = value, fontWeight = FontWeight.Medium)
    }
}