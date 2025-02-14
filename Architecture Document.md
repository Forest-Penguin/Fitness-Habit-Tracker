## Architecture Document

### 1. Overview
This document provides an overview of the architecture for the Fitness Tracking App. It outlines the major components of the system, including user management, activity tracking, habit logging, and AI-powered workout recommendations.

### 2. Languages/Frameworks/Libraries/Services/APIs
#### Languages:

- **Python**: Backend logic for activity tracking, user authentication, AI recommendations, and data processing.
- **JavaScript**: Frontend development using React Native for cross-platform mobile application.

#### Frameworks:

- **React Native**: For building the mobile application UI, compatible with both iOS and Android.
- **Flask (Python)**: Backend API development for handling requests, data processing, and AI integration.

#### Libraries:

- **SQLite (Python)**: Local database management for offline storage.
- **SQLAlchemy (Python)**: ORM for database interactions.
- **requests (Python)**: For API communication.
- **TensorFlow/PyTorch**: For implementing AI-powered workout recommendation models.
- **Chart.js**: For rendering activity and progress charts.
- **Redux (JavaScript)**: For state management in the frontend.

#### Services/APIs:

- **Strava API**: For syncing user activity data.
- **Google Fit API / Apple HealthKit**: For retrieving health metrics.
- **Expo**: For fast prototyping and testing of the React Native app.

### 3. Package/Build Manager
- **NPM**: Manages JavaScript dependencies for React Native.
- **Pip**: Manages Python libraries for backend and data processing.

### 4. Task Assignments
- **Person 1:** Frontend development using React Native (UI/UX design, implementing activity tracking, habit logging, and dashboard views).
- **Person 2:** Backend API development with Flask (user authentication, data processing, syncing with APIs, and AI model integration).
- **Person 3:** Integration with fitness APIs (Strava, Google Fit, Apple HealthKit; managing API tokens and requests).
- **Person 4:** Database design and implementation (SQLite setup, schema design, managing user data storage).
- **Person 5:** Visualization and analytics (building activity charts, habit tracking, and generating insights).

### 5. Deployment
- **Mobile Deployment:** The app will be deployed to Google Play Store.
- **Cloud Deployment:** Optional backend services will be hosted on Firebase and/or AWS for syncing and scalability.

### 6. Development/Deployment Environments
- **Development Environment:** Each developer will set up React Native and Flask locally. Emulators (iOS Simulator, Android Emulator) will be used for testing mobile features.
- **Deployment Environment:** SQLite for offline local storage; optional Firebase for cloud-based synchronization.

### 7. Mobile Application Structure
#### 7.1 Views (UI)

**Login View:**
- A screen where users register or log in to their account.
- Fields: Email input, password input, login/register button.
- ![Login View](image)

**Dashboard View:**
- Displays an overview of fitness progress, including activity stats and streaks.
- Includes navigation to habit/goal setting, and workout recommendations.
- ![Dashboard View](image)

**Habit Tracker View:**
- Allows users to log and view daily habits like water intake or steps.
- Provides streak tracking and progress indicators.
- ![Habit Tracker View](image)

**Workout Recommendations View:**
- Shows AI-powered workout plans tailored to the user's goals and activity history.
- Includes options to customize and save plans.
- ![Workout Recommendations View](image)

#### 7.2 Main Application Logic

**Activity Tracking Module:**
- Tracks daily steps, workouts, and other activities using data from APIs (Strava, Google Fit, Apple HealthKit).
- Syncs data automatically and updates progress charts in real-time.

**Habit Logging:**
- Allows users to log habits (e.g., water intake, workouts).
- Provides visual feedback through streaks and completion badges.

**AI-Powered Workout Recommendations:**
- Generates customized workout plans using AI models.
- Takes into account user goals, activity history, and fitness level.
- Recommends dynamic changes based on progress.

**Data Visualization:**
- Generates interactive charts for weekly progress and streak tracking.
- Displays goal completion rates and activity summaries.

### 8. Data Storage
#### 8.1 Database Schema

**Users Table:**
- `id`: Integer (Primary Key)
- `username`: String
- `email`: String
- `password_hash`: String (Hashed password)

**Habits Table:**
- `id`: Integer (Primary Key)
- `user_id`: Integer (Foreign Key from Users Table)
- `habit_name`: String
- `streak`: Integer (Current streak count)
- `last_logged`: DateTime

**Activities Table:**
- `id`: Integer (Primary Key)
- `user_id`: Integer (Foreign Key from Users Table)
- `activity_type`: String (e.g., "Running", "Cycling")
- `duration`: Float (in minutes)
- `calories_burned`: Float
- `date`: DateTime

### 9. Design Considerations
#### Activity Tracking:
- Keep tracking logic modular to allow easy integration of new activity types or APIs.
- Handle scenarios where activity data is unavailable due to API failures.

#### Habit Logging:
- Ensure robust validation to avoid duplicate or conflicting habit logs.
- Provide offline support for logging and sync updates when reconnected.

#### AI Recommendations:
- Use TensorFlow/PyTorch models trained on fitness datasets.
- Cache recommendations locally to ensure quick responses.
- Continuously refine models based on user feedback and results.

#### Performance Considerations:
- Optimize SQLite queries for real-time updates.
- Minimize API calls to external services by caching results where applicable.

