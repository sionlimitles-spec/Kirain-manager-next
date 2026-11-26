package com.kirau.manager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// --- TEMA & WARNA ---
val Amber = Color(0xFFE7A23B)
val BaseLight = Color(0xFFF2F4F7)
val SurfaceLight = Color(0xFFFFFFFF)
val BaseDark = Color(0xFF0F1012)
val SurfaceDark = Color(0xFF1A1C1F)
val TextLight = Color(0xFF1A1C1E)
val TextDark = Color(0xFFEDEAE3)

private val KirauColorScheme = lightColorScheme(
    primary = Amber,
    background = BaseLight,
    surface = SurfaceLight,
    onBackground = TextLight,
    onSurface = TextLight
)

@Composable
fun KirauTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = KirauColorScheme, content = content)
}

// --- MAIN ACTIVITY ---
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KirauTheme {
                KirauApp()
            }
        }
    }
}

@Composable
fun KirauApp() {
    var currentPage by remember { mutableStateOf("beranda") }
    var isGlassEnabled by remember { mutableStateOf(true) } // Toggle Glass

    Scaffold(
        containerColor = BaseLight,
        bottomBar = {
            FloatingBottomNav(
                currentPage = currentPage,
                onNavigate = { currentPage = it },
                isGlass = isGlassEnabled
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            when (currentPage) {
                "beranda" -> BerandaScreen(isGlassEnabled)
                "priv" -> PrivScreen(isGlassEnabled)
                "files" -> FilesScreen(isGlassEnabled)
                "plugins" -> PluginScreen(isGlassEnabled)
                "settings" -> SettingsScreen(isGlassEnabled) { isGlassEnabled = it }
            }
        }
    }
}

// --- KOMPONEN GLASS ---
@Composable
fun GlassSurface(
    isGlass: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val bgColor = if (isGlass) SurfaceLight.copy(alpha = 0.75f) else SurfaceLight
    val borderColor = if (isGlass) Color.White.copy(alpha = 0.8f) else Color.Transparent

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(24.dp))
            .background(bgColor)
            .border(1.dp, borderColor, RoundedCornerShape(24.dp))
    ) {
        content()
    }
}

// --- NAVIGASI BAWAH ---
@Composable
fun FloatingBottomNav(currentPage: String, onNavigate: (String) -> Unit, isGlass: Boolean) {
    val bgColor = if (isGlass) SurfaceLight.copy(alpha = 0.85f) else SurfaceLight
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 24.dp, start = 32.dp, end = 32.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier
                .clip(CircleShape)
                .background(bgColor)
                .border(1.dp, Color.White.copy(alpha = 0.5f), CircleShape)
                .padding(horizontal = 8.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            NavItem("Beranda", Icons.Outlined.Home, currentPage == "beranda") { onNavigate("beranda") }
            NavItem("Akses", Icons.Outlined.Lock, currentPage == "priv") { onNavigate("priv") }
            NavItem("File", Icons.Outlined.Folder, currentPage == "files") { onNavigate("files") }
            NavItem("Plugin", Icons.Outlined.Extension, currentPage == "plugins") { onNavigate("plugins") }
            NavItem("Set", Icons.Outlined.Settings, currentPage == "settings") { onNavigate("settings") }
        }
    }
}

@Composable
fun NavItem(label: String, icon: ImageVector, isSelected: Boolean, onClick: () -> Unit) {
    val bg = if (isSelected) Amber.copy(alpha = 0.15f) else Color.Transparent
    val tint = if (isSelected) Amber else Color.Gray
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(bg)
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(icon, contentDescription = label, tint = tint, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.height(4.dp))
        Text(label, fontSize = 10.sp, color = tint, fontWeight = if(isSelected) FontWeight.Bold else FontWeight.Normal)
    }
}

