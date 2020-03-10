package app.spidy.photoeditor2

import android.view.View
import app.spidy.photoeditor2.core.TextStyleBuilder
import app.spidy.photoeditor2.core.ViewType

data class CurrentView(
    var viewType: ViewType? = null,
    var view: View? = null,
    var rootView: View? = null,
    var textStyle: TextStyleBuilder? = null
)