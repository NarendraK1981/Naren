# OTPAuthApp – Passwordless Email OTP Authentication

A multi-module Android application featuring a Kotlin/Spring Boot backend for robust OTP management.

## Project Structure
- **:otp**: The main Android Application module.
- **:backend**: Spring Boot service for OTP generation and validation.
- **:feature:auth**: Authentication UI and logic.
- **:feature:products**: Post-login product catalog.
- **:core**: Shared UI components and data layers.

## Features
- **Secure Backend**: OTPs are generated and validated on a Spring Boot server (prevents local manipulation).
- **Email-based OTP login**: 6-digit OTP generation via backend service.
- **OTP Lifecycle**:
    - Expiry: 60 seconds.
    - Rate Limiting: Maximum 3 attempts before invalidation.
- **CI/CD Integration**: Automated builds and GitHub Releases for both Android APK and Backend JAR.
- **Session Management**: Session screen with live duration timer.
- **Logout support**.

## Backend Logic (Spring Boot)
- **@Service Layer**: Manages `ConcurrentHashMap` for OTP storage.
- **@RestController**: Exposes endpoints for generating and validating OTPs.
- **Validation**: Checks for expiration, attempt limits, and code matching.

## Architecture & Tools
- **UI**: Jetpack Compose with One-way Data Flow (MVI/MVVM pattern).
- **Dependency Injection**: Hilt (Android) and Spring Dependency Injection (Backend).
- **Concurrency**: Kotlin Coroutines & Flow.
- **CI/CD**: GitHub Actions (defined in `.github/workflows/main.yml`).
- **Logging**: Timber for Android, SLF4J/Logback for Backend.

## How to Build & Run

### Android App
1. Open in Android Studio.
2. Run the `:otp` configuration on an emulator or device.

### Backend Service
1. Navigate to the root folder.
2. Run `./gradlew :backend:bootRun` to start the local server.
3. Access the API at `http://localhost:8080/api/otp`.

### Build Executable JAR
To build the standalone backend JAR:
```bash
./gradlew :backend:bootJar
```
The file will be located at `backend/build/libs/`.

## CI/CD and Releases
This project uses GitHub Actions to automate the build process.
- **On Push**: Builds and tests both Android and Backend modules.
- **On Tag (`v*`)**: Automatically creates a GitHub Release and uploads the executable Backend JAR.

---
## AI Usage
- Used GeminiAI for guidance, architecture planning, and debugging.
- Final implementation reviewed and integrated manually.
