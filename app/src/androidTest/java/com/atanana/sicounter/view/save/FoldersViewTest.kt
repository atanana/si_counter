package com.atanana.sicounter.view.save

import android.test.AndroidTestCase
import io.reactivex.Observable

class FoldersViewTest : AndroidTestCase() {
    fun testFoldersProvider() {
        val foldersView = FoldersView(context, null)
        foldersView.setFoldersProvider(Observable.just(listOf("folder 1", "folder 2", "folder 3")))

        val adapter = foldersView.adapter as FoldersView.FoldersAdapter
        assertEquals(4, adapter.itemCount)
    }
}