package io.github.iamyours.wandroid.web

/**
 * 含坐标的图片数据
 */
data class PositionImage(
    var url: String,
    var x: Double,
    var y: Double,
    var width: Double,
    var height: Double,
    var clientWidth: Double
)