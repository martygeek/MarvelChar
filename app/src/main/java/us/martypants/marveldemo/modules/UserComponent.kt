package us.martypants.marvel.modules

import com.google.gson.Gson
import dagger.Component
import us.martypants.marvel.App
import us.martypants.marveldemo.ux.CharacterListActivity
import us.martypants.marveldemo.ux.CharacterViewModel
import us.martypants.marvel.repository.MarvelRepository
import us.martypants.marveldemo.ux.CharacterDetailActivity
import javax.inject.Singleton

@Singleton
@Component(modules = [UserModule::class, NetModule::class])
interface UserComponent {
    // Pass on any provided objects to subclasses.
    // If you are writing tests and can't get things injected
    // add to this list.
    fun gson(): Gson?
    fun inject(app: App?)
    fun inject(characterListActivity: CharacterListActivity?)
    fun inject(detailActivity: CharacterDetailActivity?)
    fun inject(viewmodel: CharacterViewModel?)
    fun inject(marvelRepository: MarvelRepository?)
}