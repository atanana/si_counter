package com.atanana.sicounter.presenter

import android.content.Context
import android.widget.Toast
import com.atanana.sicounter.model.log.SaveLogModel
import com.atanana.sicounter.view.save.SaveToFileView

class SaveFilePresenter(private val context: Context, model: SaveLogModel, view: SaveToFileView) {
    init {
        view.setFoldersProvider(model.foldersObservable)
        view.setCurrentFolderProvider(model.currentFolderObservable)
        view.setFilenameProvider(model.logName)
        model.errorsObservable.subscribe { Toast.makeText(context, it, Toast.LENGTH_SHORT).show() }
        model.setFileProvider(view.selectedFolder)
    }
}