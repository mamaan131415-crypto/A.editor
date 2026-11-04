package com.aeditor.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.aeditor.core.EditorEngine
import com.aeditor.core.EditorState

@Composable
fun EditorScreen() {
    var state by remember { mutableStateOf(EditorState()) }
    var nextClip by remember { mutableIntStateOf(1) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF101418))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("A.editor", style = MaterialTheme.typography.headlineMedium, color = Color.White)
        Text(
            "CapCut-style quick editor prototype",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.LightGray
        )

        Button(onClick = {
            state = EditorEngine.addClip(state, "Clip $nextClip", 5)
            nextClip += 1
        }) {
            Text("Import clip")
        }

        Text("Timeline", color = Color.White)
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(state.clips, key = { it.id }) { clip ->
                Card(onClick = { state = state.copy(selectedClipId = clip.id) }) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text(clip.name)
                        Text("Start ${clip.startSec}s")
                        Text("Length ${clip.durationSec}s")
                    }
                }
            }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(
                onClick = {
                    state.selectedClipId?.let { id ->
                        state = EditorEngine.trimSelectedClip(state, id, 1)
                    }
                }
            ) {
                Text("Trim -1s")
            }
            Button(
                onClick = {
                    state.selectedClipId?.let { id ->
                        state = EditorEngine.trimSelectedClip(state, id, -1)
                    }
                }
            ) {
                Text("Trim +1s")
            }
            Button(
                onClick = {
                    state.selectedClipId?.let { id ->
                        state = EditorEngine.removeClip(state, id)
                    }
                }
            ) {
                Text("Delete")
            }
        }

        Text("Playback speed: ${"%.2f".format(state.playbackSpeed)}x", color = Color.White)
        Slider(
            value = state.playbackSpeed,
            onValueChange = { speed -> state = EditorEngine.setPlaybackSpeed(state, speed) },
            valueRange = 0.25f..2f
        )

        Spacer(Modifier.height(8.dp))
        Text("Output duration: ${state.outputDurationSec}s", color = Color.White)
        Text("Selected clip: ${state.selectedClipId ?: "none"}", color = Color.White)
    }
}
