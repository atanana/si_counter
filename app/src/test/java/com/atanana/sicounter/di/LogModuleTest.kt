package com.atanana.sicounter.di

import android.content.Context
import android.content.res.Resources
import com.atanana.sicounter.R
import com.atanana.sicounter.fs.FileProvider
import org.assertj.core.api.Assertions
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import rx.lang.kotlin.emptyObservable
import java.io.File

class LogModuleTest {
    lateinit var module: LogModule

    @Mock
    lateinit var context: Context

    @Mock
    lateinit var fileProvider: FileProvider

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        val resources = mock(Resources::class.java)
        `when`(context.resources).thenReturn(resources)

        module = LogModule(emptyObservable())
    }

    @Test
    fun shouldReturnCorrectExternalAppFolder() {
        `when`(context.resources.getString(R.string.app_name)).thenReturn("test app")
        `when`(fileProvider.externalStorage).thenReturn(File("/test path/"))
        val appFolder = module.externalAppFolder(context, fileProvider)
        Assertions.assertThat(appFolder.absolutePath).isEqualTo("/test path/test app")
    }
}