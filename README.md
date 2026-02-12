# LokalAuthApp – Passwordless Email OTP Authentication

## Features
- Email-based OTP login (no backend)
- 6-digit OTP generation
- OTP expiry: 60 seconds
- Maximum attempts: 3
- Session screen with live duration timer
- Logout support

## OTP Logic
- OTP is generated locally using random 6-digit number
- Stored per email in a Map
- Old OTP invalidated on resend
- Attempts reset on new OTP

## Architecture
- Jetpack Compose UI
- ViewModel + UI State
- One-way data flow
- No global mutable state

## External SDK
- Timber for logging
- Logs OTP generation, validation success/failure, logout

## Tools Used
- Android Studio
- Kotlin
- Jetpack Compose
- Coroutines

## AI Usage
- Used ChatGPT for guidance and debugging
- Final implementation understood and written manually
# Naren
