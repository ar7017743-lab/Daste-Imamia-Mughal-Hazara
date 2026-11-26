package com.example.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.print.PrintAttributes
import android.print.PrintManager
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import com.example.R
import com.example.data.AudioTrackEntity
import com.example.data.NohaLyricsEntity
import com.example.data.SiteSettingsEntity
import com.example.media.AudioPlayerController
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.net.URLEncoder
import java.util.UUID

object PlatformHelpers {

    fun openExternalUrl(context: Context, rawUrl: String) {
        val url = if (rawUrl.startsWith("http://") || rawUrl.startsWith("https://")) {
            rawUrl
        } else {
            "https://$rawUrl"
        }
        runCatching {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }.onFailure {
            Toast.makeText(context, "Unable to open link", Toast.LENGTH_SHORT).show()
        }
    }

    fun openWhatsApp(context: Context, phoneNumber: String, message: String) {
        val digitsOnly = phoneNumber.filter { it.isDigit() }
        if (digitsOnly.isBlank()) {
            Toast.makeText(context, "WhatsApp number not configured", Toast.LENGTH_SHORT).show()
            return
        }
        val encoded = URLEncoder.encode(message, "UTF-8")
        val url = "https://wa.me/$digitsOnly?text=$encoded"
        openExternalUrl(context, url)
    }

