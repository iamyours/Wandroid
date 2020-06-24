package io.github.iamyours.wandroid.vo

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * url对应的content-type
 */
@Entity
data class UrlTypeVO(
    @PrimaryKey
    var urlMd5: String,
    var type: String
)