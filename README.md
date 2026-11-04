# A.editor

A minimal Android video editor prototype inspired by CapCut.

## Features
- Import placeholder clips into a timeline
- Select, trim, and delete clips
- Playback-speed adjustment (0.25x to 2x)
- Computed output duration preview

## Run focused logic tests
```bash
kotlinc editor-core/src/main/kotlin/com/aeditor/core/EditorState.kt \
  editor-core/src/test/kotlin/com/aeditor/core/EditorEngineTest.kt \
  -include-runtime -d /tmp/editor-engine-tests.jar
java -jar /tmp/editor-engine-tests.jar
```

## Run app
Open `/home/runner/work/A.editor/A.editor` in Android Studio, then run the app from `app/src/main/java/com/aeditor/MainActivity.kt` on an emulator/device.
