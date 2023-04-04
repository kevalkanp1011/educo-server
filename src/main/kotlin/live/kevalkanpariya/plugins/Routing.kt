package live.kevalkanpariya.plugins

import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.routing.*
import live.kevalkanpariya.data.repository.aws.FileStorage
import live.kevalkanpariya.routes.*
import live.kevalkanpariya.service.*
import live.kevalkanpariya.util.security.hashing.HashingService
import org.koin.ktor.ext.inject
import kotlin.time.ExperimentalTime


fun Application.configureRouting() {

    routing {

        val fileStorage by inject<FileStorage>()

        /*Services*/
        val userService by inject<UserService>()
        val hashingService by inject<HashingService>()
        val courseService by inject<CourseService>()
        val lessonService by inject<LessonService>()
        val resourceService by inject<ResourceService>()
        val followService by inject<FollowService>()
        val activityService by inject<ActivityService>()
        val commentService by inject<CommentService>()
        val likeService by inject<LikeService>()
        val feedBackService by inject<FeedBackService>()
        val projectService by inject<ProjectService>()
        val categoryService by inject<CategoryService>()

        val jwtIssuer = this@configureRouting.environment.config.property("jwt.domain").getString()
        val jwtAudience = this@configureRouting.environment.config.property("jwt.audience").getString()
        val jwtSecret = System.getenv("JWT_SECRET")

        /* Root Route*/
        rootRoute()

        /* Authentication Routes */
        authenticate()
        createUser(userService, hashingService) /*SignUp*/
        loginUser(hashingService, userService, jwtIssuer, jwtAudience, jwtSecret) /*SignIn*/
        deleteUser(userService)
        resetPassword(userService, hashingService)

        /*User & Profile*/
        searchUser(userService = userService)
        getUserInfos(userService = userService)
        getUserHeader(userService)
        getUserProfile(userService = userService)
        updateUserProfile(userService = userService, fileStorage = fileStorage, app = application)

        /*Course Routes*/
        getMostWatchedCourses(courseService, application)
        getPreviousWatchedCourses(courseService)
        getOthersWatchedCourses(courseService)
        getCourseDetails(courseService, application)
        searchCourses(courseService, application)

        getCoursesForProfile(courseService, application)

        createCourse(courseService, fileStorage, application)
        getCourse(courseService)
        updateCourseInfo(courseService,application,fileStorage)
        deleteCourse(courseService)


        /* Category Routes*/
        getPopularCategories(categoryService = categoryService)
        createCategory(categoryService, fileStorage, application)
        updateCategory(categoryService, fileStorage, application)
        deleteCategory(categoryService)

        /*Lesson Routes*/
        getLesson(lessonService)
        getLessonsForCourse(lessonService)
        createLesson(application, fileStorage, lessonService)
        updateLessonForCourse(lessonService, fileStorage, application)
        deleteLesson(lessonService)
        deleteAllLessons(lessonService)

        /*Resource Routes*/
        getResourcesForCourse(resourceService, application)
        createResource(fileStorage, resourceService, application)
        updateResource(fileStorage, resourceService, application)
        deleteResource(resourceService)
        deleteAllResources(resourceService)

        /*Enroll & Rate a Course*/
        enrollCourse(courseService, application)
        rateCourse(courseService, application)

        /* Follow & unFollow User*/
        followUser(followService = followService, activityService = activityService, application)
        unfollowUser(followService = followService)

        /* Comment */
        createComment(commentService, activityService, application)
        getCommentsForCourse(commentService)
        deleteComment(commentService, likeService)

        /*Like*/
        likeParent(likeService, activityService, application)
        unlikeParent(likeService)
        getLikesForParent(likeService)

        /*Feedback */
        createFeedBack(feedBackService)
        updateFeedBack(feedBackService)
        deleteFeedBack(feedBackService)
        deleteAllFeedBack(feedBackService)

        /*Project*/
        getProject(projectService)
        getProjectsForCourse(projectService)
        createProject(projectService)
        updateProject(projectService)
        deleteProject(projectService)
        deleteAllProject(projectService)

        static {
            resources("static")
        }

    }
}
