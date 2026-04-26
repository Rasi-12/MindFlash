const functions = require('firebase-functions');
const admin     = require('firebase-admin');
const fetch     = require('node-fetch');

admin.initializeApp();
const db = admin.firestore();

/**
 * fetchDailyFact — HTTP Trigger
 * Called by Android app and Web app on every home screen launch.
 * 1. Checks if today's fact is already cached in Firestore
 * 2. If not, fetches from public API and saves it
 * 3. Returns the fact as JSON
 */
exports.fetchDailyFact = functions.https.onRequest(async (req, res) => {
    // Allow CORS for web clients
    res.set('Access-Control-Allow-Origin', '*');
    res.set('Access-Control-Allow-Methods', 'GET, POST');
    res.set('Access-Control-Allow-Headers', 'Content-Type');

    if (req.method === 'OPTIONS') {
        res.status(204).send('');
        return;
    }

    try {
        const today = new Date().toISOString().split('T')[0]; // "2025-04-25"

        // 1. Check cache — return today's fact if already fetched
        const existing = await db.collection('facts')
                                 .where('date', '==', today)
                                 .limit(1)
                                 .get();

        if (!existing.empty) {
            const doc = existing.docs[0];
            return res.json({ id: doc.id, ...doc.data() });
        }

        // 2. Fetch from public API (free, no key needed)
        const apiRes = await fetch('https://uselessfacts.jsph.pl/random.json?language=en');
        const data   = await apiRes.json();
        const text   = data.text;

        // 3. Categorise with simple keyword matching
        let category = 'General';
        if (/planet|star|DNA|cell|atom|species|science|biology|physics|chemistry/i.test(text))
            category = 'Science';
        else if (/war|king|queen|century|ancient|empire|history|president|battle|revolution/i.test(text))
            category = 'History';
        else if (/computer|internet|robot|AI|code|software|technology|digital|data/i.test(text))
            category = 'Tech';

        // 4. Save to Firestore facts/ collection
        const docRef = await db.collection('facts').add({
            text,
            category,
            source:    'uselessfacts',
            date:      today,
            fetchedAt: admin.firestore.FieldValue.serverTimestamp()
        });

        // 5. Return fact to client
        return res.json({ id: docRef.id, text, category, date: today });

    } catch (error) {
        console.error('fetchDailyFact error:', error);
        return res.status(500).json({ error: 'Failed to fetch fact', details: error.message });
    }
});
