### Software Design and Development Task 3 - Develop a Solution Package

**Name:** Healthy Study

**Description:** Healthy Study is a small application written in Kotlin, designed to help students balance their health and academic responsibilities. Recognising the challenges students face in managing their time effectively between studying and maintaining a healthy lifestyle, Healthy Study aims to provide a comprehensive tool that supports both academic progress and personal well-being. .

### Hardware Requirements

**Minimum Requirements:**
- **Processor:** Quad-core ARM Cortex-A53 or equivalent
- **RAM:** 2 GB
- **Storage:** 50 MB of free space
- **Display:** 720p resolution (1280x720)
- **Network:** Internet connection

**Recommended Requirements:**
- **Processor:** Octa-core ARM Cortex-A53 or better
- **RAM:** 4 GB or more
- **Storage:** 100 MB of free space
- **Display:** 1080p resolution (1920x1080)
- **Network:** Stable internet connection (Wi-Fi or 4G)

### Software Requirements

**Minimum Android Version:** Android 8.0 (Oreo, API level 27)

**Recommended Android Version:** Android 10.0 (Q, API level 29) or higher

**Permissions Required:**
- `INTERNET`: To access network services
- `POST_NOTIFICATIONS`: To send notifications
- `VIBRATE`: To enable vibration for notifications

### Third-Party Services/APIs and Libraries

**Third-Party Services/APIs:**
- **Retrofit:** For network requests and API calls

**Libraries:**
- **Retrofit:** `com.squareup.retrofit2:retrofit:2.9.0`
- **Gson Converter:** `com.squareup.retrofit2:converter-gson:2.9.0`
- **RxJava Adapter:** `com.squareup.retrofit2:adapter-rxjava2:2.9.0`
- **OkHttp:** `com.squareup.okhttp3:okhttp:4.9.0`
- **OkHttp Logging Interceptor:** `com.squareup.okhttp3:logging-interceptor:4.9.0`
- **Coroutines:** `org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3` and `org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3`
- **Lifecycle Runtime:** `androidx.lifecycle:lifecycle-runtime-ktx:2.8.1`
- **Compose UI:** `androidx.compose.ui:ui:1.6.7` and other related Compose libraries
- **AppCompat:** `androidx.appcompat:appcompat:1.7.0`
- **Material Components:** `com.google.android.material:material:1.12.0`
- **JUnit:** `junit:junit:4.13.2`
- **Espresso:** `androidx.test.espresso:espresso-core:3.5.1`

###	Setting up Source Code in Android Studio

0.	Prerequisite

o	Ensure you have the latest version of Android Studio installed.
o	Ensure you have Git installed for cloning the repository 
(Optional since you can just download the source code directly from Github)

1.	Clone the Repository (If Git is installed)

o	Open a terminal or command prompt.
o	Navigate to the directory where you want to clone the repository.
o	Run the following command to clone the repository:
```
git clone https://github.com/Harcozy/Healthy_Study.git
```

2.	Open the project in Android Studio

o	Launch Android Studio
o	Click on ‘File’ > “Open…”
o	Navigate to the cloned repository directory and select it.
o	Click “OK” to open the project.

3.	Configure the project

o	Android Studio will automatically start syncing the project. If not, click on ‘File’  > ‘Sync Project with Gradle Files’
o	Ensure all dependencies are downloaded and the project is built successfully.

4.	Set up an Android Emulator or Connect a Device

o	To run the app, you can either use an Android Emulator or connect a physical device.
o	For setting up an Emulator:

•	Go to ‘Tools’ > ‘AVD Manager’
•	Create a new virtual device and follow the prompts to set it up.
(Remember to match the Specification)

o	For using a Physical Device:

•	Enable ‘Developer Options’ and ‘USB Debugging’ on your device.
•	Connect your device to the computer via USB.

5.	Run the app

o	Once the setup is complete, you can run the app by clicking the ‘Run’ button (Green play button) in the tool bar.
o	Select the target device (emulator or connected device) and click ‘OK’

FocusActivity.kt LICENSE
```
Copyright 2020 The Android Open Source Project

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    https://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
