# OOP-client - AndroidStudio project

## Implementations

- Android Studio Hedgehog (or higher versions)

- Kotlin programming language

- Gradlew v8.2.2

- OkHttp v4.12.0

- Jetpack for navigation

- Hilt v2.48 dependency injection library from dagger

- Coroutines v1.7.3 for asynchronous code symplifaction

- Retrofit v2.9.0 for network calls

- Datastore preferences v1.1.1. for storing token and other values

## Getting started

### Prerequisites

1. Android Studio: Download and install Android Studio Hedgehog or newer.

2. Git: Ensure you have Git installed for cloning the repository.

3. OOP-server: Complete the setup process for the back-end before attempting front-end side.

See README.md from https://github.com/Cerins/OOP-server

### Installation

  1) Clone the Repository

Clone the repository to your local machine using SSH or HTTPS.

SSH example:

  ```shell
    git clone git@github.com:Cerins/OOP-client.git
  ```

HTTPS example:

  ```shell
    git clone https://github.com/Cerins/OOP-client
  ```

  2) Open the Project

Open the project in Android Studio:
  
- Navigate to the directory where you cloned the repository.
  
- Select the OOP-Client folder.
  
- Click "Open".

  3) Configure IP Address

To set up your IP address in the NetworkModule file, follow these steps:

1. Open the NetworkModule.kt file located in the di package.
2. Find the line where the base URL is defined in the Retrofit builder.
3. Replace the placeholder with your actual IP address according to the back-end solution.

  4) Build the Project

Wait for the Gradle build to complete. This may take a few minutes as it will download and configure all dependencies.

### Running the Application

1. Connect a Device or Start an Emulator

Ensure you have an Android device connected or an Android emulator running.

2. Run the App

Click the "Run" button in Android Studio or press Shift + F10 to build and run the app on your selected device/emulator

### Papildus informācija:

    Daļa no “Back-End” funkcionalitātēm vēl netiek atbalstīta “Front-End” pusē;
    Darbs tika izstrādāts grupā.
    Projekts balstīts uz projektējumu, kas izstrādāts "Programminženierija" kursa ietvaros. Dokuments papildināts ar izvietojuma un programmas modeļu klašu diagrammu: https://docs.google.com/document/d/1Yi3KF1tdhZ42VxTFy1Twajkgzo7QjPu6B03mGTWt80s/edit?usp=sharing

