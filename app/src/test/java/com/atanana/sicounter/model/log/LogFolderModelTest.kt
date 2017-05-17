package com.atanana.sicounter.model.log

import android.content.Context
import android.content.res.Resources
import com.atanana.sicounter.R
import com.atanana.sicounter.data.Folder
import com.atanana.sicounter.data.ParentFolder
import com.atanana.sicounter.data.SelectedFolder
import com.atanana.sicounter.fs.FileProvider
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import rx.lang.kotlin.PublishSubject
import rx.observers.TestSubscriber
import rx.subjects.Subject
import java.io.File

class LogFolderModelTest {
    lateinit var model: LogFolderModel

    @Mock
    lateinit var fileProvider: FileProvider

    @Mock
    lateinit var context: Context

    @Mock
    lateinit var folderProvider: Subject<SelectedFolder, SelectedFolder>

    lateinit var file: File

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        file = File(".")
        folderProvider = PublishSubject()

        model = LogFolderModel(file, fileProvider, context)
        model.setFolderProvider(folderProvider)

        val mock = mock(Resources::class.java)
        `when`(context.resources).thenReturn(mock)
    }

    @Test
    fun shouldEmitInitialFolder() {
        val subscriber = TestSubscriber<String>()
        model.currentFolderObservable.subscribe(subscriber)
        subscriber.assertValue(file.absolutePath)
    }

    @Test
    fun shouldEmitInitialFolderContents() {
        val folders = listOf("test 1", "test 2", "test 3")
        `when`(fileProvider.directories(file)).thenReturn(folders)
        model = LogFolderModel(file, fileProvider, context)

        val subscriber = TestSubscriber<List<String>>()
        model.foldersObservable.subscribe(subscriber)
        subscriber.assertValue(folders)
    }

    @Test
    fun shouldSetInitialFolder() {
        assertThat(model.currentFolder).isEqualTo(file)
    }

    @Test
    fun shouldChangeFolderToParent() {
        val parent1 = File("parent 1")
        val parent2 = File("parent 2")
        `when`(fileProvider.parent(file)).thenReturn(parent1)
        `when`(fileProvider.parent(parent1)).thenReturn(parent2)

        folderProvider.onNext(ParentFolder)
        folderProvider.onNext(ParentFolder)

        assertThat(model.currentFolder).isEqualTo(parent2)
    }

    @Test
    fun shouldNotifyAboutCurrentFolderWhenChangingFolderToParent() {
        val subscriber = TestSubscriber<String>()
        model.currentFolderObservable.subscribe(subscriber)

        val parent1 = File("parent 1")
        val parent2 = File("parent 2")
        `when`(fileProvider.parent(file)).thenReturn(parent1)
        `when`(fileProvider.parent(parent1)).thenReturn(parent2)

        folderProvider.onNext(ParentFolder)
        folderProvider.onNext(ParentFolder)

        subscriber.assertValues(file.absolutePath, parent1.absolutePath, parent2.absolutePath)
    }

    @Test
    fun shouldNotifyAboutFoldersWhenChangingFolderToParent() {
        val subscriber = TestSubscriber<List<String>>()
        model.foldersObservable.subscribe(subscriber)

        val parent1 = File("parent 1")
        val parent2 = File("parent 2")
        `when`(fileProvider.parent(file)).thenReturn(parent1)
        `when`(fileProvider.parent(parent1)).thenReturn(parent2)

        val folders2 = listOf("parent 1 1", "parent 1 2", "parent 1 3")
        `when`(fileProvider.directories(parent1)).thenReturn(folders2)
        val folders3 = listOf("parent 2 1", "parent 2 2", "parent 2 3")
        `when`(fileProvider.directories(parent2)).thenReturn(folders3)

        folderProvider.onNext(ParentFolder)
        folderProvider.onNext(ParentFolder)

        subscriber.assertValues(listOf(), folders2, folders3)
    }

    @Test
    fun shouldShowCorrespondingErrorWhenCannotOpenParent() {
        val parent = File("parent")
        `when`(fileProvider.parent(file)).thenReturn(parent)
        `when`(fileProvider.directories(parent)).thenThrow(RuntimeException())
        `when`(context.resources.getString(R.string.parent_list_exception)).thenReturn("parent exception")

        val subscriber = TestSubscriber<String>()
        model.errorsObservable.subscribe(subscriber)

        folderProvider.onNext(ParentFolder)

        subscriber.assertValue("parent exception")
    }

    @Test
    fun shouldChangeFolderToSubfolder() {
        val subfolder1 = File("subfolder 1")
        val subfolder2 = File("subfolder 2")
        `when`(fileProvider.subfolder(file, "1")).thenReturn(subfolder1)
        `when`(fileProvider.subfolder(subfolder1, "2")).thenReturn(subfolder2)

        folderProvider.onNext(Folder("1"))
        folderProvider.onNext(Folder("2"))

        assertThat(model.currentFolder).isEqualTo(subfolder2)
    }

    @Test
    fun shouldNotifyAboutCurrentFolderWhenChangingFolderToSubfolder() {
        val subscriber = TestSubscriber<String>()
        model.currentFolderObservable.subscribe(subscriber)

        val subfolder1 = File("subfolder 1")
        val subfolder2 = File("subfolder 2")
        `when`(fileProvider.subfolder(file, "1")).thenReturn(subfolder1)
        `when`(fileProvider.subfolder(subfolder1, "2")).thenReturn(subfolder2)

        folderProvider.onNext(Folder("1"))
        folderProvider.onNext(Folder("2"))

        subscriber.assertValues(file.absolutePath, subfolder1.absolutePath, subfolder2.absolutePath)
    }

    @Test
    fun shouldNotifyAboutFoldersWhenChangingFolderToSubfolder() {
        val subscriber = TestSubscriber<List<String>>()
        model.foldersObservable.subscribe(subscriber)

        val subfolder1 = File("subfolder 1")
        val subfolder2 = File("subfolder 2")
        `when`(fileProvider.subfolder(file, "1")).thenReturn(subfolder1)
        `when`(fileProvider.subfolder(subfolder1, "2")).thenReturn(subfolder2)

        val folders2 = listOf("subfolder 1 1", "subfolder 1 2", "subfolder 1 3")
        `when`(fileProvider.directories(subfolder1)).thenReturn(folders2)
        val folders3 = listOf("subfolder 2 1", "subfolder 2 2", "subfolder 2 3")
        `when`(fileProvider.directories(subfolder2)).thenReturn(folders3)

        folderProvider.onNext(Folder("1"))
        folderProvider.onNext(Folder("2"))

        subscriber.assertValues(listOf(), folders2, folders3)
    }

    @Test
    fun shouldShowCorrespondingErrorWhenCannotOpenSubfolder() {
        val subfolder = File("subfolder")
        `when`(fileProvider.subfolder(file, "1")).thenReturn(subfolder)
        `when`(fileProvider.directories(subfolder)).thenThrow(RuntimeException())
        `when`(context.resources.getString(R.string.folder_list_exception)).thenReturn("subfolder exception")

        val subscriber = TestSubscriber<String>()
        model.errorsObservable.subscribe(subscriber)

        folderProvider.onNext(Folder("1"))

        subscriber.assertValue("subfolder exception")
    }
}