package net.iessochoa.tomassolerlinares.practica7.model;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

public class Conferencia {

    private boolean encurso;
    private Date fecha;
    private String horario;
    private String id;
    private String nombre;
    private int plazas;
    private String ponente;
    private String sala;

    public Conferencia() {
    }

    public Conferencia(boolean encurso, Date fecha, String horario, String id, String nombre, int plazas, String ponente, String sala) {
        this.encurso = encurso;
        this.fecha = fecha;
        this.horario = horario;
        this.id = id;
        this.nombre = nombre;
        this.plazas = plazas;
        this.ponente = ponente;
        this.sala = sala;
    }

    public boolean isEncurso() {
        return encurso;
    }

    public void setEncurso(boolean encurso) {
        this.encurso = encurso;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getPlazas() {
        return plazas;
    }

    public void setPlazas(int plazas) {
        this.plazas = plazas;
    }

    public String getPonente() {
        return ponente;
    }

    public void setPonente(String ponente) {
        this.ponente = ponente;
    }

    public String getSala() {
        return sala;
    }

    public void setSala(String sala) {
        this.sala = sala;
    }

    public String getFechaFormatoLocal() {
        DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM,
                Locale.getDefault());
        return df.format(fecha);
    }

    @Override
    public String toString() {
        return "Conferencia{" +
                "encurso=" + encurso +
                ", fecha=" + fecha +
                ", horario='" + horario + '\'' +
                ", id='" + id + '\'' +
                ", nombre='" + nombre + '\'' +
                ", plazas=" + plazas +
                ", ponente='" + ponente + '\'' +
                ", sala='" + sala + '\'' +
                '}';
    }
}
