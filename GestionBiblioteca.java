
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;

public class GestionBiblioteca extends JFrame {

    private Biblioteca biblioteca;
    private JTabbedPane tabbedPane;
    private JPanel sociosPanel, librosPanel, prestamosPanel;
    private JTable sociosTable, librosTable, prestamosTable;
    private DefaultTableModel sociosModel, librosModel, prestamosModel;

    public GestionBiblioteca() {
        // Inicializar la biblioteca
        biblioteca = new Biblioteca("Biblioteca Central");

        // Configurar la ventana principal
        setTitle("Gestión de Biblioteca");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Crear el panel principal con pestañas
        tabbedPane = new JTabbedPane();

        // Inicializar paneles
        inicializarPanelSocios();
        inicializarPanelLibros();
        inicializarPanelPrestamos();

        // Agregar pestañas
        tabbedPane.addTab("Socios", new JScrollPane(sociosPanel));
        tabbedPane.addTab("Libros", new JScrollPane(librosPanel));
        tabbedPane.addTab("Préstamos", new JScrollPane(prestamosPanel));

        add(tabbedPane);

        // Crear barra de menú
        crearMenu();
    }

    private void inicializarPanelSocios() {
        sociosPanel = new JPanel(new BorderLayout());

        // Crear modelo de tabla para socios
        sociosModel = new DefaultTableModel(
                new Object[]{"DNI", "Nombre", "Tipo", "Carrera/Área", "Libros Prestados"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        sociosTable = new JTable(sociosModel);

        // Panel de botones para socios
        JPanel botonesPanel = new JPanel();
        JButton agregarSocio = new JButton("Agregar Socio");
        JButton eliminarSocio = new JButton("Eliminar Socio");

        agregarSocio.addActionListener(e -> mostrarDialogoAgregarSocio());
        eliminarSocio.addActionListener(e -> eliminarSocioSeleccionado());

        botonesPanel.add(agregarSocio);
        botonesPanel.add(eliminarSocio);

        sociosPanel.add(new JScrollPane(sociosTable), BorderLayout.CENTER);
        sociosPanel.add(botonesPanel, BorderLayout.SOUTH);
    }

    private void eliminarSocioSeleccionado() {
        int selectedRow = sociosTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Por favor, seleccione un socio para eliminar",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        int dni = (int) sociosModel.getValueAt(selectedRow, 0);
        String nombre = (String) sociosModel.getValueAt(selectedRow, 1);

        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Está seguro que desea eliminar el socio " + nombre + " (DNI: " + dni + ")?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                // Buscar el socio en la biblioteca por DNI
                Socio socioAEliminar = null;
                for (Socio socio : biblioteca.getSocios()) {
                    if (socio.getDniSocio() == dni) {
                        socioAEliminar = socio;
                        break;
                    }
                }

                if (socioAEliminar != null) {
                    if (biblioteca.eliminarSocio(socioAEliminar)) {
                        actualizarTablaSocios();
                        JOptionPane.showMessageDialog(this,
                                "Socio eliminado exitosamente",
                                "Éxito",
                                JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this,
                                "No se pudo eliminar el socio. Puede tener préstamos pendientes.",
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(this,
                            "No se encontró el socio en la biblioteca",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                        "Error al eliminar socio: " + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void inicializarPanelLibros() {
        librosPanel = new JPanel(new BorderLayout());

        // Crear modelo de tabla para libros
        librosModel = new DefaultTableModel(
                new Object[]{"Título", "Edición", "Editorial", "Año", "Estado"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        librosTable = new JTable(librosModel);

        // Panel de botones para libros
        JPanel botonesPanel = new JPanel();
        JButton agregarLibro = new JButton("Agregar Libro");
        JButton eliminarLibro = new JButton("Eliminar Libro");

        agregarLibro.addActionListener(e -> mostrarDialogoAgregarLibro());
        eliminarLibro.addActionListener(e -> eliminarLibroSeleccionado());

        botonesPanel.add(agregarLibro);
        botonesPanel.add(eliminarLibro);

        librosPanel.add(new JScrollPane(librosTable), BorderLayout.CENTER);
        librosPanel.add(botonesPanel, BorderLayout.SOUTH);
    }

    // Método para devolver libro seleccionado
    private void devolverLibroSeleccionado() {
        int selectedRow = prestamosTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Por favor, seleccione un préstamo para devolver el libro",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        Libro libro = (Libro) prestamosModel.getValueAt(selectedRow, 1);

        try {
            biblioteca.devolverLibro(libro);
            actualizarTablaPrestamos();
            JOptionPane.showMessageDialog(this,
                    "Libro devuelto exitosamente",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (LibroNoPrestadoException e) {
            JOptionPane.showMessageDialog(this,
                    e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

// Método para actualizar la tabla de préstamos
    private void actualizarTablaPrestamos() {
        prestamosModel.setRowCount(0);
        for (Socio socio : biblioteca.getSocios()) {
            for (Prestamo prestamo : socio.getPrestamos()) {
                Object[] row = new Object[5];
                row[0] = socio.getNombre();
                row[1] = prestamo.getLibro();
                row[2] = formatearFecha(prestamo.getFechaRetiro());
                row[3] = formatearFecha(prestamo.getFechaDevolucion());
                row[4] = prestamo.vencido(new GregorianCalendar()) ? "Vencido" : "Vigente";
                prestamosModel.addRow(row);
            }
        }
    }

// Método auxiliar para formatear fechas
    private String formatearFecha(Calendar fecha) {
        if (fecha == null) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(fecha.getTime());
    }

// Método para mostrar préstamos vencidos
    private void mostrarPrestamosVencidos() {
        ArrayList<Prestamo> vencidos = biblioteca.prestamosVencidos();

        if (vencidos.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No hay préstamos vencidos",
                    "Información",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        JDialog dialog = new JDialog(this, "Préstamos Vencidos", true);
        dialog.setLayout(new BorderLayout());

        DefaultTableModel modeloVencidos = new DefaultTableModel(
                new Object[]{"Socio", "Libro", "Fecha Retiro", "Días de Retraso"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable tablaVencidos = new JTable(modeloVencidos);
        Calendar fechaActual = new GregorianCalendar();

        for (Prestamo prestamo : vencidos) {
            Object[] row = new Object[4];
            // Aquí necesitarás buscar el socio que tiene este préstamo
            row[0] = buscarSocioPorPrestamo(prestamo).getNombre();
            row[1] = prestamo.getLibro().getTitulo();
            row[2] = formatearFecha(prestamo.getFechaRetiro());
            row[3] = calcularDiasRetraso(prestamo.getFechaRetiro(), fechaActual);
            modeloVencidos.addRow(row);
        }

        dialog.add(new JScrollPane(tablaVencidos), BorderLayout.CENTER);
        dialog.setSize(600, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

// Método para mostrar docentes responsables
    private void mostrarDocentesResponsables() {
        ArrayList<Docente> docentes = biblioteca.docentesResponsables();

        if (docentes.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No hay docentes responsables",
                    "Información",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        JDialog dialog = new JDialog(this, "Docentes Responsables", true);
        dialog.setLayout(new BorderLayout());

        DefaultTableModel modeloDocentes = new DefaultTableModel(
                new Object[]{"DNI", "Nombre", "Área", "Libros Prestados"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable tablaDocentes = new JTable(modeloDocentes);

        for (Docente docente : docentes) {
            Object[] row = new Object[4];
            row[0] = docente.getDniSocio();
            row[1] = docente.getNombre();
            row[2] = docente.getArea();
            row[3] = docente.cantLibrosPrestados();
            modeloDocentes.addRow(row);
        }

        dialog.add(new JScrollPane(tablaDocentes), BorderLayout.CENTER);
        dialog.setSize(500, 300);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

// Método auxiliar para buscar el socio que tiene un préstamo específico
    private Socio buscarSocioPorPrestamo(Prestamo prestamo) {
        for (Socio socio : biblioteca.getSocios()) {
            if (socio.getPrestamos().contains(prestamo)) {
                return socio;
            }
        }
        return null;
    }

// Método auxiliar para calcular días de retraso
    private int calcularDiasRetraso(Calendar fechaRetiro, Calendar fechaActual) {
        long diff = fechaActual.getTimeInMillis() - fechaRetiro.getTimeInMillis();
        return (int) (diff / (1000 * 60 * 60 * 24)) - 30; // Asumiendo 30 días de préstamo
    }

    private void inicializarPanelPrestamos() {
        prestamosPanel = new JPanel(new BorderLayout());

        // Crear modelo de tabla para préstamos
        prestamosModel = new DefaultTableModel(
                new Object[]{"Socio", "Libro", "Fecha Retiro", "Fecha Devolución", "Estado"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        prestamosTable = new JTable(prestamosModel);

        // Panel de botones para préstamos
        JPanel botonesPanel = new JPanel();
        JButton nuevoPrestamo = new JButton("Nuevo Préstamo");
        JButton devolverLibro = new JButton("Devolver Libro");

        nuevoPrestamo.addActionListener(e -> mostrarDialogoNuevoPrestamo());
        devolverLibro.addActionListener(e -> devolverLibroSeleccionado());

        botonesPanel.add(nuevoPrestamo);
        botonesPanel.add(devolverLibro);

        prestamosPanel.add(new JScrollPane(prestamosTable), BorderLayout.CENTER);
        prestamosPanel.add(botonesPanel, BorderLayout.SOUTH);
    }

    // Método para mostrar diálogo de nuevo préstamo
    private void mostrarDialogoNuevoPrestamo() {
    JDialog dialog = new JDialog(this, "Nuevo Préstamo", true);
    dialog.setLayout(new GridLayout(5, 2, 5, 5));  // Aumentamos el tamaño del grid

    // Componentes existentes
    DefaultComboBoxModel<Socio> sociosComboModel = new DefaultComboBoxModel<>();
    for (Socio socio : biblioteca.getSocios()) {
        sociosComboModel.addElement(socio);
    }
    JComboBox<Socio> socioCombo = new JComboBox<>(sociosComboModel);

    DefaultComboBoxModel<Libro> librosComboModel = new DefaultComboBoxModel<>();
    for (Libro libro : biblioteca.getLibros()) {
        if (!libro.prestado()) {
            librosComboModel.addElement(libro);
        }
    }
    JComboBox<Libro> libroCombo = new JComboBox<>(librosComboModel);

    // Agregar selector de fecha
    JSpinner fechaDevolucion = new JSpinner(new SpinnerDateModel());
    JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(fechaDevolucion, "dd/MM/yyyy");
    fechaDevolucion.setEditor(dateEditor);
    // Establecer fecha mínima como hoy + 1 día
    Calendar minDate = Calendar.getInstance();
    minDate.add(Calendar.DAY_OF_MONTH, 1);
    ((SpinnerDateModel)fechaDevolucion.getModel()).setStart(minDate.getTime());

    dialog.add(new JLabel("Socio:"));
    dialog.add(socioCombo);
    dialog.add(new JLabel("Libro:"));
    dialog.add(libroCombo);
    dialog.add(new JLabel("Fecha Devolución:"));
    dialog.add(fechaDevolucion);

    JButton aceptar = new JButton("Aceptar");
    JButton cancelar = new JButton("Cancelar");

    aceptar.addActionListener(e -> {
        try {
            Socio socioSeleccionado = (Socio) socioCombo.getSelectedItem();
            Libro libroSeleccionado = (Libro) libroCombo.getSelectedItem();

            if (socioSeleccionado != null && libroSeleccionado != null) {
                Calendar fechaRetiro = new GregorianCalendar();
                // Convertir la fecha seleccionada a Calendar
                Calendar fechaDev = new GregorianCalendar();
                fechaDev.setTime((Date) fechaDevolucion.getValue());
                
                boolean prestado = biblioteca.prestarLibro(fechaRetiro, socioSeleccionado,fechaDev, libroSeleccionado);

                if (prestado) {
                    actualizarTablaPrestamos();
                    dialog.dispose();
                    JOptionPane.showMessageDialog(this,
                            "Préstamo realizado exitosamente",
                            "Éxito",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(dialog,
                            "No se pudo realizar el préstamo",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(dialog,
                    "Error al realizar el préstamo: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    });

    cancelar.addActionListener(e -> dialog.dispose());

    dialog.add(aceptar);
    dialog.add(cancelar);

    dialog.pack();
    dialog.setLocationRelativeTo(this);
    dialog.setVisible(true);
}

    private void eliminarLibroSeleccionado() {
        int selectedRow = librosTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Por favor, seleccione un libro para eliminar",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        String titulo = (String) librosModel.getValueAt(selectedRow, 0);
        int edicion = (int) librosModel.getValueAt(selectedRow, 1);
        String editorial = (String) librosModel.getValueAt(selectedRow, 2);

        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Está seguro que desea eliminar el libro '" + titulo + "'?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                // Buscar el libro en la biblioteca por título y edición
                Libro libroAEliminar = null;
                for (Libro libro : biblioteca.getLibros()) {
                    if (libro.getTitulo().equals(titulo)
                            && libro.getEdicion() == edicion
                            && libro.getEditorial().equals(editorial)) {
                        libroAEliminar = libro;
                        break;
                    }
                }

                if (libroAEliminar != null) {
                    if (biblioteca.eliminarLibro(libroAEliminar)) {
                        actualizarTablaLibros();
                        JOptionPane.showMessageDialog(this,
                                "Libro eliminado exitosamente",
                                "Éxito",
                                JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this,
                                "No se pudo eliminar el libro. Puede estar prestado.",
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(this,
                            "No se encontró el libro en la biblioteca",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                        "Error al eliminar libro: " + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

// Método para mostrar diálogo de agregar libro
    private void mostrarDialogoAgregarLibro() {
        JDialog dialog = new JDialog(this, "Agregar Libro", true);
        dialog.setLayout(new GridLayout(6, 2, 5, 5));

        JTextField tituloField = new JTextField();
        JTextField edicionField = new JTextField();
        JTextField editorialField = new JTextField();
        JTextField anioField = new JTextField();

        dialog.add(new JLabel("Título:"));
        dialog.add(tituloField);
        dialog.add(new JLabel("Edición:"));
        dialog.add(edicionField);
        dialog.add(new JLabel("Editorial:"));
        dialog.add(editorialField);
        dialog.add(new JLabel("Año:"));
        dialog.add(anioField);

        JButton aceptar = new JButton("Aceptar");
        JButton cancelar = new JButton("Cancelar");

        aceptar.addActionListener(e -> {
            try {
                String titulo = tituloField.getText();
                int edicion = Integer.parseInt(edicionField.getText());
                String editorial = editorialField.getText();
                int anio = Integer.parseInt(anioField.getText());

                biblioteca.nuevoLibro(titulo, edicion, editorial, anio);
                actualizarTablaLibros();
                dialog.dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Por favor, ingrese valores numéricos válidos para edición y año");
            }
        });

        cancelar.addActionListener(e -> dialog.dispose());

        dialog.add(aceptar);
        dialog.add(cancelar);

        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

// Método para actualizar tabla de libros
    private void actualizarTablaLibros() {
        librosModel.setRowCount(0);
        for (Libro libro : biblioteca.getLibros()) {
            Object[] row = new Object[5];
            row[0] = libro.getTitulo();
            row[1] = libro.getEdicion();
            row[2] = libro.getEditorial();
            row[3] = libro.getAnio();
            librosModel.addRow(row);
        }
    }

    private void crearMenu() {
    JMenuBar menuBar = new JMenuBar();

    // Menú Archivo
    JMenu archivo = new JMenu("Archivo");
    JMenuItem salir = new JMenuItem("Salir");
    salir.addActionListener(e -> System.exit(0));
    archivo.add(salir);

    // Menú Reportes
    JMenu reportes = new JMenu("Reportes");
    JMenuItem prestamosVencidos = new JMenuItem("Préstamos Vencidos");
    JMenuItem docentesResponsables = new JMenuItem("Docentes Responsables");

    prestamosVencidos.addActionListener(e -> mostrarPrestamosVencidos());
    docentesResponsables.addActionListener(e -> mostrarDocentesResponsables());

    reportes.add(prestamosVencidos);
    reportes.add(docentesResponsables);

    menuBar.add(archivo);
    menuBar.add(reportes);
    setJMenuBar(menuBar);
}

    private void mostrarDialogoAgregarSocio() {
        JDialog dialog = new JDialog(this, "Agregar Socio", true);
        dialog.setLayout(new GridLayout(6, 2, 5, 5));

        JTextField dniField = new JTextField();
        JTextField nombreField = new JTextField();
        JComboBox<String> tipoCombo = new JComboBox<>(new String[]{"Estudiante", "Docente"});
        JTextField carreraAreaField = new JTextField();

        dialog.add(new JLabel("DNI:"));
        dialog.add(dniField);
        dialog.add(new JLabel("Nombre:"));
        dialog.add(nombreField);
        dialog.add(new JLabel("Tipo:"));
        dialog.add(tipoCombo);
        dialog.add(new JLabel("Carrera/Área:"));
        dialog.add(carreraAreaField);

        JButton aceptar = new JButton("Aceptar");
        JButton cancelar = new JButton("Cancelar");

        aceptar.addActionListener(e -> {
            try {
                int dni = Integer.parseInt(dniField.getText());
                String nombre = nombreField.getText();
                String tipo = (String) tipoCombo.getSelectedItem();
                String carreraArea = carreraAreaField.getText();

                if (tipo.equals("Estudiante")) {
                    biblioteca.nuevoSocioEstudiante(dni, nombre, carreraArea);
                } else {
                    biblioteca.nuevoSocioDocente(dni, nombre, carreraArea);
                }

                actualizarTablaSocios();
                dialog.dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "DNI inválido");
            }
        });

        cancelar.addActionListener(e -> dialog.dispose());

        dialog.add(aceptar);
        dialog.add(cancelar);

        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    // ... (Otros métodos de diálogo similares para libros y préstamos)
    private void actualizarTablaSocios() {
        sociosModel.setRowCount(0);
        for (Socio socio : biblioteca.getSocios()) {
            Object[] row = new Object[5];
            row[0] = socio.getDniSocio();
            row[1] = socio.getNombre();
            row[2] = socio.soyDeLaClase();
            row[3] = (socio instanceof Estudiante)
                    ? ((Estudiante) socio).getCarrera()
                    : ((Docente) socio).getArea();
            row[4] = socio.cantLibrosPrestados();
            sociosModel.addRow(row);
        }
    }

    // ... (Métodos similares para actualizar las otras tablas)
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            new GestionBiblioteca().setVisible(true);
        });
    }
}
