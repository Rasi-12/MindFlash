package com.demo.mindflash;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.demo.mindflash.adapter.FactAdapter;
import com.demo.mindflash.model.Fact;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.*;
import java.util.ArrayList;
import java.util.List;

public class SavedActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FirebaseFirestore db;
    private String uid;
    private List<Fact> factList = new ArrayList<>();
    private FactAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved);

        db = FirebaseFirestore.getInstance();
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("📌 Saved Facts");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        adapter = new FactAdapter(factList, this::deleteFact);
        recyclerView.setAdapter(adapter);

        loadSaved();
    }

    private void loadSaved() {
        db.collection("users").document(uid).collection("saved")
            .orderBy("savedAt", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener(snap -> {
                factList.clear();
                for (DocumentSnapshot doc : snap.getDocuments()) {
                    Fact f = new Fact();
                    f.setId(doc.getId());
                    f.setText(doc.getString("text"));
                    f.setCategory(doc.getString("category") != null ? doc.getString("category") : "General");
                    factList.add(f);
                }
                if (factList.isEmpty()) {
                    Toast.makeText(this, "No saved facts yet. Save some on the Home screen!", Toast.LENGTH_LONG).show();
                }
                adapter.notifyDataSetChanged();
            })
            .addOnFailureListener(e ->
                Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void deleteFact(Fact fact) {
        db.collection("users").document(uid).collection("saved")
            .document(fact.getId()).delete()
            .addOnSuccessListener(a -> {
                factList.remove(fact);
                adapter.notifyDataSetChanged();
                Toast.makeText(this, "Removed from saved", Toast.LENGTH_SHORT).show();
            });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
