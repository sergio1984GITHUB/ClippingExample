package ipcasergio.am2.clippingexample

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.os.Build
import android.provider.DocumentsContract
import android.util.AttributeSet
import android.view.View


@Suppress("DEPRECATION")
class ClippedView  @JvmOverloads constructor(

        context: Context,
        attrs:AttributeSet? = null,
        defStyleAttr: Int = 0
): View(context, attrs, defStyleAttr){


    private val paint = Paint().apply{
        isAntiAlias = true
        strokeWidth = resources.getDimension(R.dimen.strokeWidth)
        textSize = resources.getDimension(R.dimen.textSize)


}

    private val path = Path()

    private val clipRectRight = resources.getDimension(R.dimen.clipRectRight)
    private val clipRectBottom = resources.getDimension(R.dimen.clipRectBottom)
    private val clipRectTop = resources.getDimension(R.dimen.clipRectTop)
    private val clipRectLeft = resources.getDimension(R.dimen.clipRectLeft)


    private val rectInset = resources.getDimension(R.dimen.rectInset)
    private val smallRectOffset = resources.getDimension(R.dimen.smallRectOffset)

    private val circleRadius = resources.getDimension(R.dimen.circleRadius)


    private val textOffset = resources.getDimension(R.dimen.textOffset)
    private val textSize = resources.getDimension(R.dimen.textSize)

    private val columnOne = rectInset
    private val columnTwo = columnOne + rectInset + clipRectRight


    private val rowOne = rectInset
    private val rowTwo = rowOne + rectInset + clipRectBottom
    private val rowThree = rowTwo + rectInset + clipRectBottom
    private val rowFour = rowThree + rectInset + clipRectBottom
    private val textRow = rowFour + (1.5F * clipRectBottom)

    private val rejectRow = rowFour + rectInset + 2*clipRectBottom

    private var rectF = RectF(
        rectInset,
        rectInset,
        clipRectRight - rectInset,
        clipRectBottom - rectInset
    )



    private fun draClipperRectangle(canvas: Canvas){

        canvas.clipRect(
            clipRectLeft,clipRectTop,
            clipRectRight,clipRectBottom

        )

        canvas.drawColor(Color.WHITE)

        paint.color = Color.RED
        canvas.drawLine(
            clipRectLeft,clipRectTop,
            clipRectRight,clipRectBottom,paint

        )
        paint.color = Color.GREEN
        canvas.drawCircle(
            circleRadius,clipRectBottom - circleRadius,
            circleRadius,paint

        )
        paint.color = Color.BLUE
        paint.textSize = textSize
        paint.textAlign = Paint.Align.RIGHT
        canvas.drawText(
            context.getString(R.string.clipping) ,
                    clipRectRight, textOffset,paint
        )

    }





    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        drawBackAndUncappedRectangle(canvas)
        drawDifferenceClippingExample(canvas)
        drawCircularClippingExample(canvas)
        drawIntersectionClippingExample(canvas)
        drawCombinedClippingExample(canvas)
        drawRoundedRectangleClippingExample(canvas)
        drawOutsideClippingExample(canvas)
        drawTranslatedTextExample(canvas)
        drawSkewedTextExample(canvas)
        drawQuickRejectExample(canvas)


    }

    private fun drawBackAndUncappedRectangle(canvas: Canvas){

        canvas.drawColor(Color.GRAY)
        canvas.save()
        canvas.translate(columnOne, rowOne)
        draClipperRectangle(canvas)
        canvas.restore()

    }

    private fun drawDifferenceClippingExample(canvas: Canvas){
        canvas.save()
        canvas.translate(columnTwo,rowOne)

        canvas.clipRect(
            2 * rectInset, 2 * rectInset,
            clipRectRight- 2 * rectInset,
            clipRectRight - 2 * rectInset




        )
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O){
            canvas.clipRect(
                4 *rectInset, 4 * rectInset,
                clipRectRight - 4 - rectInset,
                clipRectBottom - 4 * rectInset,
                Region.Op.DIFFERENCE
            )
        } else{
            canvas.clipOutRect(
                4 * rectInset,4 * rectInset,
                clipRectRight - 4 * rectInset,
                clipRectBottom - 4 * rectInset

            )
        }
        draClipperRectangle(canvas)
        canvas.restore()
    }


    private fun drawCircularClippingExample(canvas: Canvas){
        canvas.save()
        canvas.translate(columnOne,rowTwo)

        path.rewind()
        path.addCircle(
            circleRadius,clipRectBottom - circleRadius,
            circleRadius,Path.Direction.CCW
        )
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O){
            canvas.clipPath( path, Region.Op.DIFFERENCE)
            }else{
                canvas.clipOutPath(path)
        }
        draClipperRectangle(canvas)
        canvas.restore()
    }
    private fun drawIntersectionClippingExample(canvas: Canvas){
        canvas.save()
        canvas.translate(columnTwo,rowTwo)
        canvas.clipRect(
            clipRectLeft,clipRectTop,
            clipRectRight - smallRectOffset,
            clipRectBottom - smallRectOffset
        )

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            canvas.clipRect(
                clipRectLeft + smallRectOffset,
                clipRectTop + smallRectOffset,
                clipRectRight,clipRectBottom,
                Region.Op.INTERSECT
            )
        } else {
            canvas.clipRect(
                clipRectLeft + smallRectOffset,
                clipRectTop + smallRectOffset,
                clipRectRight,clipRectBottom
            )
        }
        draClipperRectangle(canvas)
        canvas.restore()
    }


    private fun drawCombinedClippingExample(canvas: Canvas){
        canvas.save()
        canvas.translate(columnOne, rowThree)
        path.rewind()

        path.addCircle(
            clipRectLeft + rectInset + circleRadius,
            clipRectTop + circleRadius + rectInset,
            circleRadius, Path.Direction.CCW
        )
        path.addRect(
            clipRectRight / 2 - clipRectRight / 2 - circleRadius,
            clipRectTop + circleRadius + rectInset,
            clipRectRight / 2 + circleRadius,
            clipRectBottom - rectInset,Path.Direction.CCW
        )
        canvas.clipPath(path)
        draClipperRectangle(canvas)
        canvas.restore()
    }
    private fun drawRoundedRectangleClippingExample(canvas: Canvas){
        canvas.save()
        canvas.translate(columnTwo,rowThree)
        path.rewind()
        path.addRoundRect(
            rectF,clipRectRight / 4,
            clipRectRight / 4, Path.Direction.CCW
        )
        canvas.clipPath(path)
        draClipperRectangle(canvas)
        canvas.restore()


    }
    private fun drawOutsideClippingExample(canvas: Canvas){
        canvas.save()
        canvas.translate(columnOne,rowFour)
        canvas.clipRect(2 * rectInset,2 * rectInset,
            clipRectRight - 2 * rectInset,
            clipRectBottom - 2 * rectInset)
        draClipperRectangle(canvas)
        canvas.restore()
    }
    private fun drawTranslatedTextExample(canvas: Canvas){
        canvas.save()
        paint.color = Color.YELLOW
        paint.textAlign = Paint.Align.RIGHT

        canvas.translate(columnTwo, textRow)

        canvas.skew(0.2f, 0.3f)
        canvas.drawText(context.getString(R.string.skewed),
            clipRectLeft, clipRectTop, paint)
        canvas.restore()
    }
    private fun drawSkewedTextExample(canvas: Canvas){
    }
    private fun drawQuickRejectExample(canvas: Canvas){
        val inClipRectangle = RectF(clipRectRight / 2,
            clipRectBottom / 2,
            clipRectRight * 2,
            clipRectBottom * 2)

        val notInClipRectangle = RectF(RectF(clipRectRight+1,
            clipRectBottom+1,
            clipRectRight * 2,
            clipRectBottom * 2))

        canvas.save()
        canvas.translate(columnOne, rejectRow)
        canvas.clipRect(
            clipRectLeft,clipRectTop,
            clipRectRight,clipRectBottom
        )
        if (canvas.quickReject(
                inClipRectangle, Canvas.EdgeType.AA)) {
            canvas.drawColor(Color.WHITE)
        }
        else {
            canvas.drawColor(Color.BLACK)
            canvas.drawRect(inClipRectangle, paint
            )
        }
        canvas.restore()

    }


}


