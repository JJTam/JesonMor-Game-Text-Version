package castle.comp3021.assignment.piece;

import castle.comp3021.assignment.protocol.Game;
import castle.comp3021.assignment.protocol.Move;
import castle.comp3021.assignment.protocol.Piece;
import castle.comp3021.assignment.protocol.Place;
import castle.comp3021.assignment.protocol.Player;

/**
 * Archer piece that moves similar to cannon in chinese chess.
 * Rules of move of Archer can be found in wikipedia (https://en.wikipedia.org/wiki/Xiangqi#Cannon).
 * <p>
 * <strong>Attention: If you want to implement Archer as the bonus task, you should remove "{@code throw new
 * UnsupportedOperationException();}" in the constructor of this class.</strong>
 *
 * @see <a href='https://en.wikipedia.org/wiki/Xiangqi#Cannon'>Wikipedia</a>
 */
public class Archer extends Piece {
    public Archer(Player player) {
        super(player);
    }

    @Override
    public char getLabel() {
        return 'A';
    }

    /**
     * Returns an array of moves that are valid given the current place of the piece.
     * Given the {@link Game} object and the {@link Place} that current knight piece locates, this method should
     * return ALL VALID {@link Move}s according to the current {@link Place} of this archer piece.
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
        Place[] placeOfDown = new Place[source.y()];
        Place[] placeOfLeft = new Place[source.x()];
        Place[] placeOfRight = new Place[game.getConfiguration().getSize() - 1 - source.x()];
        Place[] placeOfUp = new Place[game.getConfiguration().getSize() - 1 - source.y()];

        for (int y = source.y(); y > 0; y--) { // down side
            placeOfDown[source.y() - y] = new Place(source.x(), y - 1);
        }

        for (int x = source.x(); x > 0; x--) {  // left side
            placeOfLeft[source.x() - x] = new Place(x - 1,source.y());
        }

        for (int x = source.x(); x < game.getConfiguration().getSize() - 1; x++) {  // right side
            placeOfRight[x - source.x()] = new Place(x + 1,source.y());
        }

        for (int y = source.y(); y < game.getConfiguration().getSize() - 1; y++) {  // up side
            placeOfUp[y - source.y()] = new Place(source.x(), y + 1);
        }

        originalMoves = this.getJumpMoves(originalMoves, placeOfDown, game, source, false);
        originalMoves = this.getJumpMoves(originalMoves, placeOfLeft, game, source, false);
        originalMoves = this.getJumpMoves(originalMoves, placeOfRight, game, source, false);
        originalMoves = this.getJumpMoves(originalMoves, placeOfUp, game, source, false);

        return originalMoves;
    }

    /**
     * Helper method of the move operation of jumping
     * ***
     * @param originalMoves the original move array
     * @param placeOfDirection 4 directions {Down, Left, Right, Up}
     * @param game the game object
     * @param source the current place of the piece
     * @param needJump whether need a jump
     * @return the updated array of available moves
     */
    private Move[] getJumpMoves (Move[] originalMoves, final Place [] placeOfDirection,
                                 final Game game, final Place source, Boolean needJump) {
        for (int i = 0; i < placeOfDirection.length; i++) {  // move downwards operation
            Place downSide = placeOfDirection[i];
            Piece getDestinationPiece = game.getPiece(downSide);
            if (getDestinationPiece == null && !needJump) { // move straight
                Move[] createNewMoves = new Move[originalMoves.length + 1];
                System.arraycopy(originalMoves, 0, createNewMoves, 0, originalMoves.length);
                createNewMoves[originalMoves.length] = new Move(source, downSide);
                originalMoves = createNewMoves;
            } else if (getDestinationPiece != null && !needJump) {  // jumping
                needJump = true;
                for (int j = i + 1; j < placeOfDirection.length; j++) {
                    Place jumpDownSide = placeOfDirection[j];
                    Piece getDestinationJumpPiece = game.getPiece(jumpDownSide);
                    if (getDestinationJumpPiece != null
                            && !getDestinationJumpPiece.getPlayer().equals(game.getCurrentPlayer())
                            && game.getNumMoves() > game.getConfiguration().getNumMovesProtection()) {
                        Move[] createNewMoves = new Move[originalMoves.length + 1];
                        System.arraycopy(originalMoves, 0, createNewMoves, 0, originalMoves.length);
                        createNewMoves[originalMoves.length] = new Move(source, jumpDownSide);
                        originalMoves = createNewMoves;
                        break;
                    }
                }
            }
        }
        return originalMoves;
    }

}