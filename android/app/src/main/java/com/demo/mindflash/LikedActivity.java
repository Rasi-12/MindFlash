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

public class LikedActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liked);

        db = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("❤️ Liked Facts");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        loadLiked();
    }

    private void loadLiked() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        db.collection("users").document(uid).collection("liked")
                .orderBy("likedAt", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(snap -> {
                    List<Fact> list = new ArrayList<>();
                    for (DocumentSnapshot doc : snap.getDocuments()) {
                        Fact f = new Fact();
                        f.setId(doc.getId());
                        f.setText(doc.getString("text") != null ?
                                doc.getString("text") : "Fact #" + doc.getId());
                        f.setCategory(doc.getString("category") != null ?
                                doc.getString("category") : "General");
                        list.add(f);
                    }
                    if (list.isEmpty()) {
                        Toast.makeText(this, "No liked facts yet!", Toast.LENGTH_LONG).show();
                    }
                    recyclerView.setAdapter(new FactAdapter(list, null));
                });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
