package com.emplk.realestatemanager.domain.content_resolver

interface PictureFileRepository {
    suspend fun saveToAppFiles(stringUri: String, filePrefix: String): String?
    suspend fun deleteFromAppFiles(absolutePath: String)
}
