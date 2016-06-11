package com.atanana.sicounter.view

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.atanana.sicounter.R
import com.atanana.sicounter.data.Folder
import com.atanana.sicounter.data.ParentFolder
import com.atanana.sicounter.data.SelectedFolder
import rx.Observable
import rx.lang.kotlin.PublishSubject

class FoldersView(context: Context, attrs: AttributeSet?) : RecyclerView(context, attrs) {
    private val foldersAdapter = FoldersAdapter()
    private val _selectedFolders = PublishSubject<SelectedFolder>()
    val selectedFolders: Observable<SelectedFolder>
        get() {
            return _selectedFolders
        }

    init {
        layoutManager = LinearLayoutManager(context)
        adapter = foldersAdapter
    }

    fun setFoldersProvider(observable: Observable<List<String>>) {
        observable.subscribe({ folders -> foldersAdapter.setFolders(folders) })
    }

    inner class FoldersAdapter : RecyclerView.Adapter<FoldersAdapter.ViewHolder>() {
        private val TYPE_PARENT_FOLDER: Int = 0;
        private val TYPE_FOLDER: Int = 1;

        private val folders: MutableList<String> = arrayListOf()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder? {
            val layout = when (viewType) {
                TYPE_FOLDER -> R.layout.item_folder
                TYPE_PARENT_FOLDER -> R.layout.item_parent_folder
                else -> 0
            }
            val item = LayoutInflater.from(parent.context).inflate(layout, parent, false) as TextView
            return ViewHolder(item)
        }

        override fun getItemCount(): Int {
            return folders.size + 1
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            when (getItemViewType(position)) {
                TYPE_FOLDER -> holder.setFolder(folders[position - 1])
                TYPE_PARENT_FOLDER -> holder.item.setOnClickListener { _selectedFolders.onNext(ParentFolder) }
            }
        }

        fun setFolders(folders: List<String>) {
            this.folders.clear()
            this.folders.addAll(folders)
        }

        override fun getItemViewType(position: Int): Int {
            return if (position == 0) TYPE_PARENT_FOLDER else TYPE_FOLDER
        }

        inner class ViewHolder(val item: TextView) : RecyclerView.ViewHolder(item) {
            fun setFolder(folder: String) {
                item.text = folder
                item.setOnClickListener { _selectedFolders.onNext(Folder(folder)) }
            }
        }
    }
}