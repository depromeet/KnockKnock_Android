package com.depromeet.domain.repository

import com.depromeet.domain.NetworkResult
import com.depromeet.domain.model.*


interface  MainRepository {

    suspend fun refreshTokenAPI(refreshToken: String): NetworkResult<RefreshTokenResponse>

    suspend fun getKakaoLogin(code: String): NetworkResult<KakaoLoginResponse>

    suspend fun getGoogleLogin(code: String): NetworkResult<GoogleLoginResponse>

    suspend fun putUserNickname(nickName: String): NetworkResult<Unit>

    suspend fun getUserNickname(nickName: String): NetworkResult<SearchUserNicknameResponse>

    suspend fun getGroup(id: Int): NetworkResult<GroupResponse>

    suspend fun putGroup(
        id: Int,
        title: String,
        description: String,
        publicAccess: Boolean,
        thumbnailPath: String,
        backgroundImagePath: String,
        categoryId: Int
    ): NetworkResult<GroupResponse>

    suspend fun deleteGroup(id: Int): NetworkResult<Unit>

    suspend fun addGroupMember(id: Int, members: List<Int>): NetworkResult<GroupResponse>
}