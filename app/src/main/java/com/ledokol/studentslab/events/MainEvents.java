package com.ledokol.studentslab.events;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ledokol.studentslab.Event;
import com.ledokol.studentslab.R;

import java.util.ArrayList;
import java.util.List;

public class MainEvents extends Fragment {

    ArrayList<Event> states = new ArrayList<Event>();

    @Override
    // Переопределяем метод onCreateView
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_events, container, false);

        Toast.makeText(getActivity(),"MainEvents",Toast.LENGTH_SHORT).show();
        setInitialData();;
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.list);
        // создаем адаптер
        RecycleViewEventsMainView adapter = new RecycleViewEventsMainView(getActivity(), states);
        // устанавливаем для списка адаптер
        recyclerView.setAdapter(adapter);

        return view;
    }


    private void setInitialData(){

        states.add(new Event ("Бразилия", "Бразилиа", R.drawable.ic_launcher_background));
        states.add(new Event ("Аргентина", "Буэнос-Айрес", R.drawable.ic_launcher_background));
        states.add(new Event ("Колумбия", "Богота", R.drawable.ic_launcher_background));
        states.add(new Event ("Уругвай", "Монтевидео", R.drawable.ic_launcher_background));
        states.add(new Event("Чили", "Сантьяго", R.drawable.ic_launcher_background));
    }
}