import java.util.ArrayList;
/**
 * Clase Libro
 * 
 * @author Rios Karen Silvina
 * @version BlueJ 5.4.0
 */


public class Libro
{
    private String titulo;
    private int edicion;
    private String editorial;
    private int anio;
    private ArrayList<Prestamo> prestamos;

    public Libro(String p_titulo, int p_edicion, String p_editorial, int p_anio, ArrayList<Prestamo> p_prestamos)
    {
        this.setTitulo(p_titulo);
        this.setEdicion(p_edicion);
        this.setEditorial(p_editorial);
        this.setAnio(p_anio);
        this.setPrestamos(p_prestamos);
    }

    public Libro(String p_titulo, int p_edicion, String p_editorial, int p_anio, Prestamo p_prestamo)
    {
        this.setTitulo(p_titulo);
        this.setEdicion(p_edicion);
        this.setEditorial(p_editorial);
        this.setAnio(p_anio);
        this.setPrestamos(new ArrayList<>());
        this.agregarPrestamo(p_prestamo);
    }

    public Libro(String p_titulo, int p_edicion, String p_editorial, int p_anio)
    {
        this.setTitulo(p_titulo);
        this.setEdicion(p_edicion);
        this.setEditorial(p_editorial);
        this.setAnio(p_anio);
        this.setPrestamos(new ArrayList<>());
    }

    private void setTitulo(String p_titulo)
    {
        this.titulo = p_titulo;
    }

    private void setEdicion(int p_edicion)
    {
        this.edicion = p_edicion;
    }

    private void setEditorial(String p_editorial)
    {
        this.editorial = p_editorial;
    }

    private void setAnio(int p_anio)
    {
        this.anio = p_anio;
    }

    private void setPrestamos(ArrayList<Prestamo> p_prestamos)
    {
        this.prestamos = p_prestamos;
    }

    /**
     * @return devuelve el titulo(String) del libro.
     */
    public String getTitulo()
    {
        return this.titulo;
    }

    /**
     * @return devuelve la edición(int) del libro.
     */
    public int getEdicion()
    {
        return this.edicion;
    }

    /**
     * @return devuelve la editorial(String) del libro.
     */
    public String getEditorial()
    {
        return this.editorial;
    }

    /**
     * @return devuelve el año del libro.
     */
    public int getAnio()
    {
        return this.anio;
    }

    /**
     * @return devuelve La colección(ArrayList<Prestamo>) de prestamos.
     */
    public ArrayList<Prestamo> getPrestamos()
    {
        return this.prestamos;
    }

    /**
     * @return devuelve true o false.
     */
    public boolean agregarPrestamo(Prestamo p_prestamo)
    {
        return this.getPrestamos().add(p_prestamo);
    }

    /**
     * @return devuelve true o false.
     */
    public boolean eliminarPrestamo(Prestamo p_prestamo)
    {
        return this.getPrestamos().remove(p_prestamo);
    }

    /**
     * @return devuelve true si el libro se encuentra prestado.
     */
    public boolean prestado()
    {
        boolean resultado = false;
        for (Prestamo unPrestamo : this.getPrestamos()) {
            if (unPrestamo.getLibro().getTitulo().equals(this.getTitulo())){
                resultado = true;
            }
        }
        return resultado;
    }

    /**
     * @return devuelve el último Préstamo del libro.
     */
    public Prestamo ultimoPrestamo()
    {
        if (!this.getPrestamos().isEmpty()) {
        return this.getPrestamos().get(this.getPrestamos().size() - 1);
        } else {
        return null; 
        }
    }

    /**
     * @return devuelve el siguiente String: Titulo: <<titulo>>
     *         Ejemplo: Titulo: JAVA. Como Programar
     */
    @Override
    public String toString()
    {      
        return "Titulo: " + this.getTitulo();
    }
}