package com.kotlintesting

import android.app.Application
import com.facebook.cache.disk.DiskCacheConfig
import com.facebook.common.util.ByteConstants
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.imagepipeline.cache.MemoryCacheParams
import com.facebook.imagepipeline.core.ImagePipelineConfig

/**
 * @author Amanpal Singh.
 */
class ApplicationClass : Application() {

    companion object {
        private val MAX_HEAP_SIZE = Runtime.getRuntime().maxMemory().toInt()
        private val MAX_MEMORY_CACHE_SIZE = MAX_HEAP_SIZE / 4
        private const val MAX_DISK_CACHE_SIZE = 40L * ByteConstants.MB
    }

    override fun onCreate() {
        super.onCreate()
        val pipelineConfig = ImagePipelineConfig.newBuilder(this)
                .setBitmapMemoryCacheParamsSupplier {
                    MemoryCacheParams(
                            ApplicationClass.MAX_MEMORY_CACHE_SIZE,
                            Int.MAX_VALUE,
                            ApplicationClass.MAX_MEMORY_CACHE_SIZE,
                            Int.MAX_VALUE,
                            Int.MAX_VALUE)
                }
                .setMainDiskCacheConfig(DiskCacheConfig.newBuilder(this)
                        .setBaseDirectoryPath(cacheDir)
                        .setBaseDirectoryName("stuff")
                        .setMaxCacheSize(ApplicationClass.MAX_DISK_CACHE_SIZE)
                        .build())
                .setDownsampleEnabled(true)
                .build()
        Fresco.initialize(this,pipelineConfig)
    }

}