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

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.preference.PreferenceManager
import android.text.TextUtils
import com.facebook.common.internal.Preconditions
import java.util.*

/**
 * Provider for sample URIs that are used by the samples in the showcase app
 */
class ImageUriProvider private constructor(context: Context) {

    private val mSharedPreferences: SharedPreferences
    private val mRandom = Random()

    var uriOverride: String?
        get() {
            val uriOverride = mSharedPreferences.getString(PREF_KEY_URI_OVERRIDE, null)
            return if (!TextUtils.isEmpty(uriOverride))
                uriOverride
            else
                null
        }
        set(uri) = if (uri == null || uri.length == 0) {
            mSharedPreferences.edit()
                    .remove(PREF_KEY_URI_OVERRIDE)
                    .apply()
        } else {
            Preconditions.checkArgument(Uri.parse(uri).isAbsolute, "URI must be absolute")

            mSharedPreferences.edit()
                    .putString(PREF_KEY_URI_OVERRIDE, uri)
                    .apply()
        }

    private val isShouldBreakCacheByDefault: Boolean
        get() = mSharedPreferences.getBoolean(PREF_KEY_CACHE_BREAKING_BY_DEFAULT, false)

    /**
     * The orientation of a sample image
     */
    enum class Orientation {
        /**
         * height > width
         */
        PORTRAIT,

        /**
         * width > height
         */
        LANDSCAPE
    }

    /**
     * Indicates whether to perform some action on the URI before returning
     */
    enum class UriModification {

        /**
         * Do not perform any modification
         */
        NONE,

        /**
         * Add a unique parameter to the URI to prevent it to be served from any cache
         */
        CACHE_BREAKER
    }

    enum class ImageSize private constructor(val sizeSuffix: String) {
        /**
         * Within ~250x250 px bounds
         */
        XS("xs"),

        /**
         * Within ~450x450 px bounds
         */
        S("s"),

        /**
         * Within ~800x800 px bounds
         */
        M("m"),

        /**
         * Within ~1400x1400 px bounds
         */
        L("l"),

        /**
         * Within ~2500x2500 px bounds
         */
        XL("xl"),

        /**
         * Within ~4096x4096 px bounds
         */
        XXL("xxl")
    }

    init {
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    }

    /**
     * Creates an URI of an image that will result in a 404 (not found) HTTP error
     */
    fun createNonExistingUri(): Uri {
        return Uri.parse(NON_EXISTING_URI)
    }

    fun createSampleUri(imageSize: ImageSize, uriModification: UriModification): Uri {
        return createSampleUri(imageSize, null, uriModification)
    }

    @JvmOverloads
    fun createSampleUri(
            imageSize: ImageSize,
            orientation: Orientation? = null,
            urlModification: UriModification = UriModification.NONE): Uri {
        val baseUri: String
        if (orientation == Orientation.PORTRAIT) {
            baseUri = chooseRandom(SAMPLE_URIS_PORTRAIT)
        } else if (orientation == Orientation.LANDSCAPE) {
            baseUri = chooseRandom(SAMPLE_URIS_LANDSCAPE)
        } else {
            baseUri = chooseRandom(SAMPLE_URIS_LANDSCAPE, SAMPLE_URIS_PORTRAIT)
        }

        val fullUri = String.format( Locale.ENGLISH, baseUri, imageSize.sizeSuffix)
        return applyOverrideSettings(fullUri, urlModification)
    }

    @JvmOverloads
    fun createPngUri(orientation: Orientation? = null, urlModification: UriModification = UriModification.NONE): Uri {
        val baseUri: String
        if (orientation == Orientation.PORTRAIT) {
            baseUri = chooseRandom(SAMPLE_URIS_PORTRAIT_PNG)
        } else if (orientation == Orientation.LANDSCAPE) {
            baseUri = chooseRandom(SAMPLE_URIS_LANDSCAPE_PNG)
        } else {
            baseUri = chooseRandom(SAMPLE_URIS_LANDSCAPE_PNG, SAMPLE_URIS_PORTRAIT_PNG)
        }
        return applyOverrideSettings(baseUri, urlModification)
    }

