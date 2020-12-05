import androidx.compose.desktop.Window
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.ExperimentalKeyInput
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.io.File
import java.util.*
import javax.sound.sampled.AudioSystem


@ExperimentalKeyInput
fun main() = Window(title = "Competition Timer", size = IntSize(1200, 600)) {
    var isRunning = false
    var isPause = false
    var textSize by remember { mutableStateOf(TextFieldValue("250")) }
    val count = remember { mutableStateOf(0) }
    var text by remember { mutableStateOf(TextFieldValue("00:00:0")) }
    var time by remember { mutableStateOf(TextFieldValue("0")) }
    var soundTime by remember { mutableStateOf(TextFieldValue("30")) }
    var finalSoundTime = 30
    MaterialTheme {
        var timer = Timer("timer")
        Column(Modifier.fillMaxSize(), Arrangement.Center) {
            TextButton(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                onClick = {

                }, enabled = false
            ) {
                val finalTextSize = textSize.text.toIntOrNull()?:0
                if (finalTextSize>0) Text(text.text, color = Color.Black, fontSize = finalTextSize.sp)
            }
        }

        Row(Modifier.fillMaxSize(), Arrangement.Center) {
            OutlinedTextField(modifier = Modifier.align(Alignment.Bottom).align(Alignment.CenterVertically)
                .padding(10.dp),
                value = time,
                onValueChange = {
                    time = it
                },
                label = { Text("时间（分钟）") })
            OutlinedTextField(modifier = Modifier.align(Alignment.Bottom).align(Alignment.CenterVertically)
                .padding(10.dp),
                value = soundTime,
                onValueChange = {
                    soundTime = it
                },
                label = { Text("声音时间（秒）") })
            OutlinedTextField(modifier = Modifier.align(Alignment.Bottom).align(Alignment.CenterVertically)
                .padding(10.dp),
                value = textSize,
                onValueChange = {
                    textSize = it
                },
                label = { Text("文字大小") })
            Button(modifier = Modifier.align(Alignment.Bottom).align(Alignment.CenterVertically).padding(10.dp),
                onClick = {
                    if (isPause) {
                        isPause = false
                    } else {
                        if (!isRunning) {
                            finalSoundTime = soundTime.text.toInt()
                            count.value = (time.text.toFloat() * 60 * 1000).toInt()
                            timer.schedule(object : TimerTask() {
                                override fun run() {
                                    isRunning = true
                                    var msec = (count.value % 1000).toString()
                                    var sec = ((count.value - msec.toInt()) / 1000 % 60).toString()
                                    var min = ((count.value - (sec.toInt() * 60) - msec.toInt()) / 1000 / 60).toString()
                                    if (msec.length >= 2) msec = msec.substring(0, 1)
                                    if (sec.length == 1) sec = "0$sec"
                                    if (min.length == 1) min = "0$min"
                                    if (count.value / 1000 + 1 == finalSoundTime) {
                                        finalSoundTime = -1
                                        Thread {
                                            try {
                                                val f = File("./app/notify.wav")
                                                val audioIn = AudioSystem.getAudioInputStream(f.toURI().toURL())
                                                val clip = AudioSystem.getClip()
                                                clip.open(audioIn)
                                                clip.start()
                                            } catch (e: Exception) {
                                                e.printStackTrace()
                                            }
                                            try {
                                                val f = File("notify.wav")
                                                val audioIn = AudioSystem.getAudioInputStream(f.toURI().toURL())
                                                val clip = AudioSystem.getClip()
                                                clip.open(audioIn)
                                                clip.start()
                                            } catch (e: Exception) {
                                                e.printStackTrace()
                                            }
                                        }.start()
                                    }
                                    if (!isPause) count.value = count.value - 100
                                    text = TextFieldValue("${min}:${sec}:${msec}")
                                    if (count.value < 0) {
                                        text = TextFieldValue("00:00:0")
                                        isPause = false
                                        isRunning = false
                                        this.cancel()
                                    }
                                    //println(Thread.currentThread().name + " run " + count)
                                }
                            }, 0, 100)
                        }
                    }
                }) {
                Text("开始")
            }
            Button(modifier = Modifier.align(Alignment.Bottom).align(Alignment.CenterVertically).padding(10.dp),
                onClick = {
                    isPause = true
                    count.value = count.value + 100
                }) {
                Text("暂停")
            }
            Button(modifier = Modifier.align(Alignment.Bottom).align(Alignment.CenterVertically).padding(10.dp),
                onClick = {
                    isPause = false
                    count.value = 0
                    if (isRunning) isRunning = false
                }) {
                Text("重置")
            }
        }
    }
}
