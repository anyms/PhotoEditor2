package app.spidy.photoeditor2

data class EditorSettings(
    var isRotationEnabled: Boolean = false,
    var isTranslateEnabled: Boolean = true,
    var isScaleEnabled: Boolean = true
)