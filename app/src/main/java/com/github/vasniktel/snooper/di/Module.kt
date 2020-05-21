package com.github.vasniktel.snooper.di

import com.github.vasniktel.snooper.logic.message.MessageRepository
import com.github.vasniktel.snooper.logic.message.MessageRepositoryImpl
import com.github.vasniktel.snooper.logic.message.remote.RemoteMessageDataSource
import com.github.vasniktel.snooper.logic.user.UserRepository
import com.github.vasniktel.snooper.logic.user.UserRepositoryImpl
import com.github.vasniktel.snooper.logic.user.remote.RemoteUserDataSource
import com.github.vasniktel.snooper.ui.activity.MainActivityViewModel
import com.github.vasniktel.snooper.ui.feed.FeedFragmentViewModel
import com.github.vasniktel.snooper.ui.messagelist.MessageListViewModel
import com.github.vasniktel.snooper.ui.messagelist.MessageListViewModelImpl
import com.github.vasniktel.snooper.ui.user.UserFragmentViewModel
import com.github.vasniktel.snooper.ui.userlist.UserListViewModel
import com.github.vasniktel.snooper.util.RETROFIT_IP
import com.squareup.moshi.Moshi
import com.squareup.moshi.Rfc3339DateJsonAdapter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.*
import kotlin.time.ExperimentalTime

@FlowPreview
@ExperimentalCoroutinesApi
@ExperimentalTime
val productionModule = module {
    single<UserRepository> { UserRepositoryImpl(get())}
    single<Retrofit> {
        Retrofit.Builder()
            .baseUrl(RETROFIT_IP)
            .addConverterFactory(
                MoshiConverterFactory.create(
                    Moshi.Builder()
                        .add(Date::class.java, Rfc3339DateJsonAdapter().nullSafe())
                        .build()
                )
            )
            .build()
    }
    single<RemoteUserDataSource> {
        get<Retrofit>().create(RemoteUserDataSource::class.java)
    }
    single<RemoteMessageDataSource> {
        get<Retrofit>().create(RemoteMessageDataSource::class.java)
    }
    single<MessageRepository> { MessageRepositoryImpl(get()) }
    viewModel { FeedFragmentViewModel(get()) }
    viewModel { MainActivityViewModel(get()) }
    viewModel<MessageListViewModel> { MessageListViewModelImpl(get(), get()) }
    viewModel { UserListViewModel(get()) }
    viewModel { UserFragmentViewModel(get()) }
}

val testModule = module {

}
