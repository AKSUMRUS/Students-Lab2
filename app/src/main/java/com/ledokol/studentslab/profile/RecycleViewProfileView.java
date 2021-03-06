package com.ledokol.studentslab.profile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.ledokol.studentslab.Event;
import com.ledokol.studentslab.R;

import java.util.List;

public class RecycleViewProfileView extends RecyclerView.Adapter<RecycleViewProfileView.ViewHolder>{

    private final LayoutInflater inflater;
    private final List<Event> events;
    BottomSheetDialog bottomSheetDialog;
    View bottomSheetView;
    Context context;

    RecycleViewProfileView(Context context, List<Event> states) {
        this.events = states;
        this.inflater = LayoutInflater.from(context);
        this.context=context;
    }
    @Override
    public RecycleViewProfileView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.event_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecycleViewProfileView.ViewHolder holder, int position) {
        Event state = events.get(position);
        holder.logoView.setImageResource(state.getLogo());
        holder.titleView.setText(state.getTitle());
        holder.textView.setText(state.getText());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheet();;
            }
        });
    }

    public void showBottomSheet(){
        bottomSheetView = LayoutInflater.from(this.context).inflate(R.layout.fragment_bottom_sheet_event, null);//Главные функции
        bottomSheetDialog = new BottomSheetDialog(this.context);
        bottomSheetDialog.setContentView(bottomSheetView);
        Toast.makeText(this.context,"BottomSheetDialog",Toast.LENGTH_SHORT).show();;
        bottomSheetDialog.show();
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleView,textView;
        ImageView logoView;
        ViewHolder(View view){
            super(view);
            titleView=view.findViewById(R.id.titleEvent);
            textView=view.findViewById(R.id.textEvent);
            logoView=view.findViewById(R.id.logoEvent);
        }
    }
}