import chess.*;
import communication.Menu;

import java.net.HttpURLConnection;
import java.net.URI;

public class Main {
    public static void main(String[] args) throws Exception {
        //var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        //System.out.println("♕ 240 Chess Client: " + piece);
        URI uri = new URI("http://localhost:8080/chess");
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
        http.setRequestMethod("GET");

        http.connect();
        Menu menu = new Menu();
    }
}