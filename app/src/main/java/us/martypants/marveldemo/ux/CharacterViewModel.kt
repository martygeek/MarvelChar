package us.martypants.marveldemo.ux

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import us.martypants.marvel.App
import us.martypants.marvel.managers.DataManager.Companion.PAGE_SIZE
import us.martypants.marvel.models.MarvelData
import us.martypants.marvel.repository.MarvelRepository
import us.martypants.marveldemo.util.MutableListLiveData
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class CharacterViewModel (app: App) : BaseViewModel<CharacterViewModel.Event>(), CoroutineScope {

    init {
        app.userComponent?.inject(this)
    }

    @Inject
    lateinit var repo: MarvelRepository

    private val activityJob = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + activityJob

    var currentPage = 1
    private var currentCount = 0
    private var totalCount = 0

    private var _characterList = MutableListLiveData<us.martypants.marvel.models.Result>()
    val characterList: LiveData<List<us.martypants.marvel.models.Result>> = _characterList

    private var _characterDetail = MutableLiveData<us.martypants.marvel.models.Result>()
    val characterDetail: LiveData<us.martypants.marvel.models.Result> = _characterDetail

    private fun updateListData(marvelData: MarvelData) {
        totalCount = marvelData.data.total
        currentCount += marvelData.data.count
        _characterList.addAll(marvelData.data.results)
    }

    fun getNextPageOffset()  {
        val remaining = totalCount - currentCount
        if (remaining > 0) {
            currentPage++
            getCharacterList(currentPage * PAGE_SIZE)
        }
    }

    fun onItemClick(id: Int) {
       sendEvent(Event.ShowDetailView(id))
    }

    fun getCharacterList(page: Int) {
        repo.getMarvelCharacters(page) {
            it.first?.let {marvelData ->
               updateListData(marvelData)
            }

            it.second?.let {
                sendEvent(Event.ShowErrorDialog(it))
            }
        }
    }

    fun getCharacterDetail(characterId: Int) {
        repo.getSingleMarvelCharacter(characterId) {
            it.first?.let {
                if (it.data.results.size > 0) {
                    _characterDetail.postValue(it.data.results.get(0))
                }
            }

            it.second?.let {
                sendEvent(Event.ShowErrorDialog(it))
            }
        }
    }

    sealed class Event {
        object DismissView : Event()
        data class ShowErrorDialog(val error: Error): Event()
        data class ShowDetailView(val characterId: Int): Event()
    }


}