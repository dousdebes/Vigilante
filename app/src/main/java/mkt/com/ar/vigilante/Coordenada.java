package mkt.com.ar.vigilante;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Alberto on 08/06/2016.
 */
public class Coordenada {
    private String displayName;
    private double latitud;
    private double longitud;
    private Stats stats;
    private Integer vendedorId;
    private long timestamp;
    private double velocidadActual;
    private double velocidadPromedio;

    public Map<String, Boolean> historial = new HashMap<>();

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("latitud", latitud);
        result.put("longitud", longitud);
        result.put("vendedorId", vendedorId);
        result.put("displayName", displayName);
        result.put("stats", stats);
        result.put("timestamp", timestamp);
        return result;
    }


    public Coordenada(){

    }


    public double getVelocidadPromedio() {
        return velocidadPromedio;
    }

    public void setVelocidadPromedio(double velocidadPromedio) {
        this.velocidadPromedio = velocidadPromedio;
    }

    public double getVelocidadActual() {
        return velocidadActual;
    }

    public void setVelocidadActual(double velocidadActual) {
        this.velocidadActual = velocidadActual;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Map<String, Boolean> getHistorial() {
        return historial;
    }

    public void setHistorial(Map<String, Boolean> historial) {
        this.historial = historial;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
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

    public Stats getStats() {
        return stats;
    }

    public void setStats(Stats stats) {
        this.stats = stats;
    }

    public Integer getVendedorId() {
        return vendedorId;
    }

    public void setVendedorId(Integer vendedorId) {
        this.vendedorId = vendedorId;
    }
}
