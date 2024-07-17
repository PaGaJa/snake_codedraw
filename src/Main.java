import codedraw.*;



public class Main {

    //main function used for opening a game and running it with codedraw interface

    public static void main(String[] args) {


        Game game = new Game();
        int GAMEHEIGHT = game.getGAMEHEIGHT();
        int GAMEWIDTH = game.getGAMEWIDTH();


        game.placeFood();

        CodeDraw.run(game, GAMEWIDTH, GAMEHEIGHT+ game.TILESIZE, 8);


    }

}


