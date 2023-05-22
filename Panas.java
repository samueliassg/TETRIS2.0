import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.*;
import java.awt.*;

public class Panas {
    private JFrame frame;
    private JButton unirseSalaButton;

    // Crear instancia de cliente y establecer la conexión

    private static String ip;

    private static final int PORT = 8080; // Puerto en el que el servidor está escuchando

    public Panas() {

        // Configurar el UIManager para cambiar los valores predeterminados de apariencia
        Color darkGray = new Color(40, 40, 40); // Gris oscuro personalizado
        UIManager.put("OptionPane.messageForeground", Color.WHITE); // Color del texto del cuadro de diálogo
        UIManager.put("OptionPane.background", darkGray); // Color de fondo del cuadro de diálogo
        UIManager.put("Panel.background", darkGray); // Color de fondo del panel dentro del cuadro de diálogo
        UIManager.put("Label.foreground", Color.WHITE); // Color del texto de las etiquetas
        
        frame = new JFrame("Panas");
        frame.setSize(200, 100); // Establecer un tamaño fijo para la ventana

        JPanel panel = new JPanel();

        unirseSalaButton = new JButton("Unirse para Multijugador");
        unirseSalaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ip = JOptionPane.showInputDialog(frame, "Ingrese la IP del servidor:");
            }
        });
        panel.add(unirseSalaButton);

        frame.getContentPane().add(panel);
        frame.setVisible(true);
    }


    public String getIp() {
        return ip;
    }

    public int getPort() {
        return PORT;
    }
}
