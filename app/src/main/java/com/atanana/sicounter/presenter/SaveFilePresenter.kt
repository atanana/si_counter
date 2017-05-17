package com.atanana.sicounter.presenter

import android.content.Context
import android.widget.Toast
import com.atanana.sicounter.model.SaveFileModel
import com.atanana.sicounter.view.save.SaveToFileView

class SaveFilePresenter(private val context: Context, model: SaveFileModel, view: SaveToFileView) {
    init {
        view.setFoldersProvider(model.folders)
        view.setCurrentFolderProvider(model.currentFolder)
        view.setFilenameProvider(model.filenameProvider)
        model.errors.subscribe { Toast.makeText(context, it, Toast.LENGTH_SHORT).show() }
        model.setFileProvider(view.selectedFolder)
    }
}