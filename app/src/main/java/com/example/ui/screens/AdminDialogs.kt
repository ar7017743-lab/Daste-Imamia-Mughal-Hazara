package com.example.ui.screens

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FilterChip
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.data.AudioTrackEntity
import com.example.data.GalleryPhotoEntity
import com.example.data.IslamicArticleEntity
import com.example.data.MajlisProgramEntity
import com.example.data.NohaLyricsEntity
import com.example.data.NohaVideoEntity
import com.example.data.YouTubeHelper
import com.example.ui.theme.BurnishedGold
import com.example.ui.theme.ObsidianBlack
import com.example.util.PlatformHelpers

@Composable
fun VideoEditorDialog(
    initial: NohaVideoEntity?,
    onDismiss: () -> Unit,
    onSave: (
        id: Long,
        title: String,
        titleUrdu: String,
        reciter: String,
        poet: String,
        year: Int,
        category: String,
        youtubeUrl: String,
        description: String,
        isFeatured: Boolean,
        isHeroFeatured: Boolean
    ) -> Unit
) {
    var title by remember { mutableStateOf(initial?.title ?: "") }
    var titleUrdu by remember { mutableStateOf(initial?.titleUrdu ?: "") }
    var reciter by remember { mutableStateOf(initial?.reciter ?: "Hussain Ali") }
    var poet by remember { mutableStateOf(initial?.poet ?: "") }
    var yearStr by remember { mutableStateOf((initial?.year ?: 2026).toString()) }
    var category by remember { mutableStateOf(initial?.category ?: "Muharram") }
    var youtubeUrl by remember { mutableStateOf(initial?.youtubeUrl ?: "https://www.youtube.com/@Hussainaliofficial465") }
    var description by remember { mutableStateOf(initial?.description ?: "") }
    var isFeatured by remember { mutableStateOf(initial?.isFeatured ?: true) }
    var isHeroFeatured by remember { mutableStateOf(initial?.isHeroFeatured ?: false) }

    val extractedId = remember(youtubeUrl) { YouTubeHelper.extractVideoId(youtubeUrl) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (initial == null) "Add Noha Video" else "Edit Noha Video") },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("English Title*") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = titleUrdu, onValueChange = { titleUrdu = it }, label = { Text("Urdu Title") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = reciter, onValueChange = { reciter = it }, label = { Text("Reciter") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = poet, onValueChange = { poet = it }, label = { Text("Poet") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = yearStr, onValueChange = { yearStr = it.filter { c -> c.isDigit() } }, label = { Text("Year") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    listOf("Muharram", "Safar", "Ayyam-e-Fatima").forEach { cat ->
                        FilterChip(selected = category == cat, onClick = { category = cat }, label = { Text(cat) })
                    }
                }
                OutlinedTextField(value = youtubeUrl, onValueChange = { youtubeUrl = it }, label = { Text("YouTube Link") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                if (extractedId.isNotBlank()) {
                    Text("Auto Thumbnail ID detected: $extractedId")
                }
                OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text("Description") }, minLines = 2, modifier = Modifier.fillMaxWidth())
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = isFeatured, onCheckedChange = { isFeatured = it })
                    Text("Featured Collection")
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = isHeroFeatured, onCheckedChange = { isHeroFeatured = it })
                    Text("Homepage Hero Video")
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank()) {
                        onSave(
                            initial?.id ?: 0L,
                            title,
                            titleUrdu,
                            reciter,
                            poet,
                            yearStr.toIntOrNull() ?: 2026,
                            category,
                            youtubeUrl,
                            description,
                            isFeatured,
                            isHeroFeatured
                        )
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = BurnishedGold, contentColor = ObsidianBlack)
            ) { Text("Save") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}

