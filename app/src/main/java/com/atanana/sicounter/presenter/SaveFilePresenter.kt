package com.atanana.sicounter.presenter

import android.content.Context
import android.widget.Toast
import com.atanana.sicounter.model.SaveFileModel
import com.atanana.sicounter.view.save.SaveToFileView

class SaveFilePresenter(private val context: Context, private val model: SaveFileModel, private val view: SaveToFileView) {
    init {
        view.setFoldersProvider(model.folders)
        view.setCurrentFolderProvider(model.currentFolder)
        model.errors.subscribe { Toast.makeText(context, it, Toast.LENGTH_SHORT).show() }
        model.setFileProvider(view.selectedFolder)
    }
}