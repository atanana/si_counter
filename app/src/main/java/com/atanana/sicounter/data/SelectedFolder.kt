package com.atanana.sicounter.data

interface SelectedFolder

object ParentFolder : SelectedFolder

data class Folder(val name: String) : SelectedFolder