@Composable
fun AudioEditorDialog(
    initial: AudioTrackEntity?,
    onDismiss: () -> Unit,
    onSave: (
        id: Long,
        title: String,
        titleUrdu: String,
        reciter: String,
        category: String,
        year: Int,
        durationSeconds: Int,
        audioUri: String,
        downloadable: Boolean
    ) -> Unit
) {
    val context = LocalContext.current
    var title by remember { mutableStateOf(initial?.title ?: "") }
    var titleUrdu by remember { mutableStateOf(initial?.titleUrdu ?: "") }
    var reciter by remember { mutableStateOf(initial?.reciter ?: "Hussain Ali") }
    var category by remember { mutableStateOf(initial?.category ?: "Muharram") }
    var yearStr by remember { mutableStateOf((initial?.year ?: 2026).toString()) }
    var durationStr by remember { mutableStateOf((initial?.durationSeconds ?: 240).toString()) }
    var audioUri by remember { mutableStateOf(initial?.audioUri ?: "tone://220") }
    var downloadable by remember { mutableStateOf(initial?.downloadable ?: true) }

    val audioPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        if (uri != null) {
            val copied = PlatformHelpers.copyPickedAudioToInternalStorage(context, uri)
            if (copied != null) {
                audioUri = copied
                Toast.makeText(context, "MP3 audio attached", Toast.LENGTH_SHORT).show()
            }
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (initial == null) "Upload / Add MP3 Audio" else "Edit Audio Track") },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Noha Title*") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = titleUrdu, onValueChange = { titleUrdu = it }, label = { Text("Urdu Title") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = reciter, onValueChange = { reciter = it }, label = { Text("Reciter") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = yearStr, onValueChange = { yearStr = it.filter { c -> c.isDigit() } }, label = { Text("Year") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = durationStr, onValueChange = { durationStr = it.filter { c -> c.isDigit() } }, label = { Text("Duration (seconds)") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    listOf("Muharram", "Safar", "Ayyam-e-Fatima").forEach { cat ->
                        FilterChip(selected = category == cat, onClick = { category = cat }, label = { Text(cat) })
                    }
                }
                OutlinedButton(
                    onClick = { audioPicker.launch(arrayOf("audio/*")) },
                    modifier = Modifier.fillMaxWidth()
                ) { Text("Pick Local MP3 File") }
                OutlinedTextField(value = audioUri, onValueChange = { audioUri = it }, label = { Text("Audio URL or File Path") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = downloadable, onCheckedChange = { downloadable = it })
                    Text("Permit MP3 Download")
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank()) {
                        onSave(
                            initial?.id ?: 0L,
                            title,
                            titleUrdu,
                            reciter,
                            category,
                            yearStr.toIntOrNull() ?: 2026,
                            durationStr.toIntOrNull() ?: 240,
                            audioUri,
                            downloadable
                        )
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = BurnishedGold, contentColor = ObsidianBlack)
            ) { Text("Save") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}

