import java.util.ArrayList;
import java.util.Calendar;
/**
 * Write a description of class Socio here.
 * 
 * @author (Franco) 
 * @version (beta 1)
 */
public abstract class Socio{
    private int dniSocio;
    private String nombre;
    private int diasPrestamo;
    private ArrayList<Prestamo> prestamos;

    public Socio(int p_dniSocio, String p_nombre, int p_diasPrestamo, ArrayList<Prestamo> p_prestamos){
        this.setDniSocio(p_dniSocio);
        this.setNombre(p_nombre);
        this.setDiasPrestamo(p_diasPrestamo);
        this.setPrestamos(p_prestamos);
    }    
    public Socio(int p_dniSocio, String p_nombre, int p_diasPrestamo, Prestamo p_prestamo){
        this.setDniSocio(p_dniSocio);
        this.setNombre(p_nombre);
        this.setDiasPrestamo(p_diasPrestamo);
        this.setPrestamos(new ArrayList<Prestamo>()); 
        this.agregarPrestamo(p_prestamo);
    } 

    public Socio(int p_dniSocio, String p_nombre, int p_diasPrestamo) {
    this.setDniSocio(p_dniSocio);
    this.setNombre(p_nombre);
    this.setDiasPrestamo(p_diasPrestamo);
    this.setPrestamos(new ArrayList<Prestamo>()); 
    } 
    //metodos
        //accesos
    public int getDniSocio() {
        return this.dniSocio;
    }

    private void setDniSocio(int dniSocio) {
        this.dniSocio = dniSocio;
    }

    public String getNombre() {
        return this.nombre;
    }

    private  void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getDiasPrestamo() {
        return this.diasPrestamo;
    }

    protected void setDiasPrestamo(int diasPrestamo) {
        this.diasPrestamo = diasPrestamo;
    }

    public ArrayList<Prestamo> getPrestamos() {
        return this.prestamos;
    }

    private  void setPrestamos(ArrayList<Prestamo> prestamos) {
        this.prestamos = prestamos;
    }

    //otros metodos
    
    public boolean agregarPrestamo(Prestamo p_prestamo){
        return this.getPrestamos().add(p_prestamo);
    }

    public boolean eliminarPrestamo(Prestamo p_prestamo){
        return this.getPrestamos().remove(p_prestamo);
    }

    public int cantLibrosPrestados(){
        int contador = 0;
        for(Prestamo unPrestamo : this.getPrestamos()){
            contador++;
        }
        return contador;
    }
    public String toString(){
        return "D.N.I: " + this.getDniSocio() + "// " + this.getNombre() + "(" + this.getClass() + ")" 
        + "Libros prestados: " + this.cantLibrosPrestados();
    }
    public boolean puedePedir(){
        boolean posibilidad = true;
        for(Prestamo unPrestamo : this.getPrestamos()){
            if(unPrestamo.vencido(Calendar.getInstance()) == true){
                posibilidad = false;
                break;
            }
        }
        return posibilidad;
    }
    public abstract String soyDeLaClase();
}
