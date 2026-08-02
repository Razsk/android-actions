package com.example.androidactions.ui.theme

import androidx.compose.ui.graphics.Color
import com.example.androidactions.theme.ActionBlue
import com.example.androidactions.theme.CyberCyan
import com.example.androidactions.theme.SurfaceDark
import org.junit.Assert.assertEquals
import org.junit.Test

class ThemeTest {

    @Test
    fun testKineticHudDarkColors() {
        assertEquals(Color(0xFF0B1326), SurfaceDark)
        assertEquals(Color(0xFFB2C5FF), ActionBlue)
        assertEquals(Color(0xFF00DAF3), CyberCyan)
    }
}
