package live.kevalkanpariya.storage.aws

import java.io.File

interface FileStorage {
    suspend fun save(file: File): String
}