package live.kevalkanpariya.data.repository.category

import live.kevalkanpariya.data.models.Category
import live.kevalkanpariya.data.requests.CategoryRequest
import live.kevalkanpariya.util.Constants
import org.litote.kmongo.combine
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq
import org.litote.kmongo.setValue

class CategoryRepositoryImpl(
    db: CoroutineDatabase
): CategoryRepository {

    private val categories = db.getCollection<Category>()
    override suspend fun getPopularCategories(page: Int, pageSize: Int): List<Category>? {
        return categories.find()
            .skip(page * pageSize)
            .limit(pageSize)
            .descendingSort(Category::categoryName)
            .toList()
            .map {category ->
                Category(
                    categoryId = category.categoryId,
                    categoryName = category.categoryName,
                    categoryImageUrl = category.categoryImageUrl
                )
            }
    }

    override suspend fun createCategory(categoryRequest: CategoryRequest): Boolean {
        return categories.insertOne(
            Category(
                categoryImageUrl = categoryRequest.categoryImageUrl?: Constants.DEFAULT_BANNER_IMAGE_PATH,
                categoryName = categoryRequest.categoryName
            )
        ).wasAcknowledged()
    }

    override suspend fun updateCategory(categoryId: String, categoryRequest: CategoryRequest): Boolean {
        return categories.updateOne(
            filter = Category::categoryId eq categoryId,
            update = combine(
                setValue(Category::categoryName, categoryRequest.categoryName),
                setValue(Category::categoryImageUrl, categoryRequest.categoryImageUrl)
            )
            ).wasAcknowledged()
    }

    override suspend fun deleteCategory(categoryId: String): Boolean {
        return categories.deleteOne(categoryId).wasAcknowledged()
    }
}