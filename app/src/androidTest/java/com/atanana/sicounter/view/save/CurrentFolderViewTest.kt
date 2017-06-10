package com.atanana.sicounter.view.save

import android.test.AndroidTestCase
import android.widget.TextView
import com.atanana.sicounter.R
import io.reactivex.Observable

class CurrentFolderViewTest : AndroidTestCase() {
    fun testCurrentFolderProvider() {
        val currentFolderView = CurrentFolderView(context, null)
        currentFolderView.setCurrentFolderProvider(Observable.just("test folder"))

        val folderName = currentFolderView.findViewById(R.id.folder_text) as TextView
        assertEquals("test folder", folderName.text)
    }
}