package live.kevalkanpariya.data.repository.category

import live.kevalkanpariya.data.models.Category
import live.kevalkanpariya.data.requests.CategoryRequest
import live.kevalkanpariya.util.Constants

interface CategoryRepository {

    suspend fun getPopularCategories(
        page: Int = 0,
        pageSize: Int = Constants.DEFAULT_PAGE_SIZE
    ): List<Category>?
    suspend fun createCategory(
        categoryRequest: CategoryRequest
    ): Boolean
    suspend fun updateCategory(
        categoryId: String,
        categoryRequest: CategoryRequest
    ): Boolean
    suspend fun deleteCategory(
        categoryId: String
    ): Boolean
}