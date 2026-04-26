import { initializeApp }  from 'https://www.gstatic.com/firebasejs/10.7.1/firebase-app.js';
import { getAuth }        from 'https://www.gstatic.com/firebasejs/10.7.1/firebase-auth.js';
import { getFirestore }   from 'https://www.gstatic.com/firebasejs/10.7.1/firebase-firestore.js';

const firebaseConfig = {
    apiKey:            "AIzaSyBcQgXLiQRle9lkGwNAlD0M203GlO75oQI",
    authDomain:        "mindflash-382b0.firebaseapp.com",
    projectId:         "mindflash-382b0",
    storageBucket:     "mindflash-382b0.firebasestorage.app",
    messagingSenderId: "493869490355",
    appId:             "1:493869490355:web:fd9389be0ef2716d4450c6"
};

const app = initializeApp(firebaseConfig);
export const auth = getAuth(app);
export const db   = getFirestore(app);