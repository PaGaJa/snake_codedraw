import codedraw.*;
import codedraw.Image;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Game implements Animation {


    //game object variables
    Food food;
    Snake snake;
    int GAMEHEIGHT;
    int GAMEWIDTH;
    int TILESIZE;
    int velocityX;
    int velocityY;

    int score;

    //"Game"-object constructor
    public Game(){


        this.snake  = new Snake(6,6);
        GAMEHEIGHT = 400;
        GAMEWIDTH = GAMEHEIGHT;
        TILESIZE = 20;
        velocityX = 0;
        velocityY = 0;


    }

    //main game loop
    //clears and draws all objects on the frame aswell as checking collisions
    @Override
    public void draw(Image canvas) {


        canvas.clear();

        canvas.setColor(Color.black);
        canvas.fillRectangle(0, 0, GAMEWIDTH, GAMEHEIGHT);

        canvas.drawText(5, GAMEHEIGHT+4, "Score: " + score);


        for (int i = 0; i < GAMEHEIGHT; i += TILESIZE) {
            canvas.setColor(Color.gray);
            canvas.drawLine(i, 0, i, GAMEHEIGHT);
            canvas.drawLine(0, i, GAMEHEIGHT, i);
        }

        moveSnake();
        drawSnake((CodeDraw) canvas);

        drawFood((CodeDraw) canvas);

        if (!(checkCollision(this))){
            gameOver((CodeDraw) canvas);
        }

    }




    //reads keyboard presses and changes the direction of movement based on the keys
    // W,A,S,D aswell as arrow keys for direction
    //R for restarting the game
    @Override
    public void onKeyDown(KeyDownEvent event) {

        if ((event.getKey() == Key.W || event.getKey() == Key.UP) && velocityY != 1){
            velocityX = 0;
            velocityY = -1;
        }
        else if ((event.getKey() == Key.A || event.getKey() == Key.LEFT) && velocityX != 1) {
            velocityX = -1;
            velocityY = 0;
        }
        else if ((event.getKey() == Key.S || event.getKey() == Key.DOWN) && velocityY != -1) {
            velocityX = 0;
            velocityY = 1;
        }
        else if ((event.getKey() == Key.D || event.getKey() == Key.RIGHT) && velocityX != -1) {
            velocityX = 1;
            velocityY = 0;
        }
        else if(event.getKey() == Key.R){

           velocityY = 0;
           velocityX = 0;
           this.snake = new Snake(6,6);

            this.placeFood();

            this.score = 0;

        }

    }

    //"Feld" = field, used to divide the game window into whole number coordinates
    private class Feld{
        int x;
        int y;
        public Feld(int x, int y){
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }
    }

    //food object
    private class Food{
        private Feld place;
        public Food(Feld place){
            this.place = place;
        }


    }

    //snake object
    private class Snake{
        
       private ArrayList<Feld> parts = new ArrayList<>();
       Feld head;
        
        public Snake(int x, int y){
            Feld temp = new Feld(x,y);
            //Feld temp2 = new Feld(x-1, y);
            //Feld temp3 = new Feld(x-2,y );
            
            parts.add(temp);
            //parts.add(temp2);
            //parts.add(temp3);
            head = parts.getFirst();
            
        }

    }

    //moves parts of the snake and then the head, based on the current direction
    public void moveSnake(){

        for (int i = snake.parts.size()-1; i >= 0 ; i--) {
            Feld tempPart = snake.parts.get(i);
            if(i == 0){
                tempPart.x = snake.head.x;
                tempPart.y = snake.head.y;

            }
            else{
                tempPart.x = snake.parts.get(i-1).x;
                tempPart.y = snake.parts.get(i-1).y;

            }

        }

        this.snake.head.x += velocityX;
        this.snake.head.y += velocityY;

    }
    
    public void drawSnake(CodeDraw game){
        
        game.setColor(Color.green);

        for (int i = 0; i < snake.parts.size() ; i++) {
            Feld temp = snake.parts.get(i);
            game.fillRectangle(temp.getX()*TILESIZE, temp.getY()*TILESIZE, TILESIZE-1, TILESIZE-1 );
        }
    }

    //gets random coordinates for food and place it
    public void placeFood(){

        Random ran = new Random();
        int x = ran.nextInt(GAMEHEIGHT / TILESIZE);
        int y = ran.nextInt(GAMEHEIGHT / TILESIZE);

        this.food = new Food(new Feld(x,y));

    }

    public void drawFood(CodeDraw game){

        game.setColor(Color.red);
        game.fillCircle((this.food.place.getX()*TILESIZE)+10, (this.food.place.getY()*TILESIZE)+10, TILESIZE/2);


    }

    //checks collisions for food or game over
    public boolean checkCollision(Game game){

        boolean bodyCollision = false;

        //iterate trough parts of the snake
        if (snake.parts.size() > 1) {
            for (int i = 1; i < snake.parts.size(); i++) {
                if (snake.head.x == snake.parts.get(i).x && snake.head.y == snake.parts.get(i).y)
                    bodyCollision = true;
            }
        }

        //check cases
        if(game.snake.head.getX() >= GAMEWIDTH/TILESIZE || game.snake.head.getX() < 0){
            return false;
        }
        else if (game.snake.head.getY() >= GAMEHEIGHT/TILESIZE || game.snake.head.getY() < 0){
            return false;
        }
        else if(bodyCollision){
            return false;
        }
        else if (game.snake.head.getX() == food.place.getX() && game.snake.head.getY() == food.place.getY() ) {
            snake.parts.add(new Feld(food.place.x, food.place.y));
            game.placeFood();
            score += 100;
            return true;
        }


        /*
        for (int i = 1; i < game.snake.parts.size(); i++) {

            if (game.snake.head == game.snake.parts.get(i) ){
                return false;
            }
        }
         */


        return true;

    }

    public void gameOver(CodeDraw canvas ){

        //teleport head away, so it cant run into frame
        snake.head.x = 10000;

        canvas.setColor(Color.black);
        canvas.fillRectangle(0, 0, GAMEWIDTH, GAMEHEIGHT+TILESIZE);


        canvas.setColor(Color.red);
        TextFormat format = canvas.getTextFormat();
        int temp = format.getFontSize();
        format.setBold(true);
        format.setFontSize(60);
        format.setTextOrigin(TextOrigin.TOP_MIDDLE);

        canvas.drawText(GAMEWIDTH/2, (GAMEHEIGHT/2)-30,"Game Over");

        format.setBold(false);
        format.setFontSize(14);
        canvas.drawText(GAMEWIDTH/2, (GAMEHEIGHT/2)+80, "Score: " + score + "\n to restart Game press 'R'");




        //reset Font settings
        format.setTextOrigin(TextOrigin.TOP_LEFT);
        format.setFontSize(temp);
        format.setBold(false);


    }


    public int getGAMEHEIGHT(){ return this.GAMEHEIGHT;}

    public int getGAMEWIDTH(){ return this.GAMEWIDTH;}

    public int getTILESIZE(){ return this.TILESIZE;}

}
