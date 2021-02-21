package net.iessochoa.tomassolerlinares.practica7.model;

import android.net.Uri;

import com.google.firebase.firestore.GeoPoint;
/**
 * Clase POJO encargada de definir el objeto Empresa de firebase
 */
public class Empresa {
    private String direccion;
    private GeoPoint localizacion;
    private String nombre;
    private String telefono;

    public Empresa() {
    }

    public Empresa(String direccion, GeoPoint localizacion, String nombre, String telefono) {
        this.direccion = direccion;
        this.localizacion = localizacion;
        this.nombre = nombre;
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public GeoPoint getLocalizacion() {
        return localizacion;
    }

    public void setLocalizacion(GeoPoint localizacion) {
        this.localizacion = localizacion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    //Devuelve una uri con el teléfono.
    public Uri getUriTelefono(){
        return Uri.parse("tel:"+telefono);
    }
    //Devuelve una uri con las coordenadas de la geolocalización
    public Uri getUriLocalizacion() {
        return Uri.parse("geo:"+localizacion.getLatitude()+","+
                localizacion.getLongitude());
    }

}
