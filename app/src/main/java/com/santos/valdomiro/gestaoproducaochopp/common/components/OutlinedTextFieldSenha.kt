package com.santos.valdomiro.gestaoproducaochopp.common.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun OutlinedTextFieldSenha(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    isSenhaVisivel: Boolean,
    isErro: Boolean,
    onVisibilityChange: () -> Unit,
    icone: @Composable (() -> Unit)? = null,
) {
    OutlinedTextField(
        value = value,
        maxLines = 1,
        onValueChange = onValueChange,
        placeholder = { Text(placeholder) },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        shape = RoundedCornerShape(10.dp),
        visualTransformation = if (isSenhaVisivel) VisualTransformation.None else PasswordVisualTransformation(),
        leadingIcon = icone,
        isError = isErro,
        trailingIcon = {
            IconButton(onClick = onVisibilityChange) {
                Icon(
                    imageVector = if (isSenhaVisivel) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                    contentDescription = if (isSenhaVisivel) "Esconder senha" else "Mostrar senha"
                )
            }
        }

    )
}
