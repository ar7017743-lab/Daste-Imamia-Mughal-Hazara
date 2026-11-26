package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.ui.MainViewModel
import com.example.ui.components.MiniAudioPlayerBar
import com.example.ui.components.UrduCalligraphyBrandHeader
import com.example.ui.screens.AboutUsScreen
import com.example.ui.screens.AdminCmsScreen
import com.example.ui.screens.ArticleDetailScreen
import com.example.ui.screens.ArticlesListScreen
import com.example.ui.screens.AudioScreen
import com.example.ui.screens.ContactUsScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.LyricsDetailScreen
import com.example.ui.screens.LyricsListScreen
import com.example.ui.screens.MajalisScreen
import com.example.ui.screens.MoreHubScreen
import com.example.ui.screens.PhotoGalleryScreen
import com.example.ui.screens.VideoDetailScreen
import com.example.ui.screens.VideosArchiveScreen
import com.example.ui.theme.BurnishedGold
import com.example.ui.theme.ChampagneGold
import com.example.ui.theme.DasteImamiaTheme
import com.example.ui.theme.NastaliqUrduFontFamily
import com.example.ui.theme.ObsidianBlack
import kotlinx.coroutines.launch

object Routes {
    const val HOME = "home"
    const val VIDEOS = "videos"
    const val VIDEO_DETAIL = "video_detail/{videoId}"
    const val AUDIO = "audio"
    const val LYRICS = "lyrics"
    const val LYRICS_DETAIL = "lyrics_detail/{lyricsId}"
    const val MORE = "more"
    const val MAJALIS = "majalis"
    const val GALLERY = "gallery"
    const val ARTICLES = "articles"
    const val ARTICLE_DETAIL = "article_detail/{articleId}"
    const val ABOUT = "about"
    const val CONTACT = "contact"
    const val ADMIN = "admin"
}

