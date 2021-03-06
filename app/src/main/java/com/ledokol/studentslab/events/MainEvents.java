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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ledokol.studentslab.Event;
import com.ledokol.studentslab.R;

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
                                setInitialData(inf,cnt==task.getResult().size());
                            }
                        }
                    }
                });
        return view;
    }


    public void createRecycle(){
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.list);
        // создаем адаптер
        RecycleViewEventsMainView adapter = new RecycleViewEventsMainView(getActivity(), events);
        // устанавливаем для списка адаптер
        recyclerView.setAdapter(adapter);
    }

    private void setInitialData(final Map<String,Object> inf,final Boolean check){

        DocumentReference docRef = db.collection("Account").document(inf.get("author").toString());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()) {
                        Map<String, Object> infUser = documentSnapshot.getData();
                        String name=infUser.get("name").toString();
                        events.add(new Event(inf.get("title").toString(),inf.get("description").toString(),inf.get("author").toString(), name.toString(),inf.get("address").toString(),R.drawable.ic_launcher_background));
                        if(check){
                            createRecycle();;
                        }
                    }
                }
            }
        });

//        events.add(new Event ("Бразилия", "Бразилиа", R.drawable.ic_launcher_background));
//        events.add(new Event ("Аргентина", "Буэнос-Айрес", R.drawable.ic_launcher_background));
//        events.add(new Event ("Колумбия", "Богота", R.drawable.ic_launcher_background));
//        events.add(new Event ("Уругвай", "Монтевидео", R.drawable.ic_launcher_background));
//        events.add(new Event("Чили", "Сантьяго", R.drawable.ic_launcher_background));
    }
}