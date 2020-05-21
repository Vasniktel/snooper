package com.github.vasniktel.snooper

import android.app.Application
import com.github.vasniktel.snooper.di.productionModule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import kotlin.time.ExperimentalTime

@ExperimentalCoroutinesApi
@FlowPreview
@ExperimentalTime
class SnooperApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@SnooperApp)
            modules(productionModule)
        }
    }
}