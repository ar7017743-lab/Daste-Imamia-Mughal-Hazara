package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.PlayCircle
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.GalleryPhotoEntity
import com.example.data.IslamicArticleEntity
import com.example.data.MajlisProgramEntity
import com.example.data.SiteSettingsEntity
import com.example.ui.components.GalleryMediaImage
import com.example.ui.components.IslamicGeometricDivider
import com.example.ui.components.SectionHeader
import com.example.ui.theme.BurnishedGold
import com.example.ui.theme.ChampagneGold
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.IvoryWhite
import com.example.ui.theme.LightMutedText
import com.example.ui.theme.NastaliqUrduFontFamily
import com.example.ui.theme.ObsidianBlack
import com.example.util.PlatformHelpers

@Composable
fun MoreHubScreen(
    settings: SiteSettingsEntity,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val entries = buildList {
        if (settings.navShowMajalis) {
            add(Triple("Majalis & Programs", "Schedule, speakers, venues & live recordings", Icons.Default.CalendarMonth) to "majalis")
        }
        if (settings.navShowGallery) {
            add(Triple("Photo Gallery", "Muharram, Majalis & Matami programs lightbox", Icons.Default.PhotoLibrary) to "gallery")
        }
        if (settings.navShowArticles) {
            add(Triple("Islamic Articles", "Reflections on Karbala, Ahlulbayt & Azadari", Icons.AutoMirrored.Filled.MenuBook) to "articles")
        }
        add(Triple("About Us", "Daste Imamia Mughal Hazara & Hussaini Academy", Icons.Default.Info) to "about")
        add(Triple("Contact Us", "Contact form, WhatsApp, YouTube & email", Icons.Default.Chat) to "contact")
        add(Triple("Admin CMS Panel", "Manage videos, MP3s, lyrics, gallery, SEO & settings", Icons.Default.AdminPanelSettings) to "admin")
    }

    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 840.dp),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                SectionHeader(
                    title = "Explore Portal & CMS",
                    urduTitle = "مزید صفحات اور انتظام"
                )
            }
            items(entries) { (meta, route) ->
                val (title, subtitle, icon) = meta
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onNavigate(route) }
                        .testTag("more_nav_$route")
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(46.dp)
                                .clip(CircleShape)
                                .background(BurnishedGold.copy(alpha = 0.16f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(title, style = MaterialTheme.typography.titleLarge)
                            Text(
                                subtitle,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MajalisScreen(
    majalis: List<MajlisProgramEntity>,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var selectedCategory by remember { mutableStateOf("All") }
    val categories = listOf("All", "Muharram", "Safar", "Ayyam-e-Fatima", "Weekly")
    val filtered = remember(majalis, selectedCategory) {
        if (selectedCategory == "All") majalis
        else majalis.filter { it.category.equals(selectedCategory, ignoreCase = true) }
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
                    title = "Majalis & Religious Programs",
                    urduTitle = "مجالسِ عزا اور دینی پروگرام"
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    categories.forEach { cat ->
                        FilterChip(
                            selected = selectedCategory == cat,
                            onClick = { selectedCategory = cat },
                            label = { Text(cat) },
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
                                text = "${item.dateText} • ${item.timeText}",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        if (item.titleUrdu.isNotBlank()) {
                            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                                Text(
                                    text = item.titleUrdu,
                                    style = TextStyle(
                                        fontFamily = NastaliqUrduFontFamily,
                                        fontSize = 19.sp,
                                        lineHeight = 36.sp,
                                        color = MaterialTheme.colorScheme.primary
                                    ),
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                        Text(item.title, style = MaterialTheme.typography.titleLarge)
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Person, contentDescription = null, modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.primary)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(item.speaker, style = MaterialTheme.typography.bodyMedium)
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.LocationOn, contentDescription = null, modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.primary)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(item.venue, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(item.description, style = MaterialTheme.typography.bodyMedium)
                        if (item.youtubeUrl.isNotBlank()) {
                            Spacer(modifier = Modifier.height(10.dp))
                            OutlinedButton(
                                onClick = { PlatformHelpers.openExternalUrl(context, item.youtubeUrl) },
                                border = BorderStroke(1.dp, BurnishedGold),
                                modifier = Modifier.minimumInteractiveComponentSize()
                            ) {
                                Icon(Icons.Default.PlayCircle, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Watch Program")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PhotoGalleryScreen(
    photos: List<GalleryPhotoEntity>,
    modifier: Modifier = Modifier
) {
    var selectedCategory by remember { mutableStateOf("All") }
    var lightboxIndex by remember { mutableIntStateOf(-1) }
    val categories = listOf("All", "Muharram", "Majalis", "Matami Programs")

    val filtered = remember(photos, selectedCategory) {
        if (selectedCategory == "All") photos
        else photos.filter { it.category.equals(selectedCategory, ignoreCase = true) }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .widthIn(max = 960.dp)
    ) {
        SectionHeader(
            title = "Photo Gallery",
            urduTitle = "تصویری گیلری"
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            categories.forEach { cat ->
                FilterChip(
                    selected = selectedCategory == cat,
                    onClick = { selectedCategory = cat },
                    label = { Text(cat) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = BurnishedGold,
                        selectedLabelColor = ObsidianBlack
                    )
                )
            }
        }

        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 164.dp),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            itemsIndexed(filtered, key = { _, p -> p.id }) { index, photo ->
                Card(
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { lightboxIndex = index }
                        .testTag("gallery_item_${photo.id}")
                ) {
                    Column {
                        GalleryMediaImage(
                            imageUri = photo.imageUri,
                            contentDescription = photo.altText,
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(4f / 3f)
                        )
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(
                                text = photo.title,
                                style = MaterialTheme.typography.titleMedium,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = photo.category,
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        }
    }

    // Full-screen Image Lightbox Dialog
    if (lightboxIndex in filtered.indices) {
        val currentPhoto = filtered[lightboxIndex]
        Dialog(
            onDismissRequest = { lightboxIndex = -1 },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Surface(
                color = ObsidianBlack,
                modifier = Modifier.fillMaxSize()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${lightboxIndex + 1} / ${filtered.size} • ${currentPhoto.category}",
                            style = MaterialTheme.typography.labelLarge,
                            color = ChampagneGold
                        )
                        IconButton(
                            onClick = { lightboxIndex = -1 },
                            modifier = Modifier.minimumInteractiveComponentSize()
                        ) {
                            Icon(Icons.Default.Close, contentDescription = "Close lightbox", tint = IvoryWhite)
                        }
                    }

                    GalleryMediaImage(
                        imageUri = currentPhoto.imageUri,
                        contentDescription = currentPhoto.altText,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .clip(RoundedCornerShape(14.dp))
                    )

                    Column(modifier = Modifier.padding(top = 12.dp)) {
                        Text(
                            text = currentPhoto.title,
                            style = MaterialTheme.typography.headlineMedium,
                            color = ChampagneGold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = currentPhoto.caption,
                            style = MaterialTheme.typography.bodyMedium,
                            color = LightMutedText
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            OutlinedButton(
                                onClick = { if (lightboxIndex > 0) lightboxIndex-- },
                                enabled = lightboxIndex > 0,
                                border = BorderStroke(1.dp, BurnishedGold),
                                modifier = Modifier.minimumInteractiveComponentSize()
                            ) {
                                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Previous")
                            }
                            OutlinedButton(
                                onClick = { if (lightboxIndex < filtered.lastIndex) lightboxIndex++ },
                                enabled = lightboxIndex < filtered.lastIndex,
                                border = BorderStroke(1.dp, BurnishedGold),
                                modifier = Modifier.minimumInteractiveComponentSize()
                            ) {
                                Text("Next")
                                Spacer(modifier = Modifier.width(6.dp))
                                Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ArticlesListScreen(
    articles: List<IslamicArticleEntity>,
    onOpenArticle: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
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
                    title = "Islamic Articles",
                    urduTitle = "اسلامی مضامین و مقالات"
                )
            }
            items(articles, key = { it.id }) { article ->
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .clickable { onOpenArticle(article.id) }
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "${article.category} • ${article.publishedDate}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        if (article.titleUrdu.isNotBlank()) {
                            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                                Text(
                                    text = article.titleUrdu,
                                    style = TextStyle(
                                        fontFamily = NastaliqUrduFontFamily,
                                        fontSize = 18.sp,
                                        lineHeight = 36.sp,
                                        color = MaterialTheme.colorScheme.primary
                                    ),
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                        Text(article.title, style = MaterialTheme.typography.titleLarge)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            article.summary,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            "By ${article.author}",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ArticleDetailScreen(
    article: IslamicArticleEntity?,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    if (article == null) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Article not found")
        }
        return
    }

    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 840.dp)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack, modifier = Modifier.minimumInteractiveComponentSize()) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
                Text("Islamic Article", style = MaterialTheme.typography.titleLarge)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "${article.category} • ${article.publishedDate}",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary
            )
            if (article.titleUrdu.isNotBlank()) {
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                    Text(
                        text = article.titleUrdu,
                        style = TextStyle(
                            fontFamily = NastaliqUrduFontFamily,
                            fontSize = 24.sp,
                            lineHeight = 46.sp,
                            color = MaterialTheme.colorScheme.primary
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
            Text(article.title, style = MaterialTheme.typography.headlineLarge)
            Text(
                "Author: ${article.author}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            IslamicGeometricDivider(modifier = Modifier.padding(vertical = 10.dp))
            Text(article.content, style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedButton(
                onClick = {
                    PlatformHelpers.shareText(
                        context,
                        article.title,
                        "${article.title}\n\n${article.summary}\n\n${article.content}"
                    )
                },
                border = BorderStroke(1.dp, BurnishedGold),
                modifier = Modifier.minimumInteractiveComponentSize()
            ) {
                Text("Share Article")
            }
        }
    }
}

@Composable
fun AboutUsScreen(
    settings: SiteSettingsEntity,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 840.dp)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            SectionHeader(
                title = "About Us",
                urduTitle = "ہمارے بارے میں"
            )
            Surface(
                shape = RoundedCornerShape(18.dp),
                color = ObsidianBlack,
                border = BorderStroke(1.dp, BurnishedGold),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                        Text(
                            text = settings.siteNameUrdu,
                            style = TextStyle(
                                fontFamily = NastaliqUrduFontFamily,
                                fontSize = 26.sp,
                                lineHeight = 48.sp,
                                color = ChampagneGold
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    Text(
                        text = settings.siteSubtitleEnglish,
                        style = MaterialTheme.typography.labelLarge,
                        color = IvoryWhite
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = settings.aboutUsText,
                        style = MaterialTheme.typography.bodyLarge,
                        color = LightMutedText
                    )
                }
            }

            Card(
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Organization & Heritage", style = MaterialTheme.typography.titleLarge)
                    Text(settings.organizationInfo, style = MaterialTheme.typography.bodyLarge)
                    IslamicGeometricDivider()
                    Text("SEO & Portal Focus", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
                    Text(settings.seoKeywords, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
}

@Composable
fun ContactUsScreen(
    settings: SiteSettingsEntity,
    onSubmitContact: (String, String, String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var subject by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }

    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 840.dp)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            SectionHeader(
                title = "Contact Us",
                urduTitle = "رابطہ کریں"
            )

            // Direct Channels
            Card(
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(settings.organizationInfo, style = MaterialTheme.typography.bodyMedium)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                        Button(
                            onClick = {
                                PlatformHelpers.openWhatsApp(
                                    context,
                                    settings.whatsappNumber,
                                    "Assalam-o-Alaikum, reaching out to Daste Imamia Mughal Hazara."
                                )
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = EmeraldSuccess, contentColor = Color.White),
                            modifier = Modifier
                                .weight(1f)
                                .minimumInteractiveComponentSize()
                                .testTag("contact_whatsapp_btn")
                        ) {
                            Icon(Icons.Default.Chat, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("WhatsApp")
                        }
                        Button(
                            onClick = { PlatformHelpers.openExternalUrl(context, settings.youtubeChannelUrl) },
                            colors = ButtonDefaults.buttonColors(containerColor = BurnishedGold, contentColor = ObsidianBlack),
                            modifier = Modifier
                                .weight(1f)
                                .minimumInteractiveComponentSize()
                                .testTag("contact_youtube_btn")
                        ) {
                            Icon(Icons.Default.PlayCircle, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("YouTube", fontWeight = FontWeight.Bold)
                        }
                    }
                    OutlinedButton(
                        onClick = {
                            PlatformHelpers.sendEmail(
                                context,
                                settings.contactEmail,
                                "Inquiry - Daste Imamia Mughal Hazara",
                                ""
                            )
                        },
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                        modifier = Modifier
                            .fillMaxWidth()
                            .minimumInteractiveComponentSize()
                    ) {
                        Icon(Icons.Default.Email, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Email: ${settings.contactEmail}")
                    }
                }
            }

            // Contact Form
            Card(
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Send a Message", style = MaterialTheme.typography.titleLarge)
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Your Name") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email Address") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = subject,
                        onValueChange = { subject = it },
                        label = { Text("Subject (e.g., Majlis / Noha Request)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = message,
                        onValueChange = { message = it },
                        label = { Text("Message") },
                        minLines = 3,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Button(
                        onClick = {
                            if (name.isBlank() || message.isBlank()) {
                                Toast.makeText(context, "Please enter your name and message", Toast.LENGTH_SHORT).show()
                            } else {
                                onSubmitContact(name, email, subject, message)
                                name = ""
                                email = ""
                                subject = ""
                                message = ""
                                Toast.makeText(context, "Message saved! Admin can view it in CMS.", Toast.LENGTH_SHORT).show()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = BurnishedGold, contentColor = ObsidianBlack),
                        modifier = Modifier
                            .fillMaxWidth()
                            .minimumInteractiveComponentSize()
                            .testTag("contact_submit_button")
                    ) {
                        Text("Submit Inquiry", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
