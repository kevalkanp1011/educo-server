package live.kevalkanpariya.util.rating

import live.kevalkanpariya.domain.model.db.Rating
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq

class RatingImpl(db: CoroutineDatabase) {

    private val ratings = db.getCollection<Rating>()

    suspend fun calculateAvgRating(): Double {
        val noOfStudentRated = countTotalNoOfStudentRated()
        val rating1 = ratings.countDocuments(Rating::rating eq 1)
        val rating2 = ratings.countDocuments(Rating::rating eq 2)
        val rating3 = ratings.countDocuments(Rating::rating eq 3)
        val rating4 = ratings.countDocuments(Rating::rating eq 4)
        val rating5 = ratings.countDocuments(Rating::rating eq 5)

        return ((rating1 + rating2*2 + rating3*3 + rating4*4 + rating5*5)/noOfStudentRated).toDouble()

    }

    suspend fun countTotalNoOfStudentRated(): Long {
        return ratings.countDocuments()
    }


}