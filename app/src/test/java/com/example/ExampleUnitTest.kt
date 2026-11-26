package com.example

import com.example.data.YouTubeHelper
import com.example.util.PlatformHelpers
import org.junit.Assert.assertEquals
import org.junit.Test

class ExampleUnitTest {
    @Test
    fun youtubeIdExtraction_handlesWatchAndShortLinks() {
        assertEquals(
            "dQw4w9WgXcQ",
            YouTubeHelper.extractVideoId("https://www.youtube.com/watch?v=dQw4w9WgXcQ")
        )
        assertEquals(
            "dQw4w9WgXcQ",
            YouTubeHelper.extractVideoId("https://youtu.be/dQw4w9WgXcQ?si=abc")
        )
        assertEquals(
            "dQw4w9WgXcQ",
            YouTubeHelper.extractVideoId("https://www.youtube.com/shorts/dQw4w9WgXcQ")
        )
        assertEquals(
            "",
            YouTubeHelper.extractVideoId("https://www.youtube.com/@Hussainaliofficial465")
        )
    }

    @Test
    fun formatDuration_formatsMinutesAndSeconds() {
        assertEquals("4:05", PlatformHelpers.formatDuration(245))
        assertEquals("0:30", PlatformHelpers.formatMillis(30_000))
    }
}
