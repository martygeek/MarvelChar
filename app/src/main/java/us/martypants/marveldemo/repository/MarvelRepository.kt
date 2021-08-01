package us.martypants.marvel.repository

import android.util.Log
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import us.martypants.marvel.App
import us.martypants.marvel.managers.DataManager
import us.martypants.marvel.models.MarvelData
import javax.inject.Inject

class MarvelRepository(app: App) {

    init {
        app.userComponent?.inject(this)
    }

    @Inject
    lateinit var dataManager: DataManager


    fun getMarvelCharacters( page: Int, completion: (result: Pair<MarvelData?, Error?>) -> Unit) {

        dataManager.getMarvelCharacters(page)
            .subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe({
                completion(Pair(it, null))
            },
            {
                completion(Pair(null, Error(it.localizedMessage)))
            })
    }

    fun getSingleMarvelCharacter( characterId: Int, completion: (result: Pair<MarvelData?, Error?>) -> Unit) {

        dataManager.getSingleMarvelCharacter(characterId)
            .subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe({
                completion(Pair(it, null))
            },
            {
                completion(Pair(null, Error(it.localizedMessage)))
            })
    }
}