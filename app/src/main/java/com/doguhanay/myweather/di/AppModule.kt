package com.doguhanay.myweather.di

import android.app.Activity
import android.app.Application
import android.content.Context
import com.doguhanay.myweather.HiltApp
import com.doguhanay.myweather.network.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton
//SingletonComponent
@InstallIn(SingletonComponent::class)
@Module
class AppModule {

    @Provides
    @Singleton
    fun provideOkHttpClient():OkHttpClient{

        return OkHttpClient.Builder().build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient):Retrofit{

        return Retrofit.Builder()
            .baseUrl("https://api.open-meteo.com/v1/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit):ApiService{
        return retrofit.create(ApiService::class.java)
    }

   /* @Singleton
    @Provides
    fun provideContext(application: Application): Context = application.applicationContext*/

   /* @Provides
    fun provideActivity(@ActivityContext activity: Context): Context {
        return activity
    }*/

   /* @Singleton
    @Provides
    fun provideApplication(@ApplicationContext app: Context):   HiltApp{
        return app as HiltApp
    }*/

   /* @Provides
    @ActivityContext
    fun provideApplicationContext(@ActivityContext appContext: Context): Context {
        return appContext
    }*/

}