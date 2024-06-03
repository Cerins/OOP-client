package com.example.studybuddy.domain.auth

import android.content.Intent
import android.provider.MediaStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TakePhotoUseCase {
    operator fun invoke(): Flow<Intent> = flow {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        emit(takePictureIntent)
    }
}
