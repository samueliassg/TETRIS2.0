import java.io.IOException;
import javax.swing.JFrame;

public class TetrisGO {

    public static final int WIDTH = 325, HEIGHT = 640;

    private Tablero board;

    private JFrame window;

    private static Servidor server;


    public TetrisGO() {
        window = new JFrame("Tetris");
        window.setSize(WIDTH, HEIGHT);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setLocationRelativeTo(null);

        board = new Tablero();
        window.add(board);
        window.addKeyListener(board);
        window.setVisible(true);
    }

    public static void  iniciarServidor(int port) {
        server = new Servidor();
        try {
            server.start(port);
            System.out.println("Servidor iniciado " + port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        MenuT menuT = new MenuT();
        Panas panas = new Panas();

        Thread serverThread = new Thread(() -> {
            iniciarServidor(panas.getPort());
        });

        serverThread.start();

    }
    
}