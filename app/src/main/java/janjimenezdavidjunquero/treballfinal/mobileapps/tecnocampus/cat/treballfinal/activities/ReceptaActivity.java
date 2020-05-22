package janjimenezdavidjunquero.treballfinal.mobileapps.tecnocampus.cat.treballfinal.activities;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import janjimenezdavidjunquero.treballfinal.mobileapps.tecnocampus.cat.treballfinal.R;
import janjimenezdavidjunquero.treballfinal.mobileapps.tecnocampus.cat.treballfinal.adapters.PasosRecyclerViewAdapter;
import janjimenezdavidjunquero.treballfinal.mobileapps.tecnocampus.cat.treballfinal.adapters.ReceptesRecyclerViewAdapter;
import janjimenezdavidjunquero.treballfinal.mobileapps.tecnocampus.cat.treballfinal.domain.Recepta;
import janjimenezdavidjunquero.treballfinal.mobileapps.tecnocampus.cat.treballfinal.persistance.ReceptaBBDD;

public class ReceptaActivity extends AppCompatActivity {
    private ImageView thumbnail;
    private TextView titolText;
    private TextView duracioText;
    private TextView descripcioText;
    private TextView valoracioText;
    private ReceptaBBDD receptaBBDD = ReceptaBBDD.getInstance();
    private Recepta recepta;

    private TextView ingredientsText;

    private PasosRecyclerViewAdapter adapter;
    private RecyclerView recView;
    private RecyclerView.LayoutManager lm;

    private RatingBar ratingBar;
    private Button submitRating;
    private ImageButton shareButton;

    private static final String CHANNEL_ID = "123456";
    private static final int NOTIFICATION_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recepta_layout);

        titolText = findViewById(R.id.textView_titol_recepta);
        duracioText = findViewById(R.id.textView_duracio_recepta);
        descripcioText = findViewById(R.id.textView_descripcio_recepta);
        thumbnail = findViewById(R.id.image_thumbnail_recepta);
        valoracioText = findViewById(R.id.textView_valoracio_recepta);
        ingredientsText = findViewById(R.id.textView_ingredients);
        ratingBar = findViewById(R.id.ratingBar);
        submitRating = findViewById(R.id.button_submit_valoracion);
        shareButton = findViewById(R.id.button_share);

        Intent intent = getIntent();
        int positionRecepta = intent.getIntExtra("position", 0);
        recepta = receptaBBDD.getRecepta(positionRecepta);

        Glide.with(this).load(recepta.getThumbnail()).into(thumbnail);
        titolText.setText(recepta.getTitle());
        duracioText.setText("Duración: "+recepta.getDuracioTotal()+" min.");
        descripcioText.setText(recepta.getDescripcio());
        valoracioText.setText(""+recepta.getValoracio()+"/5");

        // Set ingredients
        ArrayList<String> ingredients = recepta.getIngredients();
        String ingredientsString = ingredients.get(0);
        for(int i=1; i<ingredients.size(); i++){
            ingredientsString += "\n- "+ingredients.get(i);
        }
        ingredientsText.setText(ingredientsString);

        initPasos();

        initRating();

        createNotificationChannel();

        compartirReceta();
    }

    private void initPasos() {
        recView = findViewById(R.id.pasos_list);
        recView.setHasFixedSize(true);

        adapter = new PasosRecyclerViewAdapter(recepta.getPassos(), this);
        recView.setAdapter(adapter);

        if(getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
            lm = new LinearLayoutManager(this);
        } else {
            lm = new GridLayoutManager(this, 2);
        }
        recView.setLayoutManager(lm);
    }

    private void initRating() {
        submitRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recepta.setValoracio(ratingBar.getRating());
                ratingBar.setEnabled(false);
            }
        });
    }

    private void compartirReceta() {
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentShare = new Intent(Intent.ACTION_SEND);
                intentShare.setType("text/plain");
                String shareBody = recepta.getDescripcio()
                        +"\n\n · Duración "+recepta.getDuracioTotal()+" minutos."
                        +"\n · Valoración "+recepta.getValoracio()+"/5";
                String shareSub = "He completado la receta:"+recepta.getTitle();
                intentShare.putExtra(Intent.EXTRA_SUBJECT, shareSub);
                intentShare.putExtra(Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(intentShare, "Compartir usando"));
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void sendNotification(String message) {
        Intent intent= new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID);
        notificationBuilder.setSmallIcon(R.mipmap.ic_launcher)
                .setContentText("Test Project")
                .setContentText(message)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());
    }

    private void createNotificationChannel(){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            String tag="Default";
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, tag, NotificationManager.IMPORTANCE_DEFAULT);

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }
}
