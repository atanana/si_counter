package com.atanana.sicounter.view.save

import android.test.AndroidTestCase
import android.widget.TextView
import com.atanana.sicounter.R
import junit.framework.Assert
import rx.Observable

class FilenameViewTest : AndroidTestCase() {
    fun testFilenameProvider() {
        val filenameView = FilenameView(context, null)
        filenameView.setFilenameProvider(Observable.just("test 1"))

        val filenameTextView = filenameView.findViewById(R.id.filename_text) as TextView
        Assert.assertEquals("test 1", filenameTextView.text)
    }
}