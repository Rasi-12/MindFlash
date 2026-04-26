package com.demo.mindflash;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {

    private TextView tvFactText, tvCategory, tvGreeting;
    private FirebaseFirestore db;
    private FirebaseAnalytics analytics;
    private String currentFactId = "";
    private String currentFactText = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        db = FirebaseFirestore.getInstance();
        analytics = FirebaseAnalytics.getInstance(this);

        tvFactText = findViewById(R.id.tvFactText);
        tvCategory = findViewById(R.id.tvCategory);
        tvGreeting = findViewById(R.id.tvGreeting);

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        db.collection("users").document(uid).get()
                .addOnSuccessListener(doc -> {
                    String name = doc.getString("name");
                    if (name != null) tvGreeting.setText("Hi, " + name + " 👋");
                });

        Button btnLike = findViewById(R.id.btnLike);
        btnLike.setOnClickListener(v -> likeFact());

        Button btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(v -> saveFact());

        TextView tvLogout = findViewById(R.id.tvLogout);
        tvLogout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });

        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                return true;
            } else if (id == R.id.nav_liked) {
                startActivity(new Intent(this, LikedActivity.class));
                return true;
            } else if (id == R.id.nav_saved) {
                startActivity(new Intent(this, SavedActivity.class));
                return true;
            }
            return false;
        });

        fetchFact();
    }

    private void fetchFact() {
        tvFactText.setText("Loading today's fact...");
        new Thread(() -> {
            try {
                java.net.URL url = new java.net.URL(
                        "https://uselessfacts.jsph.pl/random.json?language=en");
                java.net.HttpURLConnection conn =
                        (java.net.HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                java.io.BufferedReader reader = new java.io.BufferedReader(
                        new java.io.InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) sb.append(line);
                reader.close();

                org.json.JSONObject json = new org.json.JSONObject(sb.toString());
                String text = json.getString("text");

                String category = "General";
                if (text.matches("(?i).*planet|star|DNA|cell|atom|species.*")) category = "Science";
                else if (text.matches("(?i).*war|king|queen|century|ancient|empire.*")) category = "History";
                else if (text.matches("(?i).*computer|internet|robot|AI|code|software.*")) category = "Tech";

                currentFactText = text;
                currentFactId = String.valueOf(System.currentTimeMillis());

                final String finalText = text;
                final String finalCategory = category;

                runOnUiThread(() -> {
                    tvFactText.setText(finalText);
                    tvCategory.setText("🔬 " + finalCategory);
                });

            } catch (Exception e) {
                runOnUiThread(() ->
                        tvFactText.setText("Could not load fact. Check internet."));
            }
        }).start();
    }

    private void likeFact() {
        if (currentFactId.isEmpty()) {
            Toast.makeText(this, "No fact loaded yet", Toast.LENGTH_SHORT).show();
            return;
        }
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Map<String, Object> data = new HashMap<>();
        data.put("text", currentFactText);
        data.put("category", tvCategory.getText().toString().replace("🔬 ", ""));
        data.put("likedAt", FieldValue.serverTimestamp());
        db.collection("users").document(uid).collection("liked")
                .document(currentFactId).set(data)
                .addOnSuccessListener(a -> {
                    Toast.makeText(this, "Liked! ❤️", Toast.LENGTH_SHORT).show();
                    Bundle bundle = new Bundle();
                    bundle.putString("fact_id", currentFactId);
                    analytics.logEvent("fact_liked", bundle);
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void saveFact() {
        if (currentFactId.isEmpty()) {
            Toast.makeText(this, "No fact loaded yet", Toast.LENGTH_SHORT).show();
            return;
        }
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Map<String, Object> data = new HashMap<>();
        data.put("text", currentFactText);
        data.put("category", tvCategory.getText().toString().replace("🔬 ", ""));
        data.put("savedAt", FieldValue.serverTimestamp());
        db.collection("users").document(uid).collection("saved")
                .document(currentFactId).set(data)
                .addOnSuccessListener(a -> {
                    Toast.makeText(this, "Saved! 📌", Toast.LENGTH_SHORT).show();
                    Bundle bundle = new Bundle();
                    bundle.putString("fact_id", currentFactId);
                    analytics.logEvent("fact_saved", bundle);
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}