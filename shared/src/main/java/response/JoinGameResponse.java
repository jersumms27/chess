package response;

import chess.ChessGame;

public record JoinGameResponse(ChessGame game, String message) {
}
