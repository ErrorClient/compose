@file:Suppress("DEPRECATION")

package com.errorclient.vaccine

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

class MyCompose {

    @Composable
    fun MakeCard() {
        val keyboardController = LocalFocusManager.current

        Box(
            Modifier
                .fillMaxSize()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = {
                        keyboardController.clearFocus()
                    }
                )
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            MaterialTheme.colors.secondary,
                            MaterialTheme.colors.secondaryVariant
                        ), radius = 2000f
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row() {
                    Box(
                        modifier = Modifier.weight(3f),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        PhotoCard()
                    }

                    Box(
                        modifier = Modifier.weight(2f),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        LoadPictureButton()
                    }
                }
                PersonInfo()
                PersonCalendar()
            }
        }
    }

    @Composable
    fun LoadPictureButton() {
        var bla = 0.dp
        Button(
            elevation =
            ButtonDefaults.elevation(
                defaultElevation = 5.dp
            ),
            onClick = {  }
        ) {
            Text(text = "Load")
        }
    }

    @Composable
    fun PhotoCard() {
        val picture = R.drawable.ic_person
        val context = LocalContext.current

        var openDialog by rememberSaveable { mutableStateOf(false) }
        var takePhoto: Boolean? by rememberSaveable { mutableStateOf(null) }
        var launcher: ManagedActivityResultLauncher<String, Uri?>? = null

        var imageUri by remember {
            mutableStateOf<Uri?>(null)
        }

        if (openDialog) {
            takePhoto = myDialog()
        }

        launcher = rememberLauncherForActivityResult(
            contract =
            ActivityResultContracts.GetContent()
        ) {
            imageUri = it
        }

        var bitmap: Bitmap? by rememberSaveable { mutableStateOf(null) }

        imageUri?.let {
            bitmap = if (Build.VERSION.SDK_INT < 28) {
                MediaStore.Images
                    .Media.getBitmap(context.contentResolver, it)

            } else {
                val source = ImageDecoder
                    .createSource(context.contentResolver, it)
                ImageDecoder.decodeBitmap(source)
            }
        }

        bitmap?.let { btm ->
            Image(bitmap = btm.asImageBitmap(),
                contentDescription = "Person photo",
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .border(2.0.dp, MaterialTheme.colors.primary, CircleShape)
                    .clickable(
                        onClick = {
                            launcher.launch("image/*")
                        }
                    ))
        } ?: Image(
            painter = painterResource(picture),
            contentDescription = "Person photo",
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .border(2.0.dp, MaterialTheme.colors.primary, CircleShape)
                .clickable(
                    onClick = {
                        launcher.launch("image/*")
                    }
                )
        )
    }

    @Composable
    fun myDialog(): Boolean? {

        var takePhoto: Boolean? by remember { mutableStateOf(null) }

        AlertDialog(
            onDismissRequest = {},
            confirmButton = {
                Button(onClick = {
                    takePhoto = false
                }) {
                    Text(text = "Gallery")
                }
            },
            dismissButton = {
                Button(onClick = {
                    takePhoto = true
                }) {
                    Text(text = "Photo")
                }
            }
        )
        return takePhoto
    }

    @Composable
    fun PersonInfo() {
        Column {
            MyEditText("Firstname")
            MyEditText("LastName")
            MyEditDate("Date of Birth")
            MyEditNumber("Age")
        }
    }

    @Composable
    fun MyEditDate(label: String) {
        var text by rememberSaveable { mutableStateOf("") }
        val maxSize = 6

        MaterialTheme {
            OutlinedTextField(
                value = text,
                onValueChange = {
                    if (it.length <= maxSize) text = it
                },
                label = { Text(label) },
                textStyle = TextStyle(color = MaterialTheme.colors.primary),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                visualTransformation = MyTransformation(),
                placeholder = { Text("dd.mm.yy") }
            )
        }
    }

    @Composable
    fun MyEditText(label: String) {
        var text by rememberSaveable { mutableStateOf("") }

        MaterialTheme {
            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                label = { Text(label) },
                textStyle = TextStyle(color = MaterialTheme.colors.primary),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text
                )
            )
        }
    }

    @Composable
    fun MyEditNumber(label: String) {
        var text by rememberSaveable { mutableStateOf("") }
        val maxSize = 3

        MaterialTheme {
            OutlinedTextField(
                value = text,
                onValueChange = {
                    if (it.length <= maxSize) text = it
                },
                label = { Text(label) },
                textStyle = TextStyle(color = MaterialTheme.colors.primary),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                )
            )
        }
    }

    @Composable
    fun PersonCalendar() {
    }

    @Preview
    @Composable
    fun PreviewMakeCard() {
        myDialog()
    }

}