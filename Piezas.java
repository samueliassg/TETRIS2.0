import java.awt.Color;
import java.awt.Graphics;


public class Piezas {
    private int x = 4, y = 0;

    //Se definen aqui las velocidades en las que baja la pieza
    private int normal = 600;
    private int fast = 50;
    private int delayTimeForMovement = normal;
    private long beginTime; 

    private int deltaX = 0;
    private boolean collision = false;
    private int [][] coords;
    private Tablero board;
    private Color color;


    public Piezas(int[][] coords,Tablero board, Color color){
        this.coords= coords;
        this.board=board;
        this.color=color;
    }
    public void setX(int x) {
        this.x=x;
    }
    public void setY(int y) {
        this.y=y;
    }
    public void reset() {
        this.x=4;
        this.y=0;
        collision=false;
    }


    public void update(){
        if(collision){
            
        for( int row=0; row<coords.length; row++){
            for(int col=0; col<coords[0].length; col++){
                if(coords[row][col] !=0){
                    board.getTablero()[y+ row][x+col]= color;
                }
            }
        }    
            checkline(board);
            //set current shape (eso esta en el tablero, son clases y blabla)
            board.setCurrentShape();
            return;
        }
        //check movimientos horizontales
        boolean moveX=true;
        if(!(x+deltaX+coords[0].length>10)&&!(x+deltaX<0)){
            for(int row=0; row<coords.length;row++){
                for(int col=0; col<coords[row].length;col++){
                    if(coords[row][col]!=0){
                        if(board.getTablero()[y+row][x+deltaX+col] !=null){
                            moveX=false;
                        }
                        
                    }
                }   
            }
            if(moveX){
                x+=deltaX;
            }
        }
        deltaX=0;
        //check movimientos verticales
        if(System.currentTimeMillis()- beginTime>delayTimeForMovement){
            //movimiento vertical
            if(!(y+1+coords.length>Tablero.TABLERO_HEIGHT)){
                for(int row=0; row<coords.length;row++){
                    for(int col=0; col<coords[row].length;col++){
                        if(coords[row][col] !=0){
                            if(board.getTablero()[y+1+row][x+deltaX+col] !=null){
                                collision=true;
                            }
                        }
                    }
                }
                if(!collision){y++;}
                
            } else{
                collision=true;
            }
            beginTime=System.currentTimeMillis();
        }

        //chequear movimiento horizontalmente
        if(!(x + deltaX + coords[0].length > 10) && !(x + deltaX < 0)){
        x += deltaX; 
        }
        deltaX = 0;

        if(System.currentTimeMillis() - beginTime > delayTimeForMovement){    
            if(!(y + 1 + coords.length > Tablero.TABLERO_HEIGHT)){
            y++; 
            }
            else {
                collision = true;
            }
            
            beginTime = System.currentTimeMillis();
        }

    } 
   
    private void checkline(Tablero tablero) {
        int bottomline = tablero.getTablero().length - 1;
        int linesCleared = 0;
    
        for (int topLine = tablero.getTablero().length - 1; topLine > 0; topLine--) {
            int count = 0;
            for (int col = 0; col < tablero.getTablero()[0].length; col++) {
                if (tablero.getTablero()[topLine][col] != null) {
                    count++;
                }
                tablero.getTablero()[bottomline][col] = tablero.getTablero()[topLine][col];
            }
            if (count < tablero.getTablero()[0].length) {
                bottomline--;
            } else {
                linesCleared++;
            }
        }
    
        // Actualizar la puntuación del tablero
        if (linesCleared > 0) {
            int currentScore = tablero.getScore();
            int newScore = currentScore + calculateScore(linesCleared);
            tablero.setScore(newScore);
        }
    }
    
    //Logica para la puntuacion de los jugadores
    private int calculateScore(int linesCleared) {
        int baseScore = 1; // Puntuación base por línea eliminada
        int comboMultiplier = 1; // Multiplicador de combo (se incrementa si se eliminan múltiples líneas consecutivas)
        
        // Aplica un multiplicador de combo si se eliminan múltiples líneas consecutivas
        if (linesCleared > 1) {
            comboMultiplier = linesCleared; // El multiplicador de combo es igual a la cantidad de líneas eliminadas
        }
        
        // Calcula la puntuación final
        int score = baseScore * linesCleared * comboMultiplier;
        
        return score;
    }


    public void rotarPieza() {
        int[][] rotarPieza= transposeMatrix(coords);
        reverseRows(rotarPieza);
        //check para el lado y fondo correcto
        if((x+rotarPieza[0].length> Tablero.TABLERO_WIDTH) || (y+rotarPieza.length>20)){
            return;
        }
        //check para las colisiones entre piezas (bug)
        for(int row=0; row<rotarPieza.length; row++){
            for(int col=0; col<rotarPieza[row].length;col++){
                if(board.getTablero()[y+row][x+col] !=null){
                    return;
                }
            }
        }
        coords=rotarPieza;
    }
    private int[][] transposeMatrix(int[][] matrix) {
        int[][] temp = new int[matrix[0].length][matrix.length];
        for(int row=0;row<matrix.length;row++){
            for(int col=0;col<matrix[0].length;col++){
                temp[col][row]=matrix[row][col];
            }
        }
        return temp;
    }
    private void reverseRows(int[][] matrix) {
        int middle= matrix.length/2;
        for(int row=0; row<middle;row++){
            int[] temp=matrix[row];
            matrix[row]=matrix[matrix.length-row-1];
            matrix[matrix.length-row-1]=temp;
        }
    }
   
    public void render(Graphics g) {
        
        //Se dibujan las figuras
        for(int row = 0; row< coords.length; row++){
            for(int col = 0; col< coords[0].length; col++){
                if(coords[row][col] !=0) {
                    g.setColor(color);
                    g.fillRect(col * Tablero.BLOCK_SIZE + x * Tablero.BLOCK_SIZE, row * Tablero.BLOCK_SIZE + y * Tablero.BLOCK_SIZE, Tablero.BLOCK_SIZE, Tablero.BLOCK_SIZE);
                }
                
            }
        }

    }

    public int[][] getCoord(){
        return coords;
    }

    public void fast() {
        delayTimeForMovement = fast;
    }
    public void slow() {
        delayTimeForMovement = normal;
    }
    public void moveD() {
        deltaX = 1;
    }
    public void moveI() {
        deltaX = -1;
    }
    public int getY(){
        return y;
    }
    public int getX(){
        return x;
    }
}