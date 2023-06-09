package com.example.proyecto.adapters;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.proyecto.R;
import com.example.proyecto.databinding.AdapterMensajeBinding;
import com.example.proyecto.modelos.Mensaje;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Objects;

public class MensajeAdapter extends RecyclerView.Adapter<MensajeAdapter.ViewHolder> {
    private final List<Mensaje> messages;
    public static final String TAG = MensajeAdapter.class.getName();
    private final static String MESSAGE_DATE_FORMAT = "dd MMMM yyyy h:ma";
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(MESSAGE_DATE_FORMAT);
    Context context;


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_mensaje, parent, false);
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //Grab the person to render
        Mensaje message = messages.get(position);
        //Format and Set the values in the view
        holder.binding.messageUser.setText(String.format("%s says:", message.getFrom()));
        holder.binding.messageContent.setText(message.getMessage());
        if(message.isHasImage()) {
            holder.binding.messageImage.setVisibility(View.VISIBLE);
            Glide.with(holder.binding.messageImage.getContext())
                    .load(message.getImagePath())
                    .fitCenter()
                    .transition(withCrossFade())
                    .into(holder.binding.messageImage);
        } else holder.binding.messageImage.setVisibility(View.GONE);
        holder.binding.messageDate.setText(simpleDateFormat.format(message.getStamp()));
        if (Objects.equals(FirebaseAuth.getInstance().getCurrentUser().getEmail(), message.getFrom())) {
            holder.binding.materialCard.setCardBackgroundColor(context.getColor(R.color.verde));
            holder.binding.materialCard.setStrokeColor(context.getColor(R.color.verde));
        } else {
            holder.binding.materialCard.setCardBackgroundColor(context.getColor(R.color.light_blue_400));;
            holder.binding.materialCard.setStrokeColor(context.getColor(R.color.light_blue_400));
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        AdapterMensajeBinding binding;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = AdapterMensajeBinding.bind(itemView);
        }
    }

    public MensajeAdapter(List<Mensaje> positionList) {
        this.messages = positionList;
    }
}
