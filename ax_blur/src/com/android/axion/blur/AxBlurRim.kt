/*
 * Copyright (C) 2026 AxionOS
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.axion.blur

import android.graphics.Color
import androidx.compose.runtime.Immutable

/**
 * An optional inner highlight, independent of blur radius and tint customization.
 *
 * Dimensions are in dp on both View and Compose surfaces. Alpha values and [strength] are in 0..1;
 * the color's own alpha also multiplies the result. [glowDepthDp] is the total inward glow depth,
 * independent of [edgeWidthDp]. [glowAlpha] scales the glow relative to the edge highlight.
 *
 * [cornerWidthScale] and [cornerAlphaScale] apply at the diagonal of a rounded corner and blend
 * smoothly back to full width/alpha at its tangents. A scale of 1 disables that taper; 0 removes
 * the highlight at the diagonal. [cornerTaperPower] controls how narrowly the taper is confined to
 * the diagonal (larger values produce a narrower tapered region).
 */
@Immutable
data class AxBlurRim
@JvmOverloads
constructor(
    val enabled: Boolean = true,
    val color: Int = Color.WHITE,
    val strength: Float = 1f,
    val topAlpha: Float = 0.14f,
    val bottomAlpha: Float = 0.5f,
    val sideAlpha: Float = 0f,
    val edgeWidthDp: Float = 0.8f,
    val glowDepthDp: Float = 12.5f,
    val glowAlpha: Float = 0.3f,
    val cornerWidthScale: Float = 1f,
    val cornerAlphaScale: Float = 0.8f,
    val cornerTaperPower: Float = 6.8f,
    val style: Int = STYLE_CLASSIC,
    val lightAngleDeg: Float = 131f,
    val lightIntensity: Float = 1.8f,
    val oppositeLightIntensity: Float = 1.2f,
    val lightAngleRange: Float = 0.7f,
    val glassEdgeWidthDp: Float = 8.0f,
    val glassEdgePower: Float = 3.0f,
) {
    init {
        for (value in
            floatArrayOf(
                strength,
                topAlpha,
                bottomAlpha,
                sideAlpha,
                glowAlpha,
                cornerWidthScale,
                cornerAlphaScale,
            )) {
            require(value.isFinite() && value in 0f..1f) { "Rim alpha and scale must be in 0..1" }
        }
        require(edgeWidthDp.isFinite() && edgeWidthDp >= 0f) {
            "Rim edge width must be nonnegative"
        }
        require(glowDepthDp.isFinite() && glowDepthDp >= 0f) {
            "Rim glow depth must be nonnegative"
        }
        require(cornerTaperPower.isFinite() && cornerTaperPower > 0f) {
            "Rim corner taper power must be positive"
        }
        require(style == STYLE_CLASSIC || style == STYLE_DIRECTIONAL_GLASS) {
            "Unsupported rim style: $style"
        }
        require(lightAngleDeg.isFinite()) { "Rim light angle must be finite" }
        require(lightIntensity.isFinite() && lightIntensity >= 0f) {
            "Rim light intensity must be nonnegative"
        }
        require(oppositeLightIntensity.isFinite() && oppositeLightIntensity >= 0f) {
            "Rim opposite light intensity must be nonnegative"
        }
        require(lightAngleRange.isFinite() && lightAngleRange in 0.05f..1f) {
            "Rim light angle range must be in 0.05..1"
        }
        require(glassEdgeWidthDp.isFinite() && glassEdgeWidthDp > 0f) {
            "Rim glass edge width must be positive"
        }
        require(glassEdgePower.isFinite() && glassEdgePower > 0f) {
            "Rim glass edge power must be positive"
        }
    }

    companion object {
        const val STYLE_CLASSIC = 0
        const val STYLE_DIRECTIONAL_GLASS = 1

        /** Existing surfaces keep their current appearance until explicitly enabled. */
        @JvmField val None = AxBlurRim(enabled = false)

        /** Starting preset based on the subtle top rim and brighter bottom notification rim. */
        @JvmField val ColorOS = AxBlurRim()

        /** HyperOS-inspired 3D directional specular frosted glass rim shader preset. */
        @JvmField val DirectionalGlass = AxBlurRim(style = STYLE_DIRECTIONAL_GLASS)
    }
}
