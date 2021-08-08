package com.example.remittancetracker

import android.app.Application
import androidx.databinding.library.BuildConfig
import com.example.remittancetracker.repo.IRepoDataSource
import com.example.remittancetracker.repo.MashProRepository
import com.example.remittancetracker.repo.remote.IRemoteDataSource
import com.example.remittancetracker.repo.remote.RemoteDataSourceImpl
import com.example.remittancetracker.ui.authentication.AuthenticationViewModel
import com.example.remittancetracker.ui.createAgent.CreateAgentViewModel
import com.example.remittancetracker.ui.home.HomeViewModel
import com.example.remittancetracker.ui.transaction.TransactionViewModel
import com.example.remittancetracker.util.SharedPrefsHelper
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import timber.log.Timber

class App : Application() {

     lateinit var sharedPref : SharedPrefsHelper

    override fun onCreate() {
        super.onCreate()

        sharedPref = SharedPrefsHelper(this)

        if(BuildConfig.DEBUG){
            Timber.plant(Timber.DebugTree())
        }

        /**
         * use Koin Library as a service locator
         */
        val myModule = module {
            //Declare a ViewModel - be later inject into Fragment with dedicated injector using by viewModel()

            viewModel {
                AuthenticationViewModel(
                    this@App,
                    get() as IRepoDataSource
                )
            }



            viewModel {
                HomeViewModel(
                    this@App,
                    get() as IRepoDataSource
                )
            }


            viewModel {
                CreateAgentViewModel(
                    this@App,
                    get() as IRepoDataSource
                )
            }

            single {
                TransactionViewModel(
                    this@App,
                    get() as IRepoDataSource
                )
            }


            //  single { MyFirebaseMessagingService(get() as IRepoDataSource) }
           // single { SharedPrefsHelper(this@App) }
            single { MashProRepository(get() as IRemoteDataSource) as IRepoDataSource }
            single { RemoteDataSourceImpl() as IRemoteDataSource }

        }

        startKoin {
            androidContext(this@App)
            modules(listOf(myModule))
        }


    }



    companion object {
        const val DEFAULT_PREFERENCES = "default_preferences"

    }

}