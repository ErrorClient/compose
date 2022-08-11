package com.errorclient.vaccine

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class MyTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        return myFilterTransformation(text)
    }

    private fun myFilterTransformation(text: AnnotatedString): TransformedText {
        // __.__.__
        val trimmed =
            if (text.text.length > 6) text.text.substring(0, 5)
            else text.text
        var outText = ""

        for (i in trimmed.indices) {
            outText += trimmed[i]
            if (i == 1 || i == 3) outText += "."
        }

        val myOffsetMapping = object : OffsetMapping {

            override fun originalToTransformed(offset: Int): Int {
                return when (offset) {
                    in listOf(0, 1) -> offset
                    in listOf(2, 3) -> offset + 1
                    in listOf(4, 5) -> offset + 2
                    else -> 8
                }
            }

            override fun transformedToOriginal(offset: Int): Int {
                return when (offset) {
                    in listOf(0, 1, 2) -> offset
                    in listOf(3, 4, 5) -> offset - 1
                    in listOf(6, 7) -> offset - 2
                    else -> 6
                }
            }
        }

        return TransformedText(AnnotatedString(outText), myOffsetMapping)
    }
}
