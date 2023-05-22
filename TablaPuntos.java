
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.WindowConstants;


public class TablaPuntos {
    private static List<Jugador> jugadores;
    public JFrame tablapuntosFrame;
    public JTable tablaJugadores;


    public TablaPuntos() {

        if (jugadores == null) {
            jugadores = new ArrayList<>();
        }
        // Configurar el UIManager para cambiar los valores predeterminados de apariencia
        Color darkGray = new Color(40, 40, 40); // Gris oscuro personalizado
        UIManager.put("OptionPane.messageForeground", Color.WHITE); // Color del texto del cuadro de diálogo
        UIManager.put("OptionPane.background", darkGray); // Color de fondo del cuadro de diálogo
        UIManager.put("Panel.background", darkGray); // Color de fondo del panel dentro del cuadro de diálogo
        UIManager.put("Label.foreground", Color.WHITE); // Color del texto de las etiquetas
        UIManager.put("Table.background", darkGray); // Color de fondo de la tabla
        UIManager.put("Table.foreground", Color.WHITE); // Color del texto de la tabla


        tablapuntosFrame = new JFrame("Tabla de Jugadores");
        tablaJugadores = new JTable();
    }

    public static void agregarJugador(Jugador jugador) {
        jugadores.stream().filter(j -> j.getNombre().equals(jugador.getNombre())).findFirst().ifPresentOrElse(j -> agregarOReemplazar(j, jugador), () -> jugadores.add(jugador));
    }

    private static void agregarOReemplazar(Jugador oldJugador, Jugador newJugador) {
        if (oldJugador.getScore() < newJugador.getScore()) {
            jugadores.remove(oldJugador);
            jugadores.add(newJugador);
        }
    }

    public void mostrarTabla() {
        // Ordenar la lista de jugadores por puntuación de mayor a menor
        jugadores.sort(Collections.reverseOrder());

        // Crear un modelo de tabla
        DefaultTableModel modeloTabla = new DefaultTableModel();
        modeloTabla.addColumn("Jugadores");
        modeloTabla.addColumn("Puntuación");

        // Agregar los datos de los jugadores al modelo de tabla
        for (Jugador jugador : jugadores) {
            modeloTabla.addRow(new Object[]{jugador.getNombre(), jugador.getScore()});
        }

        // Establecer el modelo de tabla en la JTable
        JTable tablaJugadores = new JTable(modeloTabla);
        tablaJugadores.setBackground(Color.DARK_GRAY);
        tablaJugadores.setForeground(Color.WHITE);
        tablaJugadores.setSelectionBackground(Color.GRAY);
        tablaJugadores.setSelectionForeground(Color.WHITE);

        // Agregar la JTable a un JScrollPane
        JScrollPane scrollPane = new JScrollPane(tablaJugadores);
        scrollPane.setBackground(Color.DARK_GRAY);
        scrollPane.setPreferredSize(new Dimension(300, 200)); // Establecer el tamaño preferido del JScrollPane
    
        // Agregar el JScrollPane al panel principal
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(Color.DARK_GRAY);
        panelPrincipal.add(scrollPane, BorderLayout.CENTER);
    
        // Configurar la ventana y mostrarla
        JFrame tablapuntosFrame = new JFrame("Tabla de Puntos");
        tablapuntosFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        tablapuntosFrame.getContentPane().add(panelPrincipal);
        tablapuntosFrame.getContentPane().setBackground(Color.DARK_GRAY);
        tablapuntosFrame.pack();
        tablapuntosFrame.setLocationRelativeTo(null);
        tablapuntosFrame.setVisible(true);
    }

    public List<Jugador> getJugadores() {
        return jugadores.stream().sorted().toList();
    }
}


