package app.spidy.photoeditor2.core

import android.graphics.Paint
import android.graphics.Path


class LinePath(
    drawPath: Path?,
    drawPaints: Paint?
) {
    val drawPaint: Paint = Paint(drawPaints)
    val drawPath: Path = Path(drawPath)

}