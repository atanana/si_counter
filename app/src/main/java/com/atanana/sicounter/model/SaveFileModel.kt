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
import java.io.File

class SaveFileModel(private var file: File, private val fileProvider: FileProvider, context: Context) {
    private val cannotListParentException: String by lazy { context.resources.getString(R.string.parent_list_exception) }
    private val cannotListSubfolderException: String by lazy { context.resources.getString(R.string.folder_list_exception) }

    private val _folders: PublishSubject<List<String>> = PublishSubject()
    val folders: Observable<List<String>> = _folders

    private val _errors: PublishSubject<String> = PublishSubject()
    val errors: Observable<String> = _errors

    private val _currentFolder: PublishSubject<String> = PublishSubject()
    val currentFolder: Observable<String> = _currentFolder

    init {
        _currentFolder.onNext(file.absolutePath)
    }

    fun setFileProvider(folderProvider: Observable<SelectedFolder>) {
        folderProvider.subscribe { folder ->
            when (folder) {
                is ParentFolder -> {
                    try {
                        val parent = fileProvider.parent(file)
                        _folders.onNext(fileProvider.directories(parent))
                        file = parent
                        updateCurrentFolder()
                    } catch(e: Exception) {
                        Log.e(javaClass.simpleName, "Cannot list parent of ${file.absolutePath}!", e)
                        _errors.onNext(cannotListParentException)
                    }
                }
                is Folder -> {
                    val subfolder = fileProvider.subfolder(file, folder.name)
                    try {
                        _folders.onNext(fileProvider.directories(subfolder))
                        file = subfolder
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
        _currentFolder.onNext(file.absolutePath)
    }
}