
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Clase encargada de la logica de biblioteca
 *
 * @author Merlo Wilson Julian - Scetti Santiago - Francisco Romero
 * @version 1.0
 */
public class Biblioteca {

    // instance variables
    private String nombre;
    private ArrayList<Socio> socios;
    private ArrayList<Libro> libros;

    /**
     * Constructors for objects of class Biblioteca
     */
    //Constructor de biblioteca
    public Biblioteca(String p_nombre, Socio p_socio, Libro p_libro) {
        this.setNombre(p_nombre);
        this.setSocios(new ArrayList<>());
        this.agregarSocio(p_socio);
        this.setLibros(new ArrayList<>());
        this.agregarLibro(p_libro);
    }

    public Biblioteca(String p_nombre, ArrayList<Socio> p_socios, ArrayList<Libro> p_libros) {
        this.setNombre(p_nombre);
        this.setSocios(p_socios);
        this.setLibros(p_libros);
    }

    public Biblioteca(String p_nombre) {
        this.setNombre(p_nombre);
        this.setSocios(new ArrayList<>());
        this.setLibros(new ArrayList<>());
    }

    //Metodos Setters
    private void setNombre(String p_nombre) {
        this.nombre = p_nombre;
    }

    private void setSocios(ArrayList<Socio> p_socios) {
        this.socios = p_socios;
    }

    private void setLibros(ArrayList<Libro> p_libros) {
        this.libros = p_libros;
    }

    //Metodos Getters
    public String getNombre() {
        return this.nombre;
    }

    public ArrayList<Socio> getSocios() {
        return this.socios;
    }

    public ArrayList<Libro> getLibros() {
        return this.libros;
    }

    // Método para agregar un socio
    public boolean agregarSocio(Socio p_socio) {
        return this.getSocios().add(p_socio);
    }

    // Método para eliminar un socio
    public boolean eliminarSocio(Socio p_socio) {
        return this.getSocios().remove(p_socio);
    }

    // Método para agregar un libro
    public boolean agregarLibro(Libro p_libro) {
        return this.getLibros().add(p_libro);
    }

    // Método para eliminar un libro
    public boolean eliminarLibro(Libro p_libro) {
        return this.getLibros().remove(p_libro);
    }

    //METODOS DE CLLASE
    public void nuevoLibro(String p_titulo, int p_edicion, String p_editorial, int p_anio) {
        Libro nuevoLibro = new Libro(p_titulo, p_edicion, p_editorial, p_anio);

        this.agregarLibro(nuevoLibro);
    }

    public void nuevoSocioEstudiante(int p_dniSocio, String p_nombre, String p_carrera) {
        Estudiante nuevoEs = new Estudiante(p_dniSocio, p_nombre, p_carrera);
        this.agregarSocio(nuevoEs);
    }

    public void nuevoSocioDocente(int p_dniSocio, String p_nombre, String p_area) {
        Docente nuevoDoc = new Docente(p_dniSocio, p_nombre, p_area);
        this.agregarSocio(nuevoDoc);
    }

    public boolean prestarLibro(Calendar p_fechaRetiro, Socio p_socio, Calendar p_fechaDevolucion, Libro p_libro) {
        if (p_libro.prestado()) {
            return false; //Si el libro ya está prestado, no se puede prestar.
        } else {
            Prestamo nuevoPrestamo = new Prestamo(p_fechaRetiro, p_fechaDevolucion, p_libro);
            p_libro.agregarPrestamo(nuevoPrestamo);
            p_socio.agregarPrestamo(nuevoPrestamo);
            return true;
        }
    }

    public void devolverLibro(Libro p_libro) throws LibroNoPrestadoException {
        if (!p_libro.prestado()) {
            throw new LibroNoPrestadoException("El libro " + p_libro.getTitulo() + " no se puede devolver ya que se encuentra en la biblioteca.");
        } else {
            for (Socio unSocio : this.getSocios()) {
                for (Prestamo unPrestamo : unSocio.getPrestamos()) {
                    if (unPrestamo.getLibro().equals(p_libro)) {
                        unSocio.eliminarPrestamo(unPrestamo);
                    }
                }
            }
        }
        throw new LibroNoPrestadoException("No se encontró el prestamo para el libro: " + p_libro.getTitulo());
    }

