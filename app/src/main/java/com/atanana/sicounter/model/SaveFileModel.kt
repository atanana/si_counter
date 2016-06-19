package com.atanana.sicounter.model

import android.content.Context
import android.util.Log
import com.atanana.sicounter.R
import com.atanana.sicounter.data.Folder
import com.atanana.sicounter.data.ParentFolder
import com.atanana.sicounter.data.SelectedFolder
import rx.Observable
import rx.lang.kotlin.PublishSubject
import rx.subjects.PublishSubject
import java.io.File

class SaveFileModel(var file: File, context: Context) {
    private val cannotListParentException: String by lazy { context.resources.getString(R.string.parent_list_exception) }
    private val cannotListSubfolderException: String by lazy { context.resources.getString(R.string.folder_list_exception) }

    private val _folders: PublishSubject<List<String>> = PublishSubject()
    val folders: Observable<List<String>>
        get() = _folders

    private val _errors: PublishSubject<String> = PublishSubject()
    val errors: Observable<String>
        get() = _errors

    fun setFileProvider(folderProvider: Observable<SelectedFolder>) {
        folderProvider.subscribe { folder ->
            when (folder) {
                is ParentFolder -> {
                    try {
                        val parent = file.parentFile
                        _folders.onNext(getDirectories(parent))
                        file = parent
                    } catch(e: Exception) {
                        Log.e(javaClass.simpleName, "Cannot list parent of ${file.absolutePath}!", e)
                        _errors.onNext(cannotListParentException)
                    }
                }
                is Folder -> {
                    val subfolder = File(listOf(file.absolutePath, folder.name).joinToString(File.separator))
                    try {
                        _folders.onNext(getDirectories(subfolder))
                        file = subfolder
                    } catch(e: Exception) {
                        Log.e(javaClass.simpleName, "Cannot list subfolder ${subfolder.absolutePath}!", e)
                        _errors.onNext(cannotListSubfolderException)
                    }
                }
            }
        }
    }

    private fun getDirectories(file: File) = file.listFiles()
            ?.asList()
            ?.filter { it.isDirectory }
            ?.map { it.name }
            ?: emptyList()
}