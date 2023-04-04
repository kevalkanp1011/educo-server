package live.kevalkanpariya.di


import live.kevalkanpariya.data.repository.activity.ActivityRepository
import live.kevalkanpariya.data.repository.activity.ActivityRepositoryImpl
import live.kevalkanpariya.data.repository.aws.AmazonFileStorage
import live.kevalkanpariya.data.repository.aws.FileStorage
import live.kevalkanpariya.data.repository.category.CategoryRepository
import live.kevalkanpariya.data.repository.category.CategoryRepositoryImpl
import live.kevalkanpariya.data.repository.comment.CommentRepository
import live.kevalkanpariya.data.repository.comment.CommentRepositoryImpl
import live.kevalkanpariya.data.repository.course.CourseRepository
import live.kevalkanpariya.data.repository.course.CourseRepositoryImpl
import live.kevalkanpariya.data.repository.feedback.FeedbackRepository
import live.kevalkanpariya.data.repository.feedback.FeedbackRepositoryImpl
import live.kevalkanpariya.data.repository.follow.FollowRepository
import live.kevalkanpariya.data.repository.follow.FollowRepositoryImpl
import live.kevalkanpariya.data.repository.lesson.LessonRepository
import live.kevalkanpariya.data.repository.lesson.LessonRepositoryImpl
import live.kevalkanpariya.data.repository.like.LikeRepository
import live.kevalkanpariya.data.repository.like.LikeRepositoryImpl
import live.kevalkanpariya.data.repository.project.ProjectRepository
import live.kevalkanpariya.data.repository.project.ProjectRepositoryImpl
import live.kevalkanpariya.data.repository.resource.ResourceRepository
import live.kevalkanpariya.data.repository.resource.ResourceRepositoryImpl
import live.kevalkanpariya.data.repository.user.UserRepository
import live.kevalkanpariya.data.repository.user.UserRepositoryImpl
import live.kevalkanpariya.service.*
import live.kevalkanpariya.util.Constants.DATABASE_NAME
import live.kevalkanpariya.util.security.hashing.HashingService
import live.kevalkanpariya.util.security.hashing.SHA256HashingService
import org.koin.dsl.module
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo


fun getKoinModule() = module {

    single {
        KMongo.createClient("your_connection_string")
            .coroutine
            .getDatabase(DATABASE_NAME)
    }

    single<FileStorage> {
        AmazonFileStorage()
    }

    /*Repository Instance*/
    single<UserRepository> {
        UserRepositoryImpl(get())
    }
    single<FollowRepository> {
        FollowRepositoryImpl(get())
    }
    single<CourseRepository> {
        CourseRepositoryImpl(get())
    }
    single<LessonRepository> {
        LessonRepositoryImpl(get())
    }
    single<ResourceRepository> {
        ResourceRepositoryImpl(get())
    }
    single<ActivityRepository> {
        ActivityRepositoryImpl(get())
    }
    single<CommentRepository> {
        CommentRepositoryImpl(get())
    }
    single<LikeRepository> {
        LikeRepositoryImpl(get())
    }
    single<FeedbackRepository> {
        FeedbackRepositoryImpl(get())
    }
    single<ProjectRepository> {
        ProjectRepositoryImpl(get())
    }
    single<CategoryRepository> {
        CategoryRepositoryImpl(get())
    }


    /*Service Instance*/
    single { UserService(get(), get()) }
    single { CourseService(get()) }
    single { LessonService(get()) }
    single { ResourceService(get()) }
    single { FollowService(get()) }
    single { ActivityService(get(), get(), get()) }
    single { CommentService(get(), get()) }
    single { LikeService(get(), get(), get()) }
    single { FeedBackService(get()) }
    single { ProjectService(get()) }
    single { CategoryService(get()) }
    single<HashingService> {
        SHA256HashingService()
    }


}