package janjimenezdavidjunquero.treballfinal.mobileapps.tecnocampus.cat.treballfinal.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import java.util.ArrayList;

import janjimenezdavidjunquero.treballfinal.mobileapps.tecnocampus.cat.treballfinal.R;
import janjimenezdavidjunquero.treballfinal.mobileapps.tecnocampus.cat.treballfinal.adapters.ReceptesRecyclerViewAdapter;
import janjimenezdavidjunquero.treballfinal.mobileapps.tecnocampus.cat.treballfinal.persistance.ReceptaBBDD;

public class MainActivity extends AppCompatActivity {
    private ReceptaBBDD receptaBBDD = ReceptaBBDD.getInstance();
    private ReceptesRecyclerViewAdapter adapter;
    private RecyclerView recView;
    private RecyclerView.LayoutManager lm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recView = findViewById(R.id.data_list);
        recView.setHasFixedSize(true);

        adapter = new ReceptesRecyclerViewAdapter(receptaBBDD, this);
        recView.setAdapter(adapter);

        if(getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
            lm = new LinearLayoutManager(this);
        } else {
            lm = new GridLayoutManager(this, 2);
        }
        recView.setLayoutManager(lm);

        recView.setClickable(true);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    public void viewRecepta(int position){
        Intent intent = new Intent(this, ReceptaActivity.class);
        intent.putExtra("position", position);
        startActivity(intent);
        finish();
    }
}
