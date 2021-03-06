package com.ledokol.studentslab.create;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.ledokol.studentslab.R;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.HashMap;

public class CreateEvent extends AppCompatActivity {

    EditText title,description,address;
    Button time,sendEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        title = findViewById(R.id.title_input);
        description = findViewById(R.id.description_input);
        address = findViewById(R.id.address_input);

        time = findViewById(R.id.time_input);
        sendEvent = findViewById(R.id.send_event_button);

        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        sendEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!title.getText().toString().isEmpty() && !description.getText().toString().isEmpty() && !address.getText().toString().isEmpty()) {
                    sendEvent();
                }
            }
        });

    }



    void sendEvent(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("title",title.getText().toString());
        hashMap.put("description",description.getText().toString());
        hashMap.put("address",address.getText().toString());
        db.collection("Events").add(hashMap);
        Log.e("SEND POST", "Sent");
    }
}