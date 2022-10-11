/*
 * *
 *  * Created by Mohsen on 10/4/22, 1:30 PM
 *  * Copyright (c) 2022 . All rights reserved.
 *  * Last modified 10/4/22, 1:30 PM
 *
 */

package com.example.appsfactory.domain.repository

import com.example.appsfactory.data.source.local.entity.AlbumInfoEntity
import com.example.appsfactory.util.Resource
import kotlinx.coroutines.flow.Flow

interface AlbumInfoRepository {

    suspend fun getAlbumInfo(
        id: Int,
        albumName: String,
        artistName: String
    ): Flow<Resource<AlbumInfoEntity>>
}