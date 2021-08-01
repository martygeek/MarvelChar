package us.martypants.marvel.modules

import android.content.Context
import com.google.gson.*
import com.google.gson.internal.bind.util.ISO8601Utils
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import us.martypants.marvel.App
import java.lang.reflect.Type
import java.text.ParseException
import java.text.ParsePosition
import java.util.*
import javax.inject.Singleton

/**
 * Module for providing network related dependencies.
 *
 */
@Module
class NetModule(context: App, networkEndpoint: String) {
    private val mContext: Context
    private val mNetworkEndpoint: String
    private val mApp: App
    @Singleton
    @Provides
    fun provideApp(): App {
        return mApp
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder()
            .registerTypeAdapter(
                Date::class.java,
                GsonUtcDateAdapter()
            ) //                .excludeFieldsWithoutExposeAnnotation()
            .create()
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        // Set up some caching
        val cacheSize = 10 * 1024 * 1024 // 10 MiB
        val cache = Cache(mContext.cacheDir, cacheSize.toLong())

        // Set up some logging
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder()
            .cache(cache)
            .addInterceptor { chain: Interceptor.Chain ->
                var request = chain.request()
                request = request
                    .newBuilder()
                    .addHeader("Content-Type", "application/json").build()
                chain.proceed(request)
            }
            .addInterceptor(interceptor)
            .build()
    }

    inner class GsonUtcDateAdapter internal constructor() : JsonSerializer<Date?>,
        JsonDeserializer<Date> {
        @Synchronized
        override fun serialize(
            date: Date?,
            type: Type,
            jsonSerializationContext: JsonSerializationContext
        ): JsonElement {
            return JsonPrimitive(ISO8601Utils.format(date))
        }

        @Synchronized
        override fun deserialize(
            jsonElement: JsonElement,
            type: Type,
            jsonDeserializationContext: JsonDeserializationContext
        ): Date {
            return try {
                ISO8601Utils.parse(jsonElement.asString, ParsePosition(0))
            } catch (e: ParseException) {
                throw JsonParseException(e)
            }
        }
    }

    @Singleton
    @Provides
    fun provideRetrofit(gson: Gson?, client: OkHttpClient?): Retrofit {
        return Retrofit.Builder()
            .baseUrl(mNetworkEndpoint)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .build()
    }

    init {
        mContext = context
        mApp = context
        mNetworkEndpoint = networkEndpoint
    }
}