/*
 * *
 *  * Created by Mohsen on 10/8/22, 3:50 AM
 *  * Copyright (c) 2022 . All rights reserved.
 *  * Last modified 10/8/22, 3:39 AM
 *
 */

package com.example.appsfactory.data.repository

import com.bumptech.glide.load.HttpException
import com.example.appsfactory.data.source.local.AppDatabase
import com.example.appsfactory.data.source.local.entity.AlbumInfoEntity
import com.example.appsfactory.data.source.remote.ApiService
import com.example.appsfactory.di.modules.IoDispatcher
import com.example.appsfactory.domain.model.albumInfo.toEntity
import com.example.appsfactory.domain.repository.AlbumInfoRepository
import com.example.appsfactory.util.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.IOException

class AlbumInfoRepositoryImpl(
    private val apiService: ApiService,
    private val appDb: AppDatabase,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : AlbumInfoRepository {

    private val albumInfoDao = appDb.albumInfoDao()

    override suspend fun getAlbumInfo(
        id: Int,
        albumName: String,
        artistName: String
    ): Flow<Resource<AlbumInfoEntity>> = flow {
        emit(Resource.Loading())

        val albumInfo = appDb.albumInfoDao().getAlbumInfo(id)
        emit(Resource.Loading(data = albumInfo))

        try {
            val remoteAlbumInfo = apiService.fetchAlbumInfo(albumName, artistName).album
            albumInfoDao.deleteAll()
            albumInfoDao.insert(remoteAlbumInfo.toEntity())
        } catch (e: HttpException) {
            emit(Resource.Error(message = "Request failed! Please try again...", data = albumInfo))
        } catch (e: IOException) {
            emit(Resource.Error(message = "No Internet connection!", data = albumInfo))
        }

        val newAlbumInfo = appDb.albumInfoDao().getAlbumInfo(id)
        emit(Resource.Success(data = newAlbumInfo))
    }.flowOn(ioDispatcher)
}