package net.iessochoa.tomassolerlinares.practica7.model;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class Mensaje {
    String usuario;
    String body;
    //@ServerTimestamp permite que sea el servidor el que asigne la horaal crear el documento
    @ServerTimestamp
    Date fechaCreacion;

    public Mensaje() {
    }

    public Mensaje(String usuario, String body) {
        this.usuario = usuario;
        this.body = body;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    @Override
    public String toString() {
        return "Mensaje{" +
                "usuario='" + usuario + '\'' +
                ", body='" + body + '\'' +
                ", fechaCreacion=" + fechaCreacion +
                '}';
    }
}
