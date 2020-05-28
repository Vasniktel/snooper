package com.github.vasniktel.snooper.di

import androidx.room.Room
import com.github.vasniktel.snooper.logic.Db
import com.github.vasniktel.snooper.logic.location.LocationProvider
import com.github.vasniktel.snooper.logic.location.LocationProviderImpl
import com.github.vasniktel.snooper.logic.message.MessageRepository
import com.github.vasniktel.snooper.logic.message.MessageRepositoryImpl
import com.github.vasniktel.snooper.logic.message.remote.RemoteMessageDataSource
import com.github.vasniktel.snooper.logic.search.SearchQueryBroadcaster
import com.github.vasniktel.snooper.logic.search.SearchQueryProducer
import com.github.vasniktel.snooper.logic.search.SearchQueryStore
import com.github.vasniktel.snooper.logic.subscription.RemoteSubscriptionDataSource
import com.github.vasniktel.snooper.logic.subscription.SubscriptionRepository
import com.github.vasniktel.snooper.logic.subscription.SubscriptionRepositoryImpl
import com.github.vasniktel.snooper.logic.user.UserRepository
import com.github.vasniktel.snooper.logic.user.UserRepositoryImpl
import com.github.vasniktel.snooper.logic.user.remote.RemoteUserDataSource
import com.github.vasniktel.snooper.ui.feed.FeedFragmentViewModel
import com.github.vasniktel.snooper.ui.login.LoginViewModel
import com.github.vasniktel.snooper.ui.messagelist.MessageListType
import com.github.vasniktel.snooper.ui.messagelist.viewmodel.MessageFeedViewModel
import com.github.vasniktel.snooper.ui.messagelist.viewmodel.MessageListViewModel
import com.github.vasniktel.snooper.ui.messagelist.viewmodel.MessagePostsViewModel
import com.github.vasniktel.snooper.ui.messagelist.viewmodel.MessageSearchViewModel
import com.github.vasniktel.snooper.ui.user.UserFragmentViewModel
import com.github.vasniktel.snooper.ui.userlist.UserListType
import com.github.vasniktel.snooper.ui.userlist.viewmodel.UserFolloweesViewModel
import com.github.vasniktel.snooper.ui.userlist.viewmodel.UserFollowersViewModel
import com.github.vasniktel.snooper.ui.userlist.viewmodel.UserListViewModel
import com.github.vasniktel.snooper.ui.userlist.viewmodel.UserSearchViewModel
import com.github.vasniktel.snooper.util.RETROFIT_IP
import com.squareup.moshi.Moshi
import com.squareup.moshi.Rfc3339DateJsonAdapter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import java.util.*
import kotlin.time.ExperimentalTime

@FlowPreview
@ExperimentalCoroutinesApi
@ExperimentalTime
val productionModule = module {
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

    single {
        Room
            .databaseBuilder(
                androidApplication(),
                Db::class.java,
                "snooper_db"
            )
            .fallbackToDestructiveMigration()
            .build()
    }
    single { get<Db>().getFollowersDao() }
    single { get<Db>().getFolloweesDao() }
    single { get<Db>().getFeedDao() }
    single { get<Db>().getPostsDao() }

    single { get<Retrofit>().create<RemoteUserDataSource>() }
    single<UserRepository> { UserRepositoryImpl(get(), get(), get())}

    single<RemoteMessageDataSource> { get<Retrofit>().create() }
    single<MessageRepository> { MessageRepositoryImpl(get(), get(), get()) }

    single<RemoteSubscriptionDataSource> { get<Retrofit>().create() }
    single<SubscriptionRepository> { SubscriptionRepositoryImpl(get()) }

    single<LocationProvider> { LocationProviderImpl(androidApplication(), Locale.US) }

    single { SearchQueryStore() }
    single<SearchQueryBroadcaster> { get<SearchQueryStore>() }
    single<SearchQueryProducer> { get<SearchQueryStore>() }

    viewModel { FeedFragmentViewModel(get()) }
    viewModel { LoginViewModel(get()) }
    viewModel { UserFragmentViewModel(get(), get(), get(), get()) }
    viewModel<MessageListViewModel>(named(MessageListType.FEED)) {
        MessageFeedViewModel(get())
    }
    viewModel<MessageListViewModel>(named(MessageListType.POSTS)) {
        MessagePostsViewModel(get())
    }
    viewModel<MessageListViewModel>(named(MessageListType.SEARCH)) {
        MessageSearchViewModel(get(), get())
    }
    viewModel<UserListViewModel>(named(UserListType.FOLLOWEES)) {
        UserFolloweesViewModel(get())
    }
    viewModel<UserListViewModel>(named(UserListType.FOLLOWERS)) {
        UserFollowersViewModel(get())
    }
    viewModel<UserListViewModel>(named(UserListType.SEARCH)) {
        UserSearchViewModel(get(), get())
    }
}
