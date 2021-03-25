package dev.kaitei.doggoapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import dagger.hilt.android.AndroidEntryPoint
import dev.kaitei.common.theme.DoggoTheme
import dev.kaitei.doggoapp.navigation.NavGraphFactory
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var navGraphFactory: NavGraphFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DoggoTheme {
                Surface(color = MaterialTheme.colors.surface) {
                    with(navGraphFactory) {
                        Create()
                    }
                }
            }
        }
    }
}