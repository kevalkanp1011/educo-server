package live.kevalkanpariya.di


import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.reactivestreams.client.MongoDatabase
import live.kevalkanpariya.data.repository.UserDataSourceImpl
import live.kevalkanpariya.domain.repository.UserDataSource
import live.kevalkanpariya.storage.aws.AmazonFileStorage
import live.kevalkanpariya.storage.aws.FileStorage
import live.kevalkanpariya.storage.exposed.LocalSource
import live.kevalkanpariya.storage.exposed.LocalSourceImpl
import live.kevalkanpariya.util.Constants.DATABASE_NAME
import org.koin.dsl.module
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo


fun getKoinModule() = module {
    single<FileStorage> {
        AmazonFileStorage()
    }
    single<LocalSource> {
        LocalSourceImpl(get(), get())
    }
    single<UserDataSource> {
        UserDataSourceImpl(get())
    }
    /*single<CourseRepo> {
        CourseRepoImpl(get())
    }*/
    single {
        KMongo.createClient("YOUR-CONNECTION-STRING")
            .coroutine
            .getDatabase(DATABASE_NAME)
    }

}