@Composable
fun LyricsEditorDialog(
    initial: NohaLyricsEntity?,
    onDismiss: () -> Unit,
    onSave: (
        id: Long,
        title: String,
        titleUrdu: String,
        reciter: String,
        poet: String,
        category: String,
        primaryLanguage: String,
        lyricsUrdu: String,
        lyricsPersian: String,
        lyricsArabic: String,
        lyricsRoman: String
    ) -> Unit
) {
    var title by remember { mutableStateOf(initial?.title ?: "") }
    var titleUrdu by remember { mutableStateOf(initial?.titleUrdu ?: "") }
    var reciter by remember { mutableStateOf(initial?.reciter ?: "Hussain Ali") }
    var poet by remember { mutableStateOf(initial?.poet ?: "") }
    var category by remember { mutableStateOf(initial?.category ?: "Muharram") }
    var primaryLanguage by remember { mutableStateOf(initial?.primaryLanguage ?: "Urdu") }
    var lyricsUrdu by remember { mutableStateOf(initial?.lyricsUrdu ?: "") }
    var lyricsPersian by remember { mutableStateOf(initial?.lyricsPersian ?: "") }
    var lyricsArabic by remember { mutableStateOf(initial?.lyricsArabic ?: "") }
    var lyricsRoman by remember { mutableStateOf(initial?.lyricsRoman ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (initial == null) "Publish Noha Lyrics" else "Edit Noha Lyrics") },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Title*") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = titleUrdu, onValueChange = { titleUrdu = it }, label = { Text("Urdu Title") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = reciter, onValueChange = { reciter = it }, label = { Text("Reciter") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = poet, onValueChange = { poet = it }, label = { Text("Poet") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    listOf("Muharram", "Safar", "Ayyam-e-Fatima").forEach { cat ->
                        FilterChip(selected = category == cat, onClick = { category = cat }, label = { Text(cat) })
                    }
                }
                OutlinedTextField(value = lyricsUrdu, onValueChange = { lyricsUrdu = it }, label = { Text("Urdu Lyrics (RTL)") }, minLines = 3, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = lyricsPersian, onValueChange = { lyricsPersian = it }, label = { Text("Persian Lyrics (RTL)") }, minLines = 2, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = lyricsArabic, onValueChange = { lyricsArabic = it }, label = { Text("Arabic Lyrics (RTL)") }, minLines = 2, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = lyricsRoman, onValueChange = { lyricsRoman = it }, label = { Text("Roman Urdu Lyrics") }, minLines = 2, modifier = Modifier.fillMaxWidth())
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank()) {
                        onSave(
                            initial?.id ?: 0L,
                            title,
                            titleUrdu,
                            reciter,
                            poet,
                            category,
                            primaryLanguage,
                            lyricsUrdu,
                            lyricsPersian,
                            lyricsArabic,
                            lyricsRoman
                        )
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = BurnishedGold, contentColor = ObsidianBlack)
            ) { Text("Publish") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}

@Composable
fun GalleryPhotoEditorDialog(
    initial: GalleryPhotoEntity?,
    onDismiss: () -> Unit,
    onSave: (id: Long, title: String, caption: String, category: String, imageUri: String, altText: String) -> Unit
) {
    val context = LocalContext.current
    var title by remember { mutableStateOf(initial?.title ?: "") }
    var caption by remember { mutableStateOf(initial?.caption ?: "") }
    var category by remember { mutableStateOf(initial?.category ?: "Muharram") }
    var imageUri by remember { mutableStateOf(initial?.imageUri ?: "drawable://img_gallery_alam") }
    var altText by remember { mutableStateOf(initial?.altText ?: "") }

    // Zero-permission Android Photo Picker
    val photoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            val copied = PlatformHelpers.copyPickedImageToInternalStorage(context, uri)
            if (copied != null) {
                imageUri = copied
                Toast.makeText(context, "Photo selected", Toast.LENGTH_SHORT).show()
            }
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (initial == null) "Upload Gallery Photo" else "Edit Gallery Photo") },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = {
                        photoPicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                    },
                    modifier = Modifier.fillMaxWidth()
                ) { Text("Choose Photo from Device") }
                OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Photo Title*") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = caption, onValueChange = { caption = it }, label = { Text("Caption") }, minLines = 2, modifier = Modifier.fillMaxWidth())
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    listOf("Muharram", "Majalis", "Matami Programs").forEach { cat ->
                        FilterChip(selected = category == cat, onClick = { category = cat }, label = { Text(cat) })
                    }
                }
                OutlinedTextField(value = altText, onValueChange = { altText = it }, label = { Text("SEO Image Alt Text") }, singleLine = true, modifier = Modifier.fillMaxWidth())
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank()) {
                        onSave(initial?.id ?: 0L, title, caption, category, imageUri, altText)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = BurnishedGold, contentColor = ObsidianBlack)
            ) { Text("Save") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}

