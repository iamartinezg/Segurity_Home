package com.example.proyecto.servicios;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.JobIntentService;
import androidx.core.app.NotificationCompat;

import com.example.proyecto.R;
import com.example.proyecto.activities.ChatFamiliar;
import com.example.proyecto.modelos.Mensaje;
import com.example.proyecto.modelos.DatabaseRoutes;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

public class NotificationService extends JobIntentService {
    private long lastNotificationTime = new Date().getTime();
    private final static int JOB_ID = 10;
    private int NOTIFICATION_ID = 1;
    NotificationChannel channel;
    NotificationManager manager;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mReference = database.getReference(DatabaseRoutes.MESSAGES_PATH);

    public static void enqueueWork(Context context, Intent intent) {
        enqueueWork(context, NotificationService.class, JOB_ID, intent);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            channel = new NotificationChannel("default", "Nuevos mensajes", NotificationManager.IMPORTANCE_HIGH);
            manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        mReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Mensaje message = snapshot.getValue(Mensaje.class);
                if (message.getStamp() > lastNotificationTime && !message.getFrom().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())){
                    Log.d("NOT", "onChildAdded: NEW MESSAGE");
                    Intent intent = new Intent(getApplicationContext(), ChatFamiliar.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_IMMUTABLE);

                    NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "default")
                            .setSmallIcon(R.drawable.logo_securityhome_background)
                            .setContentTitle(String.format("%s dice:", message.getFrom()))
                            .setContentText(message.getMessage())
                            .setAutoCancel(true)
                            .setContentIntent(pendingIntent);

                    manager.notify(NOTIFICATION_ID++, builder.build());
                    lastNotificationTime = message.getStamp();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
