import java.util.ArrayList;
import java.util.Calendar;
/**
 * Representa un docente que es un socio.
 * 
 * @author Sosa Diana Abril
 * @version (1)
 */
public class Docente extends Socio {
    private String area;

    /**
     * Constructor para crear un objeto de la clase Docente con una lista de préstamos.
     * 
     * @param p_dniSocio     El DNI del socio.
     * @param p_nombre       El nombre del docente.
     * @param p_area         El área de trabajo del docente.
     * @param p_prestamos    Lista de préstamos asociados al docente.
     */
    public Docente(int p_dniSocio, String p_nombre, String p_area, ArrayList<Prestamo> p_prestamos) {
        super(p_dniSocio, p_nombre, 5, p_prestamos);
        this.setArea(p_area);
    }

    /**
     * Constructor para crear un objeto de la clase Docente con un préstamo inicial.
     * 
     * @param p_dniSocio     El DNI del socio.
     * @param p_nombre       El nombre del docente.
     * @param p_area         El área de trabajo del docente.
     * @param p_prestamo     El préstamo inicial.
     */
    public Docente(int p_dniSocio, String p_nombre, String p_area, Prestamo p_prestamo) {
        super(p_dniSocio, p_nombre, 5, p_prestamo);
        this.setArea(p_area);
    }

    /**
     * Constructor para crear un objeto Docente sin préstamos iniciales.
     *
     * @param p_dniSocio El DNI del docente.
     * @param p_nombre El nombre del docente.
     * @param p_area El área de especialización del docente.
     */
    public Docente(int p_dniSocio, String p_nombre, String p_area) {
        super(p_dniSocio, p_nombre, 5); 
        this.setArea(p_area);
    }

    private void setArea(String p_area) {
        this.area = p_area;
    }

    /**
     * Obtiene el área de trabajo del docente.
     * 
     * @return El área del docente.
     */
    public String getArea() {
        return this.area;
    }

    /**
     * Indica de qué clase es el objeto.
     * 
     * @return "Docente".
     */
    @Override
    public String soyDeLaClase() {
        return "Docente";
    }

    /**
     * Verifica si el docente puede pedir un libro en préstamo.
     * 
     * @return true si el docente puede pedir, false de lo contrario.
     */
    @Override
    public boolean puedePedir() {
        return super.puedePedir();
    }

    /**
     * Verifica si el docente es responsable en sus préstamos.
     * 
     * @return true si el docente es responsable, false de lo contrario.
     */
    public boolean esResponsable() {
        boolean responsable = true;
        for (Prestamo unPrestamo : this.getPrestamos()) {
            if (unPrestamo.vencido(Calendar.getInstance()) || unPrestamo.vencido(unPrestamo.getFechaDevolucion())) {
                responsable = false;
            }
        }
        return responsable;
    }

    //adiciona días de préstamo al docente. Es un premio a la responsabilidad.  
    public void cambiarDiasPrestamo(int p_dias){
        if(this.esResponsable()){
            int masDiasPrestamo = this.getDiasPrestamo() + p_dias;
            this.setDiasPrestamo(masDiasPrestamo);
        }
    }
}

