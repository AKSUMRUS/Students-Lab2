package com.ledokol.studentslab.events;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ledokol.studentslab.R;

public class MainEvents extends Fragment {

    @Override
    // Переопределяем метод onCreateView
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_events, container, false);

//        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.list);
//        // создаем адаптер
//        RecycleViewEventsMainView adapter = new RecycleViewEventsMainView(this, states);
//        // устанавливаем для списка адаптер
//        recyclerView.setAdapter(adapter);

        return view;
    }
}