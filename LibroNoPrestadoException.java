

public class LibroNoPrestadoException extends Exception {
    
    public LibroNoPrestadoException(){
        super("El libro se encuentra en la biblioteca");
    }

    public LibroNoPrestadoException(String mensaje)
    {
        super(mensaje);
    }
}

