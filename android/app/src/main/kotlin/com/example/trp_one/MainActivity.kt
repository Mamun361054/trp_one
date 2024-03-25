package com.example.trp_one

import android.content.ContentResolver
import android.content.ContentUris
import android.database.Cursor
import android.graphics.Bitmap
import android.os.Build
import android.provider.MediaStore
import android.util.Size
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel
import java.io.ByteArrayOutputStream
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainActivity : FlutterActivity() {

    companion object {
        const val imageType = "image"

        const val allAlbumId = "__ALL__"
        const val allAlbumName = "All"

        val imageMetadataProjection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.TITLE,
            MediaStore.Images.Media.WIDTH,
            MediaStore.Images.Media.HEIGHT,
            MediaStore.Images.Media.SIZE,
            MediaStore.Images.Media.ORIENTATION,
            MediaStore.Images.Media.MIME_TYPE,
            MediaStore.Images.Media.DATE_ADDED,
            MediaStore.Images.Media.DATE_MODIFIED
        )

        val imageBriefMetadataProjection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.WIDTH,
            MediaStore.Images.Media.HEIGHT,
            MediaStore.Images.Media.ORIENTATION,
            MediaStore.Images.Media.DATE_ADDED,
            MediaStore.Images.Media.DATE_MODIFIED
        )
    }

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)
        val executor: ExecutorService = Executors.newSingleThreadExecutor()
        val channel = "photo_gallery"

        MethodChannel(
            flutterEngine.dartExecutor.binaryMessenger,
            channel
        ).setMethodCallHandler { call, result ->
            when (call.method) {
                "listAlbums" -> {
                    val mediumType = call.argument<String>("mediumType")
                    executor.submit {
                        result.success(
                            listAlbums(mediumType)
                        )
                    }
                }

                "listMedia" -> {
                    val albumId = call.argument<String>("albumId")
                    val mediumType = call.argument<String>("mediumType")
                    val newest = call.argument<Boolean>("newest")
                    val skip = call.argument<Int>("skip")
                    val take = call.argument<Int>("take")
                    val lightWeight = call.argument<Boolean>("lightWeight")
                    executor.submit {
                        result.success(
                            listMedia(mediumType, albumId!!, newest!!, skip, take, lightWeight)
                        )
                    }
                }

                "getMedium" -> {
                    val mediumId = call.argument<String>("mediumId")
                    val mediumType = call.argument<String>("mediumType")
                    executor.submit {
                        result.success(
                            getMedium(mediumId!!, mediumType)
                        )
                    }
                }

                "getThumbnail" -> {
                    val mediumId = call.argument<String>("mediumId")
                    val mediumType = call.argument<String>("mediumType")
                    val width = call.argument<Int>("width")
                    val height = call.argument<Int>("height")
                    val highQuality = call.argument<Boolean>("highQuality")
                    executor.submit {
                        result.success(
                            getThumbnail(mediumId!!, mediumType, width, height, highQuality)
                        )
                    }
                }

                "getAlbumThumbnail" -> {
                    val albumId = call.argument<String>("albumId")
                    val mediumType = call.argument<String>("mediumType")
                    val newest = call.argument<Boolean>("newest")
                    val width = call.argument<Int>("width")
                    val height = call.argument<Int>("height")
                    val highQuality = call.argument<Boolean>("highQuality")
                    executor.submit {
                        result.success(
                            getAlbumThumbnail(albumId!!, mediumType, newest!!, width, height, highQuality)
                        )
                    }
                }
                "getFile" -> {
                    val mediumId = call.argument<String>("mediumId")
                    val mediumType = call.argument<String>("mediumType")
                    val mimeType = call.argument<String>("mimeType")
                    executor.submit {
                        result.success(
                            getFile(mediumId!!, mediumType, mimeType)
                        )
                    }
                }
            }
        }
    }

    private fun getFile(mediumId: String, mediumType: String?, mimeType: String?): String? {
        return when (mediumType) {
            imageType -> {
                getImageFile(mediumId, mimeType = mimeType)
            }


            else -> {
                getImageFile(mediumId, mimeType = mimeType)
            }
        }
    }

    private fun getImageFile(mediumId: String, mimeType: String? = null): String? {
        return this.context.run {
            mimeType?.let {
                val type = this.contentResolver.getType(
                    ContentUris.withAppendedId(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        mediumId.toLong()
                    )
                )
            }

            val imageCursor = this.contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                arrayOf(MediaStore.Images.Media.DATA),
                "${MediaStore.Images.Media._ID} = ?",
                arrayOf(mediumId),
                null
            )

            imageCursor?.use { cursor ->
                if (cursor.moveToNext()) {
                    val dataColumn = cursor.getColumnIndex(MediaStore.Images.Media.DATA)
                    return@run cursor.getString(dataColumn)
                }
            }

            return@run null
        }
    }


    private fun getAlbumThumbnail(
        albumId: String,
        mediumType: String?,
        newest: Boolean,
        width: Int?,
        height: Int?,
        highQuality: Boolean?
    ): ByteArray? {
        return when (mediumType) {
            imageType -> {
                getImageAlbumThumbnail(albumId, newest, width, height, highQuality)
            }
            else -> {
                getImageAlbumThumbnail(albumId, newest, width, height, highQuality)
            }
        }
    }

    private fun getImageAlbumThumbnail(
        albumId: String,
        newest: Boolean,
        width: Int?,
        height: Int?,
        highQuality: Boolean?
    ): ByteArray? {
        return this.context.run {
            val projection = arrayOf(MediaStore.Images.Media._ID)

            val imageCursor = getImageCursor(albumId, newest, projection, null, 1)

            imageCursor?.use { cursor ->
                if (cursor.moveToFirst()) {
                    val idColumn = cursor.getColumnIndex(MediaStore.Images.Media._ID)
                    val id = cursor.getLong(idColumn)
                    return@run getImageThumbnail(id.toString(), width, height, highQuality)
                }
            }

            return@run null
        }
    }

    private fun getThumbnail(
        mediumId: String,
        mediumType: String?,
        width: Int?,
        height: Int?,
        highQuality: Boolean?
    ): ByteArray? {
        return when (mediumType) {
            imageType -> {
                getImageThumbnail(mediumId, width, height, highQuality)
            }

            else -> {
                getImageThumbnail(mediumId, width, height, highQuality)
            }
        }
    }

    private fun getImageThumbnail(mediumId: String, width: Int?, height: Int?, highQuality: Boolean?): ByteArray? {
        var byteArray: ByteArray? = null

        val bitmap: Bitmap? = this.context.run {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                try {
                    val widthSize = width ?: if (highQuality == true) 512 else 96
                    val heightSize = height ?: if (highQuality == true) 384 else 96
                    this.contentResolver.loadThumbnail(
                        ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, mediumId.toLong()),
                        Size(widthSize, heightSize),
                        null
                    )
                } catch (e: Exception) {
                    null
                }
            } else {
                val kind =
                    if (highQuality == true) MediaStore.Images.Thumbnails.MINI_KIND
                    else MediaStore.Images.Thumbnails.MICRO_KIND
                MediaStore.Images.Thumbnails.getThumbnail(
                    this.contentResolver, mediumId.toLong(),
                    kind, null
                )
            }
        }
        bitmap?.run {
            ByteArrayOutputStream().use { stream ->
                this.compress(Bitmap.CompressFormat.JPEG, 100, stream)
                byteArray = stream.toByteArray()
            }
        }

        return byteArray
    }

    private fun getMedium(mediumId: String, mediumType: String?): Map<String, Any?>? {
        return when (mediumType) {
            imageType -> {
                getImageMedia(mediumId)
            }

            else -> {
                getImageMedia(mediumId)
            }
        }
    }

    private fun getImageMedia(mediumId: String): Map<String, Any?>? {
        return this.context.run {
            val imageCursor = this.contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                imageMetadataProjection,
                "${MediaStore.Images.Media._ID} = ?",
                arrayOf(mediumId),
                null
            )

            imageCursor?.use { cursor ->
                if (cursor.moveToFirst()) {
                    return@run getImageMetadata(cursor)
                }
            }

            return@run null
        }
    }

    private fun getImageMetadata(cursor: Cursor): Map<String, Any?> {
        val idColumn = cursor.getColumnIndex(MediaStore.Images.Media._ID)
        val filenameColumn = cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME)
        val titleColumn = cursor.getColumnIndex(MediaStore.Images.Media.TITLE)
        val widthColumn = cursor.getColumnIndex(MediaStore.Images.Media.WIDTH)
        val heightColumn = cursor.getColumnIndex(MediaStore.Images.Media.HEIGHT)
        val sizeColumn = cursor.getColumnIndex(MediaStore.Images.Media.SIZE)
        val orientationColumn = cursor.getColumnIndex(MediaStore.Images.Media.ORIENTATION)
        val mimeColumn = cursor.getColumnIndex(MediaStore.Images.Media.MIME_TYPE)
        val dateAddedColumn = cursor.getColumnIndex(MediaStore.Images.Media.DATE_ADDED)
        val dateModifiedColumn = cursor.getColumnIndex(MediaStore.Images.Media.DATE_MODIFIED)

        val id = cursor.getLong(idColumn)
        val filename = cursor.getString(filenameColumn)
        val title = cursor.getString(titleColumn)
        val width = cursor.getLong(widthColumn)
        val height = cursor.getLong(heightColumn)
        val size = cursor.getLong(sizeColumn)
        val orientation = cursor.getLong(orientationColumn)
        val mimeType = cursor.getString(mimeColumn)
        var dateAdded: Long? = null
        if (cursor.getType(dateAddedColumn) == Cursor.FIELD_TYPE_INTEGER) {
            dateAdded = cursor.getLong(dateAddedColumn) * 1000
        }
        var dateModified: Long? = null
        if (cursor.getType(dateModifiedColumn) == Cursor.FIELD_TYPE_INTEGER) {
            dateModified = cursor.getLong(dateModifiedColumn) * 1000
        }

        return mapOf(
            "id" to id.toString(),
            "filename" to filename,
            "title" to title,
            "mediumType" to imageType,
            "width" to width,
            "height" to height,
            "size" to size,
            "orientation" to orientationDegree2Value(orientation),
            "mimeType" to mimeType,
            "creationDate" to dateAdded,
            "modifiedDate" to dateModified
        )
    }

    private fun listMedia(
        mediumType: String?,
        albumId: String,
        newest: Boolean,
        skip: Int?,
        take: Int?,
        lightWeight: Boolean? = false
    ): Map<String, Any?> {
        return when (mediumType) {
            imageType -> {
                listImages(albumId, newest, skip, take, lightWeight)
            }


            else -> {
                val images = listImages(albumId, newest, null, null, lightWeight)["items"] as List<Map<String, Any?>>
                val comparator = compareBy<Map<String, Any?>> { it["creationDate"] as Long }
                    .thenBy { it["modifiedDate"] as Long }
                var items = (images).sortedWith(comparator)
                if (newest) {
                    items = items.reversed()
                }
                if (skip != null || take != null) {
                    val start = skip ?: 0
                    val total = items.size
                    val end = if (take == null) total else Integer.min(start + take, total)
                    items = items.subList(start, end)
                }
                mapOf(
                    "start" to (skip ?: 0),
                    "items" to items
                )
            }
        }
    }

    private fun listImages(
        albumId: String,
        newest: Boolean,
        skip: Int?,
        take: Int?,
        lightWeight: Boolean? = false
    ): Map<String, Any?> {
        val media = mutableListOf<Map<String, Any?>>()

        this.context.run {
            val projection = if (lightWeight == true) imageBriefMetadataProjection else imageMetadataProjection
            val imageCursor = getImageCursor(albumId, newest, projection, skip, take)

            imageCursor?.use { cursor ->
                while (cursor.moveToNext()) {
                    val metadata = if (lightWeight == true) getImageBriefMetadata(cursor) else getImageMetadata(cursor)
                    media.add(metadata)
                }
            }
        }

        return mapOf(
            "start" to (skip ?: 0),
            "items" to media
        )
    }

    private fun getImageBriefMetadata(cursor: Cursor): Map<String, Any?> {
        val idColumn = cursor.getColumnIndex(MediaStore.Images.Media._ID)
        val widthColumn = cursor.getColumnIndex(MediaStore.Images.Media.WIDTH)
        val heightColumn = cursor.getColumnIndex(MediaStore.Images.Media.HEIGHT)
        val orientationColumn = cursor.getColumnIndex(MediaStore.Images.Media.ORIENTATION)
        val dateAddedColumn = cursor.getColumnIndex(MediaStore.Images.Media.DATE_ADDED)
        val dateModifiedColumn = cursor.getColumnIndex(MediaStore.Images.Media.DATE_MODIFIED)

        val id = cursor.getLong(idColumn)
        val width = cursor.getLong(widthColumn)
        val height = cursor.getLong(heightColumn)
        val orientation = cursor.getLong(orientationColumn)
        var dateAdded: Long? = null
        if (cursor.getType(dateAddedColumn) == Cursor.FIELD_TYPE_INTEGER) {
            dateAdded = cursor.getLong(dateAddedColumn) * 1000
        }
        var dateModified: Long? = null
        if (cursor.getType(dateModifiedColumn) == Cursor.FIELD_TYPE_INTEGER) {
            dateModified = cursor.getLong(dateModifiedColumn) * 1000
        }

        return mapOf(
            "id" to id.toString(),
            "mediumType" to imageType,
            "width" to width,
            "height" to height,
            "orientation" to orientationDegree2Value(orientation),
            "creationDate" to dateAdded,
            "modifiedDate" to dateModified
        )
    }

    private fun orientationDegree2Value(degree: Long): Int {
        return when (degree) {
            0L -> 1
            90L -> 8
            180L -> 3
            270L -> 6
            else -> 0
        }
    }

    private fun getImageCursor(
        albumId: String,
        newest: Boolean,
        projection: Array<String>,
        skip: Int?,
        take: Int?
    ): Cursor? {
        this.context.run {
            val isSelection = albumId != allAlbumId
            val selection = if (isSelection) "${MediaStore.Images.Media.BUCKET_ID} = ?" else null
            val selectionArgs = if (isSelection) arrayOf(albumId) else null
            val orderBy = if (newest) {
                "${MediaStore.Images.Media.DATE_ADDED} DESC, ${MediaStore.Images.Media.DATE_MODIFIED} DESC"
            } else {
                "${MediaStore.Images.Media.DATE_ADDED} ASC, ${MediaStore.Images.Media.DATE_MODIFIED} ASC"
            }

            val imageCursor: Cursor?

            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
                imageCursor = this.contentResolver.query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    projection,
                    android.os.Bundle().apply {
                        // Selection
                        putString(ContentResolver.QUERY_ARG_SQL_SELECTION, selection)
                        putStringArray(ContentResolver.QUERY_ARG_SQL_SELECTION_ARGS, selectionArgs)
                        // Sort
                        putString(ContentResolver.QUERY_ARG_SQL_SORT_ORDER, orderBy)
                        // Offset & Limit
                        if (skip != null) putInt(ContentResolver.QUERY_ARG_OFFSET, skip)
                        if (take != null) putInt(ContentResolver.QUERY_ARG_LIMIT, take)
                    },
                    null
                )
            } else {
                val offset = if (skip != null) "OFFSET $skip" else ""
                val limit = if (take != null) "LIMIT $take" else ""

                imageCursor = this.contentResolver.query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    projection,
                    selection,
                    selectionArgs,
                    "$orderBy $offset $limit"
                )
            }

            return imageCursor
        }
    }

    private fun listAlbums(mediumType: String?): List<Map<String, Any?>> {
        return when (mediumType) {
            imageType -> {
                listImageAlbums().values.toList()
            }
            else -> {
                listAllAlbums().values.toList()
            }
        }
    }

    private fun listAllAlbums(): Map<String, Map<String, Any?>> {
        val imageMap = this.listImageAlbums()
        val albumMap = (imageMap.keys ).associateWith {
            mapOf(
                "id" to it,
                "name" to imageMap[it]?.get("name"),
                "count" to (imageMap[it]?.get("count") ?: 0) as Int,
            )
        }
        return albumMap
    }

    private fun listImageAlbums(): Map<String, Map<String, Any>> {
        this.context.run {
            var total = 0
            val albumHashMap = hashMapOf<String, HashMap<String, Any>>()

            val imageProjection = arrayOf(
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Images.Media.BUCKET_ID
            )

            val imageCursor = this.contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                imageProjection,
                null,
                null,
                null
            )

            imageCursor?.use { cursor ->
                val bucketColumn = cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
                val bucketColumnId = cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_ID)

                while (cursor.moveToNext()) {
                    val bucketId = cursor.getString(bucketColumnId)
                    val album = albumHashMap[bucketId]
                    if (album == null) {
                        val folderName = cursor.getString(bucketColumn)
                        albumHashMap[bucketId] = hashMapOf(
                            "id" to bucketId,
                            "name" to folderName,
                            "count" to 1
                        )
                    } else {
                        val count = album["count"] as Int
                        album["count"] = count + 1
                    }
                    total++
                }
            }

            val albumLinkedMap = linkedMapOf<String, Map<String, Any>>()
            albumLinkedMap[allAlbumId] = hashMapOf(
                "id" to allAlbumId,
                "name" to allAlbumName,
                "count" to total
            )
            albumLinkedMap.putAll(albumHashMap)
            return albumLinkedMap
        }
    }

}
