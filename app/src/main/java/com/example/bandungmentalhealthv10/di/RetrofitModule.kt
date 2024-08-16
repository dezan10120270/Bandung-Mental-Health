package com.example.bandungmentalhealthv10.di

import com.example.bandungmentalhealthv10.data.retrofit.ApiInterface
import com.example.bandungmentalhealthv10.utils.RetrofitConstants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RetrofitModule {

    @Provides
    @Singleton
    fun provideRetrofitInstance(): ApiInterface {
        return Retrofit.Builder()
            .baseUrl(RetrofitConstants.PROFANITY_CHECK_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
//            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiInterface::class.java)
    }

}