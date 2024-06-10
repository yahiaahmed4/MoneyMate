package com.example.moneymate4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class info extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference guestsCollectionRef = db.collection("categoryname");

    private RecyclerView recyclerViewMoney;
    private moneyAdapter adapter;
    private ListenerRegistration listenerRegistration;

    // Create the data list
    private List<moneyData> moneyDataList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

//        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
//        bottomNavigationView.setSelectedItemId(R.id.navigation_guestinfo);
//        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
//
//            int id = item.getItemId();
//
//            if (id == R.id.navigation_guestinfo) {
//                return true;
//            } else if (id == R.id.navigation_home) {
//                finish();
//                return true;
//            }
//            return false;
//        });

        // Initialize the RecyclerView
        recyclerViewMoney = findViewById(R.id.recyclerView);
        recyclerViewMoney.setLayoutManager(new LinearLayoutManager(this));
        // Create the adapter and pass the data list
        adapter = new moneyAdapter(moneyDataList);
        recyclerViewMoney.setAdapter(adapter);

        // Fetch the user data from Firestore
        fetchUserData();

        // Listen for real-time updates
        listenForUpdates();
    }

    private void fetchUserData() {
        guestsCollectionRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                moneyDataList.clear(); // Clear the existing data list

                for (QueryDocumentSnapshot document : task.getResult()) {
                    // Retrieve the desired fields from the document
                    String type = document.getString("type");
                    String name = document.getString("name");
                    String amount = document.getString("amount");


                    // Create a GuestData object with the retrieved values
                    moneyData moneyData = new moneyData(type, name, amount);
                    moneyDataList.add(moneyData);
                }

                // Notify the adapter that the data set has changed
                adapter.notifyDataSetChanged();
            } else {
                Toast.makeText(info.this, "An error occurred. Please try again later.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void listenForUpdates() {
        listenerRegistration = guestsCollectionRef.addSnapshotListener((value, error) -> {
            if (error != null) {
                Toast.makeText(info.this, "Error occurred while listening for updates.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (value != null) {
                fetchUserData();
            }
        });
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Unregister the listener to avoid memory leaks
        if (listenerRegistration != null) {
            listenerRegistration.remove();
        }
    }

    private class moneyAdapter extends RecyclerView.Adapter<moneyAdapter.ViewHolder> {

        private List<moneyData> moneyDataList;

        public moneyAdapter(List<moneyData> moneyDataList) {
            this.moneyDataList = moneyDataList;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.moneylist, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            if (moneyDataList != null && !moneyDataList.isEmpty()) {
                moneyData guestData = moneyDataList.get(position);
                holder.bind(guestData);

            }
        }

        @Override
        public int getItemCount() {
            return moneyDataList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView amount;
            TextView name;
            TextView type;

            public ViewHolder(View itemView) {
                super(itemView);
                type = itemView.findViewById(R.id.Type);
                name = itemView.findViewById(R.id.Name);
                amount = itemView.findViewById(R.id.Amount);
            }

            public void bind(moneyData moneyData) {
                type.setText("Type: " + moneyData.getType());
                name.setText("Name: " + moneyData.getName());
                amount.setText("Amount: " + moneyData.getAmount());
            }
        }
    }
}