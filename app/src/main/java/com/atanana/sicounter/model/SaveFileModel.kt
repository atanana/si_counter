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

const val DEFAULT_FILE_NAME = "empty"
const val EXTENSION = ".txt"

class SaveFileModel(private var _folder: File, private val fileProvider: FileProvider,
                    newPlayersNameProvider: Observable<String>, context: Context) {
    private val cannotListParentException: String by lazy { context.resources.getString(R.string.parent_list_exception) }
    private val cannotListSubfolderException: String by lazy { context.resources.getString(R.string.folder_list_exception) }

    private val _folders: ReplaySubject<List<String>> = ReplaySubject.createWithSize<List<String>>(1)
    val folders: Observable<List<String>> = _folders

    private val _errors: PublishSubject<String> = PublishSubject()
    val errors: Observable<String> = _errors

    private val _currentFolder: ReplaySubject<String> = ReplaySubject.createWithSize<String>(1)
    val currentFolder: Observable<String> = _currentFolder

    private val _filenameSubject: ReplaySubject<String> = ReplaySubject.createWithSize(1)
    val filenameProvider: Observable<String> = _filenameSubject

    private var filename: String = "empty"

    init {
        _currentFolder.onNext(_folder.absolutePath)
        _folders.onNext(fileProvider.directories(_folder))
        _filenameSubject.onNext(fullFilename)

        newPlayersNameProvider.subscribe {
            if (filename == DEFAULT_FILE_NAME) {
                filename = it
            } else {
                filename += "-" + it
            }

            _filenameSubject.onNext(fullFilename)
        }
    }

    val fileToSave: File
        get() {
            return File(_folder, fullFilename)
        }

    private val fullFilename: String
        get() {
            return filename + EXTENSION
        }

    fun setFileProvider(folderProvider: Observable<SelectedFolder>) {
        folderProvider.subscribe { folder ->
            when (folder) {
                is ParentFolder -> {
                    try {
                        val parent = fileProvider.parent(_folder)
                        _folders.onNext(fileProvider.directories(parent))
                        _folder = parent
                        updateCurrentFolder()
                    } catch(e: Exception) {
                        Log.e(javaClass.simpleName, "Cannot list parent of ${_folder.absolutePath}!", e)
                        _errors.onNext(cannotListParentException)
                    }
                }
                is Folder -> {
                    val subfolder = fileProvider.subfolder(_folder, folder.name)
                    try {
                        _folders.onNext(fileProvider.directories(subfolder))
                        _folder = subfolder
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
        _currentFolder.onNext(_folder.absolutePath)
    }
}