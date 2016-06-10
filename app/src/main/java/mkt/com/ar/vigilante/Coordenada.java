package mkt.com.ar.vigilante;

/**
 * Created by Alberto on 08/06/2016.
 */
public class Coordenada {

    private double latitud;
    private double longitud;


    public Coordenada(){

    }


    public Coordenada(double latitud, double longitud) {
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }
}
