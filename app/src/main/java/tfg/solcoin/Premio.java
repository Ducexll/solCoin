package tfg.solcoin;

public class Premio {
    private int idPremio;
    private String nombre;
    private double precio;

    public Premio(int idPremio, String nombre, double precio) {
        this.idPremio = idPremio;
        this.nombre = nombre;
        this.precio = precio;
    }

    public int getIdPremio() {
        return idPremio;
    }

    public void setIdPremio(int idPremio) {
        this.idPremio = idPremio;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }
}

