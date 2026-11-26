package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.NohaLyricsEntity
import com.example.data.SiteSettingsEntity
import com.example.ui.components.IslamicGeometricDivider
import com.example.ui.components.SectionHeader
import com.example.ui.theme.ArabicClassicFontFamily
import com.example.ui.theme.BurnishedGold
import com.example.ui.theme.CharcoalMuted
import com.example.ui.theme.InkBlack
import com.example.ui.theme.IvoryWhite
import com.example.ui.theme.NastaliqUrduFontFamily
import com.example.ui.theme.ObsidianBlack
import com.example.util.PlatformHelpers

@Composable
fun LyricsListScreen(
    lyricsList: List<NohaLyricsEntity>,
    onOpenLyricsDetail: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedLanguage by remember { mutableStateOf("All") }
    val languages = listOf("All", "Urdu", "Persian", "Arabic", "Roman Urdu")

    val filtered = remember(lyricsList, searchQuery, selectedLanguage) {
        lyricsList.filter { item ->
            val matchesQuery = searchQuery.isBlank() ||
                item.title.contains(searchQuery, ignoreCase = true) ||
                item.titleUrdu.contains(searchQuery, ignoreCase = true) ||
                item.reciter.contains(searchQuery, ignoreCase = true) ||
                item.poet.contains(searchQuery, ignoreCase = true)

            val matchesLang = when (selectedLanguage) {
                "Urdu" -> item.lyricsUrdu.isNotBlank()
                "Persian" -> item.lyricsPersian.isNotBlank()
                "Arabic" -> item.lyricsArabic.isNotBlank()
                "Roman Urdu" -> item.lyricsRoman.isNotBlank()
                else -> true
            }
            matchesQuery && matchesLang
        }
    }

    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 840.dp),
            contentPadding = PaddingValues(bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                SectionHeader(
                    title = "Noha Lyrics & Kalaam",
                    urduTitle = "نوحہ کلام — اردو، فارسی، عربی"
                )
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search lyrics by title, reciter, or poet...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(Icons.Default.Close, contentDescription = "Clear search")
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .testTag("lyrics_search_input")
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    languages.forEach { lang ->
                        FilterChip(
                            selected = selectedLanguage == lang,
                            onClick = { selectedLanguage = lang },
                            label = { Text(lang) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = BurnishedGold,
                                selectedLabelColor = ObsidianBlack
                            )
                        )
                    }
                }
            }

            items(filtered, key = { it.id }) { item ->
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .clickable { onOpenLyricsDetail(item.id) }
                        .testTag("lyrics_card_${item.id}")
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = MaterialTheme.colorScheme.primaryContainer
                            ) {
                                Text(
                                    text = item.category,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                            Text(
                                text = buildList {
                                    if (item.lyricsUrdu.isNotBlank()) add("اردو")
                                    if (item.lyricsPersian.isNotBlank()) add("فارسی")
                                    if (item.lyricsArabic.isNotBlank()) add("عربی")
                                    if (item.lyricsRoman.isNotBlank()) add("Roman")
                                }.joinToString(" • "),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        if (item.titleUrdu.isNotBlank()) {
                            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                                Text(
                                    text = item.titleUrdu,
                                    style = TextStyle(
                                        fontFamily = NastaliqUrduFontFamily,
                                        fontSize = 20.sp,
                                        lineHeight = 38.sp,
                                        color = MaterialTheme.colorScheme.primary
                                    ),
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                        Text(text = item.title, style = MaterialTheme.typography.titleLarge)
                        Text(
                            text = "Reciter: ${item.reciter} • Poet: ${item.poet}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        if (item.lyricsUrdu.isNotBlank()) {
                            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                                Text(
                                    text = item.lyricsUrdu.lines().take(2).joinToString("\n"),
                                    style = TextStyle(
                                        fontFamily = NastaliqUrduFontFamily,
                                        fontSize = 15.sp,
                                        lineHeight = 30.sp,
                                        color = MaterialTheme.colorScheme.onSurface
                                    ),
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LyricsDetailScreen(
    lyrics: NohaLyricsEntity?,
    settings: SiteSettingsEntity,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val clipboard = LocalClipboardManager.current

    if (lyrics == null) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Lyrics not found")
        }
        return
    }

    val availableTabs = remember(lyrics) {
        buildList {
            if (lyrics.lyricsUrdu.isNotBlank()) add("Urdu")
            if (lyrics.lyricsPersian.isNotBlank()) add("Persian")
            if (lyrics.lyricsArabic.isNotBlank()) add("Arabic")
            if (lyrics.lyricsRoman.isNotBlank()) add("Roman Urdu")
        }
    }
    var selectedTab by remember(lyrics.id) {
        mutableStateOf(availableTabs.firstOrNull() ?: "Urdu")
    }
    var fontScale by remember { mutableFloatStateOf(1.0f) }

    val activeText = when (selectedTab) {
        "Persian" -> lyrics.lyricsPersian
        "Arabic" -> lyrics.lyricsArabic
        "Roman Urdu" -> lyrics.lyricsRoman
        else -> lyrics.lyricsUrdu
    }
    val isRtl = selectedTab != "Roman Urdu"

    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 840.dp)
                .verticalScroll(rememberScrollState())
                .padding(bottom = 28.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onBack, modifier = Modifier.minimumInteractiveComponentSize()) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                    Text("Noha Lyrics", style = MaterialTheme.typography.titleLarge)
                }

                // Font Size Stepper
                Row(verticalAlignment = Alignment.CenterVertically) {
                    OutlinedButton(
                        onClick = { fontScale = (fontScale - 0.1f).coerceAtLeast(0.8f) },
                        contentPadding = PaddingValues(horizontal = 10.dp),
                        modifier = Modifier.minimumInteractiveComponentSize()
                    ) { Text("A-") }
                    Spacer(modifier = Modifier.width(4.dp))
                    OutlinedButton(
                        onClick = { fontScale = (fontScale + 0.1f).coerceAtMost(1.5f) },
                        contentPadding = PaddingValues(horizontal = 10.dp),
                        modifier = Modifier.minimumInteractiveComponentSize()
                    ) { Text("A+") }
                }
            }

            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                if (lyrics.titleUrdu.isNotBlank()) {
                    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                        Text(
                            text = lyrics.titleUrdu,
                            style = TextStyle(
                                fontFamily = NastaliqUrduFontFamily,
                                fontSize = 26.sp,
                                lineHeight = 50.sp,
                                color = MaterialTheme.colorScheme.primary
                            ),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
                Text(
                    text = lyrics.title,
                    style = MaterialTheme.typography.headlineLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Reciter: ${lyrics.reciter} • Poet: ${lyrics.poet} • ${lyrics.category}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                IslamicGeometricDivider(modifier = Modifier.padding(vertical = 8.dp))

                // Language selector tabs
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    availableTabs.forEach { tab ->
                        FilterChip(
                            selected = selectedTab == tab,
                            onClick = { selectedTab = tab },
                            label = { Text(tab) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = BurnishedGold,
                                selectedLabelColor = ObsidianBlack
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Crisp Ivory White Calligraphy Reading Sheet (Black ink on White & Gold frame)
                Surface(
                    shape = RoundedCornerShape(18.dp),
                    color = IvoryWhite,
                    border = BorderStroke(1.5.dp, BurnishedGold),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("lyrics_reading_sheet")
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        CompositionLocalProvider(
                            LocalLayoutDirection provides if (isRtl) LayoutDirection.Rtl else LayoutDirection.Ltr
                        ) {
                            val fontFamily = when (selectedTab) {
                                "Arabic" -> ArabicClassicFontFamily
                                "Urdu", "Persian" -> NastaliqUrduFontFamily
                                else -> MaterialTheme.typography.bodyLarge.fontFamily
                            }
                            val baseSize = if (selectedTab == "Urdu" || selectedTab == "Persian") 20f else if (selectedTab == "Arabic") 21f else 16f
                            val lineHeightMultiplier = if (selectedTab == "Urdu" || selectedTab == "Persian") 2.0f else 1.7f
                            Text(
                                text = activeText,
                                style = TextStyle(
                                    fontFamily = fontFamily,
                                    fontSize = (baseSize * fontScale).sp,
                                    lineHeight = (baseSize * fontScale * lineHeightMultiplier).sp,
                                    color = InkBlack
                                ),
                                textAlign = if (isRtl) TextAlign.Right else TextAlign.Left,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                        Spacer(modifier = Modifier.height(14.dp))
                        Text(
                            text = "${settings.siteSubtitleEnglish} • ${lyrics.poet}",
                            style = MaterialTheme.typography.labelSmall,
                            color = CharcoalMuted,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Copy, Print, Share Actions
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = {
                            val copyPayload = buildString {
                                appendLine(lyrics.titleUrdu)
                                appendLine(lyrics.title)
                                appendLine("Reciter: ${lyrics.reciter} | Poet: ${lyrics.poet}")
                                appendLine()
                                appendLine(activeText)
                            }
                            clipboard.setText(AnnotatedString(copyPayload))
                            Toast.makeText(context, "Lyrics copied to clipboard", Toast.LENGTH_SHORT).show()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = BurnishedGold,
                            contentColor = ObsidianBlack
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .minimumInteractiveComponentSize()
                            .testTag("copy_lyrics_button")
                    ) {
                        Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Copy", fontWeight = FontWeight.Bold)
                    }

                    OutlinedButton(
                        onClick = { PlatformHelpers.printLyrics(context, lyrics, settings) },
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                        modifier = Modifier
                            .weight(1f)
                            .minimumInteractiveComponentSize()
                            .testTag("print_lyrics_button")
                    ) {
                        Icon(Icons.Default.Print, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Print")
                    }

                    OutlinedButton(
                        onClick = {
                            PlatformHelpers.shareText(
                                context,
                                lyrics.title,
                                "${lyrics.titleUrdu}\n${lyrics.title}\n\n$activeText"
                            )
                        },
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                        modifier = Modifier
                            .minimumInteractiveComponentSize()
                            .testTag("share_lyrics_button")
                    ) {
                        Icon(Icons.Default.Share, contentDescription = "Share lyrics", modifier = Modifier.size(18.dp))
                    }
                }
            }
        }
    }
}
