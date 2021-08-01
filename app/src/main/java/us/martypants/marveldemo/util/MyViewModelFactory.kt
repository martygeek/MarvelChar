package us.martypants.marveldemo.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import us.martypants.marvel.App
import us.martypants.marveldemo.ux.CharacterViewModel

class MyViewModelFactory(private val mApplication: App) :
    ViewModelProvider.Factory {


    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CharacterViewModel(mApplication) as T
    }
}