    public int cantidadDeSociosPorTipo(String p_objeto) {
        int cantidad = 0;
        for (Socio unSocio : this.getSocios()) {
            if (unSocio.soyDeLaClase().equals(p_objeto)) {
                cantidad++;
            }
        }
        return cantidad;
    }

    public ArrayList<Prestamo> prestamosVencidos() {
        ArrayList<Prestamo> prestamosVencidos = new ArrayList<>();
        Calendar fechaActual = new GregorianCalendar();
        for (Socio unSocio : this.getSocios()) {

            for (Prestamo unPrestamo : unSocio.getPrestamos()) {
                if (unPrestamo.vencido(fechaActual)) {
                    prestamosVencidos.add(unPrestamo);
                }
            }
        }
        return prestamosVencidos;
    }

    public ArrayList<Docente> docentesResponsables() {
        ArrayList<Docente> sociosDocentes = new ArrayList<>();
        ArrayList<Docente> docentesRespon = new ArrayList<>();

        for (Socio unSocio : this.getSocios()) {
            if (unSocio.soyDeLaClase().equals("Docente")) {
                sociosDocentes.add((Docente) unSocio);
            }
        }

        for (Docente unDocente : sociosDocentes) {
            if (unDocente.esResponsable()) {
                docentesRespon.add(unDocente);
            }
        }

        return docentesRespon;
    }

    public String quienTieneElLibro(Libro p_libro) throws LibroNoPrestadoException {
        if (!p_libro.prestado()) {
            throw new LibroNoPrestadoException();
        }
        for (Socio unSocio : this.getSocios()) {
            for (Prestamo unPrestamo : unSocio.getPrestamos()) {
                if (unPrestamo.getLibro().equals(p_libro)) {
                    return unSocio.getNombre();
                }
            }
        }
        throw new LibroNoPrestadoException();
    }

    public String listaDeSocios() {
        System.out.println("Lista de Socios");
        int posicion = 0;
        String espacio = "";
        for (Socio unSocio : this.getSocios()) {
            posicion++;
            System.out.println(posicion + ")" + unSocio.toString());
        }
        System.out.println("**************************************");
        System.out.println("Cantidad de Socios del tipo Estudiante: " + this.cantidadDeSociosPorTipo("Estudiante"));
        System.out.println("Cantidad de Socios del tipo Docente: " + this.cantidadDeSociosPorTipo("Docente"));
        System.out.println("**************************************");

        return espacio;
    }

    public Socio buscarSocio(int p_dni) {
        for (Socio unSocio : this.getSocios()) {
            if (unSocio.getDniSocio() == p_dni) {
                return unSocio;
            }
        }
        return null;
    }

    public String listaDeTitulos() {
        System.out.println("Lista de Titulos");
        int posicion = 0;
        String espacio = "";
        for (Libro unLibro : this.getLibros()) {
            posicion++;
            System.out.println(posicion + ")" + unLibro.toString());
        }
        return espacio;
    }

    public String listaDeLibros() {
        System.out.println("Lista de Libros");
        int posicion = 0;
        String espacio = "";
        for (Libro unLibro : this.getLibros()) {
            posicion++;
            if (unLibro.prestado()) {
                System.out.println(posicion + ")" + unLibro.toString() + "|| Prestado: (Si)");
            } else {
                System.out.println(posicion + ")" + unLibro.toString() + "|| Prestado: (No)");
            }
        }
        return espacio;
    }

    public String listaDeDocentesResponsables() {
        System.out.println("Lista de Docentes Responsables");
        int posicion = 0;
        String espacio = "";
        for (Docente unDocente : this.docentesResponsables()) {
            posicion++;
            System.out.println(posicion + ")" + unDocente.toString());
        }
        return espacio;
    }
}
