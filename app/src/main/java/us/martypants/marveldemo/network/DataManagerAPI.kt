package us.martypants.marvel.network

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import rx.Observable
import us.martypants.marvel.models.MarvelData

interface DataManagerAPI {

    @GET("/v1/public/characters")
    fun getMarvelCharacters(@Query("ts")  ts: String,
                            @Query("apikey") apikey: String,
                            @Query("hash") hash: String,
                            @Query("offset") offset: Int): Observable<MarvelData>

    @GET("/v1/public/characters/{characterId}")
    fun getSingleMarvelCharacter(@Path("characterId") characterId: Int,
                                 @Query("ts") ts: String,
                                 @Query("apikey") apikey: String,
                                 @Query("hash") hash: String): Observable<MarvelData>
}