package com.ledokol.studentslab.events;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.ledokol.studentslab.Event;
import com.ledokol.studentslab.R;
import com.ledokol.studentslab.profile.ProfileActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RecycleViewEventsMainView  extends RecyclerView.Adapter<RecycleViewEventsMainView.ViewHolder>{

//    private final LayoutInflater inflater;
    private final List<Event> events;
    BottomSheetDialog bottomSheetDialog;
    View bottomSheetView;
    Context context;
    FirebaseFirestore db;
    FirebaseUser user;
    Boolean ItIsMyAccount;
    private static final String BACK_STACK_ROOT_TAG = "root_fragment";

    public RecycleViewEventsMainView(Context context, List<Event> states, Boolean ItIsMyAccount) {
        this.events = states;
        this.context=context;
        this.ItIsMyAccount=ItIsMyAccount;
        db=FirebaseFirestore.getInstance();
        FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
        user=firebaseAuth.getCurrentUser();
    }
    @Override
    public RecycleViewEventsMainView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_card, parent, false);
        return new RecycleViewEventsMainView.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecycleViewEventsMainView.ViewHolder holder, final int position) {
        final Event state = events.get(position);
        holder.titleView.setText(state.getTitle());
        holder.teacherView.setText(state.getTeacherName());
        holder.addressView.setText(state.getAddress());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheet(state,position);
            }
        });
    }

    public void showBottomSheet(final Event state, final int position){
        bottomSheetView = LayoutInflater.from(this.context).inflate(R.layout.fragment_bottom_sheet_event, null);//Главные функции
        bottomSheetDialog = new BottomSheetDialog(this.context,R.style.SheetDialog);
        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.getWindow().findViewById(R.id.linearLayoutBottomSheet).setBackgroundResource(android.R.color.transparent);

        final Button buttonSubscribe=((Button)bottomSheetDialog.findViewById(R.id.subscribe_me));

        final DocumentReference docRef = db.collection("Events").document(state.getToken());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()) {
                        Map<String, Object> inf= documentSnapshot.getData();

                        ArrayList subscribeUsers=new ArrayList();
                        if(inf.get("viewers")!=null){
                            subscribeUsers=(ArrayList)inf.get("viewers");
                        }

                        final Boolean subscribed;

                        if(subscribeUsers.contains(user.getUid().toString())){
                            subscribed=true;
                            buttonSubscribe.setBackgroundResource(R.drawable.rounded_button_did_subscribe);
                            buttonSubscribe.setText("Отписаться");
                            buttonSubscribe.setClickable(false);

                        }else{
                            subscribed=false;
                            buttonSubscribe.setBackgroundResource(R.drawable.rounded_button_subscribe);
                            buttonSubscribe.setText("Записаться");
                            buttonSubscribe.setClickable(true);
                        }

                        final TextView teacherName=((TextView)bottomSheetDialog.findViewById(R.id.teacherNameEventSheet));

                        ((TextView)bottomSheetDialog.findViewById(R.id.titleEventSheet)).setText(state.getTitle());
                        ((TextView)bottomSheetDialog.findViewById(R.id.timeEventSheet)).setText(state.getTime());
                        ((TextView)bottomSheetDialog.findViewById(R.id.addressEventSheet)).setText(state.getAddress());
                        teacherName.setText(state.getTeacherName());
                        ((TextView)bottomSheetDialog.findViewById(R.id.textViewSheet)).setText(state.getText());
                        bottomSheetDialog.show();

                        buttonSubscribe.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                updateEventViewers(state,subscribed);
                                updateMyEvents(state,subscribed,position);

                                bottomSheetDialog.hide();
                                bottomSheetDialog.dismiss();
                            }
                        });


                        teacherName.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                bottomSheetDialog.hide();
                                bottomSheetDialog.dismiss();
                                ((AppCompatActivity)context).getSupportFragmentManager().
                                        beginTransaction().
                                        replace(R.id.mainView, new ProfileActivity(state.getToken()))
                                        .addToBackStack(null)
                                        .commit();
                            }
                        });

                    }
                }
            }
        });
    }


    public void updateEventViewers(Event state, final Boolean subscribe){
        final DocumentReference docRef = db.collection("Events").document(state.getToken());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()) {
                        Map<String, Object> inf= documentSnapshot.getData();

                        ArrayList subscribeUsers=new ArrayList();
                        if(inf.get("viewers")!=null){
                            subscribeUsers=(ArrayList)inf.get("viewers");
                        }

                        if(!subscribe){
                            subscribeUsers.add(user.getUid().toString());
                        }else{
                            subscribeUsers.remove(user.getUid().toString());
                        }

                        inf.put("viewers",subscribeUsers);
                        docRef.set(inf, SetOptions.merge());
                    }
                }
            }
        });
    }

    public void updateMyEvents(final Event state, final Boolean subscribe, final int position){
        final DocumentReference docRef = db.collection("Account").document(user.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()) {
                        Map<String, Object> inf= documentSnapshot.getData();

                        ArrayList myEvents=new ArrayList();
                        if(inf.get("myEvents")!=null){
                            myEvents=(ArrayList)inf.get("myEvents");
                        }

                        if(!subscribe){
                            myEvents.add(state.getToken());
                        }else{
                            myEvents.remove(state.getToken());
                            if(ItIsMyAccount){
                                Log.e("positionRemove",String.valueOf(position));
                                events.remove(position);
                                notifyItemRemoved(position);
                                notifyDataSetChanged();

                            }
                        }

                        inf.put("myEvents",myEvents);
                        docRef.set(inf, SetOptions.merge());
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleView,teacherView,addressView;
        ImageView logoView;
        ViewHolder(View view){
            super(view);
            titleView=view.findViewById(R.id.titleEvent);
            teacherView=view.findViewById(R.id.teacherEvent);
            addressView=view.findViewById(R.id.classroomEvent);
        }
    }
}