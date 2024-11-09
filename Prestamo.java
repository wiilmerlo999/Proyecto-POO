import java.util.Calendar;
/**
 * Write a description of class Prestamo here.
 import java.time.temporal.TemporalField;
* 
 * @author (Franco) 
 * @version (beta 1)
 */

public class Prestamo{
    private Calendar fechaRetiro;
    private Calendar fechaDevolucion;
    private Libro libro;

    public Prestamo(Calendar p_fechaRetiro, Calendar p_fechaDevolucion, Libro p_libro){
        this.setFechaRetiro(p_fechaRetiro);
        this.setFechaDevolucion(p_fechaDevolucion);
        this.setLibro(p_libro);
    }

    //Metodos
    //Accesors 

    public Calendar getFechaRetiro() {
        return this.fechaRetiro;
    }

    private  void setFechaRetiro(Calendar fechaRetiro) {
        this.fechaRetiro = fechaRetiro;
    }

    public Calendar getFechaDevolucion() {
        return this.fechaDevolucion;
    }

    private  void setFechaDevolucion(Calendar p_fechaDevolucion) {
        this.fechaDevolucion = p_fechaDevolucion;
    }

    public Libro getLibro() {
        return this.libro;
    }

    private void setLibro(Libro libro) {
        this.libro = libro;
    }

    //Otros metodos
    
    public void registrarFechaDevolucion(Calendar p_fecha){
        this.setFechaDevolucion(p_fecha);
    }

    public boolean vencido(Calendar p_fecha){
        if(p_fecha.compareTo(this.getFechaDevolucion()) > 0){
            return true;
        }
        return false;   
    }

    public String toString(){
        return "";
    }
}