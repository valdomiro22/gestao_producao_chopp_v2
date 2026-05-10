package com.santos.valdomiro.gestaoproducaochopp.common.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun CustomOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    isErro: Boolean,
    icone: @Composable (() -> Unit)? = null,
    inputType: KeyboardType = KeyboardType.Text
) {
    OutlinedTextField(
        value = value,
        maxLines = 1,
        onValueChange = onValueChange,
        placeholder = { Text(placeholder) },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = inputType),
        shape = RoundedCornerShape(10.dp),
        leadingIcon = icone,
        isError = isErro,

    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    val value = ""
    CustomOutlinedTextField(
        value = value,
        onValueChange = {},
        "Email",
        isErro = false,
    )
}