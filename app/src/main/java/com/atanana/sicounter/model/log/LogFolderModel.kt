package com.atanana.sicounter.model.log

import android.content.Context
import android.util.Log
import com.atanana.sicounter.R
import com.atanana.sicounter.data.Folder
import com.atanana.sicounter.data.ParentFolder
import com.atanana.sicounter.data.SelectedFolder
import com.atanana.sicounter.fs.FileProvider
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.ReplaySubject
import java.io.File

class LogFolderModel(private var _currentFolder: File, private val fileProvider: FileProvider, context: Context) {
    private val cannotListParentException: String by lazy { context.resources.getString(R.string.parent_list_exception) }
    private val cannotListSubfolderException: String by lazy { context.resources.getString(R.string.folder_list_exception) }

    private val _foldersSubject: ReplaySubject<List<String>> = ReplaySubject.createWithSize<List<String>>(1)
    val foldersObservable: Observable<List<String>> = _foldersSubject

    private val _errorsSubject: PublishSubject<String> = PublishSubject.create()
    val errorsObservable: Observable<String> = _errorsSubject

    private val _currentFolderSubject: ReplaySubject<String> = ReplaySubject.createWithSize<String>(1)
    val currentFolderObservable: Observable<String> = _currentFolderSubject

    init {
        _currentFolderSubject.onNext(_currentFolder.absolutePath)
        _foldersSubject.onNext(fileProvider.directories(_currentFolder))
    }

    fun setFolderProvider(folderProvider: Observable<SelectedFolder>) {
        folderProvider.subscribe { folder ->
            when (folder) {
                is ParentFolder -> {
                    val parent = fileProvider.parent(_currentFolder)
                    tryChangeFolder(parent, "Cannot list parent of ${_currentFolder.absolutePath}!", cannotListParentException)
                }
                is Folder -> {
                    val subfolder = fileProvider.subfolder(_currentFolder, folder.name)
                    tryChangeFolder(subfolder, "Cannot list subfolder ${subfolder.absolutePath}!", cannotListSubfolderException)
                }
            }
        }
    }

    private fun tryChangeFolder(folder: File, logMessage: String, exception: String) {
        try {
            _foldersSubject.onNext(fileProvider.directories(folder))
            _currentFolder = folder
            _currentFolderSubject.onNext(_currentFolder.absolutePath)
        } catch(e: Exception) {
            Log.e(javaClass.simpleName, logMessage, e)
            _errorsSubject.onNext(exception)
        }
    }

    val currentFolder get() = _currentFolder
}