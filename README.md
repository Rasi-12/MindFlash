⚡ MindFlash 

🔧 Project Configuration

Project ID: mindflash-382b0
Package Name: com.demo.mindflash
Project Number: 493869490355


🚀 Step 1 — Enable Firebase Services

Go to: https://console.firebase.google.com

Select your project: mindflash-382b0

Authentication

Enable Email/Password
Enable Google Sign-In and set support email
Firestore Database
Create database
Start in test mode
Choose region (e.g., asia-south1)
Billing Plan
Upgrade to Blaze Plan (required for Cloud Functions)
Additional Services
Enable Cloud Messaging
Enable Crashlytics


🌐 Step 2 — Add Web App

Go to Project Settings → Your Apps → Add Web App
Register app (e.g., MindFlash Web)
Copy appId
Update web/firebase-config.js with the new appId
Add localhost in Authorized Domains for testing


☁️ Step 3 — Deploy Cloud Functions

npm install -g firebase-tools
firebase login

cd mindflash-project-folder
cd functions
npm install
cd ..
firebase deploy --only functions
After deployment, you will get a URL like:
https://us-central1-mindflash-382b0.cloudfunctions.net/fetchDailyFact
Test it in browser to verify JSON response.



🔐 Step 4 — Deploy Firestore Rules

firebase deploy --only firestore:rules


📱 Step 5 — Run Android App

Open Android Studio
Open the android folder
Wait for Gradle sync
Configure SHA-1
./gradlew signingReport
Copy SHA-1 from debug variant
Add it in Firebase Console → Project Settings → Android App
Run App
Connect device or emulator
Click Run


✅ Step 6 — Test Android Features

Register/Login (Email & Google)
Fetch daily fact
Like and Save functionality
View liked and saved facts
Delete saved items
Logout


🌍 Step 7 — Run Web App

cd web
npx serve .

Open:

http://localhost:3000
Test Features
Register/Login
View daily fact
Like/Save
View saved facts
Delete saved items


🔔 Step 8 — Push Notifications

Go to Firebase Console → Messaging
Create new campaign
Add title and message
Target app package
Send or schedule notification


🔒 Step 9 — Firestore Rules

Users can read shared facts
Users can read/write only their own data
Only Cloud Functions can write to facts collection


📁 Project Structure

mindflash/
├── firebase.json
├── firestore.rules
├── firestore.indexes.json
├── .firebaserc
│
├── functions/
│   ├── index.js
│   └── package.json
│
├── web/
│   ├── firebase-config.js
│   ├── index.html
│   ├── register.html
│   ├── home.html
│   └── saved.html
│
└── android/
    └── app/src/main/

    
🎯 Features Implemented
Firebase Authentication (Email & Google)
Firestore Database (CRUD operations)
Cloud Functions (Daily fact API)
Firebase Cloud Messaging (Push notifications)
Firebase Analytics (Event tracking)
Firebase Crashlytics (Error monitoring)
Android UI with Activities & RecyclerView
Web frontend with Firebase SDK
