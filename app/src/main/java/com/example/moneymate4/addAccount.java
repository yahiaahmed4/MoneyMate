package com.example.moneymate4;

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


public class addAccount extends AppCompatActivity {


    EditText cardnumber, namee;
    Button addfavorite;

    public static String addSpaces( String str ) {
        StringBuilder formatted = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            formatted.append(str.charAt(i));
            if ((i + 1) % 4 == 0 && i != str.length() - 1) {
                formatted.append(" ");
            }
        }
        return formatted.toString();
    }


        @Override
        protected void onCreate (Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_add_account);

            CollectionReference collectionRef = FirebaseFirestore.getInstance().collection("users");
            namee = findViewById(R.id.Name);
            cardnumber = findViewById(R.id.CardNo);
            addfavorite = findViewById(R.id.btnAddFav);

            BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
            bottomNavigationView.setSelectedItemId(R.id.navigation_home);
            bottomNavigationView.setOnItemSelectedListener(item -> {

                int id = item.getItemId();

                if (id == R.id.navigation_home) {
                    startActivity(new Intent(getApplicationContext(), accountinfo.class));
                    return true;
                } else if (id == R.id.navigation_addfavourite) {
                    return true;
                } else if (id == R.id.navigation_addpayment) {
                    startActivity(new Intent(getApplicationContext(), add_payment.class));
                    return true;
                }
                return false;
            });


            SharedPreferences sharedPreferences2 = getSharedPreferences("user_data", MODE_PRIVATE);
//            String userName = sharedPreferences2.getString("name", "");
//            String userCardnumber = sharedPreferences2.getString("cardnumber", "");
//            String userPhone = sharedPreferences2.getString("phone", "");
//            String userCvv = sharedPreferences2.getString("cvv", "");
//            String userEmail = sharedPreferences2.getString("email", "");
            String userID = sharedPreferences2.getString("userId", "");






            addfavorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String cardnumber_ = cardnumber.getText().toString();
                    String name_ = namee.getText().toString();
                    String cardnumber__=addSpaces(cardnumber_);

                    Map<String, Object> data = new HashMap<>();
                    String _name = "favourites"+"."+name_;
                    data.put(_name, cardnumber__);


                    collectionRef.document(userID)
                            .update(data)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(addAccount.this, "Favourite Added Successfully!", Toast.LENGTH_SHORT).show();

                                    namee.setText("");
                                    cardnumber.setText("");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(addAccount.this, "Failed to add favourite", Toast.LENGTH_SHORT).show();
                                }
                            });



                }
            });





        }
    }
