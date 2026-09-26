package com.skillx.features.ratings.data.mapper
import com.skillx.features.ratings.domain.model.Rating

object RatingMapper {
    fun fromMap(id: String, data: Map<String, Any?>): Rating = Rating(
        id = id,
        lessonId = (data["lessonId"] as? String) ?: "",
        raterId = (data["raterId"] as? String) ?: "",
        ratedUserId = (data["ratedUserId"] as? String) ?: "",
        ratedUserName = (data["ratedUserName"] as? String) ?: "",
        rating = ((data["rating"] as? Number)?.toInt()) ?: 0,
        comment = (data["comment"] as? String) ?: "",
        timestamp = ((data["timestamp"] as? Number)?.toLong()) ?: 0L
    )
}
