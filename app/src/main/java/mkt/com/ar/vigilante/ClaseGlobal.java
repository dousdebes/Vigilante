package mkt.com.ar.vigilante;
import android.app.Application;

/**
 * Created by Alberto
 */
public class ClaseGlobal extends Application {

    private int vendedorId;
    private String nombreVendedor;

    public int getVendedorId() {
        return vendedorId;
    }

    public void setVendedorId(int vendedorId) {
        this.vendedorId = vendedorId;
    }

    public String getNombreVendedor() {
        return nombreVendedor;
    }

    public void setNombreVendedor(String nombreVendedor) {
        this.nombreVendedor = nombreVendedor;
    }
}