private data class NavDestinationItem(
    val route: String,
    val label: String,
    val icon: ImageVector
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: MainViewModel = viewModel()
            val isDarkTheme by viewModel.isDarkTheme.collectAsStateWithLifecycle()

            DasteImamiaTheme(darkTheme = isDarkTheme) {
                DasteImamiaApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun DasteImamiaApp(viewModel: MainViewModel) {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val settings by viewModel.settings.collectAsStateWithLifecycle()
    val language by viewModel.appLanguage.collectAsStateWithLifecycle()
    val isDarkTheme by viewModel.isDarkTheme.collectAsStateWithLifecycle()
    val videos by viewModel.videos.collectAsStateWithLifecycle()
    val audioTracks by viewModel.audioTracks.collectAsStateWithLifecycle()
    val lyrics by viewModel.lyrics.collectAsStateWithLifecycle()
    val majalis by viewModel.majalis.collectAsStateWithLifecycle()
    val galleryPhotos by viewModel.galleryPhotos.collectAsStateWithLifecycle()
    val articles by viewModel.articles.collectAsStateWithLifecycle()
    val playbackState by viewModel.playbackState.collectAsStateWithLifecycle()
    val contactMessages by viewModel.contactMessages.collectAsStateWithLifecycle()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: Routes.HOME

    fun navigateTopLevel(route: String) {
        navController.navigate(route) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    val primaryTabs = listOf(
        NavDestinationItem(Routes.HOME, "Home", Icons.Default.Home),
        NavDestinationItem(Routes.VIDEOS, "Nohas", Icons.Default.PlayCircle),
        NavDestinationItem(Routes.AUDIO, "Audio", Icons.Default.LibraryMusic),
        NavDestinationItem(Routes.LYRICS, "Lyrics", Icons.AutoMirrored.Filled.MenuBook),
        NavDestinationItem(Routes.MORE, "More", Icons.Default.Explore)
    )

    val drawerItems = buildList {
        add(NavDestinationItem(Routes.HOME, "Home", Icons.Default.Home))
        add(NavDestinationItem(Routes.ABOUT, "About Us", Icons.Default.Info))
        add(NavDestinationItem(Routes.VIDEOS, "Latest Noha & Archive", Icons.Default.PlayCircle))
        if (settings.navShowMajalis) add(NavDestinationItem(Routes.MAJALIS, "Majalis & Programs", Icons.Default.CalendarMonth))
        add(NavDestinationItem(Routes.AUDIO, "Audio / MP3", Icons.Default.LibraryMusic))
        add(NavDestinationItem(Routes.LYRICS, "Noha Lyrics", Icons.AutoMirrored.Filled.MenuBook))
        if (settings.navShowGallery) add(NavDestinationItem(Routes.GALLERY, "Photo Gallery", Icons.Default.PhotoLibrary))
        if (settings.navShowArticles) add(NavDestinationItem(Routes.ARTICLES, "Islamic Articles", Icons.AutoMirrored.Filled.MenuBook))
        add(NavDestinationItem(Routes.CONTACT, "Contact Us", Icons.Default.Chat))
        add(NavDestinationItem(Routes.ADMIN, "Admin CMS Panel", Icons.Default.AdminPanelSettings))
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = ObsidianBlack
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .verticalScroll(rememberScrollState())
                        .padding(vertical = 20.dp)
                ) {
                    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                        Text(
                            text = settings.siteNameUrdu,
                            style = TextStyle(
                                fontFamily = NastaliqUrduFontFamily,
                                fontSize = 24.sp,
                                lineHeight = 46.sp,
                                color = ChampagneGold
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp)
                        )
                    }
                    Text(
                        text = settings.siteSubtitleEnglish,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(horizontal = 20.dp)
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    HorizontalDivider(color = BurnishedGold.copy(alpha = 0.35f))
                    Spacer(modifier = Modifier.height(8.dp))

                    drawerItems.forEach { item ->
                        NavigationDrawerItem(
                            icon = { Icon(item.icon, contentDescription = null) },
                            label = { Text(item.label) },
                            selected = currentRoute == item.route,
                            onClick = {
                                scope.launch { drawerState.close() }
                                navigateTopLevel(item.route)
                            },
                            colors = NavigationDrawerItemDefaults.colors(
                                selectedContainerColor = BurnishedGold.copy(alpha = 0.2f),
                                selectedIconColor = ChampagneGold,
                                selectedTextColor = ChampagneGold,
                                unselectedContainerColor = ObsidianBlack,
                                unselectedIconColor = ChampagneGold.copy(alpha = 0.75f),
                                unselectedTextColor = MaterialTheme.colorScheme.onSurface
                            ),
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 2.dp)
                        )
                    }
                }
            }
        }
    ) {
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val useNavigationRail = maxWidth >= 700.dp

            Scaffold(
                containerColor = MaterialTheme.colorScheme.background,
                contentWindowInsets = WindowInsets(0, 0, 0, 0),
                topBar = {
                    UrduCalligraphyBrandHeader(
                        settings = settings,
                        currentLanguage = language,
                        isDarkTheme = isDarkTheme,
                        onLanguageSelected = { viewModel.setLanguage(it) },
                        onToggleTheme = { viewModel.toggleTheme() },
                        onOpenMenu = { scope.launch { drawerState.open() } },
                        onOpenAdmin = { navigateTopLevel(Routes.ADMIN) },
                        modifier = Modifier.statusBarsPadding()
                    )
                },
                bottomBar = {
                    Column {
                        MiniAudioPlayerBar(
                            playbackState = playbackState,
                            onTogglePlayPause = { viewModel.toggleAudio(it) },
                            onStop = { viewModel.stopAudio() },
                            onOpenAudioScreen = { navigateTopLevel(Routes.AUDIO) }
                        )
                        if (!useNavigationRail) {
                            NavigationBar(
                                containerColor = ObsidianBlack,
                                windowInsets = WindowInsets.navigationBars
                            ) {
                                primaryTabs.forEach { tab ->
                                    val selected = currentRoute == tab.route ||
                                        (tab.route == Routes.VIDEOS && currentRoute.startsWith("video_detail")) ||
                                        (tab.route == Routes.LYRICS && currentRoute.startsWith("lyrics_detail"))
                                    NavigationBarItem(
                                        selected = selected,
                                        onClick = { navigateTopLevel(tab.route) },
                                        icon = { Icon(tab.icon, contentDescription = tab.label) },
                                        label = {
                                            Text(
                                                tab.label,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                        },
                                        colors = NavigationBarItemDefaults.colors(
                                            selectedIconColor = ObsidianBlack,
                                            selectedTextColor = ChampagneGold,
                                            indicatorColor = BurnishedGold,
                                            unselectedIconColor = ChampagneGold.copy(alpha = 0.7f),
                                            unselectedTextColor = ChampagneGold.copy(alpha = 0.7f)
                                        ),
                                        modifier = Modifier.testTag("bottom_nav_${tab.route}")
                                    )
                                }
                            }
                        }
                    }
                }
            ) { innerPadding ->
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    if (useNavigationRail) {
                        NavigationRail(
                            containerColor = ObsidianBlack
                        ) {
                            primaryTabs.forEach { tab ->
                                val selected = currentRoute == tab.route
                                NavigationRailItem(
                                    selected = selected,
                                    onClick = { navigateTopLevel(tab.route) },
                                    icon = { Icon(tab.icon, contentDescription = tab.label) },
                                    label = { Text(tab.label) }
                                )
                            }
                        }
                    }

                    Box(modifier = Modifier.weight(1f)) {
                        NavHost(
                            navController = navController,
                            startDestination = Routes.HOME,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            composable(Routes.HOME) {
                                HomeScreen(
                                    settings = settings,
                                    language = language,
                                    videos = videos,
                                    audioTracks = audioTracks,
                                    lyrics = lyrics,
                                    majalis = majalis,
                                    galleryPhotos = galleryPhotos,
                                    articles = articles,
                                    playbackState = playbackState,
                                    onNavigate = { route -> navigateTopLevel(route) },
                                    onOpenVideoDetail = { id -> navController.navigate("video_detail/$id") },
                                    onOpenLyricsDetail = { id -> navController.navigate("lyrics_detail/$id") },
                                    onOpenArticleDetail = { id -> navController.navigate("article_detail/$id") },
                                    onToggleAudio = { track -> viewModel.toggleAudio(track) }
                                )
                            }
                            composable(Routes.VIDEOS) {
                                VideosArchiveScreen(
                                    videos = videos,
                                    onOpenVideoDetail = { id -> navController.navigate("video_detail/$id") }
                                )
                            }
                            composable(
                                route = Routes.VIDEO_DETAIL,
                                arguments = listOf(navArgument("videoId") { type = NavType.LongType })
                            ) { backStack ->
                                val videoId = backStack.arguments?.getLong("videoId") ?: -1L
                                val video = videos.firstOrNull { it.id == videoId }
                                val related = lyrics.firstOrNull {
                                    video != null && (it.title.equals(video.title, ignoreCase = true) ||
                                        it.category.equals(video.category, ignoreCase = true))
                                }
                                VideoDetailScreen(
                                    video = video,
                                    relatedLyrics = related,
                                    onBack = { navController.popBackStack() },
                                    onOpenLyrics = { lyrId -> navController.navigate("lyrics_detail/$lyrId") }
                                )
                            }
                            composable(Routes.AUDIO) {
                                AudioScreen(
                                    tracks = audioTracks,
                                    playbackState = playbackState,
                                    audioController = viewModel.audioController,
                                    onTogglePlay = { viewModel.toggleAudio(it) },
                                    onSeek = { viewModel.seekAudio(it) }
                                )
                            }
                            composable(Routes.LYRICS) {
                                LyricsListScreen(
                                    lyricsList = lyrics,
                                    onOpenLyricsDetail = { id -> navController.navigate("lyrics_detail/$id") }
                                )
                            }
                            composable(
                                route = Routes.LYRICS_DETAIL,
                                arguments = listOf(navArgument("lyricsId") { type = NavType.LongType })
                            ) { backStack ->
                                val lyricsId = backStack.arguments?.getLong("lyricsId") ?: -1L
                                val item = lyrics.firstOrNull { it.id == lyricsId }
                                LyricsDetailScreen(
                                    lyrics = item,
                                    settings = settings,
                                    onBack = { navController.popBackStack() }
                                )
                            }
                            composable(Routes.MORE) {
                                MoreHubScreen(
                                    settings = settings,
                                    onNavigate = { route -> navigateTopLevel(route) }
                                )
                            }
                            composable(Routes.MAJALIS) {
                                MajalisScreen(majalis = majalis)
                            }
                            composable(Routes.GALLERY) {
                                PhotoGalleryScreen(photos = galleryPhotos)
                            }
                            composable(Routes.ARTICLES) {
                                ArticlesListScreen(
                                    articles = articles,
                                    onOpenArticle = { id -> navController.navigate("article_detail/$id") }
                                )
                            }
                            composable(
                                route = Routes.ARTICLE_DETAIL,
                                arguments = listOf(navArgument("articleId") { type = NavType.LongType })
                            ) { backStack ->
                                val articleId = backStack.arguments?.getLong("articleId") ?: -1L
                                val article = articles.firstOrNull { it.id == articleId }
                                ArticleDetailScreen(
                                    article = article,
                                    onBack = { navController.popBackStack() }
                                )
                            }
                            composable(Routes.ABOUT) {
                                AboutUsScreen(settings = settings)
                            }
                            composable(Routes.CONTACT) {
                                ContactUsScreen(
                                    settings = settings,
                                    onSubmitContact = { name, email, subject, message ->
                                        viewModel.submitContact(name, email, subject, message)
                                    }
                                )
                            }
                            composable(Routes.ADMIN) {
                                AdminCmsScreen(
                                    viewModel = viewModel,
                                    videos = videos,
                                    audioTracks = audioTracks,
                                    lyrics = lyrics,
                                    majalis = majalis,
                                    galleryPhotos = galleryPhotos,
                                    articles = articles,
                                    settings = settings,
                                    contactMessages = contactMessages
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
