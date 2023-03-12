package pl.tkadziolka.snipmeandroid.di

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level.BASIC
import okhttp3.logging.HttpLoggingInterceptor.Level.BODY
import org.koin.dsl.module
import pl.tkadziolka.snipmeandroid.BuildConfig
import pl.tkadziolka.snipmeandroid.util.AuthInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import timber.log.Timber
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.*

private const val TAG_LOG_OKHTTP = "OkHttp"
private const val KEY_HEADER_AUTHORIZATION = "Authorization"
private const val KEY_HEADER_COOKIE = "Cookie"
private const val KEY_PROTOCOL_SSL = "SSL"

internal val networkModule = module {

    single {
        HttpLoggingInterceptor.Logger { message -> Timber.tag(TAG_LOG_OKHTTP).d(message) }
    }

    single {
        HttpLoggingInterceptor(get()).apply {
            if (BuildConfig.DEBUG) setLevel(BODY) else setLevel(BASIC)
            redactHeader(KEY_HEADER_AUTHORIZATION)
            redactHeader(KEY_HEADER_COOKIE)
        }
    }

    single { AuthInterceptor(get()) }

    single<X509TrustManager> {
        object : X509TrustManager {
            override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) = Unit

            override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) = Unit

            override fun getAcceptedIssuers(): Array<X509Certificate> = emptyArray()
        }
    }
    single<TrustManager> { get<X509TrustManager>() }

    single<SSLSocketFactory> {
        SSLContext.getInstance(KEY_PROTOCOL_SSL).apply {
            init(null, arrayOf<TrustManager>(get()), SecureRandom())
        }.socketFactory
    }

    single {
        OkHttpClient.Builder().apply {
            addInterceptor(get<HttpLoggingInterceptor>())
            addInterceptor(get<AuthInterceptor>())
            sslSocketFactory(get(), get())
        }.build()
    }

    single<Retrofit> {
        Retrofit.Builder()
            .client(get<OkHttpClient>())
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }
}