package us.martypants.marvel.managers

import rx.Observable
import us.martypants.marvel.models.MarvelData
import us.martypants.marvel.network.DataManagerAPI
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*


class DataManager(private val mApi: DataManagerAPI) {

    fun getMarvelCharacters(offset: Int) : Observable<MarvelData> {
        val ts = Date().time.toString()
        val hash = md5(ts + PRIVATE_KEY + API_KEY)
        return mApi.getMarvelCharacters(ts, API_KEY, hash, offset)
    }

    fun getSingleMarvelCharacter(characterId: Int): Observable<MarvelData> {
        val ts = Date().time.toString()
        val hash = md5(ts + PRIVATE_KEY + API_KEY)
        return mApi.getSingleMarvelCharacter(characterId, ts, API_KEY, hash)
    }

    private fun md5(s: String): String {
        try {
            // Create MD5 Hash
            val digest = MessageDigest
                .getInstance(MD5)
            digest.update(s.toByteArray())
            val messageDigest = digest.digest()

            // Create Hex String
            val hexString = StringBuilder()
            for (aMessageDigest in messageDigest) {
                var h = Integer.toHexString(0xFF and aMessageDigest.toInt())
                while (h.length < 2) h = "0$h"
                hexString.append(h)
            }
            return hexString.toString()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }
        return ""
    }

    companion object {
        const val API_KEY = "1458a5a33b1f8484f27b801e6d226daf"
        const val PRIVATE_KEY = "671d6d8535daa198ca7c41dd89a83125af313c60"
        const val PAGE_SIZE = 20
        const val MD5 = "MD5"
    }
}