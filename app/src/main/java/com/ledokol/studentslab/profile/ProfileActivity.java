package com.ledokol.studentslab.profile;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.ledokol.studentslab.Event;
import com.ledokol.studentslab.R;
import com.ledokol.studentslab.events.RecycleViewEventsMainView;
import com.ledokol.studentslab.registration.LoginActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProfileActivity extends Fragment {
    View view;
    FirebaseUser user;
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    TextView name,role,phone,email;
    String author_uid,name_text,role_text,phone_text,email_text;
    ArrayList<Event> events;
    ArrayList<String> tokenEvents;
    String userId;

    public ProfileActivity(String UserId){
        this.userId=UserId;
    }
    public ProfileActivity(){ }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_profile,container,false);

        name = view.findViewById(R.id.name);
        role = view.findViewById(R.id.role);
        phone = view.findViewById(R.id.contacts_phone);
        email = view.findViewById(R.id.contacts_email);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        if(userId==null){
            userId=user.getUid();
        }
//        author_uid = user.getUid();

        events=new ArrayList<>();
        tokenEvents=new ArrayList<>();
        downloadData();

        return view;
    }

    void downloadData(){
        DocumentReference docRef = db.collection("Account").document(userId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    Map<String,Object> m = task.getResult().getData();

                    if(m != null) {

                        if (m.get("name") != null) {
                            name_text = m.get("name").toString();
                        } else {
                            name_text = getString(R.string.unknown);
                        }
                        if (m.get("email") != null) {
                            email_text = m.get("email").toString();
                        } else {
                            email_text = getString(R.string.unknown);
                        }
                        if (m.get("phone") != null) {
                            phone_text = m.get("phone").toString();
                        } else {
                            phone_text = getString(R.string.unknown);
                        }
                        if (m.get("role") != null) {
                            role_text = m.get("role").toString();
                        } else {
                            role_text = getString(R.string.unknown);
                        }

                        if (m.get("myEvents") != null) {
                            tokenEvents = (ArrayList) m.get("myEvents");
                        }

                        setUI();
                    }
                }
            }
        });
    }

    void setUI(){
        email_text = getString(R.string.email_contacts) + " " +  email_text;
        phone_text = getString(R.string.phone) + " " + phone_text;
        name.setText(name_text);
        email.setText(email_text);
        role.setText(role_text);
        phone.setText(phone_text);
        if(tokenEvents.size() > 0) {
            downloadEvents(tokenEvents.size()-1);
            Log.e("EVENTS IN", "SIZE: " + tokenEvents.size());
        }
    }

    public void onBackPressed() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        Log.e("onBackPressed",String.valueOf(fm.getBackStackEntryCount()));
        if (fm.getBackStackEntryCount() > 0)
            fm.popBackStack();
        else
            getActivity().finish();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    void downloadEvents(final int pos){
        DocumentReference docRef = db.collection("Events").document(tokenEvents.get(pos).toString());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                Map<String,Object> m = task.getResult().getData();

                if(m != null) {
                    String title = m.get("title").toString();
                    String description = m.get("description").toString();
                    String address = m.get("address").toString();
                    List<String> viewers = new ArrayList<>();
                    if (m.get("viewers") != null) {
                        viewers = (ArrayList) m.get("viewers");
                    }

                    events.add(new Event(task.getResult().getId(), title, description, userId, "Иванов", address, "10:00 1 января", viewers, R.drawable.add_event));
                }

                    if (pos - 1 >= 0) {
                        downloadEvents(pos - 1);
                        Log.e("RECYCLER VIEW", "POS: " + pos);
                    } else {
                        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.listMyEventsLecture);
                        // создаем адаптер
                        RecycleViewEventsMainView adapter = new RecycleViewEventsMainView(getActivity(), events, user.getUid().toString().equals(userId));
                        // устанавливаем для списка адаптер
                        recyclerView.setAdapter(adapter);

                        Log.e("RECYCLER VIEW", "Done");
                    }
            }
        });
    }
}