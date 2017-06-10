package com.atanana.sicounter.presenter

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AlertDialog
import android.view.View
import android.widget.Button
import com.atanana.sicounter.R
import com.atanana.sicounter.di.MainComponent
import com.atanana.sicounter.model.HistoryModel
import com.atanana.sicounter.model.ScoresModel
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

private fun <T> anyObject(): T {
    return Mockito.anyObject<T>()
}

class MainUiPresenterTest {
    lateinit var presenter: MainUiPresenter

    @Mock
    lateinit var component: MainComponent

    @Mock
    lateinit var fabButton: FloatingActionButton

    @Mock
    lateinit var addDividerButton: Button

    @Mock
    lateinit var addPlayerDialog: AlertDialog.Builder

    @Mock
    lateinit var resetDialog: AlertDialog.Builder

    @Mock
    lateinit var exitDialog: AlertDialog.Builder

    @Mock
    lateinit var saveLogPresenter: SaveFilePresenter

    @Mock
    lateinit var scoresModel: ScoresModel

    @Mock
    lateinit var historyModel: HistoryModel

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        `when`(component.inject(anyObject<MainUiPresenter>())).thenAnswer {
            val uiPresenter = it.arguments[0] as MainUiPresenter
            uiPresenter.fabButton = fabButton
            uiPresenter.addDivider = addDividerButton
            uiPresenter.addPlayerDialog = addPlayerDialog
            uiPresenter.resetDialog = resetDialog
            uiPresenter.exitDialog = exitDialog
            uiPresenter.saveLogPresenter = saveLogPresenter
            uiPresenter.scoresModel = scoresModel
            uiPresenter.historyModel = historyModel
            return@thenAnswer null
        }
    }

    @Test
    fun shouldInjectDependencies() {
        createPresenter()
        verify(component).inject(presenter)
    }

    @Test
    fun shouldShowAddPlayerDialogOnFabButtonClick() {
        val argumentCaptor = ArgumentCaptor.forClass(View.OnClickListener::class.java)
        createPresenter()
        verify(fabButton).setOnClickListener(argumentCaptor.capture())

        argumentCaptor.value.onClick(fabButton)
        verify(addPlayerDialog).show()
    }

    @Test
    fun shouldAddDividerOnButtonClick() {
        val argumentCaptor = ArgumentCaptor.forClass(View.OnClickListener::class.java)
        createPresenter()
        verify(addDividerButton).setOnClickListener(argumentCaptor.capture())

        argumentCaptor.value.onClick(addDividerButton)
        verify(historyModel).addDivider()
    }

    @Test
    fun shouldShowResetDialog() {
        createPresenter()
        presenter.toolbarItemSelected(R.id.mi_new)
        verify(resetDialog).show()
    }

    @Test
    fun shouldReturnTrueWhenResetClicked() {
        createPresenter()
        assertThat(presenter.toolbarItemSelected(R.id.mi_new)).isTrue()
    }

    @Test
    fun shouldShowSaveLog() {
        createPresenter()
        presenter.toolbarItemSelected(R.id.mi_save)
        verify(saveLogPresenter).showDialog()
    }

    @Test
    fun shouldReturnTrueWhenSaveClicked() {
        createPresenter()
        assertThat(presenter.toolbarItemSelected(R.id.mi_save)).isTrue()
    }

    @Test
    fun shouldShowDialogOnBackPress() {
        createPresenter()
        presenter.onBackPressed()
        verify(exitDialog).show()
    }

    @Test
    fun shouldSaveModelsToBundle() {
        createPresenter()
        val bundle = mock(Bundle::class.java)
        presenter.saveToBundle(bundle)

        verify(scoresModel).save(bundle)
        verify(historyModel).save(bundle)
    }

    @Test
    fun shouldRestoreModelsFromBundle() {
        createPresenter()
        val bundle = mock(Bundle::class.java)
        presenter.restoreFromBundle(bundle)

        verify(scoresModel).restore(bundle)
        verify(historyModel).restore(bundle)
    }

    private fun createPresenter() {
        presenter = MainUiPresenter(component)
    }
}