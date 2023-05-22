import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.Random;
import javax.swing.SwingUtilities;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.Timer;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.Font;

public class Tablero extends JPanel implements KeyListener {

    //variables del estado del juego 
    public static int STATE_GAME_PLAY = 0;
    public static int STATE_GAME_PAUSE = 1;
    public static int STATE_GAME_OVER = 2;
    private int state = STATE_GAME_PLAY;

    //variable de puntuacion
    int score = 0;

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    private JButton botonRegresar;

    private void crearBotonRegresar() {
        botonRegresar = new JButton("Volver");
        botonRegresar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                MenuT menuT = new MenuT();
                menuT.setVisible(true);
                JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(Tablero.this);
                frame.dispose();
            }
        });

        // Crear un panel para el botón de regresar
        JPanel panelBotonRegresar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBotonRegresar.add(botonRegresar);

        // Establecer un layout FlowLayout para el panel principal (Tablero)
        setLayout(new FlowLayout(FlowLayout.RIGHT));

        // Agregar el panel del botón de regresar al panel principal (Tablero)
        add(panelBotonRegresar);
    }


    //Se definen las variables de velocidad principales
    private static int FPS = 60;
    private static int delay = FPS / 1000;

    //Se define el tamano de las filas y columnas del tablero de juego
    public static final int TABLERO_WIDTH = 10;
    public static final int TABLERO_HEIGHT = 20;

    //Se define el tamano de los bloques del tablero de juego
    public static final int BLOCK_SIZE = 30;

    //se definen las variables del bucle de tiempo
    private Timer looper;
    private Color[][] board = new Color[TABLERO_HEIGHT][TABLERO_WIDTH];

    private Color[] colors = {Color.decode("#91A6FF"), Color.decode("#FF88DC"),
            Color.decode("#FAFF7F"), Color.decode("#32A287"), Color.decode("#FF5154"),
            Color.decode("#7B4B94"), Color.decode("#FF674D"), Color.decode("#CCD1D1")};
    // private int[][] shape1 = {
    //     {1,1,1},
    //     {0,1,0}
    // };

    private Random random;
    private Piezas[] shape = new Piezas[7];
    private Piezas currentPiezas;

    //private Piezas shape= new Piezas(shape1);
    //bucle del tiempo de caida de las piezas
    public Tablero() {

        random = new Random();

        shape[0] = new Piezas(new int[][]{
                //segun la pieza I
                {1, 1, 1, 1}
        }, this, colors[0]);
        shape[1] = new Piezas(new int[][]{
                //segun la pieza T
                {1, 1, 1},
                {0, 1, 0}
        }, this, colors[1]);
        shape[2] = new Piezas(new int[][]{
                //segun la pieza L
                {1, 1, 1},
                {1, 0, 0}
        }, this, colors[2]);
        shape[3] = new Piezas(new int[][]{
                //segun la pieza J
                {1, 1, 1},
                {0, 0, 1}
        }, this, colors[3]);
        shape[4] = new Piezas(new int[][]{
                //segun la pieza Z
                {1, 1, 0},
                {0, 1, 1}
        }, this, colors[4]);
        shape[5] = new Piezas(new int[][]{
                //segun la pieza S
                {0, 1, 1},
                {1, 1, 0}
        }, this, colors[5]);
        shape[6] = new Piezas(new int[][]{
                //segun la pieza cuadrada
                {1, 1},
                {1, 1}
        }, this, colors[6]);


        currentPiezas = shape[0];
        looper = new Timer(delay, new ActionListener() {


            @Override
            public void actionPerformed(ActionEvent e) {
                update();
                repaint();
            }

        });
        looper.start();
        crearBotonRegresar();

        configurarTeclado();

    }

    private void configurarTeclado() {
        setFocusable(true);
        addKeyListener(this);
    }

    private void update() {
        if (state == STATE_GAME_PLAY) {

            currentPiezas.update();
            actu();
        }
    }

    public void setCurrentShape() {
        currentPiezas = shape[random.nextInt(shape.length)];
        currentPiezas.reset();
        FinJuego();
    }


    //Cuadro de dialogo para registrar usuarios

    
    private boolean gameOverDialogShown = false;

    //logica del cuadro

    private void FinJuego() {
        if (gameOverDialogShown) {
            state = STATE_GAME_OVER;
            return;
        }

        int[][] coords = currentPiezas.getCoord();
        for (int row = 0; row < coords.length; row++) {
            for (int col = 0; col < coords[0].length; col++) {
                if (coords[row][col] != 0) {
                    if (board[row + currentPiezas.getY()][col + currentPiezas.getX()] != null) {
                        String nombre = JOptionPane.showInputDialog("PERDISTE \n introduce tu nombre:");
    
                        // Crear un objeto Jugador con el nombre y la puntuación del jugador
                        Jugador jugador = new Jugador(nombre, score);
                        TablaPuntos tablapuntos = new TablaPuntos();

                        // Agregar el jugador a la tabla de líderes
                        tablapuntos.agregarJugador(jugador);

                        enviarScore(jugador);

                        // Mostrar la tabla de líderes
                        tablapuntos.mostrarTabla();

                        gameOverDialogShown = true;
                        state = STATE_GAME_OVER;

                        return;
                    }
                }
            }
        }
    }


    private void enviarScore(Jugador jugador) {
        System.out.printf("guardando puntuación...%n");
       Panas panas = new Panas();
        Cliente client = new Cliente();
        try {
            String serverIP = panas.getIp(); // IP del servidor
            int serverPort = panas.getPort(); // Puerto del servidor
            client.startConnection(serverIP, serverPort);

            // Enviar nombre y puntuación al servidor
            client.sendScore(jugador.getNombre(), jugador.getScore());
            System.out.printf("Puntuación guardada.%n");

            // Cerrar la conexión con el servidor
            client.stopConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);


        g.setColor(colors[7]);
        g.fillRect(0, 0, getWidth(), getHeight());
        currentPiezas.render(g);

        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[row].length; col++) {
                if (board[row][col] != null) {
                    g.setColor(board[row][col]);
                    g.fillRect(col * BLOCK_SIZE, row * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
                }

            }
        }


        //Se dibuja el tablero
        g.setColor(new Color(220, 220, 220)); // Gris claro
        for (int row = 0; row < TABLERO_HEIGHT; row++) {
            g.drawLine(0, BLOCK_SIZE * row, BLOCK_SIZE * TABLERO_WIDTH, BLOCK_SIZE * row);
        }

        for (int col = 0; col < TABLERO_WIDTH + 1; col++) {
            g.drawLine(col * BLOCK_SIZE, 0, col * BLOCK_SIZE, BLOCK_SIZE * TABLERO_HEIGHT);
        }



        //mensaje de perdiste
        if (state == STATE_GAME_OVER) {

            g.setColor(Color.black);
            FinJuego();
        }
        if (state == STATE_GAME_PAUSE) {
            g.setColor(Color.WHITE); // Establecer el color del texto a blanco
            Font font = new Font("Arial", Font.BOLD, 24); // Crear una nueva fuente con estilo negrita y tamaño 24
            g.setFont(font); // Establecer la fuente en el contexto gráfico
            g.drawString("PAUSA", 110, 300);
        }

        //se muestra la puntuacion del jugador
        g.setColor(Color.black);
        g.drawString("Puntuación: " + score, 10, 49);

        add(botonRegresar);

    }


    public Color[][] getTablero() {
        return board;
    }


    @Override
    public void keyTyped(KeyEvent e) {

    }

    //Funciones de las teclas

    private boolean isDownKeyPressed = false;

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();

        if (keyCode == KeyEvent.VK_DOWN) {
            currentPiezas.fast();
            isDownKeyPressed = true;
        } else if (keyCode == KeyEvent.VK_RIGHT) {
            currentPiezas.moveD();
        } else if (keyCode == KeyEvent.VK_LEFT) {
            currentPiezas.moveI();
        } else if (keyCode == KeyEvent.VK_UP) {
            currentPiezas.rotarPieza();
        }

        // Pausa
        if (keyCode == KeyEvent.VK_SPACE) {
            if (state == STATE_GAME_PLAY) {
                state = STATE_GAME_PAUSE;
            } else if (state == STATE_GAME_PAUSE) {
                state = STATE_GAME_PLAY;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            currentPiezas.slow();
            isDownKeyPressed = false;
        }
    }

    // Método de actualización del juego
    public void actu() {
        if (isDownKeyPressed) {
            currentPiezas.fast();
        } else {
            currentPiezas.slow();
        }
    }

}