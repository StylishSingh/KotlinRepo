/*
 * This file provided by Facebook is for non-commercial testing and evaluation
 * purposes only.  Facebook reserves all rights not expressly granted.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
 * FACEBOOK BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN
 * ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.kotlintesting.fresco.libs

import android.graphics.Matrix
import android.graphics.Rect
import com.facebook.drawee.drawable.ScalingUtils

/**
 * Custom scale type examples.
 */
object CustomScaleTypes {

    val FIT_X: ScalingUtils.ScaleType = ScaleTypeFitX()
    val FIT_Y: ScalingUtils.ScaleType = ScaleTypeFitY()

    private class ScaleTypeFitX : ScalingUtils.AbstractScaleType() {

        override fun getTransformImpl(
                outTransform: Matrix,
                parentRect: Rect,
                childWidth: Int,
                childHeight: Int,
                focusX: Float,
                focusY: Float,
                scaleX: Float,
                scaleY: Float) {
            val scale: Float
            val dx: Float
            val dy: Float
            scale = scaleX
            dx = parentRect.left.toFloat()
            dy = parentRect.top + (parentRect.height() - childHeight * scale) * 0.5f
            outTransform.setScale(scale, scale)
            outTransform.postTranslate((dx + 0.5f).toInt().toFloat(), (dy + 0.5f).toInt().toFloat())
        }
    }

    private class ScaleTypeFitY : ScalingUtils.AbstractScaleType() {

        override fun getTransformImpl(
                outTransform: Matrix,
                parentRect: Rect,
                childWidth: Int,
                childHeight: Int,
                focusX: Float,
                focusY: Float,
                scaleX: Float,
                scaleY: Float) {
            val scale: Float
            val dx: Float
            val dy: Float
            scale = scaleY
            dx = parentRect.left + (parentRect.width() - childWidth * scale) * 0.5f
            dy = parentRect.top.toFloat()
            outTransform.setScale(scale, scale)
            outTransform.postTranslate((dx + 0.5f).toInt().toFloat(), (dy + 0.5f).toInt().toFloat())
        }
    }
}