// --- HALAMAN BERANDA ---
@Composable
fun BerandaScreen(isGlass: Boolean) {
    Column(modifier = Modifier.fillMaxSize()) {
        Spacer(modifier = Modifier.height(24.dp))
        Text("Kirau Manager", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        
        Spacer(modifier = Modifier.height(16.dp))
        // Banner Peringatan/Root
        GlassSurface(isGlass, modifier = Modifier.fillMaxWidth()) {
            Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.Warning, contentDescription = "Warning", tint = Color(0xFFD9544D))
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text("Status Root Terdeteksi", fontWeight = FontWeight.Bold, color = Color(0xFFD9544D))
                    Text("Magisk v27.0 aktif · Seccomp Disaring", fontSize = 12.sp, color = Color(0xFFD9544D).copy(alpha = 0.8f))
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
        Text("Info Perangkat", color = Amber, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        Spacer(modifier = Modifier.height(8.dp))
        
        // Grid Info
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            InfoCard(Modifier.weight(1f), "Model", "Infinix GT 50 Pro", "Android 15", isGlass)
            InfoCard(Modifier.weight(1f), "Kernel", "6.6.30", "arm64-v8a", isGlass)
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            InfoCard(Modifier.weight(1f), "Storage", "87 / 128 GB", "68%", isGlass, progress = 0.68f)
            InfoCard(Modifier.weight(1f), "RAM", "3.9 / 6.0 GB", "65%", isGlass, progress = 0.65f, isOk = true)
        }
    }
}

@Composable
fun InfoCard(modifier: Modifier, title: String, valStr: String, sub: String, isGlass: Boolean, progress: Float? = null, isOk: Boolean = false) {
    GlassSurface(isGlass, modifier) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title.uppercase(), fontSize = 10.sp, color = Color.Gray, letterSpacing = 1.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(valStr, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Text(sub, fontSize = 12.sp, color = Color.Gray, fontFamily = FontFamily.Monospace)
            if (progress != null) {
                Spacer(modifier = Modifier.height(8.dp))
                LinearProgressIndicator(
                    progress = progress, 
                    modifier = Modifier.fillMaxWidth().height(4.dp).clip(RoundedCornerShape(2.dp)),
                    color = if (isOk) Color(0xFF4FA96B) else Amber,
                    trackColor = Color.LightGray.copy(alpha = 0.3f)
                )
            }
        }
    }
}

// --- HALAMAN FILE EXPLORER ---
@Composable
fun FilesScreen(isGlass: Boolean) {
    val files = listOf(
        "archive/" to true, "plugins/" to true, "backup.7z" to false, "kiraumgr.apk" to false
    )
    Column(modifier = Modifier.fillMaxSize()) {
        Spacer(modifier = Modifier.height(24.dp))
        Text("File Explorer", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        
        GlassSurface(isGlass, modifier = Modifier.fillMaxWidth()) {
            Column {
                files.forEach { (name, isDir) ->
                    Row(
                        modifier = Modifier.fillMaxWidth().clickable { }.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            if (isDir) Icons.Outlined.Folder else Icons.Outlined.InsertDriveFile,
                            contentDescription = null,
                            tint = if (isDir) Amber else Color.Gray
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(name, fontFamily = FontFamily.Monospace, fontSize = 14.sp)
                    }
                    HorizontalDivider(color = Color.LightGray.copy(alpha = 0.3f))
                }
            }
        }
    }
}

// --- HALAMAN PLUGIN ---
@Composable
fun PluginScreen(isGlass: Boolean) {
    Column(modifier = Modifier.fillMaxSize()) {
        Spacer(modifier = Modifier.height(24.dp))
        Text("Plugin Kirau", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        
        GlassSurface(isGlass, modifier = Modifier.fillMaxWidth()) {
            Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier.size(40.dp).clip(RoundedCornerShape(8.dp)).background(Amber.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) { Text("AT", color = Amber, fontWeight = FontWeight.Bold) }
                
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text("AutoTask", fontWeight = FontWeight.Bold)
                    Text("v1.2.0 · Dibuat oleh Kirau", fontSize = 12.sp, color = Color.Gray)
                }
                Switch(checked = true, onCheckedChange = {})
            }
        }
    }
}

// --- HALAMAN HAK ISTIMEWA ---
@Composable
fun PrivScreen(isGlass: Boolean) {
    Column(modifier = Modifier.fillMaxSize()) {
        Spacer(modifier = Modifier.height(24.dp))
        Text("Hak Istimewa", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        
        GlassSurface(isGlass, modifier = Modifier.fillMaxWidth()) {
            Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                Text("Termux", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
                // Segmented control tiruan
                Row(modifier = Modifier.clip(RoundedCornerShape(8.dp)).background(BaseLight)) {
                    Text("Allow", modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp).background(SurfaceLight, RoundedCornerShape(6.dp)).padding(horizontal = 4.dp), fontSize = 12.sp, color = Amber, fontWeight = FontWeight.Bold)
                    Text("Prompt", modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), fontSize = 12.sp, color = Color.Gray)
                }
            }
        }
    }
}

// --- HALAMAN PENGATURAN ---
@Composable
fun SettingsScreen(isGlass: Boolean, onGlassToggle: (Boolean) -> Unit) {
    Column(modifier = Modifier.fillMaxSize()) {
        Spacer(modifier = Modifier.height(24.dp))
        Text("Pengaturan", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        
        GlassSurface(isGlass, modifier = Modifier.fillMaxWidth()) {
            Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Dynamic Glass", fontWeight = FontWeight.Bold)
                    Text("Efek kaca transparan pada UI", fontSize = 12.sp, color = Color.Gray)
                }
                Switch(checked = isGlass, onCheckedChange = onGlassToggle)
            }
        }
    }
}
