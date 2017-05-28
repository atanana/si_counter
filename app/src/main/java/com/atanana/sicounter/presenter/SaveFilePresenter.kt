package com.atanana.sicounter.presenter

import android.Manifest
import android.content.Context
import android.support.v7.app.AlertDialog
import android.widget.Toast
import com.atanana.sicounter.R
import com.atanana.sicounter.model.log.SaveLogModel
import com.atanana.sicounter.view.save.SaveToFileView
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import java.util.ArrayList

class SaveFilePresenter(private val context: Context, model: SaveLogModel, view: SaveToFileView,
                        private val dialogBuilder: AlertDialog.Builder) {
    init {
        view.setFoldersProvider(model.foldersObservable)
        view.setCurrentFolderProvider(model.currentFolderObservable)
        view.setFilenameProvider(model.logName)
        model.errorsObservable.subscribe { Toast.makeText(context, it, Toast.LENGTH_SHORT).show() }
        model.setFolderProvider(view.selectedFolder)
    }

    fun show() {
        TedPermission(context)
                .setPermissionListener(object : PermissionListener {
                    override fun onPermissionGranted() {
                        dialogBuilder.show()
                    }

                    override fun onPermissionDenied(deniedPermissions: ArrayList<String>?) {}
                })
                .setDeniedMessage(R.string.denied_storage_permission)
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check()
    }
}