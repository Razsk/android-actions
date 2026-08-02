package com.example.androidactions.ui.theme

import androidx.compose.ui.graphics.Color
import org.junit.Assert.assertEquals
import org.junit.Test

class ThemeTest {

    @Test
    fun testKineticHudDarkColors() {
        assertEquals(Color(0xFF0B1326), KineticHudBackground)
        assertEquals(Color(0xFFB2C5FF), KineticHudPrimary)
        assertEquals(Color(0xFF00DAF3), KineticHudTertiaryCyan)
    }
}
