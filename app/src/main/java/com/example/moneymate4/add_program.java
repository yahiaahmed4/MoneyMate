package com.example.moneymate4;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.os.Bundle;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import android.view.View;
import android.widget.Toast;
import java.util.HashMap;
import java.util.Map;



public class add_program extends AppCompatActivity {

    EditText balance, essentials, nonessentials;
    Button addprogram;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_program);

        CollectionReference collectionRef = FirebaseFirestore.getInstance().collection("users");


        SharedPreferences sharedPreferences4 = getSharedPreferences("user_balances", MODE_PRIVATE);
        SharedPreferences.Editor editor4 = sharedPreferences4.edit();

        balance=findViewById(R.id.balanc);
        essentials=findViewById(R.id.essent);
        nonessentials=findViewById(R.id.nonessent);
        addprogram=findViewById(R.id.btnAddProg);

        SharedPreferences sharedPreferences3 = getSharedPreferences("user_id", MODE_PRIVATE);
        String userID = sharedPreferences3.getString("userId", "");




        addprogram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String essentials_1 = essentials.getText().toString();
                String nonessentials_1 = nonessentials.getText().toString();
                String balance_1= balance.getText().toString();
                Map<String, Object> data4 = new HashMap<>();
                String _essentials = "essentials";
                String _nonessentials = "nonessentials";
                String _balance = "balance";


                if (!essentials_1.isEmpty() && !nonessentials_1.isEmpty() && !balance_1.isEmpty()) {

                    data4.put(_essentials, essentials_1);
                    data4.put(_nonessentials, nonessentials_1);
                    data4.put(_balance, balance_1);
                }
                else {
                    data4.put(_essentials, "NONE");
                    data4.put(_nonessentials, "NONE");
                    data4.put(_balance, "NONE");
                }






                collectionRef.document(userID)
                        .update(data4)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(add_program.this, "Program Added Successfully!", Toast.LENGTH_SHORT).show();

                                editor4.putString("essentials", essentials_1);
                                editor4.putString("nonessentials", nonessentials_1);
                                editor4.putString("balance", balance_1);
                                editor4.apply();
                                startActivity(new Intent(add_program.this, userLogin.class));

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(add_program.this, "Failed to add program", Toast.LENGTH_SHORT).show();
                            }
                        });



            }
        });

    }
}