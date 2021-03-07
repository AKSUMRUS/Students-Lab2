package com.ledokol.studentslab.create;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.ledokol.studentslab.R;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class CreateEvent extends Fragment {

    View view;
    EditText title,description,address;
    Button time,sendEvent;
    FirebaseUser user;
    int mMinute,mHour,mYear,mMonth, mDay;
    String date_time;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_create_event, container, false);
        user = FirebaseAuth.getInstance().getCurrentUser();

        title = view.findViewById(R.id.title_input);
        description = view.findViewById(R.id.description_input);
        address = view.findViewById(R.id.address_input);

        final Button startTime = view.findViewById(R.id.time_start_lesson);
        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final java.util.Calendar c = java.util.Calendar.getInstance();
                mYear = c.get(java.util.Calendar.YEAR);
                mMonth = c.get(java.util.Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),R.style.AlertDatePickerDialogTheme,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                                date_time = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                                //*************Call Time Picker Here ********************
                                tiemPicker(startTime);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });


        sendEvent = view.findViewById(R.id.send_event_button);

        sendEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!title.getText().toString().isEmpty() && !description.getText().toString().isEmpty() && !address.getText().toString().isEmpty()) {
                    sendEvent();
                }
            }
        });
        return view;
    }

    private void tiemPicker(final Button time){
        // Get Current Time
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),R.style.AlertDatePickerDialogTheme,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,int minute) {

                        mHour = hourOfDay;
                        mMinute = minute;

                        time.setText(date_time+" "+hourOfDay + ":" + minute);
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }




    void sendEvent(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("title",title.getText().toString());
        hashMap.put("description",description.getText().toString());
        hashMap.put("address",address.getText().toString());
        hashMap.put("author",user.getUid());
        final String id = db.collection("Events").document().getId();
        db.collection("Events").document(id).set(hashMap);
        final DocumentReference docRef = db.collection("Account").document(user.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                Map<String,Object> m = task.getResult().getData();
                ArrayList arrayList = (ArrayList) m.get("myEvents");
                arrayList.add(id);
                m.put("myEvents",arrayList);
                docRef.set(m,SetOptions.merge());
                Toast.makeText(getContext(),"New event has sent",Toast.LENGTH_LONG).show();
            }
        });
        Log.e("SEND POST", "Sent");
    }


}