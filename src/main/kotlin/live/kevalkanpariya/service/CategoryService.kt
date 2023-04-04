package live.kevalkanpariya.service

import live.kevalkanpariya.data.models.Category
import live.kevalkanpariya.data.repository.category.CategoryRepository
import live.kevalkanpariya.data.requests.CategoryRequest
import live.kevalkanpariya.util.Constants

class CategoryService(
    private val categoryRepository: CategoryRepository
) {

    suspend fun getPopularCategories(
        page: Int = 0,
        pageSize: Int = Constants.DEFAULT_PAGE_SIZE
    ): List<Category>? {
        return categoryRepository.getPopularCategories(page, pageSize)
    }

    suspend fun createCategory(
        categoryRequest: CategoryRequest
    ): Boolean {
        return categoryRepository.createCategory(categoryRequest)
    }

    suspend fun updateCategory(
        categoryId: String,
        categoryRequest: CategoryRequest
    ): Boolean {
        return categoryRepository.updateCategory(categoryId, categoryRequest)
    }

    suspend fun deleteCategory(
        categoryId: String
    ): Boolean {
        return categoryRepository.deleteCategory(categoryId)
    }
}