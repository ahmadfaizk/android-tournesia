package com.d3itb.tournesia.di

import android.content.Context
import com.d3itb.tournesia.data.TournesiaRepository
import com.d3itb.tournesia.data.remote.RemoteDataSource

object Injection {
    fun provideRepository(context: Context): TournesiaRepository {
        val remoteDataSource = RemoteDataSource.getInstance(context)
        return TournesiaRepository.getInstance(remoteDataSource)
    }
}