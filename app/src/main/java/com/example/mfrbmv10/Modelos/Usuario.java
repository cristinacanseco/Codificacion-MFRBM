package com.example.mfrbmv10.Modelos;

public class Usuario {

    public String nombre_usr,apellido_usr,correo_usr,clave_usr,imagen_usr, uid;
    public boolean admin_usr;

    public Usuario(){}

    public Usuario(String nombre_usr, String apellido_usr, String correo_usr, String clave_usr, String imagen_usr, boolean admin_usr) {
        this.nombre_usr = nombre_usr;
        this.apellido_usr = apellido_usr;
        this.correo_usr = correo_usr;
        this.clave_usr = clave_usr;
        this.imagen_usr = imagen_usr;
        this.admin_usr = admin_usr;
    }

    public Usuario(String uid, String nombre_usr, String apellido_usr, String correo_usr, String clave_usr, String imagen_usr, boolean admin_usr) {
        this.nombre_usr = nombre_usr;
        this.apellido_usr = apellido_usr;
        this.correo_usr = correo_usr;
        this.clave_usr = clave_usr;
        this.imagen_usr = imagen_usr;
        this.uid = uid;
        this.admin_usr = admin_usr;
    }

    public String getNombre_usr() {
        return nombre_usr;
    }

    public String getApellido_usr() {
        return apellido_usr;
    }

    public String getCorreo_usr() {
        return correo_usr;
    }

    public String getClave_usr() {
        return clave_usr;
    }

    public String getImagen_usr() {
        return imagen_usr;
    }

    public boolean getAdmin_usr() {
        return admin_usr;
    }

    public String getUid() { return uid; }

    public void setNombre_usr(String nombre_usr) {
        this.nombre_usr = nombre_usr;
    }

    public void setApellido_usr(String apellido_usr) {
        this.apellido_usr = apellido_usr;
    }

    public void setCorreo_usr(String correo_usr) {
        this.correo_usr = correo_usr;
    }

    public void setClave_usr(String clave_usr) {
        this.clave_usr = clave_usr;
    }

    public void setImagen_usr(String imagen_usr) {
        this.imagen_usr = imagen_usr;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setAdmin_usr(boolean admin_usr) {
        this.admin_usr = admin_usr;
    }
}
