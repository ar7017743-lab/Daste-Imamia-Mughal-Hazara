package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppDatabase
import com.example.data.AudioTrackEntity
import com.example.data.ContactMessageEntity
import com.example.data.ContentRepository
import com.example.data.GalleryPhotoEntity
import com.example.data.IslamicArticleEntity
import com.example.data.MajlisProgramEntity
import com.example.data.NohaLyricsEntity
import com.example.data.NohaVideoEntity
import com.example.data.SiteSettingsEntity
import com.example.data.YouTubeHelper
import com.example.media.AudioPlaybackState
import com.example.media.AudioPlayerController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class AppLanguage(val label: String, val nativeLabel: String, val isRtl: Boolean) {
    ENGLISH("English", "EN", false),
    URDU("Urdu", "اردو", true),
    ARABIC("Arabic", "العربية", true)
}

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ContentRepository(AppDatabase.getInstance(application))
    val audioController = AudioPlayerController(application)

    val videos: StateFlow<List<NohaVideoEntity>> = repository.videos
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val audioTracks: StateFlow<List<AudioTrackEntity>> = repository.audioTracks
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val lyrics: StateFlow<List<NohaLyricsEntity>> = repository.lyrics
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val majalis: StateFlow<List<MajlisProgramEntity>> = repository.majalis
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val galleryPhotos: StateFlow<List<GalleryPhotoEntity>> = repository.galleryPhotos
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val articles: StateFlow<List<IslamicArticleEntity>> = repository.articles
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val settings: StateFlow<SiteSettingsEntity> = repository.settings
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), SiteSettingsEntity())

    val contactMessages: StateFlow<List<ContactMessageEntity>> = repository.contactMessages
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val playbackState: StateFlow<AudioPlaybackState> = audioController.state

    private val _appLanguage = MutableStateFlow(AppLanguage.ENGLISH)
    val appLanguage: StateFlow<AppLanguage> = _appLanguage.asStateFlow()

    private val _isDarkTheme = MutableStateFlow(true)
    val isDarkTheme: StateFlow<Boolean> = _isDarkTheme.asStateFlow()

    fun setLanguage(lang: AppLanguage) {
        _appLanguage.value = lang
    }

    fun toggleTheme() {
        _isDarkTheme.value = !_isDarkTheme.value
    }

    // Audio actions
    fun toggleAudio(track: AudioTrackEntity) = audioController.togglePlayPause(track)
    fun seekAudio(ms: Int) = audioController.seekTo(ms)
    fun stopAudio() = audioController.stop()

    // Admin Video CRUD
    fun saveVideo(
        id: Long = 0,
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
    ) {
        viewModelScope.launch {
            val videoId = YouTubeHelper.extractVideoId(youtubeUrl)
            val entity = NohaVideoEntity(
                id = id,
                title = title.trim(),
                titleUrdu = titleUrdu.trim(),
                reciter = reciter.trim(),
                poet = poet.trim(),
                year = year,
                category = category,
                youtubeUrl = youtubeUrl.trim(),
                youtubeVideoId = videoId,
                fallbackDrawable = "img_hero_banner",
                description = description.trim(),
                isFeatured = isFeatured,
                isHeroFeatured = isHeroFeatured
            )
            val savedId = repository.saveVideo(entity)
            if (isHeroFeatured) {
                repository.setHeroFeaturedVideo(if (id > 0) id else savedId)
            }
        }
    }

    fun deleteVideo(video: NohaVideoEntity) {
        viewModelScope.launch { repository.deleteVideo(video) }
    }

    fun markHeroFeatured(videoId: Long) {
        viewModelScope.launch { repository.setHeroFeaturedVideo(videoId) }
    }

    // Admin Audio CRUD
    fun saveAudio(
        id: Long = 0,
        title: String,
        titleUrdu: String,
        reciter: String,
        category: String,
        year: Int,
        durationSeconds: Int,
        audioUri: String,
        downloadable: Boolean
    ) {
        viewModelScope.launch {
            repository.saveAudio(
                AudioTrackEntity(
                    id = id,
                    title = title.trim(),
                    titleUrdu = titleUrdu.trim(),
                    reciter = reciter.trim(),
                    category = category,
                    year = year,
                    durationSeconds = durationSeconds.coerceAtLeast(30),
                    audioUri = audioUri.trim().ifBlank { "tone://220" },
                    downloadable = downloadable
                )
            )
        }
    }

    fun deleteAudio(track: AudioTrackEntity) {
        viewModelScope.launch {
            if (playbackState.value.currentTrack?.id == track.id) {
                audioController.stop()
            }
            repository.deleteAudio(track)
        }
    }

    // Admin Lyrics CRUD
    fun saveLyrics(
        id: Long = 0,
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
    ) {
        viewModelScope.launch {
            repository.saveLyrics(
                NohaLyricsEntity(
                    id = id,
                    title = title.trim(),
                    titleUrdu = titleUrdu.trim(),
                    reciter = reciter.trim(),
                    poet = poet.trim(),
                    category = category,
                    primaryLanguage = primaryLanguage,
                    lyricsUrdu = lyricsUrdu.trim(),
                    lyricsPersian = lyricsPersian.trim(),
                    lyricsArabic = lyricsArabic.trim(),
                    lyricsRoman = lyricsRoman.trim()
                )
            )
        }
    }

    fun deleteLyrics(item: NohaLyricsEntity) {
        viewModelScope.launch { repository.deleteLyrics(item) }
    }

    // Admin Majalis CRUD
    fun saveMajlis(
        id: Long = 0,
        title: String,
        titleUrdu: String,
        speaker: String,
        venue: String,
        dateText: String,
        timeText: String,
        category: String,
        description: String,
        youtubeUrl: String
    ) {
        viewModelScope.launch {
            repository.saveMajlis(
                MajlisProgramEntity(
                    id = id,
                    title = title.trim(),
                    titleUrdu = titleUrdu.trim(),
                    speaker = speaker.trim(),
                    venue = venue.trim(),
                    dateText = dateText.trim(),
                    timeText = timeText.trim(),
                    category = category,
                    description = description.trim(),
                    youtubeUrl = youtubeUrl.trim()
                )
            )
        }
    }

    fun deleteMajlis(item: MajlisProgramEntity) {
        viewModelScope.launch { repository.deleteMajlis(item) }
    }

    // Admin Gallery CRUD
    fun savePhoto(
        id: Long = 0,
        title: String,
        caption: String,
        category: String,
        imageUri: String,
        altText: String
    ) {
        viewModelScope.launch {
            repository.savePhoto(
                GalleryPhotoEntity(
                    id = id,
                    title = title.trim(),
                    caption = caption.trim(),
                    category = category,
                    imageUri = imageUri.trim().ifBlank { "drawable://img_gallery_alam" },
                    altText = altText.trim().ifBlank { title.trim() }
                )
            )
        }
    }

    fun deletePhoto(photo: GalleryPhotoEntity) {
        viewModelScope.launch { repository.deletePhoto(photo) }
    }

    // Admin Articles CRUD
    fun saveArticle(
        id: Long = 0,
        title: String,
        titleUrdu: String,
        author: String,
        category: String,
        summary: String,
        content: String,
        seoDescription: String,
        publishedDate: String
    ) {
        viewModelScope.launch {
            repository.saveArticle(
                IslamicArticleEntity(
                    id = id,
                    title = title.trim(),
                    titleUrdu = titleUrdu.trim(),
                    author = author.trim(),
                    category = category,
                    summary = summary.trim(),
                    content = content.trim(),
                    seoDescription = seoDescription.trim(),
                    publishedDate = publishedDate.trim()
                )
            )
        }
    }

    fun deleteArticle(article: IslamicArticleEntity) {
        viewModelScope.launch { repository.deleteArticle(article) }
    }

    // Admin Site Settings & SEO
    fun saveSettings(updated: SiteSettingsEntity) {
        viewModelScope.launch { repository.updateSettings(updated) }
    }

    // Contact Submission
    fun submitContact(name: String, email: String, subject: String, message: String) {
        viewModelScope.launch {
            repository.submitContactMessage(
                ContactMessageEntity(
                    senderName = name.trim(),
                    senderEmail = email.trim(),
                    subject = subject.trim(),
                    message = message.trim()
                )
            )
        }
    }

    override fun onCleared() {
        audioController.release()
        super.onCleared()
    }
}
