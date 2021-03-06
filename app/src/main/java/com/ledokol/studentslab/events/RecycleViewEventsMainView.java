package com.ledokol.studentslab.events;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ledokol.studentslab.R;

import java.util.List;

public class RecycleViewEventsMainView  extends RecyclerView.Adapter<RecycleViewEventsMainView.ViewHolder>{

    private final LayoutInflater inflater;
    private final List<String> states;

    RecycleViewEventsMainView(Context context, List<String> states) {
        this.states = states;
        this.inflater = LayoutInflater.from(context);
    }
    @Override
    public RecycleViewEventsMainView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.event_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecycleViewEventsMainView.ViewHolder holder, int position) {
        String state = states.get(position);
//        holder.flagView.setImageResource(state.getFlagResource());
//        holder.nameView.setText(state.getName());
//        holder.capitalView.setText(state.getCapital());
        holder.titleView.setText(state);
    }

    @Override
    public int getItemCount() {
        return states.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
//        ImageView flagView;
//        TextView nameView, capitalView;
        TextView titleView;
        ViewHolder(View view){
            super(view);
            titleView=view.findViewById(R.id.nameEvents);
//            flagView = (ImageView)view.findViewById(R.id.flag);
//            nameView = (TextView) view.findViewById(R.id.name);
//            capitalView = (TextView) view.findViewById(R.id.capital);
        }
    }
}