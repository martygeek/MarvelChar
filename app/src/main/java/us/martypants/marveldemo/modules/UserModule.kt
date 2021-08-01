package us.martypants.marvel.modules

import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import us.martypants.marvel.App
import us.martypants.marvel.managers.DataManager
import us.martypants.marvel.network.DataManagerAPI
import us.martypants.marvel.repository.MarvelRepository
import javax.inject.Singleton

@Module
class UserModule {
    @Singleton
    @Provides
    fun getServerAPI(retrofit: Retrofit): DataManagerAPI {
        return retrofit.create(DataManagerAPI::class.java)
    }

    @Singleton
    @Provides
    fun getDataManager(api: DataManagerAPI?): DataManager {
        return DataManager(api!!)
    }

    @Singleton
    @Provides
    fun getMarvelRepository(app: App?): MarvelRepository {
        return MarvelRepository(app!!)
    }
}