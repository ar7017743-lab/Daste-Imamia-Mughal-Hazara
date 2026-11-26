package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        NohaVideoEntity::class,
        AudioTrackEntity::class,
        NohaLyricsEntity::class,
        MajlisProgramEntity::class,
        GalleryPhotoEntity::class,
        IslamicArticleEntity::class,
        SiteSettingsEntity::class,
        ContactMessageEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun nohaVideoDao(): NohaVideoDao
    abstract fun audioTrackDao(): AudioTrackDao
    abstract fun nohaLyricsDao(): NohaLyricsDao
    abstract fun majlisProgramDao(): MajlisProgramDao
    abstract fun galleryPhotoDao(): GalleryPhotoDao
    abstract fun islamicArticleDao(): IslamicArticleDao
    abstract fun siteSettingsDao(): SiteSettingsDao
    abstract fun contactMessageDao(): ContactMessageDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "daste_imamia_cms.db"
                )
                    .addCallback(SeedDatabaseCallback())
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class SeedDatabaseCallback : Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                CoroutineScope(Dispatchers.IO).launch {
                    seedInitialContent(database)
                }
            }
        }
    }
}

private suspend fun seedInitialContent(db: AppDatabase) {
    db.siteSettingsDao().upsert(SiteSettingsEntity())

    val now = System.currentTimeMillis()
    db.nohaVideoDao().insertAll(
        listOf(
            NohaVideoEntity(
                title = "Karbala Ki Khaak Se Aati Hai Sada",
                titleUrdu = "کربلا کی خاک سے آتی ہے صدا",
                reciter = "Hussain Ali (Daste Imamia Mughal Hazara)",
                poet = "Mirza Hasan Raza",
                year = 2026,
                category = "Muharram",
                youtubeUrl = "https://www.youtube.com/@Hussainaliofficial465",
                youtubeVideoId = "",
                fallbackDrawable = "img_hero_banner",
                description = "Official Muharram 1448 AH central Noha presented by Daste Imamia Mughal Hazara in collaboration with Hussaini Academy Karachi.",
                isFeatured = true,
                isHeroFeatured = true,
                createdAt = now
            ),
            NohaVideoEntity(
                title = "Ya Fatimah Zehra (S.A) — Ayyam-e-Fatima",
                titleUrdu = "یا فاطمہ زہرا سلام اللہ علیہا",
                reciter = "Daste Imamia Mughal Hazara",
                poet = "Ustad Qamar Abbas",
                year = 2026,
                category = "Ayyam-e-Fatima",
                youtubeUrl = "https://www.youtube.com/@Hussainaliofficial465",
                youtubeVideoId = "",
                fallbackDrawable = "img_gallery_manuscript",
                description = "Solemn elegy dedicated to Sayyida Fatimah al-Zahra (S.A) recited during Ayyam-e-Fatimiyah.",
                isFeatured = true,
                isHeroFeatured = false,
                createdAt = now - 10_000
            ),
            NohaVideoEntity(
                title = "Qafila Safar Ka Chala Shaam Ki Taraf",
                titleUrdu = "قافلہ صفر کا چلا شام کی طرف",
                reciter = "Hussain Ali Official",
                poet = "Syed Ali Haider",
                year = 2025,
                category = "Safar",
                youtubeUrl = "https://www.youtube.com/@Hussainaliofficial465",
                youtubeVideoId = "",
                fallbackDrawable = "img_gallery_alam",
                description = "Chehlum / Arbaeen commemoration Noha recounting the return of Ahlulbayt to Karbala.",
                isFeatured = true,
                isHeroFeatured = false,
                createdAt = now - 20_000
            ),
            NohaVideoEntity(
                title = "Alamdar-e-Wafa Abbas-e-Dilawar",
                titleUrdu = "علمدارِ وفا عباسِ دلاور",
                reciter = "Daste Imamia Mughal Hazara",
                poet = "Agha Safdar Ali",
                year = 2025,
                category = "Muharram",
                youtubeUrl = "https://www.youtube.com/@Hussainaliofficial465",
                youtubeVideoId = "",
                fallbackDrawable = "img_gallery_majlis",
                description = "Shab-e-Ashoor matami tribute honoring Hazrat Abbas ibn Ali (A.S), the standard-bearer of Karbala.",
                isFeatured = false,
                isHeroFeatured = false,
                createdAt = now - 30_000
            )
        )
    )

    db.audioTrackDao().insertAll(
        listOf(
            AudioTrackEntity(
                title = "Karbala Ki Khaak Se Aati Hai Sada",
                titleUrdu = "کربلا کی خاک سے آتی ہے صدا",
                reciter = "Hussain Ali",
                category = "Muharram",
                year = 2026,
                durationSeconds = 245,
                audioUri = "tone://220",
                downloadable = true,
                createdAt = now
            ),
            AudioTrackEntity(
                title = "Madina Chhoot Raha Hai",
                titleUrdu = "مدینہ چھوٹ رہا ہے",
                reciter = "Daste Imamia Mughal Hazara",
                category = "Muharram",
                year = 2026,
                durationSeconds = 310,
                audioUri = "tone://196",
                downloadable = true,
                createdAt = now - 10_000
            ),
            AudioTrackEntity(
                title = "Chadar-e-Zehra (S.A)",
                titleUrdu = "چادرِ زہرا سلام اللہ علیہا",
                reciter = "Hussaini Academy",
                category = "Ayyam-e-Fatima",
                year = 2025,
                durationSeconds = 278,
                audioUri = "tone://174",
                downloadable = true,
                createdAt = now - 20_000
            ),
            AudioTrackEntity(
                title = "Arbaeen — Labaik Ya Hussain",
                titleUrdu = "لبیک یا حسین علیہ السلام",
                reciter = "Hussain Ali",
                category = "Safar",
                year = 2025,
                durationSeconds = 264,
                audioUri = "tone://246",
                downloadable = false,
                createdAt = now - 30_000
            )
        )
    )

    db.nohaLyricsDao().insertAll(
        listOf(
            NohaLyricsEntity(
                title = "Karbala Ki Khaak Se Aati Hai Sada",
                titleUrdu = "کربلا کی خاک سے آتی ہے صدا",
                reciter = "Hussain Ali",
                poet = "Mirza Hasan Raza",
                category = "Muharram",
                primaryLanguage = "Urdu",
                lyricsUrdu = """کربلا کی خاک سے آتی ہے صدا یا حسین
ہر دلِ مومن کی ہے اب یہی دعا یا حسین

صبر کا پیکر وہ جس نے دیں بچایا خاک پر
آسماں روتا رہا دشتِ نینوا یا حسین

پیاسے بچوں کی صدا گونجی فراتِ نہر پر
تشنگی میں بھی نہ چھوڑا حق کا راستہ یا حسین

دستہ امامیہ مغل ہزارہ کا ہے یہ نوحہ
قیامت تک رہے گا تیرا ماتم بپا یا حسین""",
                lyricsPersian = """از خاکِ کربلا آید ندا یا حسین
هر دلِ مؤمن دارد این دعا یا حسین

آن مظهرِ صبر که دین را نجات داد
آسمان گریست در دشتِ نینوا یا حسین""",
                lyricsArabic = """مِنْ تُرَابِ كَرْبَلَاءَ يَأْتِي النِّدَاءُ يَا حُسَيْن
كُلُّ قَلْبٍ مُؤْمِنٍ يَهْتِفُ وَفَاءً يَا حُسَيْن

سَيِّدُ الشُّهَدَاءِ فِي أَرْضِ الطُّفُوفِ صَامِدٌ
عَلَّمَ الأَحْرَارَ مَعْنَى الإِبَاءِ يَا حُسَيْن""",
                lyricsRoman = """Karbala ki khaak se aati hai sada Ya Hussain
Har dil-e-momin ki hai ab yahi dua Ya Hussain

Sabr ka paikar woh jis ne deen bachaya khaak par
Aasmaan rota raha dasht-e-Nainawa Ya Hussain

Daste Imamia Mughal Hazara ka hai yeh noha
Qayamat tak rahega tera matam bapa Ya Hussain"""
            ),
            NohaLyricsEntity(
                title = "Ya Fatimah Zehra (S.A)",
                titleUrdu = "یا فاطمہ زہرا سلام اللہ علیہا",
                reciter = "Daste Imamia Mughal Hazara",
                poet = "Ustad Qamar Abbas",
                category = "Ayyam-e-Fatima",
                primaryLanguage = "Urdu",
                lyricsUrdu = """یا فاطمہ زہرا سلام اللہ علیہا
امّ ابیہا، سیدۂ نساءِ عالمین

روضۂ رسول پر اندھیرا چھا گیا
جب جہانِ فانی سے زہرا گزر گئیں

حسنین روتے ہیں مصلّے پر بیٹھ کر
شمعِ بتول آج مدینے میں بجھ گئیں""",
                lyricsPersian = """ای فاطمه زهرا سلام الله علیها
شمعِ مدینه در دلِ شب خاموش شد""",
                lyricsArabic = """يَا فَاطِمَةُ الزَّهْرَاءُ سَلَامُ اللَّهِ عَلَيْهَا
بِضْعَةُ الْمُصْطَفَى وَسَيِّدَةُ النِّسَاءِ""",
                lyricsRoman = """Ya Fatimah Zehra Salamullah Alaiha
Umm-e-Abiha, Sayyida-e-Nisa-il-Alameen

Roza-e-Rasool par andhera chha gaya
Jab jahan-e-faani se Zehra guzar gayin"""
            ),
            NohaLyricsEntity(
                title = "Salam Bar Hussain (A.S) — Persian & Arabic",
                titleUrdu = "سلام بر حسین مظلوم",
                reciter = "Hussaini Academy",
                poet = "Allama Muqaddam",
                category = "Safar",
                primaryLanguage = "Persian",
                lyricsUrdu = """سلام بر حسینِ مظلومِ کربلا
جس نے اپنے لہو سے اسلام زندہ کیا""",
                lyricsPersian = """سلام بر حسین، شهیدِ راهِ خدا
سلام بر علمدارِ باوفای کربلا

ای تشنه لبِ فرات، جان‌ها فدای تو
تا ابد زنده است نام و نوای تو""",
                lyricsArabic = """السَّلَامُ عَلَى الْحُسَيْنِ وَعَلَى عَلِيِّ بْنِ الْحُسَيْنِ
وَعَلَى أَوْلَادِ الْحُسَيْنِ وَعَلَى أَصْحَابِ الْحُسَيْنِ""",
                lyricsRoman = """Salam bar Hussain, shaheed-e-rah-e-Khuda
Salam bar Alamdar-e-bawafa-e-Karbala"""
            )
        )
    )

    db.majlisProgramDao().insertAll(
        listOf(
            MajlisProgramEntity(
                title = "Ashra-e-Muharram Central Majlis",
                titleUrdu = "عشرہ محرم الحرام مرکزی مجلسِ عزا",
                speaker = "Allama Syed Jawad Naqvi / Local Ulema",
                venue = "Imambargah Mughal Hazara, Karachi",
                dateText = "1st – 10th Muharram",
                timeText = "8:30 PM PKT",
                category = "Muharram",
                description = "Daily Hadith-e-Kisa, scholarly discourse on the philosophy of Karbala, followed by Noha Khawani & Matamdari by Daste Imamia Mughal Hazara.",
                youtubeUrl = "https://www.youtube.com/@Hussainaliofficial465"
            ),
            MajlisProgramEntity(
                title = "Ayyam-e-Fatima (S.A) Majlis-e-Aza",
                titleUrdu = "ایامِ فاطمیہ سلام اللہ علیہا مجلسِ عزا",
                speaker = "Maulana Raza Abbas",
                venue = "Hussaini Academy Hall, Karachi",
                dateText = "13th – 15th Jamadi-ul-Awwal",
                timeText = "7:45 PM PKT",
                category = "Ayyam-e-Fatima",
                description = "Dedicated commemoration of the martyrdom of Hazrat Fatimah al-Zahra (S.A) with Marsiya and Matam.",
                youtubeUrl = "https://www.youtube.com/@Hussainaliofficial465"
            ),
            MajlisProgramEntity(
                title = "Chehlum / Arbaeen Program",
                titleUrdu = "چہلم شہدائے کربلا — اربعینِ حسینی",
                speaker = "Zakir-e-Ahlulbayt",
                venue = "Markazi Imambargah",
                dateText = "20th Safar",
                timeText = "4:00 PM PKT",
                category = "Safar",
                description = "Annual Arbaeen procession and Majlis honoring the return of the captives of Karbala.",
                youtubeUrl = "https://www.youtube.com/@Hussainaliofficial465"
            )
        )
    )

    db.galleryPhotoDao().insertAll(
        listOf(
            GalleryPhotoEntity(
                title = "Markazi Imambargah Mimbar",
                caption = "Prepared in solemn black and gold for Ashra-e-Muharram Majalis",
                category = "Majalis",
                imageUri = "drawable://img_gallery_majlis",
                altText = "Imambargah hall decorated with black drapes and golden lights for Majlis"
            ),
            GalleryPhotoEntity(
                title = "Alam-e-Hazrat Abbas (A.S)",
                caption = "Traditional golden brass Alam illuminated during Muharram night program",
                category = "Muharram",
                imageUri = "drawable://img_gallery_alam",
                altText = "Golden brass Islamic Alam and lanterns at night"
            ),
            GalleryPhotoEntity(
                title = "Illuminated Marsiya & Noha Bayaz",
                caption = "Preserved classical Urdu and Persian Noha manuscripts of Daste Imamia",
                category = "Matami Programs",
                imageUri = "drawable://img_gallery_manuscript",
                altText = "Open illuminated Islamic poetry manuscript with golden borders"
            ),
            GalleryPhotoEntity(
                title = "Shab-e-Ashoor Shrine Lantern",
                caption = "Sacred geometric light symbolizing the eternal message of Karbala",
                category = "Muharram",
                imageUri = "drawable://img_hero_banner",
                altText = "Golden lantern and Islamic geometric pattern on dark background"
            )
        )
    )

    db.islamicArticleDao().insertAll(
        listOf(
            IslamicArticleEntity(
                title = "The Eternal Philosophy of Karbala and Moral Courage",
                titleUrdu = "فلسفۂ کربلا اور اخلاقی جرات",
                author = "Hussaini Academy Editorial",
                category = "Karbala & History",
                summary = "Exploring how Imam Hussain (A.S) stood for truth, human dignity, and spiritual integrity against tyranny.",
                content = """In the annals of human history, the stand of Imam Hussain ibn Ali (A.S) at Karbala in 61 AH remains an unmatched beacon of moral clarity. Facing overwhelming military might, Imam Hussain refused to legitimize oppression, declaring that dignity is preferable to a life of humiliation.

Through Majalis-e-Aza and Noha Khawani, congregations such as Daste Imamia Mughal Hazara preserve not merely grief, but a living ethical covenant: to uphold justice, compassion for the vulnerable, and steadfast devotion to the teachings of the Prophet Muhammad (PBUH) and his Ahlulbayt (A.S).

Every verse recited in Urdu, Persian, and Arabic during Muharram, Safar, and Ayyam-e-Fatima reconnects believers with patience (Sabr), loyalty (Wafa), and selfless sacrifice.""",
                seoDescription = "Reflective Islamic article on the philosophy of Karbala, Azadari, and Imam Hussain (A.S) by Daste Imamia Mughal Hazara.",
                publishedDate = "Muharram 1448 AH"
            ),
            IslamicArticleEntity(
                title = "The Spiritual Legacy of Sayyida Fatimah al-Zahra (S.A)",
                titleUrdu = "سیرتِ سیدہ فاطمہ زہرا سلام اللہ علیہا",
                author = "Hussaini Academy Karachi",
                category = "Ayyam-e-Fatima",
                summary = "Reflections on the piety, charity, and eloquence of the daughter of the Holy Prophet (PBUH).",
                content = """Sayyida Fatimah al-Zahra (S.A) embodies perfection in worship, humility, and defense of truth. Her sermon of Fadak stands as a timeless testimony of eloquence, spiritual depth, and principled leadership.

During Ayyam-e-Fatima, communities gather in solemn remembrance to study her character—her generosity to the needy, her devotion in night prayer, and her role as the mother of Imam Hasan and Imam Hussain (A.S).""",
                seoDescription = "Islamic article on Sayyida Fatimah al-Zahra (S.A) for Ayyam-e-Fatima.",
                publishedDate = "Jamadi-ul-Awwal 1448 AH"
            )
        )
    )
}
