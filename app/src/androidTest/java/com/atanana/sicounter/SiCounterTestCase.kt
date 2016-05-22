package com.atanana.sicounter

import android.test.AndroidTestCase
import org.junit.After
import org.junit.Before
import rx.Scheduler
import rx.android.plugins.RxAndroidPlugins
import rx.android.plugins.RxAndroidSchedulersHook
import rx.schedulers.Schedulers

open class SiCounterTestCase : AndroidTestCase() {
    @Before
    fun setup() {
        RxAndroidPlugins.getInstance().registerSchedulersHook(object : RxAndroidSchedulersHook() {
            override fun getMainThreadScheduler(): Scheduler? {
                return Schedulers.immediate();
            }
        });
    }

    @After
    override fun tearDown() {
        super.tearDown()
        RxAndroidPlugins.getInstance().reset();
    }
}
