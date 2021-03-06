/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.androiddevchallenge

import android.os.Bundle
import android.os.CountDownTimer
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.androiddevchallenge.ui.theme.MyTheme
import com.example.androiddevchallenge.ui.theme.typography

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyTheme {
                MyApp()
            }
        }
    }
}

// Start building your app here!
@Composable
fun MyApp() {
    Surface(
        color = MaterialTheme.colors.background,
        modifier = Modifier.padding(8.dp)
    ) {
        val counter = CountDownTimerViewModel()
        PageSwitch(counter)
        counter.startTimer(6 * 1000)
    }
}

private enum class ScreenType {
    AD,
    CONTENT
}

@Composable
fun PageSwitch(counter: CountDownTimerViewModel) {
    var scene by remember { mutableStateOf(ScreenType.AD) }
    val now: String by counter.sec.observeAsState("")

    Column(Modifier.padding(16.dp)) {

        Text(now)
        if (now == "Enjoy") {
            scene = ScreenType.CONTENT
        }
        Spacer(Modifier.height(16.dp))

        Crossfade(
            targetState = scene,
        ) {
            when (scene) {
                ScreenType.AD ->
                    ADScreen()
                ScreenType.CONTENT ->
                    ContentScreen()
            }
        }
    }
}

@Composable
fun ADScreen() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = Color.LightGray,
    ) {
        Text(
            text = "Welcome to Android Jetpack Compose!!",
            modifier = Modifier.padding(8.dp),
            style = MaterialTheme.typography.h4
        )
    }
}

@Composable
fun ContentScreen() {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Image(
            painter = painterResource(R.drawable.doge),
            contentDescription = null
        )
        Spacer(Modifier.width(16.dp))
        Column {
            Text(
                "An apple a day keeps the doctor away.",
                style = typography.h6
            )
            Text(
                "-Doge",
                style = typography.body2
            )
            Text(
                "Just a dog~",
                style = typography.body2
            )
        }
    }
}

class CountDownTimerViewModel : ViewModel() {
    private val _sec = MutableLiveData("Hello")
    val sec: LiveData<String> = _sec

    val isRunning = MutableLiveData<Boolean>(false)
    lateinit var timer: CountDownTimer

    fun startTimer(timerTimeInMillis: Long) {
        println("Count : Start")
        isRunning.value = true
        timer = object : CountDownTimer(timerTimeInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                println("Count : ${millisUntilFinished / 1000}")
                _sec.value = "You can skip this AD in : ${millisUntilFinished / 1000}"
            }

            override fun onFinish() {
                println("Count : Finish")
                isRunning.value = false
                _sec.value = "Enjoy"
            }
        }.start()
    }

    fun stopTimer() {
        isRunning.value = false
        timer.cancel()
        _sec.value = "Count down Stop"
    }
}

@Preview("Light Theme", widthDp = 360, heightDp = 640)
@Composable
fun LightPreview() {
    MyTheme {
        MyApp()
    }
}

@Preview("Dark Theme", widthDp = 360, heightDp = 640)
@Composable
fun DarkPreview() {
    MyTheme(darkTheme = true) {
        MyApp()
    }
}