@Composable
fun MajlisEditorDialog(
    initial: MajlisProgramEntity?,
    onDismiss: () -> Unit,
    onSave: (
        id: Long,
        title: String,
        titleUrdu: String,
        speaker: String,
        venue: String,
        dateText: String,
        timeText: String,
        category: String,
        description: String,
        youtubeUrl: String
    ) -> Unit
) {
    var title by remember { mutableStateOf(initial?.title ?: "") }
    var titleUrdu by remember { mutableStateOf(initial?.titleUrdu ?: "") }
    var speaker by remember { mutableStateOf(initial?.speaker ?: "") }
    var venue by remember { mutableStateOf(initial?.venue ?: "") }
    var dateText by remember { mutableStateOf(initial?.dateText ?: "") }
    var timeText by remember { mutableStateOf(initial?.timeText ?: "") }
    var category by remember { mutableStateOf(initial?.category ?: "Muharram") }
    var description by remember { mutableStateOf(initial?.description ?: "") }
    var youtubeUrl by remember { mutableStateOf(initial?.youtubeUrl ?: "https://www.youtube.com/@Hussainaliofficial465") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (initial == null) "Add Majlis Program" else "Edit Majlis Program") },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Title*") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = titleUrdu, onValueChange = { titleUrdu = it }, label = { Text("Urdu Title") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = speaker, onValueChange = { speaker = it }, label = { Text("Speaker / Zakir") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = venue, onValueChange = { venue = it }, label = { Text("Venue") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = dateText, onValueChange = { dateText = it }, label = { Text("Date") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = timeText, onValueChange = { timeText = it }, label = { Text("Time") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    listOf("Muharram", "Safar", "Ayyam-e-Fatima", "Weekly").forEach { cat ->
                        FilterChip(selected = category == cat, onClick = { category = cat }, label = { Text(cat) })
                    }
                }
                OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text("Description") }, minLines = 2, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = youtubeUrl, onValueChange = { youtubeUrl = it }, label = { Text("YouTube Stream Link") }, singleLine = true, modifier = Modifier.fillMaxWidth())
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank()) {
                        onSave(initial?.id ?: 0L, title, titleUrdu, speaker, venue, dateText, timeText, category, description, youtubeUrl)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = BurnishedGold, contentColor = ObsidianBlack)
            ) { Text("Save") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}

@Composable
fun ArticleEditorDialog(
    initial: IslamicArticleEntity?,
    onDismiss: () -> Unit,
    onSave: (
        id: Long,
        title: String,
        titleUrdu: String,
        author: String,
        category: String,
        summary: String,
        content: String,
        seoDescription: String,
        publishedDate: String
    ) -> Unit
) {
    var title by remember { mutableStateOf(initial?.title ?: "") }
    var titleUrdu by remember { mutableStateOf(initial?.titleUrdu ?: "") }
    var author by remember { mutableStateOf(initial?.author ?: "Hussaini Academy") }
    var category by remember { mutableStateOf(initial?.category ?: "Karbala & History") }
    var summary by remember { mutableStateOf(initial?.summary ?: "") }
    var content by remember { mutableStateOf(initial?.content ?: "") }
    var seoDescription by remember { mutableStateOf(initial?.seoDescription ?: "") }
    var publishedDate by remember { mutableStateOf(initial?.publishedDate ?: "1448 AH") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (initial == null) "Publish Islamic Article" else "Edit Islamic Article") },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Article Title*") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = titleUrdu, onValueChange = { titleUrdu = it }, label = { Text("Urdu Title") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = author, onValueChange = { author = it }, label = { Text("Author") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = category, onValueChange = { category = it }, label = { Text("Category") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = publishedDate, onValueChange = { publishedDate = it }, label = { Text("Date") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = summary, onValueChange = { summary = it }, label = { Text("Summary") }, minLines = 2, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = content, onValueChange = { content = it }, label = { Text("Article Body") }, minLines = 4, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = seoDescription, onValueChange = { seoDescription = it }, label = { Text("SEO Meta Description") }, minLines = 2, modifier = Modifier.fillMaxWidth())
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank()) {
                        onSave(initial?.id ?: 0L, title, titleUrdu, author, category, summary, content, seoDescription, publishedDate)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = BurnishedGold, contentColor = ObsidianBlack)
            ) { Text("Publish") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}
