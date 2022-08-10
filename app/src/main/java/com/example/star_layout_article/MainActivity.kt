package com.example.star_layout_article

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.star_layout_article.ui.theme.Star_layout_articleTheme

class MainActivity : ComponentActivity() {
    val items = listOf(false, false, true, false, false)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Star_layout_articleTheme {
                // A surface container using the 'background' color from the theme

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    StarLayout(radius = 160.dp) {
                        items.forEach {

                            StarItem(
                                selected = it,
                                radius = 42.dp,
                                modifier = Modifier.adjustPlace(0.9f)
                            ) {}
                        }
                        StarItem(
                            selected = false,
                            radius = 42.dp,
                            modifier = Modifier.adjustPlace(0.3f, false, 120.0)
                        ) {}
                        StarItem(
                            selected = false,
                            radius = 42.dp,
                            modifier = Modifier.adjustPlace(0f, false)
                        ) {

                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Star_layout_articleTheme {
        StarLayout(radius = 140.dp) {
            StarItem(selected = false, radius = 42.dp) {}
            StarItem(selected = false, radius = 42.dp) {}
            StarItem(selected = false, radius = 42.dp) {}
            StarItem(selected = true, radius = 42.dp) {}
            StarItem(selected = false, radius = 42.dp) {}
        }
    }
}
