package com.example.moneymate4;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class userRegister extends AppCompatActivity {


    EditText email_text,pass_text,confirm_pass_text,name,phone,username,cvv,cardnumber;
    Button register_button;
    TextView gotologin;

    //create attribute name firebase

    //initialise prograss bar


    ProgressBar progressBar_reg;
    //database
    private FirebaseAuth fAuth;
    private FirebaseFirestore db;
    private CollectionReference usersCollectionRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);
        fAuth =FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        usersCollectionRef = db.collection("users");
        name=findViewById(R.id.name);
        phone=findViewById(R.id.phone);
        cvv=findViewById(R.id.cvv);
        email_text = findViewById(R.id.email_reg_pag);
        pass_text = findViewById(R.id.pass_register);
        confirm_pass_text = findViewById(R.id.confirmpass);
        register_button = findViewById(R.id.registerbutton);
        username=findViewById(R.id.username);
        cardnumber=findViewById(R.id.cardnumber);
        gotologin=findViewById(R.id.gotologin);


        SharedPreferences sharedPreferences3 = getSharedPreferences("user_id", MODE_PRIVATE);
        SharedPreferences.Editor editor3 = sharedPreferences3.edit();

        gotologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(userRegister.this, userLogin.class);
                startActivity(intent);
                finish();
            }
        });
        //progressBar_reg = findViewById(R.id.prograssbar_reg);


        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String name_ = name.getText().toString();
                String phone_ = phone.getText().toString();
                String email_ = email_text.getText().toString();
                String pass_ = pass_text.getText().toString();
                String con_pass_ = confirm_pass_text.getText().toString();
                String cvv_=cvv.getText().toString();
                String username_=username.getText().toString();
                String cardnumber_=cardnumber.getText().toString();
                User user = new User(name_, phone_, email_, pass_,cvv_,username_,cardnumber_);

                if (name_.isEmpty()) {
                    name.setError("name cannot be empty");

                }
                if (phone_.isEmpty()) {
                    phone.setError("phone cannot be empty");

                }
                if (email_.isEmpty()) {
                    email_text.setError("email cannot be empty");

                }
                if (pass_.isEmpty()) {
                    pass_text.setError("password cannot be empty");

                }
                if (con_pass_.isEmpty()) {
                    confirm_pass_text.setError("password cannot be empty");

                }
                if(!pass_.equals(con_pass_)){
                    pass_text.setError("Not matched with Password");
                }
                if (cvv_.isEmpty()) {
                    name.setError("name cannot be empty");
                }
                if (username_.isEmpty()) {
                    name.setError("username cannot be empty");
                }
                if (cardnumber_.isEmpty()) {
                    name.setError("card number cannot be empty");
                }
                else {

                    usersCollectionRef.add(user)
                            .addOnSuccessListener(documentReference -> {
                                // User added successfully
                                String addedUSERId = documentReference.getId();

                                editor3.putString("userId", addedUSERId);

                                editor3.apply();

                                Toast.makeText(userRegister.this, "User Successfully Added", Toast.LENGTH_SHORT).show();

                                // Perform any additional actions after the user is added
                            })
                            .addOnFailureListener(e -> {
                                // An error occurred while adding the user
                                // Handle the error appropriately
                                Toast.makeText(userRegister.this, "An error occurred. Please try again later.", Toast.LENGTH_SHORT).show();
                            });


                    fAuth.createUserWithEmailAndPassword(email_, pass_)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(userRegister.this, "successful register", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(userRegister.this, add_program.class));
                                    } else {
                                        Toast.makeText(userRegister.this, "failed to register" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });


                }
                // Intent intent = new Intent(getApplicationContext(),Login.class);
                //startActivity (intent);
                //finish();

            }
        });






    }
}