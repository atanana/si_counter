package com.atanana.sicounter.di

import android.view.View
import com.atanana.sicounter.MainActivity
import com.atanana.sicounter.R
import com.atanana.sicounter.view.ScoresLog
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations

class MainUiModuleTest {
    private lateinit var module: MainUiModule

    @Mock
    lateinit var view: View

    @Mock
    lateinit var activity: MainActivity

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        `when`(activity.findViewById<View>(android.R.id.content)).thenReturn(view)
        module = MainUiModule(activity)
    }

    @Test
    fun shouldProvideCorrectLogView() {
        val logView = mock(ScoresLog::class.java)
        `when`(view.findViewById<View>(R.id.log_view)).thenReturn(logView)
        assertThat(module.provideLogView()).isEqualTo(logView)
    }
}