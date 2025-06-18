# Action Pad - Task Management App

A modern Android task management application with email authentication and time tracking features.

## Features

### ✅ **Authentication System**
- **Login Page**: Email and password authentication
- **Sign Up Page**: Create new accounts with email validation
- **Logout Functionality**: Secure logout with confirmation dialog
- **Session Management**: Automatic login state persistence

### ✅ **Task Management**
- **Add Tasks**: Create new tasks with title, description, and due date
- **Edit Tasks**: Modify existing task details
- **Delete Tasks**: Remove tasks with confirmation
- **Mark Complete**: Check off completed tasks
- **Favorite Tasks**: Star important tasks

### ✅ **Time Tracking**
- **Creation Time**: Automatically tracks when each task was created
- **Due Date Selection**: Calendar picker for setting task deadlines
- **Time Display**: Shows both creation time and due date in task items

### ✅ **User Interface**
- **Modern Material Design**: Clean and intuitive UI
- **Splash Screen**: App introduction with smooth animations
- **Responsive Layout**: Works on different screen sizes
- **Progress Indicators**: Loading states for better UX

## Current Implementation (Demo Mode)

The app currently uses a **local authentication helper** (`AuthHelper`) that:
- ✅ **No Firebase Required**: Works without any external services
- ✅ **Email Validation**: Accepts any valid email format
- ✅ **Password Requirements**: Minimum 6 characters
- ✅ **Local Storage**: Credentials stored in SharedPreferences
- ✅ **Session Persistence**: Stays logged in between app launches
- ✅ **Realistic UX**: Simulates network delays for authentic feel

### **Demo Credentials**
- **Email**: Any valid email format (e.g., `test@example.com`)
- **Password**: At least 6 characters (e.g., `password123`)

## Firebase Setup (Optional - For Production)

To use real Firebase Authentication instead of the demo AuthHelper:

### 1. **Create Firebase Project**
1. Go to [Firebase Console](https://console.firebase.google.com/)
2. Create a new project or select existing one
3. Enable Authentication with Email/Password provider

### 2. **Add Android App**
1. Click "Add app" → Android
2. Enter package name: `com.example.managertask`
3. Download `google-services.json`
4. Place it in the `app/` directory

### 3. **Enable Authentication**
1. In Firebase Console, go to Authentication → Sign-in method
2. Enable Email/Password provider

### 4. **Update Dependencies**
Add to `app/build.gradle.kts`:
```kotlin
plugins {
    id("com.google.gms.google-services")
}

dependencies {
    implementation(platform("com.google.firebase:firebase-bom:32.7.0"))
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-analytics-ktx")
}
```

Add to project-level `build.gradle.kts`:
```kotlin
plugins {
    id("com.google.gms.google-services") version "4.4.0" apply false
}
```

### 5. **Switch to Firebase Auth**
Replace `AuthHelper` calls in:
- `LoginActivity.kt`
- `SignUpActivity.kt`
- `MainActivity.kt`

Use Firebase Auth methods:
```kotlin
// Instead of AuthHelper.signInWithEmailAndPassword()
FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
```

## App Flow

1. **Splash Screen** → Welcome screen with app branding
2. **Login Screen** → Email/password authentication
3. **Sign Up Screen** → Create new account (optional)
4. **Main Screen** → Task management interface
5. **Logout** → Return to login screen

## Technical Details

### **Architecture**
- **MVVM Pattern**: ViewModel and LiveData for data management
- **Room Database**: Local SQLite database for tasks
- **ViewBinding**: Type-safe view access
- **Coroutines**: Asynchronous operations

### **Dependencies**
- **Room**: Local database for tasks
- **Material Design**: Modern UI components
- **Lifecycle Components**: ViewModel and LiveData

### **Database Schema**
```kotlin
@Entity(tableName = "tasks")
data class Task(
    val id: Long = 0,
    val title: String,
    val description: String,
    val dueDate: Date,
    val isCompleted: Boolean = false,
    val isFavourite: Boolean = false,
    val createdAt: Date = Date()
)
```

## Security Notes

### **Demo Mode (Current)**
- ✅ **No External Dependencies**: Works offline
- ✅ **Local Storage**: Credentials stored in SharedPreferences
- ✅ **Input Validation**: Proper email and password validation
- ✅ **Session Management**: Secure login state handling
- ⚠️ **Development Only**: Not suitable for production

### **Production Mode (Firebase)**
- 🔒 **Secure Firebase Authentication**
- 🔒 **Encrypted data transmission**
- 🔒 **Real user management**
- 🔒 **Password reset functionality**

## Building the App

1. Clone the repository
2. Open in Android Studio
3. Sync Gradle files
4. Build and run on device/emulator

**✅ No additional setup required!** The app works immediately with demo authentication.

## Testing

### **Test Scenarios**
1. **Create Account**: Use any valid email + 6+ character password
2. **Login**: Use the same credentials
3. **Add Tasks**: Create tasks with due dates
4. **Edit/Delete**: Modify or remove tasks
5. **Mark Complete**: Check off completed tasks
6. **Favorite**: Star important tasks
7. **Logout**: Use menu in top-right corner
8. **Verify Time Tracking**: Check creation times and due dates

## Future Enhancements

- [ ] Password reset functionality
- [ ] Google Sign-In integration
- [ ] Task categories/tags
- [ ] Task priority levels
- [ ] Push notifications for due dates
- [ ] Task sharing between users
- [ ] Cloud synchronization
- [ ] Dark theme support

---

**🎉 Ready to Use!** The app is fully functional with demo authentication. No Firebase setup required for testing and development. 