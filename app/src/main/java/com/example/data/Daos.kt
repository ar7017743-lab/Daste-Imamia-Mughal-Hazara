package com.example.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface NohaVideoDao {
    @Query("SELECT * FROM noha_videos ORDER BY year DESC, createdAt DESC")
    fun observeAll(): Flow<List<NohaVideoEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(video: NohaVideoEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(videos: List<NohaVideoEntity>)

    @Update
    suspend fun update(video: NohaVideoEntity)

    @Delete
    suspend fun delete(video: NohaVideoEntity)

    @Query("UPDATE noha_videos SET isHeroFeatured = CASE WHEN id = :videoId THEN 1 ELSE 0 END")
    suspend fun setHeroFeatured(videoId: Long)
}

@Dao
interface AudioTrackDao {
    @Query("SELECT * FROM audio_tracks ORDER BY year DESC, createdAt DESC")
    fun observeAll(): Flow<List<AudioTrackEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(track: AudioTrackEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(tracks: List<AudioTrackEntity>)

    @Update
    suspend fun update(track: AudioTrackEntity)

    @Delete
    suspend fun delete(track: AudioTrackEntity)
}

@Dao
interface NohaLyricsDao {
    @Query("SELECT * FROM noha_lyrics ORDER BY createdAt DESC")
    fun observeAll(): Flow<List<NohaLyricsEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(lyrics: NohaLyricsEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<NohaLyricsEntity>)

    @Update
    suspend fun update(lyrics: NohaLyricsEntity)

    @Delete
    suspend fun delete(lyrics: NohaLyricsEntity)
}

@Dao
interface MajlisProgramDao {
    @Query("SELECT * FROM majalis_programs ORDER BY createdAt DESC")
    fun observeAll(): Flow<List<MajlisProgramEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(program: MajlisProgramEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(programs: List<MajlisProgramEntity>)

    @Update
    suspend fun update(program: MajlisProgramEntity)

    @Delete
    suspend fun delete(program: MajlisProgramEntity)
}

@Dao
interface GalleryPhotoDao {
    @Query("SELECT * FROM gallery_photos ORDER BY createdAt DESC")
    fun observeAll(): Flow<List<GalleryPhotoEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(photo: GalleryPhotoEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(photos: List<GalleryPhotoEntity>)

    @Update
    suspend fun update(photo: GalleryPhotoEntity)

    @Delete
    suspend fun delete(photo: GalleryPhotoEntity)
}

@Dao
interface IslamicArticleDao {
    @Query("SELECT * FROM islamic_articles ORDER BY createdAt DESC")
    fun observeAll(): Flow<List<IslamicArticleEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(article: IslamicArticleEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(articles: List<IslamicArticleEntity>)

    @Update
    suspend fun update(article: IslamicArticleEntity)

    @Delete
    suspend fun delete(article: IslamicArticleEntity)
}

@Dao
interface SiteSettingsDao {
    @Query("SELECT * FROM site_settings WHERE id = 1")
    fun observeSettings(): Flow<SiteSettingsEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(settings: SiteSettingsEntity)
}

@Dao
interface ContactMessageDao {
    @Query("SELECT * FROM contact_messages ORDER BY createdAt DESC")
    fun observeAll(): Flow<List<ContactMessageEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(message: ContactMessageEntity): Long
}
