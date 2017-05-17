package com.atanana.sicounter.fs

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import java.io.File

class FileProviderTest {
    lateinit var fileProvider: FileProvider
    lateinit var file: File

    @Before
    fun setUp() {
        fileProvider = FileProvider()
        file = mock(File::class.java)
    }

    @Test
    fun testParent() {
        val parent = mock(File::class.java)
        `when`(file.parentFile).thenReturn(parent)

        assertEquals(parent, fileProvider.parent(file))
    }

    @Test
    fun testSubfolder() {
        `when`(file.absolutePath).thenReturn("/test/1")

        assertEquals("/test/1/123", fileProvider.subfolder(file, "123").absolutePath)
    }

    @Test
    fun testDirectories() {
        val files = arrayOf(
                createFile("test 1", true),
                createFile("test 2", false),
                createFile("test 3", true)
        )
        `when`(file.listFiles()).thenReturn(files)

        assertEquals(listOf("test 1", "test 3"), fileProvider.directories(file))
    }

    @Test
    fun testEmptyDirectories() {
        assertEquals(listOf<String>(), fileProvider.directories(file))
    }

    private fun createFile(name: String, isDirectory: Boolean) :File {
        val result = mock(File::class.java)
        `when`(result.name).thenReturn(name)
        `when`(result.isDirectory).thenReturn(isDirectory)

        return result
    }
}