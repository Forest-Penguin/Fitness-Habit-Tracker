# Fitness Habit Tracker

A mobile application that helps users track their fitness activities, set workout goals, and receive smart recommendations based on activity patterns.

## 📱 Features

- 📊 **Dashboard View**: Overview of activities, habits, and goals.
- 🏃 **Training Tracker**: Records and classifies physical activity using sensor data.
- 🎯 **Goals Management**: Set and manage fitness goals.
- 🧠 **Activity Recommendations**: Notifies users when they're likely to skip workouts.
- 💾 **Local Storage**: Uses Room for storing workouts, goals, and activity data.
- 🔐 **Permissions Handling**: Supports activity recognition, location, and notification permissions.

## 🏗️ Architecture

- **Language**: Kotlin
- **Architecture Components**: Room, ViewModels, Services
- **Sensor Integration**: Tracks motion to classify activity types
- **Notification System**: Reminds users based on behavioral patterns
- **Testing**: Includes unit and UI tests

## 📂 Project Structure

```
FuckUP/
└── app/
    ├── src/
    │   ├── androidTest/
    │   │   └── java/com/example/fitness_habit_tracker/
    │   ├── main/
    │   │   ├── java/com/example/fitness_habit_tracker/
    │   │   │   ├── adapters/
    │   │   │   ├── database/
    │   │   │   ├── model/
    │   │   │   ├── service/
    │   │   │   ├── ui/
    │   │   │   │   ├── theme/
    │   │   │   └── util/
    │   └── test/
    │       └── java/com/example/fitness_habit_tracker/
    ├── build.gradle
    └── AndroidManifest.xml
```

## 🚀 Getting Started

1. Clone the repository
2. Open in Android Studio
3. Sync Gradle
4. Run on device or emulator

## 🧪 Testing

Run unit tests and instrumented tests via Android Studio or command line:
```bash
./gradlew test
./gradlew connectedAndroidTest
```

## Authors

- **Aldrind Reyes**  
  Email: [aldrind.reyes-reyes.799@my.csun.edu]
- **Albert Atshemyan**  
  Email: [albert.atshemyan.697@my.csun.edu]
- **Mirta Mazariego**  
  Email: [mirta.mazariego.717@my.csun.edu]
- **Ramita Batchu**  
  Email: [ramita.batchu.865@my.csun.edu]
- **Monte Tamazyan**  
  Email: [monte.tamazyan.381@my.csun.edu]
---

## 📝 License

This project is for educational use.


