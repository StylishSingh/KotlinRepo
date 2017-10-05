package com.kotlintesting.fresco.libs

import android.graphics.PointF
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.facebook.drawee.drawable.ScalingUtils
import java.util.*

class SimpleScaleTypeAdapter private constructor(private val mEntries: List<Entry>) : BaseAdapter() {

    private constructor(entries: Array<Entry>) : this(Arrays.asList<Entry>(*entries)) {}

    override fun getCount(): Int {
        return mEntries.size
    }

    override fun getItem(position: Int): Any {
        return mEntries[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater = LayoutInflater.from(parent.context)

        val view = convertView ?: layoutInflater.inflate(android.R.layout.simple_spinner_dropdown_item, parent, false)

        val textView = view.findViewById<View>(android.R.id.text1) as TextView
        textView.text = mEntries[position].description

        return view
    }

    class Entry internal constructor(
            val scaleType: ScalingUtils.ScaleType,
            val description: String,
            val focusPoint: PointF?)


    companion object {

        private val BUILT_IN_SPINNER_ENTRIES = arrayOf(Entry(ScalingUtils.ScaleType.CENTER, "center", null),
                Entry(ScalingUtils.ScaleType.CENTER_CROP, "center_crop", null),
                Entry(ScalingUtils.ScaleType.CENTER_INSIDE, "center_inside", null),
                Entry(ScalingUtils.ScaleType.FIT_CENTER, "fit_center", null),
                Entry(ScalingUtils.ScaleType.FIT_START, "fit_start", null),
                Entry(ScalingUtils.ScaleType.FIT_END, "fit_end", null),
                Entry(ScalingUtils.ScaleType.FIT_XY, "fit_xy", null),
                Entry(ScalingUtils.ScaleType.FOCUS_CROP, "focus_crop (0, 0)", PointF(0f, 0f)),
                Entry(ScalingUtils.ScaleType.FOCUS_CROP, "focus_crop (1, 0.5)", PointF(1f, 0.5f)))

        private val CUSTOM_TYPES = arrayOf(Entry(CustomScaleTypes.FIT_X, "custom: fit_x", null), Entry(CustomScaleTypes.FIT_Y, "custom: fit_y", null))

        fun createForAllScaleTypes(): SimpleScaleTypeAdapter {
            val entries = ArrayList<Entry>(BUILT_IN_SPINNER_ENTRIES.size + CUSTOM_TYPES.size)
            Collections.addAll(entries, *BUILT_IN_SPINNER_ENTRIES)
            Collections.addAll(entries, *CUSTOM_TYPES)
            return SimpleScaleTypeAdapter(entries)
        }
    }
}
