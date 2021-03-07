package com.ledokol.studentslab.events;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ledokol.studentslab.Event;
import com.ledokol.studentslab.R;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainEvents extends Fragment {

    ArrayList<Event> events = new ArrayList<Event>();
    View view;
    FirebaseFirestore db;

    @Override
    // Переопределяем метод onCreateView
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main_events, container, false);

        db=FirebaseFirestore.getInstance();


        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        FirebaseFirestore db=FirebaseFirestore.getInstance();

        db.collection("Events")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int cnt=0;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                cnt++;
                                Map<String,Object> inf = document.getData();
                                setInitialData(inf,document.getId(),cnt==task.getResult().size());
                            }
                        }
                    }
                });

        return view;
    }


    public void createRecycle(){
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.list);
        // создаем адаптер
        RecycleViewEventsMainView adapter = new RecycleViewEventsMainView(getActivity(), events, false);
        // устанавливаем для списка адаптер
        recyclerView.setAdapter(adapter);
    }

    private void setInitialData(final Map<String,Object> inf,final String token, final Boolean check){

        DocumentReference docRef = db.collection("Account").document(inf.get("author").toString());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()) {
                        Map<String, Object> infUser = documentSnapshot.getData();
                        String name=infUser.get("name").toString();

                        ArrayList viewers=new ArrayList();
                        if(inf.get("viewers")!=null){
                            viewers=(ArrayList)inf.get("viewers");
                        }
                        if(inf.get("time_start") == null){
                            inf.put("time_start",Timestamp.now());
                        }
                        if(inf.get("time_end") == null){
                            inf.put("time_end",Timestamp.now());
                        }
                        Log.e("TIMESTAMP", String.valueOf(inf));
                        events.add(new Event(token, inf.get("title").toString(), inf.get("description").toString(), inf.get("author").toString(), name.toString(), inf.get("address").toString(), "10:00", viewers, R.drawable.ic_launcher_background,(Timestamp) inf.get("time_start"),(Timestamp) inf.get("time_end")));
                        if(check){
                            createRecycle();;
                        }
                    }
                }
            }
        });
    }


}