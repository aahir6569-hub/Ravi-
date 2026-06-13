package com.example.ui.components

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.Cached
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.ui.theme.BurnishedGold
import com.example.ui.theme.MatteCharcoalBlack
import com.example.ui.theme.PolishedPlatinum
import com.example.ui.theme.WarmCharcoalOnyx

data class PresetTexture(
    val name: String,
    val category: String,
    val url: String,
    val hexColorRepresentation: Color
)

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun CameraSimulator(
    onPhotoCaptured: (url: String) -> Unit,
    onDismiss: () -> Unit
) {
    val textures = remember {
        listOf(
            PresetTexture("Calacatta Gold", "Calacatta Marble", "https://images.unsplash.com/photo-1618221195710-dd6b41faaea6?auto=format&fit=crop&w=800&q=80", Color(0xFFDFDCD3)),
            PresetTexture("Charcoal Oak", "Smoked Oak", "https://images.unsplash.com/photo-1540518614846-7eded433c457?auto=format&fit=crop&w=800&q=80", Color(0xFF2E2E2D)),
            PresetTexture("Silver Travertine", "Honed Travertine", "https://images.unsplash.com/photo-1600585154526-990dced4db0d?auto=format&fit=crop&w=800&q=80", Color(0xFFAEA89F)),
            PresetTexture("Champagne Brass", "Polished Brass", "https://images.unsplash.com/photo-1600607687939-ce8a6c25118c?auto=format&fit=crop&w=800&q=80", Color(0xFFD4C29D)),
            PresetTexture("Obsidian Glass", "Other", "https://images.unsplash.com/photo-1513519245088-0e12902e5a38?auto=format&fit=crop&w=800&q=80", Color(0xFF101011))
        )
    }

    var selectedIdx by remember { mutableStateOf(0) }
    var activeLens by remember { mutableStateOf("50mm f/1.2") }
    val lenses = listOf("24mm Wide", "35mm Prime", "50mm f/1.2", "85mm Portrait")
    var isFlashOn by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = MatteCharcoalBlack),
        shape = RoundedCornerShape(24.dp),
        border = BorderStroke(1.dp, BurnishedGold.copy(alpha = 0.3f))
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header controls
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "LEICA SL2 HARDWARE FEED",
                    style = MaterialTheme.typography.labelMedium,
                    color = PolishedPlatinum
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { isFlashOn = !isFlashOn }) {
                        Icon(
                            Icons.Default.FlashOn,
                            contentDescription = "Flash",
                            tint = if (isFlashOn) BurnishedGold else PolishedPlatinum.copy(alpha = 0.5f)
                        )
                    }
                    Text(
                        "ISO 100",
                        style = MaterialTheme.typography.labelSmall,
                        color = BurnishedGold,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Camera Viewfinder Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.Black)
                    .border(1.dp, BurnishedGold.copy(alpha = 0.4f), RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                // Live visual image matching texture Selection
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(textures[selectedIdx].url)
                        .crossfade(true)
                        .build(),
                    contentDescription = "Camera texture feed view",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Classic camera graticules overlay
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val w = size.width
                    val h = size.height

                    // Viewfinder Center Reticle
                    drawCircle(
                        color = BurnishedGold,
                        radius = 20f,
                        center = Offset(w / 2, h / 2),
                        style = Stroke(width = 1.5.dp.toPx())
                    )
                    drawCircle(
                        color = Color.Black.copy(alpha = 0.2f),
                        radius = 4f,
                        center = Offset(w / 2, h / 2)
                    )

                    // Rule of Thirds Rule lines (dotted/thin)
                    drawLine(
                        color = Color.White.copy(alpha = 0.25f),
                        start = Offset(w / 3, 0f),
                        end = Offset(w / 3, h),
                        strokeWidth = 1f
                    )
                    drawLine(
                        color = Color.White.copy(alpha = 0.25f),
                        start = Offset((w * 2) / 3, 0f),
                        end = Offset((w * 2) / 3, h),
                        strokeWidth = 1f
                    )
                    drawLine(
                        color = Color.White.copy(alpha = 0.25f),
                        start = Offset(0f, h / 3),
                        end = Offset(w, h / 3),
                        strokeWidth = 1f
                    )
                    drawLine(
                        color = Color.White.copy(alpha = 0.25f),
                        start = Offset(0f, (h * 2) / 3),
                        end = Offset(w, (h * 2) / 3),
                        strokeWidth = 1f
                    )

                    // Corner framers
                    val fl = 40f
                    // Top-Left
                    drawLine(Color.White.copy(alpha = 0.6f), Offset(15f, 15f), Offset(15f + fl, 15f), strokeWidth = 2f)
                    drawLine(Color.White.copy(alpha = 0.6f), Offset(15f, 15f), Offset(15f, 15f + fl), strokeWidth = 2f)
                    // Top-Right
                    drawLine(Color.White.copy(alpha = 0.6f), Offset(w - 15f, 15f), Offset(w - 15f - fl, 15f), strokeWidth = 2f)
                    drawLine(Color.White.copy(alpha = 0.6f), Offset(w - 15f, 15f), Offset(w - 15f, 15f + fl), strokeWidth = 2f)
                    // Bottom-Left
                    drawLine(Color.White.copy(alpha = 0.6f), Offset(15f, h - 15f), Offset(15f + fl, h - 15f), strokeWidth = 2f)
                    drawLine(Color.White.copy(alpha = 0.6f), Offset(15f, h - 15f), Offset(15f, h - 15f - fl), strokeWidth = 2f)
                    // Bottom-Right
                    drawLine(Color.White.copy(alpha = 0.6f), Offset(w - 15f, h - 15f), Offset(w - 15f - fl, h - 15f), strokeWidth = 2f)
                    drawLine(Color.White.copy(alpha = 0.6f), Offset(w - 15f, h - 15f), Offset(w - 15f, h - 15f - fl), strokeWidth = 2f)
                }

                // Shutter snapshot flash overlay
                var isPressedByUI by remember { mutableStateOf(false) }
                // Active parameters label
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(12.dp)
                        .background(Color.Black.copy(alpha = 0.7f), RoundedCornerShape(4.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "FOCUS LOCK: ${textures[selectedIdx].name.uppercase()}",
                        color = BurnishedGold,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Lens selectors (horizontal chip line)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                lenses.forEach { lens ->
                    val isSelected = activeLens == lens
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isSelected) BurnishedGold else WarmCharcoalOnyx)
                            .border(1.dp, if (isSelected) PolishedPlatinum else Color.Transparent, RoundedCornerShape(8.dp))
                            .clickable { activeLens = lens }
                            .padding(horizontal = 8.dp, vertical = 6.dp)
                    ) {
                        Text(
                            lens,
                            color = if (isSelected) MatteCharcoalBlack else PolishedPlatinum,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Texture presets chooser
            Text(
                "PRESTIGE DEPOSIT TEXTURES:",
                color = PolishedPlatinum.copy(alpha = 0.6f),
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(bottom = 6.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                textures.forEachIndexed { idx, item ->
                    val isSelected = selectedIdx == idx
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(CircleShape)
                            .background(item.hexColorRepresentation)
                            .border(
                                width = if (isSelected) 3.dp else 1.dp,
                                color = if (isSelected) BurnishedGold else Color.White.copy(alpha = 0.2f),
                                shape = CircleShape
                            )
                            .clickable { selectedIdx = idx }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Action Shutter Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(onClick = onDismiss) {
                    Text("CANCEL", color = PolishedPlatinum.copy(alpha = 0.6f))
                }

                // Major circular high-prestige shutter
                Box(
                    modifier = Modifier
                        .size(76.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.15f))
                        .clickable {
                            onPhotoCaptured(textures[selectedIdx].url)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(58.dp)
                            .clip(CircleShape)
                            .background(BurnishedGold)
                            .border(2.dp, Color.Black, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Camera,
                            contentDescription = "Capture Button Trigger",
                            tint = MatteCharcoalBlack,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }

                TextButton(onClick = {
                    selectedIdx = (selectedIdx + 1) % textures.size
                }) {
                    Icon(Icons.Default.Cached, contentDescription = "Cycle Texture", tint = BurnishedGold, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("CYCLE", color = BurnishedGold)
                }
            }
        }
    }
}
