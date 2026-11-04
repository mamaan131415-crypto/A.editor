package com.aeditor.core

private fun assertEquals(expected: Any, actual: Any, message: String) {
    check(expected == actual) { "$message (expected=$expected, actual=$actual)" }
}

private fun assertTrue(value: Boolean, message: String) {
    check(value) { message }
}

fun main() {
    var state = EditorState()
    state = EditorEngine.addClip(state, "A", 3)
    state = EditorEngine.addClip(state, "B", 5)
    assertEquals(2, state.clips.size, "Should add clips")
    assertEquals(0, state.clips[0].startSec, "First clip offset")
    assertEquals(3, state.clips[1].startSec, "Second clip offset")
    assertEquals(8, state.outputDurationSec, "Output duration")

    val firstId = state.clips.first().id
    state = EditorEngine.removeClip(state, firstId)
    assertEquals(1, state.clips.size, "Should remove clip")
    assertEquals(0, state.clips.first().startSec, "Offset should reflow")
    assertTrue(state.selectedClipId != firstId, "Selection should move away from removed clip")

    val id = state.clips.first().id
    state = EditorEngine.trimSelectedClip(state, id, 99)
    state = EditorEngine.setPlaybackSpeed(state, 99f)
    assertEquals(1, state.clips.first().durationSec, "Duration floor")
    assertEquals(2f, state.playbackSpeed, "Playback speed cap")

    println("EditorEngine tests passed")
}
