package com.ledokol.studentslab.create;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.ledokol.studentslab.R;
import com.ledokol.studentslab.events.MainEvents;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateEvent extends Fragment {

    ArrayAdapter<String> adapter;

    View view;
    EditText title,description,address;
    TextInputLayout typeInput;
    AutoCompleteTextView type;
    Button time,sendEvent;
    FirebaseUser user;
    int mMinuteStart, mHourStart, mYearStart, mMonthStart, mDayStart;
    int mMinuteEnd,mHourEnd,mYearEnd,mMonthEnd, mDayEnd;
    String date_time;
    Calendar timeStart,timeEnd;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_create_event, container, false);
        user = FirebaseAuth.getInstance().getCurrentUser();

        title = view.findViewById(R.id.title_input);
        description = view.findViewById(R.id.description_input);
        address = view.findViewById(R.id.address_input);

        typeInput = view.findViewById(R.id.textField5);
        type = view.findViewById(R.id.type_input);

        final String[] typeList = getResources().getStringArray(R.array.type_events);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),R.layout.list_item,typeList);

        type.setText(typeList[0]);

        type.setAdapter(adapter);

        final Button startTime = view.findViewById(R.id.time_start_lesson);
        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar currentDate = Calendar.getInstance();
                timeStart = Calendar.getInstance();
                new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        timeStart.set(year, monthOfYear, dayOfMonth);
                        new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                timeStart.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                timeStart.set(Calendar.MINUTE, minute);
                                Log.v("Time", "The choosen one " + timeStart.getTime());
                            }
                        }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), false).show();
                    }
                }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE)).show();
            }
        });

        final Button endTime = view.findViewById(R.id.time_end_lesson);
        endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar currentDate = Calendar.getInstance();
                timeEnd = Calendar.getInstance();
                new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        timeEnd.set(year, monthOfYear, dayOfMonth);
                        new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                timeEnd.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                timeEnd.set(Calendar.MINUTE, minute);
                                Log.v("Time", "The choosen one " + timeEnd.getTime());
                            }
                        }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), false).show();
                    }
                }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE)).show();
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
        mHourStart = c.get(Calendar.HOUR_OF_DAY);
        mMinuteStart = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),R.style.AlertDatePickerDialogTheme,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,int minute) {

                        mHourStart = hourOfDay;
                        mMinuteStart = minute;

                        time.setText(date_time+" "+hourOfDay + ":" + minute);
                    }
                }, mHourStart, mMinuteStart, false);
        timePickerDialog.show();
    }


    void sendEvent(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("title",title.getText().toString());
        hashMap.put("description",description.getText().toString());
        hashMap.put("address",address.getText().toString());
        hashMap.put("author",user.getUid());
        hashMap.put("type",type.getText().toString());
        Timestamp timestampStart = new Timestamp(timeStart.getTime());
        hashMap.put("time_start",timestampStart);
        Timestamp timestampEnd = new Timestamp(timeEnd.getTime());
        hashMap.put("time_end",timestampEnd);

        // LOG

//        timestampEnd.toDate().getHours()


//        Timestamp timestampStart = new Timestamp(mYearStart,mMonthStart,mDayStart,mHourStart,mMinuteStart,0,0);
//        Timestamp timestampStart= new Timestamp(mYearStart,mMonthStart,mDayStart,mHourStart,mMinuteStart);
//        hashMap.put("time_start",timestampStart);
//        Timestamp timestampEnd = Timestamp.now();
//        hashMap.put("time_end",timestampEnd);
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
                Fragment fragment = new MainEvents();
                loadFragment(fragment);
                Toast.makeText(getContext(),"New event has sent",Toast.LENGTH_LONG).show();
            }
        });
        Log.e("SEND POST", "Sent");
    }


    private void loadFragment(Fragment fragment) {
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fl_content, fragment);
        ft.commit();
    }

}