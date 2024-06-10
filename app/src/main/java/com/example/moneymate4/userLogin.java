package com.example.moneymate4;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.regex.Pattern;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class userLogin extends AppCompatActivity {


    EditText email_login_page, pass_login_page;
    TextView gotoregister;
    TextView button_login;

    private FirebaseAuth fAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);


        email_login_page = findViewById(R.id.email_login);
        pass_login_page = findViewById(R.id.pass_login);
        button_login = findViewById(R.id.button_login_page);
        gotoregister = findViewById(R.id.gotoregister);

        fAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        SharedPreferences sharedPreferences = getSharedPreferences("userDataa", MODE_PRIVATE);
        SharedPreferences.Editor editorr = sharedPreferences.edit();

        SharedPreferences sharedPreferences2 = getSharedPreferences("user_data", MODE_PRIVATE);
        SharedPreferences.Editor editorr2 = sharedPreferences2.edit();


        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email_ = email_login_page.getText().toString();
                String pass_ = pass_login_page.getText().toString();

                if (TextUtils.isEmpty(email_)) {
                    Toast.makeText(userLogin.this, "Enter email", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(pass_)) {
                    Toast.makeText(userLogin.this, "Enter password", Toast.LENGTH_SHORT).show();
                    return;
                }

                fAuth.signInWithEmailAndPassword(email_, pass_).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Authentication successful

                            db.collection("users")
                                    .whereEqualTo("email", email_)
                                    .whereEqualTo("password", pass_)
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                QuerySnapshot querySnapshot = task.getResult();
                                                if (!querySnapshot.isEmpty()) {
                                                    // User with the given email and password exists in Firestore
                                                    for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                                                        String userId = document.getId();
                                                        String userEmail = document.getString("email");
                                                        String userPhone = document.getString("phone");
                                                        String userName = document.getString("name");
                                                        String userCardnumber = document.getString("cardnumber");
                                                        String userCvv = document.getString("cvv");


                                                        // Store the user data in shared preferences
                                                        //SharedPreferences.Editor editor = sharedPreferences.edit();
                                                        if (userName != null && userEmail != null && userPhone != null && userCardnumber != null && userCvv != null && userId != null) {
                                                            editorr2.putString("name", userName);
                                                            editorr2.putString("email", userEmail);
                                                            editorr2.putString("phone", userPhone);
                                                            editorr2.putString("cardnumber", userCardnumber);
                                                            editorr2.putString("cvv", userCvv);
                                                            editorr2.putString("userId", userId);

                                                            editorr2.apply();
                                                            //editorr.apply();



                                                            Toast.makeText(userLogin.this, "Login successful", Toast.LENGTH_SHORT).show();

                                                            // Start the new activity
                                                            Intent intent = new Intent(userLogin.this, accountinfo.class);
                                                            startActivity(intent);
                                                            finish();
                                                        }




                                                    }
                                                }else {
                                                    Toast.makeText(userLogin.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                                                }
                                            } else {
                                                Toast.makeText(userLogin.this, "Error occurred", Toast.LENGTH_SHORT).show();
                                                Log.e("Firestore Error", task.getException().getMessage());
                                            }
                                        }
                                    });
                        } else {
                            Toast.makeText(userLogin.this, "Login failed", Toast.LENGTH_SHORT).show();
                            Log.e("Authentication Error", task.getException().getMessage());
                        }
                    }
                });
            }
        });

        gotoregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(userLogin.this, userRegister.class);
                startActivity(intent);
                finish();
            }
        });




           /*     if (!email_.isEmpty()&& Patterns.EMAIL_ADDRESS.matcher(email_).matches())
                {
                    if(!pass_.isEmpty())
                    {
                    fAuth.signInWithEmailAndPassword(email_,pass_)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task)
                                {
                                    Toast.makeText(Login.this, "login successful" , Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(Login.this,Hotel_Home.class));
                                    finish();
                                }
                            })  .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                    Toast.makeText(Login.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                                }
                            });


                }
                    else{
                        pass_login_page.setError("password cannot be empty");

                    }
                }   else if (email_.isEmpty()){
                    email_login_page.setError("email cannot be empty");
                }else{
                    email_login_page.setError("please enter valid email");
                }

             */




//        gotoregister.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(userLogin.this, userRegister.class);
//                startActivity(intent);
//                finish();
//
//            }
//        });

    }

}