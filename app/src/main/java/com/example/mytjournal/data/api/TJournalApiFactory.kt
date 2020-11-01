package com.example.mytjournal.data.api

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object TJournalApiFactory {

    fun retrofit() : Retrofit = Retrofit.Builder()
        .baseUrl("https://api.tjournal.ru/v1.9/")
        .addConverterFactory(MoshiConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()


    val api : TJournalApi = retrofit().create(TJournalApi::class.java)
}