    fun sendEmail(context: Context, email: String, subject: String, body: String) {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, body)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        runCatching {
            context.startActivity(Intent.createChooser(intent, "Send Email"))
        }.onFailure {
            Toast.makeText(context, "No email app available", Toast.LENGTH_SHORT).show()
        }
    }

    fun shareText(context: Context, title: String, text: String) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, title)
            putExtra(Intent.EXTRA_TEXT, text)
        }
        runCatching {
            context.startActivity(Intent.createChooser(intent, "Share"))
        }
    }

    fun resolveDrawableRes(name: String): Int {
        return when (name) {
            "img_hero_banner" -> R.drawable.img_hero_banner
            "img_gallery_majlis" -> R.drawable.img_gallery_majlis
            "img_gallery_alam" -> R.drawable.img_gallery_alam
            "img_gallery_manuscript" -> R.drawable.img_gallery_manuscript
            "daste_imamia_icon" -> R.drawable.daste_imamia_icon
            else -> R.drawable.img_hero_banner
        }
    }

    fun copyPickedImageToInternalStorage(context: Context, sourceUri: Uri): String? {
        return runCatching {
            val dir = File(context.filesDir, "gallery").apply { mkdirs() }
            val outFile = File(dir, "photo_${UUID.randomUUID()}.jpg")
            context.contentResolver.openInputStream(sourceUri)?.use { input ->
                FileOutputStream(outFile).use { output ->
                    input.copyTo(output)
                }
            }
            outFile.absolutePath
        }.getOrNull()
    }

    fun copyPickedAudioToInternalStorage(context: Context, sourceUri: Uri): String? {
        return runCatching {
            val dir = File(context.filesDir, "audio").apply { mkdirs() }
            val outFile = File(dir, "noha_${UUID.randomUUID()}.mp3")
            context.contentResolver.openInputStream(sourceUri)?.use { input ->
                FileOutputStream(outFile).use { output ->
                    input.copyTo(output)
                }
            }
            outFile.absolutePath
        }.getOrNull()
    }

    fun exportAudioTrackToUri(
        context: Context,
        track: AudioTrackEntity,
        destinationUri: Uri,
        playerController: AudioPlayerController
    ): Boolean {
        return runCatching {
            val sourceFile: File? = when {
                track.audioUri.startsWith("tone://") -> {
                    val freq = track.audioUri.removePrefix("tone://").toDoubleOrNull() ?: 220.0
                    playerController.ensureReverentDroneWav(freq, track.id)
                }
                track.audioUri.startsWith("/") || track.audioUri.startsWith("file://") -> {
                    File(track.audioUri.removePrefix("file://"))
                }
                else -> null
            }

            context.contentResolver.openOutputStream(destinationUri)?.use { output ->
                if (sourceFile != null && sourceFile.exists()) {
                    FileInputStream(sourceFile).use { input -> input.copyTo(output) }
                } else {
                    context.contentResolver.openInputStream(Uri.parse(track.audioUri))?.use { input ->
                        input.copyTo(output)
                    }
                }
            }
            true
        }.getOrDefault(false)
    }

    fun printLyrics(context: Context, lyrics: NohaLyricsEntity, settings: SiteSettingsEntity) {
        val webView = WebView(context)
        val escapedUrdu = escapeHtml(lyrics.lyricsUrdu).replace("\n", "<br/>")
        val escapedPersian = escapeHtml(lyrics.lyricsPersian).replace("\n", "<br/>")
        val escapedArabic = escapeHtml(lyrics.lyricsArabic).replace("\n", "<br/>")
        val escapedRoman = escapeHtml(lyrics.lyricsRoman).replace("\n", "<br/>")

        val html = """
            <!DOCTYPE html>
            <html>
            <head>
              <meta charset="utf-8" />
              <style>
                body { font-family: sans-serif; padding: 28px; color: #161512; background: #fff; }
                .header { text-align: center; border-bottom: 2px solid #D4AF37; padding-bottom: 12px; margin-bottom: 20px; }
                .brand-ur { font-size: 26px; font-weight: bold; color: #9C7A23; direction: rtl; }
                .brand-en { font-size: 11px; letter-spacing: 2px; color: #444; margin-top: 4px; }
                .title { font-size: 20px; font-weight: bold; margin-bottom: 4px; }
                .title-ur { font-size: 22px; direction: rtl; text-align: right; color: #111; margin: 8px 0; }
                .meta { font-size: 13px; color: #555; margin-bottom: 18px; }
                .section-title { font-size: 12px; text-transform: uppercase; letter-spacing: 1px; color: #9C7A23; margin-top: 18px; border-bottom: 1px solid #eee; padding-bottom: 4px; }
                .rtl-block { direction: rtl; text-align: right; font-size: 18px; line-height: 2.0; margin-top: 8px; }
                .ltr-block { direction: ltr; text-align: left; font-size: 15px; line-height: 1.7; margin-top: 8px; }
              </style>
            </head>
            <body>
              <div class="header">
                <div class="brand-ur">${escapeHtml(settings.siteNameUrdu)}</div>
                <div class="brand-en">${escapeHtml(settings.siteSubtitleEnglish)}</div>
              </div>
              <div class="title">${escapeHtml(lyrics.title)}</div>
              <div class="title-ur">${escapeHtml(lyrics.titleUrdu)}</div>
              <div class="meta">Reciter: ${escapeHtml(lyrics.reciter)} &bull; Poet: ${escapeHtml(lyrics.poet)} &bull; Category: ${escapeHtml(lyrics.category)}</div>
              ${if (lyrics.lyricsUrdu.isNotBlank()) "<div class='section-title'>Urdu</div><div class='rtl-block'>$escapedUrdu</div>" else ""}
              ${if (lyrics.lyricsPersian.isNotBlank()) "<div class='section-title'>Persian</div><div class='rtl-block'>$escapedPersian</div>" else ""}
              ${if (lyrics.lyricsArabic.isNotBlank()) "<div class='section-title'>Arabic</div><div class='rtl-block'>$escapedArabic</div>" else ""}
              ${if (lyrics.lyricsRoman.isNotBlank()) "<div class='section-title'>Roman Urdu</div><div class='ltr-block'>$escapedRoman</div>" else ""}
            </body>
            </html>
        """.trimIndent()

        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                val printManager = context.getSystemService(Context.PRINT_SERVICE) as? PrintManager
                if (printManager != null && view != null) {
                    val jobName = "${lyrics.title} - Noha Lyrics"
                    val adapter = view.createPrintDocumentAdapter(jobName)
                    printManager.print(jobName, adapter, PrintAttributes.Builder().build())
                }
            }
        }
        webView.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null)
    }

    private fun escapeHtml(input: String): String {
        return input
            .replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace("\"", "&quot;")
    }

    fun formatDuration(seconds: Int): String {
        val mins = seconds / 60
        val secs = seconds % 60
        return "%d:%02d".format(mins, secs)
    }

    fun formatMillis(ms: Int): String = formatDuration((ms / 1000).coerceAtLeast(0))
}
