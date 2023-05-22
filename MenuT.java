import javax.swing.*;
import java.awt.*;


public class MenuT extends JFrame {

    private TablaPuntos tablapuntos = new TablaPuntos();

    public MenuT(){

        super("TETRIS LACRA");

        this.tablapuntos = tablapuntos;

        setSize(300, 180);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 1, 10, 10));

        JButton botonInicio = new JButton("Jugar");
        botonInicio.addActionListener(e -> {
            setVisible(false);
            new TetrisGO();
        });
        panel.add(botonInicio);

        JButton botonTablaPuntos = new JButton("TablaPuntos");
        botonTablaPuntos.addActionListener(e -> {

            setVisible(true);
            
            tablapuntos.mostrarTabla();

        });

        panel.add(botonTablaPuntos);

        
        JButton botonPanas = new JButton("Panas");
        botonPanas.addActionListener(e -> {

            setVisible(true);

            new Panas();
        });
        panel.add(botonPanas);

        

        add(panel);
        setVisible(true);
    }
}
