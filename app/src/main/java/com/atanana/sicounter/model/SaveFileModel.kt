package com.atanana.sicounter.model

import android.content.Context
import android.util.Log
import com.atanana.sicounter.R
import com.atanana.sicounter.data.Folder
import com.atanana.sicounter.data.ParentFolder
import com.atanana.sicounter.data.SelectedFolder
import com.atanana.sicounter.fs.FileProvider
import rx.Observable
import rx.lang.kotlin.PublishSubject
import rx.subjects.PublishSubject
import rx.subjects.ReplaySubject
import java.io.File

class SaveFileModel(private var _file: File, private val fileProvider: FileProvider, context: Context) {
    private val cannotListParentException: String by lazy { context.resources.getString(R.string.parent_list_exception) }
    private val cannotListSubfolderException: String by lazy { context.resources.getString(R.string.folder_list_exception) }

    private val _folders: ReplaySubject<List<String>> = ReplaySubject.createWithSize<List<String>>(1)
    val folders: Observable<List<String>> = _folders

    private val _errors: PublishSubject<String> = PublishSubject()
    val errors: Observable<String> = _errors

    private val _currentFolder: ReplaySubject<String> = ReplaySubject.createWithSize<String>(1)
    val currentFolder: Observable<String> = _currentFolder

    val file: File
        get() {
            return _file
        }

    init {
        _currentFolder.onNext(_file.absolutePath)
        _folders.onNext(fileProvider.directories(file))
    }

    fun setFileProvider(folderProvider: Observable<SelectedFolder>) {
        folderProvider.subscribe { folder ->
            when (folder) {
                is ParentFolder -> {
                    try {
                        val parent = fileProvider.parent(_file)
                        _folders.onNext(fileProvider.directories(parent))
                        _file = parent
                        updateCurrentFolder()
                    } catch(e: Exception) {
                        Log.e(javaClass.simpleName, "Cannot list parent of ${_file.absolutePath}!", e)
                        _errors.onNext(cannotListParentException)
                    }
                }
                is Folder -> {
                    val subfolder = fileProvider.subfolder(_file, folder.name)
                    try {
                        _folders.onNext(fileProvider.directories(subfolder))
                        _file = subfolder
                        updateCurrentFolder()
                    } catch(e: Exception) {
                        Log.e(javaClass.simpleName, "Cannot list subfolder ${subfolder.absolutePath}!", e)
                        _errors.onNext(cannotListSubfolderException)
                    }
                }
            }
        }
    }

    private fun updateCurrentFolder() {
        _currentFolder.onNext(_file.absolutePath)
    }
}