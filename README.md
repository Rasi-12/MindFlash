MindFlash — Daily Facts App

MindFlash is a cross-platform application that delivers a new fact from Science, History, or Technology to users every day. The app is built for both Android and Web, using Firebase as the complete backend. Facts are fetched automatically from a public API — no manual content entry is ever needed.


---

Project Details

- Project ID: mindflash-382b0
- Package Name: com.demo.mindflash
- Platforms: Android (Java) and Web (HTML, CSS, JavaScript)
- Backend: Firebase (Cloud Functions, Firestore, Authentication, FCM, Analytics, Crashlytics)



---

What the App Does

When a user opens the app, a random fact is automatically fetched and displayed. The user can like or save any fact. Liked and saved facts are stored privately under each user's account in Firestore and can be viewed or deleted at any time. A daily push notification reminds users to open the app and read their fact of the day.



---

Firestore Data Structure

facts collection — shared across all users, populated by the Cloud Function
users collection — one document per user, containing liked and saved subcollections



---

How to Set Up

1. Go to the Firebase Console and enable Authentication (Email/Password and Google), Firestore (test mode, region asia-south1), Cloud Messaging, and Crashlytics. Upgrade to the Blaze plan to deploy Cloud Functions.

2. Add a Web App in Project Settings and update the appId in web/firebase-config.js. Add localhost to the Authorized Domains list in Authentication settings.

3. Deploy the Cloud Function by running the following in the project root:
   cd functions && npm install && cd ..
   firebase deploy --only functions

4. Deploy Firestore rules:
   firebase deploy --only firestore:rules

5. Open the android folder in Android Studio. Place google-services.json inside android/app/. Run the following to get the SHA-1 fingerprint for Google Sign-In and add it in Firebase Console under Project Settings:
   ./gradlew signingReport

6. Run the web app locally:
   cd web
   npx serve .
   Then open http://localhost:3000



---

Project Folder Structure

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

---
