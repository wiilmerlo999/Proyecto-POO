import java.util.ArrayList;
/**
 * Representa un estudiante que es un socio.
 * 
 * @author Sosa Diana Abril
 * @version (1)
 */
public class Estudiante extends Socio {
    private String carrera;

    /**
     * Constructor para crear un objeto de la clase Estudiante con una lista de préstamos.
     * 
     * @param p_dniSocio     El DNI del socio.
     * @param p_nombre       El nombre del estudiante.
     * @param p_carrera      La carrera del estudiante.
     * @param p_prestamos    Lista de préstamos asociados al estudiante.
     */
    public Estudiante(int p_dniSocio, String p_nombre, String p_carrera, ArrayList<Prestamo> p_prestamos) {
        super(p_dniSocio, p_nombre, 20, p_prestamos);
        this.setCarrera(p_carrera);
    }

    /**
     * Constructor para crear un objeto de la clase Estudiante con un préstamo inicial.
     * 
     * @param p_dniSocio     El DNI del socio.
     * @param p_nombre       El nombre del estudiante.
     * @param p_carrera      La carrera del estudiante.
     * @param p_prestamo     El préstamo inicial.
     */
    public Estudiante(int p_dniSocio, String p_nombre, String p_carrera, Prestamo p_prestamo) {
        super(p_dniSocio, p_nombre, 20, p_prestamo);
        this.setCarrera(p_carrera);
    }

    /**
     * Constructor para crear un objeto Estudiante sin préstamos iniciales.
     *
     * @param p_dniSocio El DNI del estudiante.
     * @param p_nombre El nombre del estudiante.
     * @param p_carrera La carrera que estudia el estudiante.
     */
    public Estudiante(int p_dniSocio, String p_nombre, String p_carrera) {
        super(p_dniSocio, p_nombre, 20); // Llama al constructor de Socio sin préstamos
        this.setCarrera(p_carrera);
    }


    private void setCarrera(String p_carrera) {
        this.carrera = p_carrera;
    }

    /**
     * Obtiene la carrera del estudiante.
     * 
     * @return La carrera del estudiante.
     */
    public String getCarrera() {
        return this.carrera;
    }

    /**
     * Verifica si el estudiante puede pedir un libro en préstamo.
     * 
     * @return true si el estudiante puede pedir, false de lo contrario.
     */
    @Override
    public boolean puedePedir() {
        return super.puedePedir() && super.cantLibrosPrestados() < 4;
    }

    /**
     * Indica de qué clase es el objeto.
     * 
     * @return "Estudiante".
     */
    @Override
    public String soyDeLaClase() {
        return "Estudiante";
    }
}

