package janjimenezdavidjunquero.treballfinal.mobileapps.tecnocampus.cat.treballfinal.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.youtube.player.YouTubePlayerView;

import janjimenezdavidjunquero.treballfinal.mobileapps.tecnocampus.cat.treballfinal.R;
import janjimenezdavidjunquero.treballfinal.mobileapps.tecnocampus.cat.treballfinal.activities.MainActivity;
import janjimenezdavidjunquero.treballfinal.mobileapps.tecnocampus.cat.treballfinal.persistance.ReceptaBBDD;

/**
 * Created by Jan Jimenez on 16/04/2018.
 */

public class ReceptesRecyclerViewAdapter extends RecyclerView.Adapter<ReceptesRecyclerViewAdapter.ViewHolder> {
    private ReceptaBBDD receptaBBDD;
    private MainActivity mainActivity;

    public ReceptesRecyclerViewAdapter(ReceptaBBDD receptaBBDD, MainActivity mainActivity) {
        this.receptaBBDD = receptaBBDD;
        this.mainActivity = mainActivity;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public ImageView thumbnail;
        public TextView titol;
        public TextView duracio;
        public TextView descripcio;
        public TextView valoracio;
        private ItemClickListener clickListener;

        public ViewHolder(View itemView) {
            super(itemView);
            thumbnail = itemView.findViewById(R.id.image_thumbnail);
            titol = itemView.findViewById(R.id.textView_titol);
            duracio = itemView.findViewById(R.id.textView_duracio);
            descripcio = itemView.findViewById(R.id.textView_descripcio);
            valoracio = itemView.findViewById(R.id.textView_valoracio);

            itemView.setOnClickListener(this);
        }

        public void setClickListener(ItemClickListener itemClickListener) {
            this.clickListener = itemClickListener;
        }
        @Override
        public void onClick(View view) {
            clickListener.onClick(view, getPosition());
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_layout, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Glide.with(mainActivity.getBaseContext()).load(receptaBBDD.getRecepta(position).getThumbnail()).into(holder.thumbnail);
        holder.titol.setText(receptaBBDD.getRecepta(position).getTitle());
        holder.duracio.setText("Duración: "+receptaBBDD.getRecepta(position).getDuracioTotal()+" min.");
        holder.descripcio.setText(receptaBBDD.getRecepta(position).getDescripcio());
        holder.valoracio.setText(""+receptaBBDD.getRecepta(position).getValoracio()+"/5");

        holder.setClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                mainActivity.viewRecepta(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return receptaBBDD.getDataSize();
    }
}

