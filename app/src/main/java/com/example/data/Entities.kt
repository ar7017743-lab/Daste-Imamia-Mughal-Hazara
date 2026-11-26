package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

object YouTubeHelper {
    fun extractVideoId(url: String): String {
        val trimmed = url.trim()
        if (trimmed.isBlank()) return ""
        val patterns = listOf(
            Regex("(?:v=|/v/|embed/|shorts/|live/)([A-Za-z0-9_-]{11})"),
            Regex("youtu\\.be/([A-Za-z0-9_-]{11})")
        )
        for (regex in patterns) {
            val match = regex.find(trimmed)
            if (match != null && match.groupValues.size > 1) {
                return match.groupValues[1]
            }
        }
        return ""
    }

    fun thumbnailUrl(videoId: String): String {
        return if (videoId.isNotBlank()) {
            "https://img.youtube.com/vi/$videoId/hqdefault.jpg"
        } else {
            ""
        }
    }
}

@Entity(tableName = "noha_videos")
data class NohaVideoEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val titleUrdu: String,
    val reciter: String,
    val poet: String,
    val year: Int,
    val category: String, // Muharram, Safar, Ayyam-e-Fatima, Majalis
    val youtubeUrl: String,
    val youtubeVideoId: String,
    val fallbackDrawable: String = "img_hero_banner",
    val description: String,
    val isFeatured: Boolean = false,
    val isHeroFeatured: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "audio_tracks")
data class AudioTrackEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val titleUrdu: String,
    val reciter: String,
    val category: String, // Muharram, Safar, Ayyam-e-Fatima
    val year: Int,
    val durationSeconds: Int,
    val audioUri: String, // "tone://..." for synthesized offline demo, "http..." or "content://..."
    val downloadable: Boolean = true,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "noha_lyrics")
data class NohaLyricsEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val titleUrdu: String,
    val reciter: String,
    val poet: String,
    val category: String,
    val primaryLanguage: String, // Urdu, Persian, Arabic, Roman Urdu
    val lyricsUrdu: String,
    val lyricsPersian: String = "",
    val lyricsArabic: String = "",
    val lyricsRoman: String = "",
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "majalis_programs")
data class MajlisProgramEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val titleUrdu: String,
    val speaker: String,
    val venue: String,
    val dateText: String,
    val timeText: String,
    val category: String, // Muharram, Safar, Ayyam-e-Fatima, Weekly
    val description: String,
    val youtubeUrl: String,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "gallery_photos")
data class GalleryPhotoEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val caption: String,
    val category: String, // Muharram, Majalis, Matami Programs
    val imageUri: String, // "drawable://img_gallery_alam" or file/content URI
    val altText: String,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "islamic_articles")
data class IslamicArticleEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val titleUrdu: String,
    val author: String,
    val category: String,
    val summary: String,
    val content: String,
    val seoDescription: String,
    val publishedDate: String,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "site_settings")
data class SiteSettingsEntity(
    @PrimaryKey val id: Int = 1,
    val siteNameUrdu: String = "دستہ امامیہ مغل ہزارہ",
    val siteSubtitleEnglish: String = "DASTE IMAMIA MUGHAL HAZARA",
    val youtubeChannelUrl: String = "https://www.youtube.com/@Hussainaliofficial465",
    val tiktokHandle: String = "Hussaini Academy",
    val whatsappNumber: String = "+923002145678",
    val contactEmail: String = "info@dasteimamia.org",
    val organizationInfo: String = "Daste Imamia Mughal Hazara • Hussaini Academy Karachi. Dedicated to preserving Noha Khawani, Majalis-e-Aza, Marsiya, and Azadari heritage.",
    val aboutUsText: String = "Daste Imamia Mughal Hazara is a devotional matami and noha khawani congregation committed to keeping the remembrance of Karbala alive through authentic Urdu, Persian, and Arabic elegies, scholarly Majalis, and community outreach in collaboration with Hussaini Academy Karachi.",
    val seoTitle: String = "Daste Imamia Mughal Hazara | New Noha, Majalis & Lyrics",
    val seoMetaDescription: String = "Official portal of Daste Imamia Mughal Hazara & Hussaini Academy Karachi. Watch latest Muharram & Ayyam-e-Fatima Nohas, listen to MP3s, read Urdu Nastaliq lyrics & Majalis.",
    val seoKeywords: String = "Daste Imamia Mughal Hazara, Hussaini Academy Karachi, New Noha, Muharram Noha, Ayyam-e-Fatima Noha, Majalis, Noha Lyrics",
    val navShowMajalis: Boolean = true,
    val navShowGallery: Boolean = true,
    val navShowArticles: Boolean = true
)

@Entity(tableName = "contact_messages")
data class ContactMessageEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val senderName: String,
    val senderEmail: String,
    val subject: String,
    val message: String,
    val createdAt: Long = System.currentTimeMillis()
)
