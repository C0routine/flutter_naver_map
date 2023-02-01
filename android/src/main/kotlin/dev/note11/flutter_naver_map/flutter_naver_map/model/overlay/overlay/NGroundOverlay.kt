package dev.note11.flutter_naver_map.flutter_naver_map.model.overlay.overlay

import com.naver.maps.geometry.LatLngBounds
import com.naver.maps.map.overlay.GroundOverlay
import com.naver.maps.map.overlay.Overlay
import dev.note11.flutter_naver_map.flutter_naver_map.converter.AddableOverlay
import dev.note11.flutter_naver_map.flutter_naver_map.converter.DefaultTypeConverter.asDouble
import dev.note11.flutter_naver_map.flutter_naver_map.converter.DefaultTypeConverter.asMap
import dev.note11.flutter_naver_map.flutter_naver_map.converter.MapTypeConverter.asLatLngBounds
import dev.note11.flutter_naver_map.flutter_naver_map.converter.MapTypeConverter.toMap
import dev.note11.flutter_naver_map.flutter_naver_map.model.enum.NOverlayType
import dev.note11.flutter_naver_map.flutter_naver_map.model.overlay.NOverlayImage
import dev.note11.flutter_naver_map.flutter_naver_map.model.overlay.NOverlayInfo
import dev.note11.flutter_naver_map.flutter_naver_map.util.CalcUtil

internal data class NGroundOverlay(
    override val info: NOverlayInfo,
    val bounds: LatLngBounds,
    val image: NOverlayImage?,
    val alpha: Double,
) : AddableOverlay<GroundOverlay> {

    override fun createMapOverlay(): GroundOverlay = GroundOverlay().also { g ->
        g.bounds = bounds
        g.alpha = alpha.toFloat()
        image!!.applyToOverlay(g::setImage)
    }

    override fun toMap(): Map<String, Any?> = mapOf(
        infoName to info.toMap(),
        boundsName to bounds.toMap(),
        imageName to null,
        alphaName to alpha,
    )

    companion object {
        fun fromMap(rawMap: Any): NGroundOverlay = rawMap.asMap().let {
            NGroundOverlay(
                info = NOverlayInfo.fromMap(it[infoName]!!),
                bounds = it[boundsName]!!.asLatLngBounds(),
                image = NOverlayImage.fromMap(it[imageName]!!),
                alpha = it[alphaName]!!.asDouble(),
            )
        }

        fun fromGroundOverlay(
            groundOverlay: Overlay,
            id: String,
        ): NGroundOverlay = (groundOverlay as GroundOverlay).run {
            NGroundOverlay(
                info = NOverlayInfo(NOverlayType.GROUND_OVERLAY, id),
                bounds = bounds,
                image = null, // todo
                alpha = CalcUtil.float32To64(alpha),
            )
        }

        /*
            --- Messaging Name Define ---
        */

        private const val infoName = "info"
        const val boundsName = "bounds"
        const val imageName = "image"
        const val alphaName = "alpha"
    }
}