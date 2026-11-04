package com.aeditor.core

import java.util.UUID
import kotlin.math.roundToInt

data class Clip(
    val id: String,
    val name: String,
    val durationSec: Int,
    val startSec: Int
)

data class EditorState(
    val clips: List<Clip> = emptyList(),
    val selectedClipId: String? = null,
    val playbackSpeed: Float = 1f
) {
    val outputDurationSec: Int
        get() {
            if (clips.isEmpty()) return 0
            val baseDuration = clips.sumOf { it.durationSec }
            return (baseDuration / playbackSpeed).roundToInt()
        }
}

object EditorEngine {
    fun addClip(state: EditorState, name: String, durationSec: Int): EditorState {
        val safeDuration = durationSec.coerceAtLeast(1)
        val newClip = Clip(
            id = UUID.randomUUID().toString(),
            name = name,
            durationSec = safeDuration,
            startSec = 0
        )
        val newClips = withOffsets(state.clips + newClip)
        return state.copy(clips = newClips, selectedClipId = newClip.id)
    }

    fun removeClip(state: EditorState, clipId: String): EditorState {
        val filtered = state.clips.filterNot { it.id == clipId }
        val selected = state.selectedClipId.takeIf { it != clipId }
        return state.copy(clips = withOffsets(filtered), selectedClipId = selected)
    }

    fun trimSelectedClip(state: EditorState, clipId: String, trimBySec: Int): EditorState {
        val updated = state.clips.map { clip ->
            if (clip.id != clipId) clip
            else clip.copy(durationSec = (clip.durationSec - trimBySec).coerceAtLeast(1))
        }
        return state.copy(clips = withOffsets(updated), selectedClipId = clipId)
    }

    fun setPlaybackSpeed(state: EditorState, speed: Float): EditorState {
        return state.copy(playbackSpeed = speed.coerceIn(0.25f, 2f))
    }

    private fun withOffsets(clips: List<Clip>): List<Clip> {
        var cursor = 0
        return clips.map { clip ->
            val normalized = clip.copy(durationSec = clip.durationSec.coerceAtLeast(1), startSec = cursor)
            cursor += normalized.durationSec
            normalized
        }
    }
}
