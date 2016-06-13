package mkt.com.ar.vigilante;

/**
 * Created by Alberto on 11/06/2016.
 */
public class Posicion {
    private double latitud;
    private double longitud;
    private long timestamp;

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

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
