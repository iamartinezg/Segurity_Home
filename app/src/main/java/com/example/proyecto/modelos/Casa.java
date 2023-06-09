package com.example.proyecto.modelos;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Casa {

    private  String nombre;
    public  String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    private String ubicacion;
    public String getUbicacion() {
        return ubicacion;
    }
    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }
    private String codigo;
    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
    private String rol;
    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getRol() {
        return rol;
    }

    public Casa(String nombre, String ubicacion, String codigo, String rol) {
        this.nombre = nombre;
        this.ubicacion = ubicacion;
        this.codigo = codigo;
        this.rol = rol;
    }


}
