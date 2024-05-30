package com.example.studybuddy.domain.auth

import android.content.Intent
import android.provider.MediaStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PickImageUseCase {
    operator fun invoke(): Flow<Intent> = flow {
        val pickPhotoIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        emit(pickPhotoIntent)
    }
}
