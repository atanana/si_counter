package com.atanana.sicounter.view.save

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.atanana.sicounter.R
import com.atanana.sicounter.data.SelectedFolder
import io.reactivex.Observable

class SaveToFileView(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs) {
    private val foldersView: FoldersView by lazy { findViewById(R.id.folders) as FoldersView }
    private val currentFolderView: CurrentFolderView by lazy { findViewById(R.id.current_folder) as CurrentFolderView }
    private val filenameView: FilenameView by lazy { findViewById(R.id.filename) as FilenameView }

    val selectedFolder: Observable<SelectedFolder>
        get() = foldersView.selectedFolders

    init {
        LayoutInflater.from(context).inflate(R.layout.save_to_file, this, true)
    }

    fun setFoldersProvider(provider: Observable<List<String>>) {
        foldersView.setFoldersProvider(provider)
    }

    fun setCurrentFolderProvider(provider: Observable<String>) {
        currentFolderView.setCurrentFolderProvider(provider)
    }

    fun setFilenameProvider(provider: Observable<String>) {
        filenameView.setFilenameProvider(provider)
    }
}