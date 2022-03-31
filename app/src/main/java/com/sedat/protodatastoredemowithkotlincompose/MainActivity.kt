package com.sedat.protodatastoredemowithkotlincompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sedat.protodatastoredemowithkotlincompose.model.SettingsModel
import com.sedat.protodatastoredemowithkotlincompose.ui.theme.ProtoDatastoreDemoWithKotlinComposeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProtoDatastoreDemoWithKotlinComposeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    ProtoScreen(
                        gradientBackground = Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFF44E7FD),
                                Color(0xFFFB6E64)
                            )
                        ),
                        gradientTextField = Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFFE91E63),
                                Color(0xFFF44336)
                            )
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun ProtoScreen(
    gradientBackground: Brush,
    gradientTextField: Brush,
    viewModel: ProtoViewModel = hiltViewModel()
) {

    var themeColor by remember{
        mutableStateOf("")
    }
    var number by remember {
        mutableStateOf(0)
    }
    var isSaved by remember {
        mutableStateOf(false)
    }
    
    val settings = viewModel.readData().collectAsState(initial = SettingsModel()).value

    Surface(modifier = Modifier
        .fillMaxSize()
    ) {
        Box(modifier = Modifier
            .fillMaxSize()
            .background(gradientBackground),
            contentAlignment = Alignment.Center
        ){
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(12.dp)
            ) {
                CustomTextField(gradientTextField, "color...", KeyboardOptions(keyboardType = KeyboardType.Text)){
                    themeColor = it
                }

                Spacer(modifier = Modifier.height(7.dp))
                CustomTextField(gradientTextField, "number...", KeyboardOptions(keyboardType = KeyboardType.Number)){
                    number = it.toInt()
                }

                Spacer(modifier = Modifier.height(7.dp))
                RadioButtonGroup(
                    textList = listOf("true", "false")
                ){
                    isSaved = it == "true"
                }

                Spacer(modifier = Modifier.height(7.dp))
                CustomButton(
                    gradient = gradientTextField,
                    modifier = Modifier
                        .padding(horizontal = 25.dp, vertical = 12.dp)
                ){
                    if(themeColor.isNotEmpty() && number != 0)
                        viewModel.saveData(themeColor, number, isSaved)
                }

                Spacer(modifier = Modifier.height(7.dp))
                CustomText(text = settings.color)

                Spacer(modifier = Modifier.height(7.dp))
                CustomText(text = settings.number.toString())

                Spacer(modifier = Modifier.height(7.dp))
                CustomText(text = if (settings.isSaved) "True" else "False")
            }
        }
    }
}

@Composable
fun CustomText(text: String) {
    Text(
        text = text,
        color = Color(0xFFE91E63),
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        fontStyle = FontStyle.Italic
    )
}

@Composable
fun CustomTextField(
    background: Brush,
    hint: String = "",
    inputType: KeyboardOptions,
    callBack: (String) -> Unit
) {

    var text by remember{
        mutableStateOf("")
    }

    var isHint by remember {
        mutableStateOf(hint != "")
    }

    Box(
        contentAlignment = Alignment.CenterStart
    ){
        BasicTextField(
            value = text,
            onValueChange = {
                text = it
                callBack(it)
            },
            keyboardOptions = inputType,
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = background,
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(12.dp)
                .onFocusChanged {
                    isHint = it.isFocused != true && text.isEmpty()
                },
            textStyle = TextStyle(
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        )
        if(isHint)
            Text(
                modifier = Modifier.padding(start = 7.dp),
                text = hint,
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
    }
}

@Composable
fun RadioButtonGroup(
    textList: List<String>,
    callBack: (String) -> Unit
) {

    val selectedText = remember{
        mutableStateOf(textList[0])
    }

    Box(modifier = Modifier
        .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ){
        Column {
            Text(
                text = "is saved",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Italic,
                style = TextStyle(textDecoration = TextDecoration.Underline)
            )

            Spacer(modifier = Modifier.height(7.dp))

            textList.forEach { item->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = item == selectedText.value,
                        onClick = {
                            selectedText.value = item
                            callBack(item)
                        },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = Color(0xFFE91E63),
                            unselectedColor = Color.White
                        )
                    )

                    Text(
                        text = item,
                        modifier = Modifier
                            .clickable {
                                selectedText.value = item
                                callBack(item)
                            },
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        fontStyle = FontStyle.Italic
                    )
                }
            }
        }
    }
}

@Composable
fun CustomButton(
    gradient: Brush,
    modifier: Modifier,
    callBack: () -> Unit
) {
    Button(
        onClick = {
            callBack()
        },
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
        contentPadding = PaddingValues()
    ) {
        Box(
            modifier = Modifier
                .background(gradient)
                .then(modifier),
            contentAlignment = Alignment.Center
        ){
            Text(
                text = "Save",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

