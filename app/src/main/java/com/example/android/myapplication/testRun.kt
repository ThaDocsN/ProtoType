package com.example.android.myapplication

sealed class testRun{
    object clicked:testRun()
    object download:testRun()
    object complete:testRun()
}
