package com.example.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ContentRepository(private val db: AppDatabase) {

    val videos: Flow<List<NohaVideoEntity>> = db.nohaVideoDao().observeAll()
    val audioTracks: Flow<List<AudioTrackEntity>> = db.audioTrackDao().observeAll()
    val lyrics: Flow<List<NohaLyricsEntity>> = db.nohaLyricsDao().observeAll()
    val majalis: Flow<List<MajlisProgramEntity>> = db.majlisProgramDao().observeAll()
    val galleryPhotos: Flow<List<GalleryPhotoEntity>> = db.galleryPhotoDao().observeAll()
    val articles: Flow<List<IslamicArticleEntity>> = db.islamicArticleDao().observeAll()
    val settings: Flow<SiteSettingsEntity> = db.siteSettingsDao().observeSettings()
        .map { it ?: SiteSettingsEntity() }
    val contactMessages: Flow<List<ContactMessageEntity>> = db.contactMessageDao().observeAll()

    // Videos
    suspend fun saveVideo(video: NohaVideoEntity): Long = db.nohaVideoDao().insert(video)
    suspend fun updateVideo(video: NohaVideoEntity) = db.nohaVideoDao().update(video)
    suspend fun deleteVideo(video: NohaVideoEntity) = db.nohaVideoDao().delete(video)
    suspend fun setHeroFeaturedVideo(videoId: Long) = db.nohaVideoDao().setHeroFeatured(videoId)

    // Audio
    suspend fun saveAudio(track: AudioTrackEntity): Long = db.audioTrackDao().insert(track)
    suspend fun updateAudio(track: AudioTrackEntity) = db.audioTrackDao().update(track)
    suspend fun deleteAudio(track: AudioTrackEntity) = db.audioTrackDao().delete(track)

    // Lyrics
    suspend fun saveLyrics(item: NohaLyricsEntity): Long = db.nohaLyricsDao().insert(item)
    suspend fun updateLyrics(item: NohaLyricsEntity) = db.nohaLyricsDao().update(item)
    suspend fun deleteLyrics(item: NohaLyricsEntity) = db.nohaLyricsDao().delete(item)

    // Majalis
    suspend fun saveMajlis(item: MajlisProgramEntity): Long = db.majlisProgramDao().insert(item)
    suspend fun updateMajlis(item: MajlisProgramEntity) = db.majlisProgramDao().update(item)
    suspend fun deleteMajlis(item: MajlisProgramEntity) = db.majlisProgramDao().delete(item)

    // Gallery
    suspend fun savePhoto(photo: GalleryPhotoEntity): Long = db.galleryPhotoDao().insert(photo)
    suspend fun updatePhoto(photo: GalleryPhotoEntity) = db.galleryPhotoDao().update(photo)
    suspend fun deletePhoto(photo: GalleryPhotoEntity) = db.galleryPhotoDao().delete(photo)

    // Articles
    suspend fun saveArticle(article: IslamicArticleEntity): Long = db.islamicArticleDao().insert(article)
    suspend fun updateArticle(article: IslamicArticleEntity) = db.islamicArticleDao().update(article)
    suspend fun deleteArticle(article: IslamicArticleEntity) = db.islamicArticleDao().delete(article)

    // Settings & Contact
    suspend fun updateSettings(settings: SiteSettingsEntity) = db.siteSettingsDao().upsert(settings)
    suspend fun submitContactMessage(message: ContactMessageEntity) = db.contactMessageDao().insert(message)
}
