package com.atanana.sicounter.model.log

import com.atanana.sicounter.data.SelectedFolder
import rx.Observable
import java.io.File

class SaveLogModel(private val logNameModel: LogNameModel, private val logFolderModel: LogFolderModel) {
    val logName = logNameModel.filenameObservable
    val logFile get() = File(logFolderModel.currentFolder, logNameModel.fullFilename)
    val foldersObservable = logFolderModel.foldersObservable
    val errorsObservable = logFolderModel.errorsObservable
    val currentFolderObservable = logFolderModel.currentFolderObservable
    fun setFolderProvider(folderProvider: Observable<SelectedFolder>) {
        logFolderModel.setFolderProvider(folderProvider)
    }
}