package com.demo.mindflash;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private EditText etName, etEmail, etPassword;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);

        Button btnRegister = findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }
            if (password.length() < 6) {
                Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(result -> {
                    String uid = result.getUser().getUid();
                    Map<String, Object> user = new HashMap<>();
                    user.put("name", name);
                    user.put("email", email);
                    db.collection("users").document(uid).set(user)
                        .addOnSuccessListener(a -> {
                            Toast.makeText(this, "Account created!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RegisterActivity.this, HomeActivity.class));
                            finish();
                        });
                })
                .addOnFailureListener(e ->
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show());
        });

        TextView tvLogin = findViewById(R.id.tvLogin);
        tvLogin.setOnClickListener(v -> finish());
    }
}
