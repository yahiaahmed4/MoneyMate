package com.example.moneymate4;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import android.view.View;
import android.widget.Toast;
import java.util.HashMap;
import java.util.Map;

public class accountinfo extends AppCompatActivity {

    EditText cardnumber, essentials, nonessentials;
    Button editprofile;

    ImageView logoutimg;
    TextView username, balance;

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accountinfo);

        CollectionReference collectionRef = FirebaseFirestore.getInstance().collection("users");
        username=findViewById(R.id.username2);
        balance=findViewById(R.id.balance);
        cardnumber=findViewById(R.id.cardnumber);
        essentials=findViewById(R.id.essentials);
        nonessentials=findViewById(R.id.nonessentials);
        editprofile = findViewById(R.id.editprofile);
        logoutimg = findViewById(R.id.logoutimg);

        BottomNavigationView bottomNavigationView=findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);
        bottomNavigationView.setOnItemSelectedListener(item -> {

            int id = item.getItemId();

            if (id == R.id.navigation_home) {
                return true;
            }   else if (id == R.id.navigation_addfavourite) {
                startActivity(new Intent(getApplicationContext(), addAccount.class));
                return true;
            }   else if (id == R.id.navigation_addpayment) {
                startActivity(new Intent(getApplicationContext(), add_payment.class));
                return true;
            }
            return false;
        });

        logoutimg.setOnClickListener(view -> {
            Intent intent=new Intent(this,userLogin.class);
            startActivity(intent);
            finish();
        });



        SharedPreferences sharedPreferences2 = getSharedPreferences("user_data", MODE_PRIVATE);

        SharedPreferences sharedPreferences4 = getSharedPreferences("user_balances", MODE_PRIVATE);

        String userName = sharedPreferences2.getString("name", "");
        String userCardnumber = sharedPreferences2.getString("cardnumber", "");
        String userPhone = sharedPreferences2.getString("phone", "");
        String userCvv = sharedPreferences2.getString("cvv", "");
        String userEmail = sharedPreferences2.getString("email", "");
        String userID = sharedPreferences2.getString("userId", "");


//        String balance4 = sharedPreferences4.getString("balance", "");
//        String essentials4 = sharedPreferences4.getString("essentials", "");
//        String nonessentials4 = sharedPreferences4.getString("nonessentials", "");


        String cardno_=addSpaces(userCardnumber);
// Set the retrieved data to the respective TextViews
        username.setText("Hi, "+ userName);
        cardnumber.setText(cardno_);



//        phone.setText(Phone);
//        salary.setText(Salary);
//        jobtype.setText(jobType);
//        userid.setText(userID);




        collectionRef.document(userID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                   @Override
                   public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                       if (task.isSuccessful()) {
                           DocumentSnapshot document = task.getResult();
                           if (document.exists()) {
                               // Get the value of the "balance" field
                               String balance4 = document.getString("balance");
                               String essentials4 = document.getString("essentials");
                               String nonessentials4 = document.getString("nonessentials");

                               if (balance4!=null && essentials4!=null && nonessentials4!=null) {
                                   balance.setText(balance4);
                                   essentials.setText(essentials4);
                                   nonessentials.setText(nonessentials4);
                               }
                           }
                       }
                   }
               });









        editprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String cardnumber_ = cardnumber.getText().toString();
                String cardnumber__=addSpaces(cardnumber_);
                String essentials_ = essentials.getText().toString();
                String nonessentials_ = nonessentials.getText().toString();
                Map<String, Object> data = new HashMap<>();
                String _cardnumber = "cardnumber";
                String _essentials = "essentials";
                String _nonessentials = "nonessentials";
                String _balance = "balance";

                data.put(_cardnumber, cardnumber__);
                data.put(_essentials, essentials_);
                data.put(_nonessentials, nonessentials_);

                String balanceString ="UNKNOWN";
                if (!essentials_.isEmpty() && !nonessentials_.isEmpty()) {

                    int essentialsInt = Integer.parseInt(essentials_);
                    int nonessentialsInt = Integer.parseInt(nonessentials_);
                    int balanceInt = essentialsInt + nonessentialsInt;
                    balanceString = String.valueOf(balanceInt);
                    data.put(_balance, balanceString);
                }
                else {
                    data.put(_balance, balanceString);
                }




                collectionRef.document(userID)
                        .update(data)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(accountinfo.this, "Profile Edited Successfully!", Toast.LENGTH_SHORT).show();

                                cardnumber.setText(cardnumber__);
                                essentials.setText(essentials_);
                                nonessentials.setText(nonessentials_);


                                if (!essentials_.isEmpty() && !nonessentials_.isEmpty()) {

                                    int essentialsIntX = Integer.parseInt(essentials_);
                                    int nonessentialsIntX = Integer.parseInt(nonessentials_);
                                    int balanceIntX = essentialsIntX + nonessentialsIntX;
                                    String balanceStringX = String.valueOf(balanceIntX);
                                    balance.setText(balanceStringX + "  EGP");
                                }
                                else {
                                    balance.setText("UNKNOWN  EGP");
                                }


                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(accountinfo.this, "Failed to edit profile", Toast.LENGTH_SHORT).show();
                            }
                        });



            }
        });






    }
}




