package com.ledokol.studentslab.events;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.ledokol.studentslab.Event;
import com.ledokol.studentslab.R;

import java.util.List;

public class RecycleViewEventsMainView  extends RecyclerView.Adapter<RecycleViewEventsMainView.ViewHolder>{

    private final LayoutInflater inflater;
    private final List<Event> events;
    BottomSheetDialog bottomSheetDialog;
    LinearLayout bottomSheetView;
    Context context;

    RecycleViewEventsMainView(Context context, List<Event> states) {
        this.events = states;
        this.inflater = LayoutInflater.from(context);
        this.context=context;
    }
    @Override
    public RecycleViewEventsMainView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.event_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecycleViewEventsMainView.ViewHolder holder, int position) {
        final Event state = events.get(position);
//        holder.logoView.setImageResource(state.getLogo());
        holder.titleView.setText(state.getTitle());
        holder.teacherView.setText(state.getTeacherName());
        holder.classroomView.setText(state.getClassroom());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheet(state);;
            }
        });
    }

    public void showBottomSheet(Event state){
        bottomSheetView = (LinearLayout) LayoutInflater.from(this.context).inflate(R.layout.fragment_bottom_sheet_event, null);//Главные функции
        bottomSheetDialog = new BottomSheetDialog(this.context);
        bottomSheetDialog.setContentView(bottomSheetView);
        ViewGroup.LayoutParams layoutParams = bottomSheetView.getLayoutParams();


        ((TextView)bottomSheetView.findViewById(R.id.titleViewSheet)).setText(state.getTitle().toString());
        ((TextView)bottomSheetView.findViewById(R.id.textViewSheet)).setText(state.getText().toString());

        int windowHeight = getWindowHeight();
        if (layoutParams != null) {
            layoutParams.height = windowHeight;
        }
        bottomSheetView.setLayoutParams(layoutParams);
//        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetView);
//        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        bottomSheetDialog.show();
    }

    private int getWindowHeight() {
        // Calculate window height for fullscreen use
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity)this.context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleView,teacherView,classroomView;
        ImageView logoView;
        ViewHolder(View view){
            super(view);
            titleView=view.findViewById(R.id.titleEvent);
            teacherView=view.findViewById(R.id.teacherEvent);
            classroomView=view.findViewById(R.id.classroomEvent);
//            logoView=view.findViewById(R.id.logoEvent);
        }
    }
}