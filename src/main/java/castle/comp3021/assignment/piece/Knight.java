package castle.comp3021.assignment.piece;

import castle.comp3021.assignment.protocol.Game;
import castle.comp3021.assignment.protocol.Move;
import castle.comp3021.assignment.protocol.Piece;
import castle.comp3021.assignment.protocol.Place;
import castle.comp3021.assignment.protocol.Player;

/**
 * Knight piece that moves similar to knight in chess.
 * Rules of move of Knight can be found in wikipedia (https://en.wikipedia.org/wiki/Knight_(chess)).
 *
 * @see <a href='https://en.wikipedia.org/wiki/Knight_(chess)'>Wikipedia</a>
 */
public class Knight extends Piece {
    public Knight(Player player) {
        super(player);
    }

    @Override
    public char getLabel() {
        return 'K';
    }

    /**
     * Returns an array of moves that are valid given the current place of the piece.
     * Given the {@link Game} object and the {@link Place} that current knight piece locates, this method should
     * return ALL VALID {@link Move}s according to the current {@link Place} of this knight piece.
     * All the returned {@link Move} should have source equal to the source parameter.
     * <p>
     * Hint: you should consider corner cases when the {@link Move} is not valid on the gameboard.
     * Several tests are provided and your implementation should pass them.
     * <p>
     * <strong>Attention: Student should make sure all {@link Move}s returned are valid.</strong>
     *
     * @param game   the game object
     * @param source the current place of the piece
     * @return an array of available moves
     */
    @Override
    public Move[] getAvailableMoves(Game game, Place source) {
        // TODO student implementation
        Move[] originalMoves = new Move[0];
        for (int x = 0; x < game.getConfiguration().getSize(); x++) {
            for (int y = 0; y < game.getConfiguration().getSize(); y++) {
                Place destination = new Place(x, y);
                int distance = Math.abs(source.x() - destination.x()) + Math.abs(source.y() - destination.y());
                if (distance == 3) {   // Knight's rule
                    int differX = source.x() - destination.x();
                    int differY = source.y() - destination.y();
                    int blockX = -1;
                    int blockY = -1;
                    if (differX == 1 || differX == -1) {    // check whether Knight be blocked
                        blockX = source.x();
                        if (differY > 0) {
                            blockY = source.y() - 1;
                        } else {
                            blockY = source.y() + 1;
                        }
                    } else if (differY == 1 || differY == -1) {
                        blockY = source.y();
                        if (differX > 0) {
                            blockX = source.x() - 1;
                        } else {
                            blockX = source.x() + 1;
                        }
                    }

                    if (blockX != -1 && blockY != -1) {
                        Piece getBlockedPiece = game.getPiece(blockX, blockY);
                        Piece getDestinationPiece = game.getPiece(destination);
                        if (getBlockedPiece == null) {
                            if (getDestinationPiece == null
                                    || (!getDestinationPiece.getPlayer().equals(game.getCurrentPlayer())
                                    && game.getNumMoves() > game.getConfiguration().getNumMovesProtection())) { // protection
                                Move[] createNewMoves = new Move[originalMoves.length + 1];
                                System.arraycopy(originalMoves, 0, createNewMoves, 0, originalMoves.length);
                                createNewMoves[originalMoves.length] = new Move(source, destination);
                                originalMoves = createNewMoves;
                            }
                        }
                    }
                }
            }
        }
        return originalMoves;
    }
}
