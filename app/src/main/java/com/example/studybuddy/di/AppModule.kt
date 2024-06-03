package com.example.studybuddy.di

import com.example.studybuddy.data.repo.AuthRepository
import com.example.studybuddy.data.repo.MessageRepository
import com.example.studybuddy.data.repo.UserRepository
import com.example.studybuddy.domain.auth.GetConversationsUseCase
import com.example.studybuddy.domain.auth.GetFriendsUseCase
import com.example.studybuddy.domain.auth.CreateMessageUseCase
import com.example.studybuddy.domain.auth.GetMessagesUseCase
import com.example.studybuddy.domain.auth.GetUserUseCase
import com.example.studybuddy.domain.auth.LoginUseCase
import com.example.studybuddy.domain.auth.LogoutUseCase
import com.example.studybuddy.domain.auth.PickImageUseCase
import com.example.studybuddy.domain.auth.RegisterUseCase
import com.example.studybuddy.domain.auth.TakePhotoUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideLoginUseCase(repository: AuthRepository): LoginUseCase {
        return LoginUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideLogoutUseCase(repository: AuthRepository): LogoutUseCase {
        return LogoutUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideRegisterUseCase(repository: AuthRepository): RegisterUseCase {
        return RegisterUseCase(repository)
    }

    @Singleton
    @Provides
    fun provideTakePhotoUseCase(): TakePhotoUseCase {
        return TakePhotoUseCase()
    }

    @Singleton
    @Provides
    fun providePickImageUseCase(): PickImageUseCase {
        return PickImageUseCase()
    }

    @Singleton
    @Provides
    fun provideGetFriendsUseCase(repository: UserRepository): GetFriendsUseCase {
        return GetFriendsUseCase(repository)
    }

    @Singleton
    @Provides
    fun provideGetUserUseCase(repository: UserRepository): GetUserUseCase {
        return GetUserUseCase(repository)
    }

    @Singleton
    @Provides
    fun provideGetMessagesUseCase(repository: MessageRepository): GetMessagesUseCase {
        return GetMessagesUseCase(repository)
    }

    @Singleton
    @Provides
    fun provideGetConversationsUseCase(repository: MessageRepository): GetConversationsUseCase {
        return GetConversationsUseCase(repository)
    }

    @Singleton
    @Provides
    fun provideCreateMessageUseCase(repository: MessageRepository): CreateMessageUseCase {
        return CreateMessageUseCase(repository)
    }
}
