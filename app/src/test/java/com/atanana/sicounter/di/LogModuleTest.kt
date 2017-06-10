package com.atanana.sicounter.di

import android.content.Context
import android.content.res.Resources
import com.atanana.sicounter.R
import com.atanana.sicounter.data.Score
import com.atanana.sicounter.fs.FileProvider
import com.atanana.sicounter.model.ScoresModel
import io.reactivex.Observable
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import java.io.File

class LogModuleTest {
    lateinit var module: LogModule

    @Mock
    lateinit var context: Context

    @Mock
    lateinit var fileProvider: FileProvider

    @Mock
    lateinit var scoresModel: ScoresModel

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        val resources = mock(Resources::class.java)
        `when`(context.resources).thenReturn(resources)

        module = LogModule()
    }

    @Test
    fun shouldReturnCorrectExternalAppFolder() {
        `when`(context.resources.getString(R.string.app_name)).thenReturn("test app")
        `when`(fileProvider.externalStorage).thenReturn(File("/test path/"))
        val appFolder = module.externalAppFolder(context, fileProvider)
        assertThat(appFolder.absolutePath).isEqualTo("/test path/test app")
    }

    @Test
    fun shouldReturnCorrectLogNameModel() {
        `when`(scoresModel.newPlayersObservable).thenReturn(Observable.just(
                Pair(Score("test 1", 0), 1),
                Pair(Score("test 2", 0), 1),
                Pair(Score("test 3", 0), 1)
        ))
        val model = module.createLogNameModel(scoresModel)
        assertThat(model.fullFilename).isEqualTo("test 1-test 2-test 3.txt")
    }
}