    fun createWebpStaticUri(): Uri {
        return applyOverrideSettings(SAMPLE_URI_WEBP_STATIC, UriModification.NONE)
    }

    fun createWebpTranslucentUri(): Uri {
        return applyOverrideSettings(SAMPLE_URI_WEBP_TRANSLUCENT, UriModification.NONE)
    }

    fun createWebpAnimatedUri(): Uri {
        return applyOverrideSettings(SAMPLE_URI_WEBP_ANIMATED, UriModification.NONE)
    }

    private fun applyOverrideSettings(
            uriString: String,
            urlModification: UriModification): Uri {
        var uriString = uriString
        var urlModification = urlModification
        if (isShouldBreakCacheByDefault) {
            urlModification = UriModification.CACHE_BREAKER
        }

        val overrideUriString = uriOverride
        if (overrideUriString != null) {
            uriString = overrideUriString
        }

        var result = Uri.parse(uriString)
        if (UriModification.CACHE_BREAKER == urlModification) {
            result = result.buildUpon()
                    .appendQueryParameter("cache_breaker", UUID.randomUUID().toString())
                    .build()
        }
        return result
    }

    /**
     * @return a random element from a given set of arrays (uniform distribution)
     */
    private fun <T> chooseRandom(vararg arrays: Array<T>): T {
        var l = 0
        for (array in arrays) {
            l += array.size
        }
        var i = mRandom.nextInt(l)
        for (array in arrays) {
            when {
                i < array.size -> return array[i]
                else -> i -= array.size
            }
        }
        throw IllegalStateException("unreachable code")
    }

    companion object {

        private val PREF_KEY_CACHE_BREAKING_BY_DEFAULT = "uri_cache_breaking"
        private val PREF_KEY_URI_OVERRIDE = "uri_override"

        private val SAMPLE_URIS_LANDSCAPE = arrayOf("http://frescolib.org/static/sample-images/animal_a_%s.jpg", "http://frescolib.org/static/sample-images/animal_b_%s.jpg", "http://frescolib.org/static/sample-images/animal_c_%s.jpg", "http://frescolib.org/static/sample-images/animal_e_%s.jpg", "http://frescolib.org/static/sample-images/animal_f_%s.jpg", "http://frescolib.org/static/sample-images/animal_g_%s.jpg")

        private val SAMPLE_URIS_PORTRAIT = arrayOf("http://frescolib.org/static/sample-images/animal_d_%s.jpg")

        private val SAMPLE_URIS_LANDSCAPE_PNG = arrayOf("http://frescolib.org/static/sample-images/animal_a.png", "http://frescolib.org/static/sample-images/animal_b.png", "http://frescolib.org/static/sample-images/animal_c.png", "http://frescolib.org/static/sample-images/animal_e.png", "http://frescolib.org/static/sample-images/animal_f.png", "http://frescolib.org/static/sample-images/animal_g.png")

        private val SAMPLE_URIS_PORTRAIT_PNG = arrayOf("http://frescolib.org/static/sample-images/animal_d.png")

        private val NON_EXISTING_URI = "http://frescolib.org/static/sample-images/does_not_exist.jpg"

        private val SAMPLE_URI_WEBP_STATIC = "http://www.gstatic.com/webp/gallery/2.webp"

        private val SAMPLE_URI_WEBP_TRANSLUCENT = "https://www.gstatic.com/webp/gallery3/5_webp_ll.webp"

        private val SAMPLE_URI_WEBP_ANIMATED = "https://www.gstatic.com/webp/animated/1.webp"

        private lateinit var sInstance: ImageUriProvider

        fun getInstance(context: Context): ImageUriProvider {
            synchronized(ImageUriProvider::class.java) {
//                if (sInstance == null) {
                    sInstance = ImageUriProvider(context.applicationContext)
//                }
                return sInstance
            }
        }
    }
}
