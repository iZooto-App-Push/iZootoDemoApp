package com.k.deeplinkingtesting

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.google.mlkit.vision.face.Face

class FaceOverlayView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    private var faces: List<Face> = emptyList()
    var imageWidth = 0
    var imageHeight = 0
    private val paint = Paint().apply {
        color = Color.GREEN
        style = Paint.Style.STROKE
        strokeWidth = 6f
    }

    fun setFaces(faceList: List<Face>) {
        faces = faceList
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (faces.isEmpty() || imageWidth == 0 || imageHeight == 0) return

        val scaleX = width.toFloat() / imageWidth
        val scaleY = height.toFloat() / imageHeight

        for (face in faces) {
            val box = face.boundingBox

            val left = box.left * scaleX
            val top = box.top * scaleY
            val right = box.right * scaleX
            val bottom = box.bottom * scaleY

            canvas.drawRect(left, top, right, bottom, paint)
        }
    }
    fun setImageSourceInfo(width: Int, height: Int) {
        imageWidth = width
        imageHeight = height
    }
}