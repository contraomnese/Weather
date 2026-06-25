package com.contraomnese.weather.core.ui.widgets

import android.content.Intent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import androidx.core.net.toUri

@Composable
fun LocationApiItem(
    modifier: Modifier,
    url: String,
    description: String,
) {
    val context = LocalContext.current

    val style = MaterialTheme.typography.labelMedium.toSpanStyle().copy(
        color = MaterialTheme.colorScheme.onBackground,
        textDecoration = TextDecoration.Underline
    )

    val annotatedText = remember(url, description) {
        buildAnnotatedString {
            withLink(
                link = LinkAnnotation.Url(
                    url = url,
                    styles = TextLinkStyles(style = style),
                    linkInteractionListener = {
                        val intent = Intent(Intent.ACTION_VIEW, url.toUri())
                        context.startActivity(intent)
                    }
                )
            ) {
                append(description)
            }
        }
    }

    Text(
        modifier = modifier,
        text = annotatedText,
        textAlign = TextAlign.Center,
    )
}