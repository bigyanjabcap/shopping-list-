# Fix: Missing 'room_version' Property

I have resolved the Gradle sync error where the `room_version` property was missing from the extra properties extension.

## Changes Made

### Root Build Configuration
I added the missing `room_version` property to the root `build.gradle.kts` file. This allows the `app` module to access the version string via `rootProject.extra["room_version"]`.

#### [MODIFY] [build.gradle.kts](file:///C:/Users/bigya/AndroidStudioProjects/datastoreapplication/build.gradle.kts)
```diff
 // Top-level build file where you can add configuration options common to all sub-projects/modules.
 plugins {
     alias(libs.plugins.android.application) apply false
     alias(libs.plugins.kotlin.compose) apply false
 }
+
+extra["room_version"] = "2.6.1"
```

## Verification Results

### Automated Tests
- Executed `gradle_sync`, which now finishes successfully:
  - **Status**: Sync finished successfully.

## Next Steps (Optional)
While the sync error is fixed, you may want to:
1. **Uncomment Room Compiler**: If you intend to use Room, you should uncomment the `ksp` line in `app/build.gradle.kts` and ensure the KSP plugin is applied.
2. **Use Version Catalog**: Consider moving Room and other hardcoded dependencies (like Navigation) to `gradle/libs.versions.toml` to follow modern Android best practices.
