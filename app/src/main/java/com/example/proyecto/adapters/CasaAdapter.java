package com.example.proyecto.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.proyecto.R;
import com.example.proyecto.modelos.Casa;

import java.util.ArrayList;

import lombok.NonNull;

public class CasaAdapter extends RecyclerView.Adapter <CasaAdapter.ViewHolder> {

    private int resource;
    private ArrayList<Casa> casaslist;

    public CasaAdapter(ArrayList<Casa> casaslist){
        this.casaslist=casaslist;
        this.resource=resource;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(resource,parent,false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Casa casa = casaslist.get(position);

        holder.TextViewNombre.setText(casa.getNombre());
        holder.TextViewUbication.setText(casa.getUbicacion());
        holder.TextViewCode.setText(casa.getCodigo());
        holder.TextViewRol.setText(casa.getRol());
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView TextViewNombre;
        private TextView TextViewUbication;
        private TextView TextViewCode;
        private TextView TextViewRol;
        public View view;

        public ViewHolder(View view){
            super(view);

            this.view=view;
            this.TextViewNombre=(TextView) view.findViewById(R.id.homeName);
            this.TextViewUbication=(TextView) view.findViewById(R.id.homeUbication);
            this.TextViewCode=(TextView) view.findViewById(R.id.homeCode);
            this.TextViewRol=(TextView) view.findViewById(R.id.homeRole);
        }}
}
