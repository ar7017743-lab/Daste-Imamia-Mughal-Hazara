package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.data.AudioTrackEntity
import com.example.data.ContactMessageEntity
import com.example.data.GalleryPhotoEntity
import com.example.data.IslamicArticleEntity
import com.example.data.MajlisProgramEntity
import com.example.data.NohaLyricsEntity
import com.example.data.NohaVideoEntity
import com.example.data.SiteSettingsEntity
import com.example.ui.MainViewModel
import com.example.ui.components.SectionHeader
import com.example.ui.theme.BurnishedGold
import com.example.ui.theme.ObsidianBlack

private enum class AdminTab(val label: String) {
    VIDEOS("Videos"),
    AUDIO("Audio MP3"),
    LYRICS("Lyrics"),
    GALLERY("Gallery"),
    MAJALIS("Majalis"),
    ARTICLES("Articles"),
    SETTINGS("Settings & SEO")
}

@Composable
fun AdminCmsScreen(
    viewModel: MainViewModel,
    videos: List<NohaVideoEntity>,
    audioTracks: List<AudioTrackEntity>,
    lyrics: List<NohaLyricsEntity>,
    majalis: List<MajlisProgramEntity>,
    galleryPhotos: List<GalleryPhotoEntity>,
    articles: List<IslamicArticleEntity>,
    settings: SiteSettingsEntity,
    contactMessages: List<ContactMessageEntity>,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var activeTab by remember { mutableStateOf(AdminTab.VIDEOS) }

    // Dialog states
    var editingVideo by remember { mutableStateOf<NohaVideoEntity?>(null) }
    var showVideoDialog by remember { mutableStateOf(false) }

    var editingAudio by remember { mutableStateOf<AudioTrackEntity?>(null) }
    var showAudioDialog by remember { mutableStateOf(false) }

    var editingLyrics by remember { mutableStateOf<NohaLyricsEntity?>(null) }
    var showLyricsDialog by remember { mutableStateOf(false) }

    var editingPhoto by remember { mutableStateOf<GalleryPhotoEntity?>(null) }
    var showPhotoDialog by remember { mutableStateOf(false) }

    var editingMajlis by remember { mutableStateOf<MajlisProgramEntity?>(null) }
    var showMajlisDialog by remember { mutableStateOf(false) }

    var editingArticle by remember { mutableStateOf<IslamicArticleEntity?>(null) }
    var showArticleDialog by remember { mutableStateOf(false) }

    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 880.dp),
            contentPadding = PaddingValues(bottom = 28.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item {
                SectionHeader(
                    title = "Admin CMS Dashboard",
                    urduTitle = "ایڈمن کنٹینٹ مینجمنٹ پینل"
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    AdminTab.entries.forEach { tab ->
                        FilterChip(
                            selected = activeTab == tab,
                            onClick = { activeTab = tab },
                            label = { Text(tab.label) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = BurnishedGold,
                                selectedLabelColor = ObsidianBlack
                            ),
                            modifier = Modifier.testTag("admin_tab_${tab.name.lowercase()}")
                        )
                    }
                }
            }

            if (activeTab != AdminTab.SETTINGS) {
                item {
                    Button(
                        onClick = {
                            when (activeTab) {
                                AdminTab.VIDEOS -> { editingVideo = null; showVideoDialog = true }
                                AdminTab.AUDIO -> { editingAudio = null; showAudioDialog = true }
                                AdminTab.LYRICS -> { editingLyrics = null; showLyricsDialog = true }
                                AdminTab.GALLERY -> { editingPhoto = null; showPhotoDialog = true }
                                AdminTab.MAJALIS -> { editingMajlis = null; showMajlisDialog = true }
                                AdminTab.ARTICLES -> { editingArticle = null; showArticleDialog = true }
                                AdminTab.SETTINGS -> Unit
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = BurnishedGold, contentColor = ObsidianBlack),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .minimumInteractiveComponentSize()
                            .testTag("admin_add_button")
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Add New ${activeTab.label}", fontWeight = FontWeight.Bold)
                    }
                }
            }

            when (activeTab) {
                AdminTab.VIDEOS -> {
                    items(videos, key = { it.id }) { video ->
                        AdminItemCard(
                            title = video.title,
                            subtitle = "${video.reciter} • ${video.category} • ${video.year}" + if (video.isHeroFeatured) " • [HERO]" else "",
                            onEdit = { editingVideo = video; showVideoDialog = true },
                            onDelete = { viewModel.deleteVideo(video) },
                            extraAction = {
                                IconButton(
                                    onClick = {
                                        viewModel.markHeroFeatured(video.id)
                                        Toast.makeText(context, "Set as Homepage Hero Video", Toast.LENGTH_SHORT).show()
                                    },
                                    modifier = Modifier.minimumInteractiveComponentSize()
                                ) {
                                    Icon(
                                        Icons.Default.Star,
                                        contentDescription = "Set Hero Featured",
                                        tint = if (video.isHeroFeatured) BurnishedGold else MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        )
                    }
                }
                AdminTab.AUDIO -> {
                    items(audioTracks, key = { it.id }) { track ->
                        AdminItemCard(
                            title = track.title,
                            subtitle = "${track.reciter} • ${track.category} • ${track.year}",
                            onEdit = { editingAudio = track; showAudioDialog = true },
                            onDelete = { viewModel.deleteAudio(track) }
                        )
                    }
                }
                AdminTab.LYRICS -> {
                    items(lyrics, key = { it.id }) { item ->
                        AdminItemCard(
                            title = item.title,
                            subtitle = "${item.reciter} • Poet: ${item.poet} • ${item.category}",
                            onEdit = { editingLyrics = item; showLyricsDialog = true },
                            onDelete = { viewModel.deleteLyrics(item) }
                        )
                    }
                }
                AdminTab.GALLERY -> {
                    items(galleryPhotos, key = { it.id }) { photo ->
                        AdminItemCard(
                            title = photo.title,
                            subtitle = "${photo.category} • ${photo.caption}",
                            onEdit = { editingPhoto = photo; showPhotoDialog = true },
                            onDelete = { viewModel.deletePhoto(photo) }
                        )
                    }
                }
                AdminTab.MAJALIS -> {
                    items(majalis, key = { it.id }) { item ->
                        AdminItemCard(
                            title = item.title,
                            subtitle = "${item.speaker} • ${item.dateText} • ${item.venue}",
                            onEdit = { editingMajlis = item; showMajlisDialog = true },
                            onDelete = { viewModel.deleteMajlis(item) }
                        )
                    }
                }
                AdminTab.ARTICLES -> {
                    items(articles, key = { it.id }) { article ->
                        AdminItemCard(
                            title = article.title,
                            subtitle = "${article.author} • ${article.category}",
                            onEdit = { editingArticle = article; showArticleDialog = true },
                            onDelete = { viewModel.deleteArticle(article) }
                        )
                    }
                }
                AdminTab.SETTINGS -> {
                    item {
                        SettingsAndSeoForm(
                            settings = settings,
                            contactMessages = contactMessages,
                            onSaveSettings = { updated ->
                                viewModel.saveSettings(updated)
                                Toast.makeText(context, "Settings & SEO saved", Toast.LENGTH_SHORT).show()
                            }
                        )
                    }
                }
            }
        }
    }

    if (showVideoDialog) {
        VideoEditorDialog(
            initial = editingVideo,
            onDismiss = { showVideoDialog = false },
            onSave = { id, title, titleUrdu, reciter, poet, year, category, url, desc, featured, hero ->
                viewModel.saveVideo(id, title, titleUrdu, reciter, poet, year, category, url, desc, featured, hero)
                showVideoDialog = false
            }
        )
    }

    if (showAudioDialog) {
        AudioEditorDialog(
            initial = editingAudio,
            onDismiss = { showAudioDialog = false },
            onSave = { id, title, titleUrdu, reciter, category, year, dur, uri, downloadable ->
                viewModel.saveAudio(id, title, titleUrdu, reciter, category, year, dur, uri, downloadable)
                showAudioDialog = false
            }
        )
    }

    if (showLyricsDialog) {
        LyricsEditorDialog(
            initial = editingLyrics,
            onDismiss = { showLyricsDialog = false },
            onSave = { id, title, titleUrdu, reciter, poet, cat, lang, ur, fa, ar, rom ->
                viewModel.saveLyrics(id, title, titleUrdu, reciter, poet, cat, lang, ur, fa, ar, rom)
                showLyricsDialog = false
            }
        )
    }

    if (showPhotoDialog) {
        GalleryPhotoEditorDialog(
            initial = editingPhoto,
            onDismiss = { showPhotoDialog = false },
            onSave = { id, title, caption, cat, uri, alt ->
                viewModel.savePhoto(id, title, caption, cat, uri, alt)
                showPhotoDialog = false
            }
        )
    }

    if (showMajlisDialog) {
        MajlisEditorDialog(
            initial = editingMajlis,
            onDismiss = { showMajlisDialog = false },
            onSave = { id, title, titleUrdu, speaker, venue, date, time, cat, desc, yt ->
                viewModel.saveMajlis(id, title, titleUrdu, speaker, venue, date, time, cat, desc, yt)
                showMajlisDialog = false
            }
        )
    }

    if (showArticleDialog) {
        ArticleEditorDialog(
            initial = editingArticle,
            onDismiss = { showArticleDialog = false },
            onSave = { id, title, titleUrdu, author, cat, summary, content, seo, date ->
                viewModel.saveArticle(id, title, titleUrdu, author, cat, summary, content, seo, date)
                showArticleDialog = false
            }
        )
    }
}

@Composable
private fun AdminItemCard(
    title: String,
    subtitle: String,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    extraAction: (@Composable () -> Unit)? = null
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.titleMedium, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text(
                    subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
            extraAction?.invoke()
            IconButton(onClick = onEdit, modifier = Modifier.minimumInteractiveComponentSize()) {
                Icon(Icons.Default.Edit, contentDescription = "Edit", tint = MaterialTheme.colorScheme.primary)
            }
            IconButton(onClick = onDelete, modifier = Modifier.minimumInteractiveComponentSize()) {
                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
            }
        }
    }
}

@Composable
private fun SettingsAndSeoForm(
    settings: SiteSettingsEntity,
    contactMessages: List<ContactMessageEntity>,
    onSaveSettings: (SiteSettingsEntity) -> Unit
) {
    var urduName by remember(settings) { mutableStateOf(settings.siteNameUrdu) }
    var englishSub by remember(settings) { mutableStateOf(settings.siteSubtitleEnglish) }
    var youtubeUrl by remember(settings) { mutableStateOf(settings.youtubeChannelUrl) }
    var tiktok by remember(settings) { mutableStateOf(settings.tiktokHandle) }
    var whatsapp by remember(settings) { mutableStateOf(settings.whatsappNumber) }
    var email by remember(settings) { mutableStateOf(settings.contactEmail) }
    var orgInfo by remember(settings) { mutableStateOf(settings.organizationInfo) }
    var aboutText by remember(settings) { mutableStateOf(settings.aboutUsText) }
    var seoTitle by remember(settings) { mutableStateOf(settings.seoTitle) }
    var seoDesc by remember(settings) { mutableStateOf(settings.seoMetaDescription) }
    var seoKeywords by remember(settings) { mutableStateOf(settings.seoKeywords) }
    var showMajalis by remember(settings) { mutableStateOf(settings.navShowMajalis) }
    var showGallery by remember(settings) { mutableStateOf(settings.navShowGallery) }
    var showArticles by remember(settings) { mutableStateOf(settings.navShowArticles) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Card(
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("Branding & Calligraphy", style = MaterialTheme.typography.titleLarge)
                OutlinedTextField(value = urduName, onValueChange = { urduName = it }, label = { Text("Urdu Calligraphy Title") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = englishSub, onValueChange = { englishSub = it }, label = { Text("English Subtitle") }, singleLine = true, modifier = Modifier.fillMaxWidth())

                Text("Social & Contact Details", style = MaterialTheme.typography.titleLarge)
                OutlinedTextField(value = whatsapp, onValueChange = { whatsapp = it }, label = { Text("WhatsApp Number") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = youtubeUrl, onValueChange = { youtubeUrl = it }, label = { Text("YouTube Channel URL") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = tiktok, onValueChange = { tiktok = it }, label = { Text("TikTok Handle") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Contact Email") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = orgInfo, onValueChange = { orgInfo = it }, label = { Text("Organization Info") }, minLines = 2, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = aboutText, onValueChange = { aboutText = it }, label = { Text("About Us Content") }, minLines = 3, modifier = Modifier.fillMaxWidth())

                Text("Navigation Menu Visibility", style = MaterialTheme.typography.titleLarge)
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text("Show Majalis Page")
                    Switch(checked = showMajalis, onCheckedChange = { showMajalis = it })
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text("Show Photo Gallery Page")
                    Switch(checked = showGallery, onCheckedChange = { showGallery = it })
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text("Show Islamic Articles Page")
                    Switch(checked = showArticles, onCheckedChange = { showArticles = it })
                }

                Text("SEO Configuration", style = MaterialTheme.typography.titleLarge)
                OutlinedTextField(value = seoTitle, onValueChange = { seoTitle = it }, label = { Text("SEO Title") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = seoDesc, onValueChange = { seoDesc = it }, label = { Text("SEO Meta Description") }, minLines = 2, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = seoKeywords, onValueChange = { seoKeywords = it }, label = { Text("SEO Keywords") }, minLines = 2, modifier = Modifier.fillMaxWidth())

                Button(
                    onClick = {
                        onSaveSettings(
                            settings.copy(
                                siteNameUrdu = urduName.ifBlank { "دستہ امامیہ مغل ہزارہ" },
                                siteSubtitleEnglish = englishSub.ifBlank { "DASTE IMAMIA MUGHAL HAZARA" },
                                youtubeChannelUrl = youtubeUrl,
                                tiktokHandle = tiktok,
                                whatsappNumber = whatsapp,
                                contactEmail = email,
                                organizationInfo = orgInfo,
                                aboutUsText = aboutText,
                                seoTitle = seoTitle,
                                seoMetaDescription = seoDesc,
                                seoKeywords = seoKeywords,
                                navShowMajalis = showMajalis,
                                navShowGallery = showGallery,
                                navShowArticles = showArticles
                            )
                        )
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = BurnishedGold, contentColor = ObsidianBlack),
                    modifier = Modifier
                        .fillMaxWidth()
                        .minimumInteractiveComponentSize()
                        .testTag("admin_save_settings_button")
                ) {
                    Text("Save Settings & SEO", fontWeight = FontWeight.Bold)
                }
            }
        }

        if (contactMessages.isNotEmpty()) {
            Card(
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Contact Form Submissions (${contactMessages.size})", style = MaterialTheme.typography.titleLarge)
                    contactMessages.forEach { msg ->
                        Text(
                            text = "${msg.senderName} (${msg.senderEmail}) — ${msg.subject}\n${msg.message}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}
