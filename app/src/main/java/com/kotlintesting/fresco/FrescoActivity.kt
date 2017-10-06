package com.kotlintesting.fresco

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.PointF
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v4.content.ContextCompat
import android.support.v4.graphics.drawable.DrawableCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import com.facebook.common.internal.Preconditions
import com.facebook.drawee.drawable.ProgressBarDrawable
import com.facebook.drawee.drawable.ScalingUtils.ScaleType
import com.facebook.drawee.generic.RoundingParams
import com.facebook.drawee.view.SimpleDraweeView
import com.kotlintesting.R
import com.kotlintesting.fresco.libs.ImageUriProvider
import com.kotlintesting.fresco.libs.SimpleScaleTypeAdapter
import kotlinx.android.synthetic.main.activity_fresco.*
import com.facebook.drawee.backends.pipeline.Fresco




class FrescoActivity : AppCompatActivity() {

    private val BITMAP_ONLY_SCALETYPES = arrayOf(
            ScaleType.CENTER_CROP,
            ScaleType.FOCUS_CROP,
            ScaleType.FIT_XY)

    private var mPreviousScaleType = ScaleType.CENTER

    private var mWindowBackgroundColor: Int = 0
    private var mColorPrimary: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fresco)

        initColors()


//        val uri = Uri.parse("http://www.bigmapblog.com/maps/top40us/XLVHysyaLODYwjoe.jpg")
        val uri = Uri.parse("http://www.3d-wallpapers.info/wp-content/uploads/3d-nature-wallpapers-hd-download.jpg")
        sdvImage.setImageURI(uri, this)
        sdvImage1.setImageURI(uri, this)

        val imageUriProvider: ImageUriProvider = ImageUriProvider.getInstance(this)

        drawee_round.setImageURI(imageUriProvider.createSampleUri(ImageUriProvider.ImageSize.L), this)
        drawee_radius.setImageURI(imageUriProvider.createSampleUri(ImageUriProvider.ImageSize.L), this)
        drawee_some.setImageURI(imageUriProvider.createSampleUri(ImageUriProvider.ImageSize.L), this)
        drawee_some_rtl.setImageURI(imageUriProvider.createSampleUri(ImageUriProvider.ImageSize.L), this)
        drawee_fancy.setImageURI(imageUriProvider.createSampleUri(ImageUriProvider.ImageSize.L), this)

        val scaleTypeAdapter = SimpleScaleTypeAdapter.createForAllScaleTypes()
        scaleType.adapter = scaleTypeAdapter

        scaleType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val spinnerEntry = scaleTypeAdapter.getItem(position) as SimpleScaleTypeAdapter.Entry
                val scaleType = spinnerEntry.scaleType

                changeDraweeViewScaleType(drawee_round, scaleType, spinnerEntry.focusPoint)
                changeDraweeViewScaleType(drawee_radius, scaleType, spinnerEntry.focusPoint)
                changeDraweeViewScaleType(drawee_some, scaleType, spinnerEntry.focusPoint)
                changeDraweeViewScaleType(drawee_some_rtl, scaleType, spinnerEntry.focusPoint)
                changeDraweeViewScaleType(drawee_fancy, scaleType, spinnerEntry.focusPoint)

                if (BITMAP_ONLY_SCALETYPES.contains(scaleType) && !BITMAP_ONLY_SCALETYPES.contains(mPreviousScaleType)) {
                    Toast.makeText(
                            this@FrescoActivity,
                            R.string.drawee_rounded_corners_bitmap_only_toast,
                            Toast.LENGTH_SHORT).show()
                } else if (!BITMAP_ONLY_SCALETYPES.contains(scaleType) && BITMAP_ONLY_SCALETYPES.contains(mPreviousScaleType)) {


                    Toast.makeText(
                            this@FrescoActivity,
                            R.string.drawee_rounded_corners_overlay_color_toast,
                            Toast.LENGTH_SHORT).show()
                }
                mPreviousScaleType = scaleType
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }


        borders.setOnCheckedChangeListener { buttonView, isChecked ->
            setShowBorder(drawee_round, isChecked)
            setShowBorder(drawee_radius, isChecked)
            setShowBorder(drawee_some, isChecked)
            setShowBorder(drawee_some_rtl, isChecked)
            setShowBorder(drawee_fancy, isChecked)
        }

        val res = resources
        val fancyRoundingParams = RoundingParams.fromCornersRadii(
                res.getDimensionPixelSize(R.dimen.drawee_rounded_corners_fancy_top_left).toFloat(),
                res.getDimensionPixelSize(R.dimen.drawee_rounded_corners_fancy_top_right).toFloat(),
                res.getDimensionPixelSize(R.dimen.drawee_rounded_corners_fancy_bottom_right).toFloat(),
                res.getDimensionPixelSize(R.dimen.drawee_rounded_corners_fancy_bottom_left).toFloat())
        drawee_fancy.hierarchy.roundingParams = fancyRoundingParams


        //image loading with progress bar
        val imageUriProviderProgress: ImageUriProvider = ImageUriProvider.getInstance(this)
        val uriSuccess: Uri = imageUriProviderProgress.createSampleUri(
                ImageUriProvider.ImageSize.XL,
                ImageUriProvider.UriModification.CACHE_BREAKER)
        val uriFailure: Uri = imageUriProviderProgress.createNonExistingUri()

        //noinspection deprecation
        val failureDrawable: Drawable = ContextCompat.getDrawable(this, R.drawable.ic_error_black_96dp)
        DrawableCompat.setTint(failureDrawable, Color.RED);

        val progressBarDrawable = ProgressBarDrawable()
        progressBarDrawable.color = ContextCompat.getColor(this, R.color.colorAccent)
        progressBarDrawable.backgroundColor = ContextCompat.getColor(this, R.color.colorPrimary)
        progressBarDrawable.radius = resources.getDimensionPixelSize(R.dimen.drawee_hierarchy_progress_radius)

        drawee.hierarchy.setProgressBarImage(progressBarDrawable)
        drawee.hierarchy.setFailureImage(failureDrawable, ScaleType.CENTER_INSIDE)

        load_success.setOnClickListener(View.OnClickListener { setUri(drawee, uriSuccess, retry_enabled.isChecked) })

        load_fail.setOnClickListener(View.OnClickListener { setUri(drawee, uriFailure, retry_enabled.isChecked) })

        clear.setOnClickListener(View.OnClickListener {
            drawee.controller = null
            Fresco.getImagePipeline().evictFromCache(uriSuccess)
        })


    }


    private fun setUri(draweeView: SimpleDraweeView, uri: Uri, retryEnabled: Boolean) {
        draweeView.controller = Fresco.newDraweeControllerBuilder()
                .setOldController(draweeView.controller)
                .setTapToRetryEnabled(retryEnabled)
                .setUri(uri)
                .build()
    }


    private fun changeDraweeViewScaleType(
            draweeView: SimpleDraweeView,
            scaleType: ScaleType,
            @Nullable focusPoint: PointF?) {
        val hierarchy = draweeView.hierarchy
        hierarchy.actualImageScaleType = scaleType
        hierarchy.setActualImageFocusPoint(focusPoint ?: PointF(0.5f, 0.5f))

        val roundingParams = Preconditions.checkNotNull(hierarchy.roundingParams)
        when {
            BITMAP_ONLY_SCALETYPES.contains(scaleType) -> roundingParams!!.roundingMethod = RoundingParams.RoundingMethod.BITMAP_ONLY
            else -> roundingParams!!.overlayColor = mWindowBackgroundColor
        }
        hierarchy.roundingParams = roundingParams
    }

    @SuppressLint("ResourceType")
    private fun initColors() {
        val attrs = theme.obtainStyledAttributes(R.style.AppTheme, intArrayOf(R.attr.colorPrimary, android.R.attr.windowBackground))
        try {
            mColorPrimary = attrs.getColor(0, Color.BLACK)
            mWindowBackgroundColor = attrs.getColor(1, Color.BLUE)
        } finally {
            attrs.recycle()
        }
    }

    private fun setShowBorder(draweeView: SimpleDraweeView, show: Boolean) {
        val roundingParams = Preconditions.checkNotNull(draweeView.hierarchy.roundingParams!!)
        if (show) {
            roundingParams.setBorder(
                    mColorPrimary,
                    resources.getDimensionPixelSize(R.dimen.drawee_rounded_corners_border_width).toFloat())
        } else {
            roundingParams.setBorder(Color.TRANSPARENT, 0f)
        }
        draweeView.hierarchy.roundingParams = roundingParams
    }

}
