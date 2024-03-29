package castle.comp3021.assignment;

import castle.comp3021.assignment.piece.Knight;
import castle.comp3021.assignment.protocol.*;
import org.jetbrains.annotations.NotNull;

/**
 * This class extends {@link Game}, implementing the game logic of JesonMor game.
 * Student needs to implement methods in this class to make the game work.
 * Hint: make good use of methods predefined in {@link Game} to get various information to facilitate your work.
 * <p>
 * Several sample tests are provided to test your implementation of each method in the test directory.
 * Please make make sure all tests pass before submitting the assignment.
 */
public class JesonMor extends Game {
    public JesonMor(Configuration configuration) {
        super(configuration);
    }

    /**
     * Start the game
     * Players will take turns according to the order in {@link Configuration#getPlayers()} to make a move until
     * a player wins.
     * <p>
     * In the implementation, student should implement the loop letting two players take turns to move pieces.
     * The order of the players should be consistent to the order in {@link Configuration#getPlayers()}.
     * {@link Player#nextMove(Game, Move[])} should be used to retrieve the player's choice of his next move.
     * After each move, {@link Game#refreshOutput()} should be called to refresh the gameboard printed in the console.
     * <p>
     * When a winner appears, set the local variable {@code winner} so that this method can return the winner.
     *
     * @return the winner
     */
    @Override
    public Player start() {
        // reset all things
        Player winner = null;
        this.numMoves = 0;
        this.board = configuration.getInitialBoard();
        this.currentPlayer = null;
        this.refreshOutput();
        while (true) {
            // TODO student implementation starts here
            this.currentPlayer = this.getPlayers()[numMoves % 2];  // current player
            Player lastPlayer = (numMoves % 2) == 0 ? this.getPlayers()[1] : this.getPlayers()[0];  // last player
            Move[] availMoves = this.getAvailableMoves(currentPlayer);
            if (availMoves.length == 0) {   // Tie Breaking Rule
                winner = currentPlayer.getScore() <= lastPlayer.getScore() ? currentPlayer : lastPlayer;
                System.out.println();
                System.out.println("Congratulations! ");
                System.out.printf("Winner: %s%s%s\n", winner.getColor(), winner.getName(), Color.DEFAULT);
                return winner;
            }

            Move currentMove = currentPlayer.nextMove(this, availMoves);
            Piece currentPiece = this.getPiece(currentMove.getSource());
            ++numMoves;
            this.movePiece(currentMove);
            this.updateScore(currentPlayer, currentPiece, currentMove);
            this.refreshOutput();
            winner = this.getWinner(currentPlayer, currentPiece, currentMove);
            // student implementation ends here

            if (winner != null) {
                System.out.println();
                System.out.println("Congratulations! ");
                System.out.printf("Winner: %s%s%s\n", winner.getColor(), winner.getName(), Color.DEFAULT);
                return winner;
            }
        }
    }


    /**
     * Get the winner of the game. If there is no winner yet, return null;
     * This method will be called every time after a player makes a move and after
     * {@link JesonMor#updateScore(Player, Piece, Move)} is called, in order to
     * check whether any {@link Player} wins.
     * If this method returns a player (the winner), then the game will exit with the winner.
     * If this method returns null, next player will be asked to make a move.
     *
     * @param lastPlayer the last player who makes a move
     * @param lastPiece  the last piece that is moved by the player
     * @param lastMove   the last move made by lastPlayer
     * @return the winner if it exists, otherwise return null
     */
    @Override
    public Player getWinner(Player lastPlayer, Piece lastPiece, Move lastMove) {
        // TODO student implementation
        int gameSizeX = this.getConfiguration().getSize();
        int gameSizeY = this.getConfiguration().getSize();
        if (lastPlayer != null && lastPiece != null && lastMove != null) {
            if (lastMove.getSource().equals(this.getCentralPlace()) && lastPiece instanceof Knight
                    && this.getNumMoves() > this.getConfiguration().getNumMovesProtection()) {  // Winning Condition 1
                return lastPlayer;
            } else {
                for (int i = 0; i < gameSizeX; i++) {
                    for (int j = 0; j < gameSizeY; j++) {
                        Piece getAPiece = this.getPiece(i, j);
                        if (getAPiece != null) {
                            if (!getAPiece.getPlayer().equals(lastPlayer)) {
                                return null;
                            } else if (i == gameSizeX - 1 && j == gameSizeY - 1
                                    && this.getNumMoves() > this.getConfiguration().getNumMovesProtection()) {  // Winning Condition 2
                                return lastPlayer;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * Update the score of a player according to the {@link Piece} and corresponding move made by him just now.
     * This method will be called every time after a player makes a move, in order to update the corresponding score
     * of this player.
     * <p>
     * The score of a player is the cumulative score of each move he makes.
     * The score of each move is calculated with the Manhattan distance between the source and destination {@link Place}.
     * <p>
     * Student can use {@link Player#getScore()} to get the current score of a player before updating.
     * {@link Player#setScore(int)} can be used to update the score of a player.
     * <p>
     * <strong>Attention: do not need to validate move in this method.</strong>
     *
     * @param player the player who just makes a move
     * @param piece  the piece that is just moved
     * @param move   the move that is just made
     */
    public void updateScore(Player player, Piece piece, Move move) {
        // TODO student implementation
        int currentScore = player.getScore();
        int distance = Math.abs(move.getDestination().x() - move.getSource().x())
                + Math.abs(move.getDestination().y() - move.getSource().y());
        player.setScore(currentScore + distance);
    }


    /**
     * Make a move.
     * This method performs moving a {@link Piece} from source to destination {@link Place} according {@link Move} object.
     * Note that after the move, there will be no {@link Piece} in source {@link Place}.
     * <p>
     * Positions of all {@link Piece}s on the gameboard are stored in {@link JesonMor#board} field as a 2-dimension array of
     * {@link Piece} objects.
     * The x and y coordinate of a {@link Place} on the gameboard are used as index in {@link JesonMor#board}.
     * E.g. {@code board[place.x()][place.y()]}.
     * If one {@link Place} does not have a piece on it, it will be null in {@code board[place.x()][place.y()]}.
     * Student may modify elements in {@link JesonMor#board} to implement moving a {@link Piece}.
     * The {@link Move} object can be considered valid on present gameboard.
     *
     * @param move the move to make
     */
    public void movePiece(@NotNull Move move) {
        // TODO student implementation
        Piece pieceToMove = this.getPiece(move.getSource().x(), move.getSource().y());
        this.board[move.getSource().x()][move.getSource().y()] = null;
        this.board[move.getDestination().x()][move.getDestination().y()] = pieceToMove;
    }

    /**
     * Get all available moves of one player.
     * This method is called when it is the {@link Player}'s turn to make a move.
     * It will iterate all {@link Piece}s belonging to the {@link Player} on board and obtain available moves of
     * each of the {@link Piece}s through method {@link Piece#getAvailableMoves(Game, Place)} of each {@link Piece}.
     * <p>
     * <strong>Attention: Student should make sure all {@link Move}s returned are valid.</strong>
     *
     * @param player the player whose available moves to get
     * @return an array of available moves
     */
    public @NotNull Move[] getAvailableMoves(Player player) {
        // TODO student implementation
        Move[] originalMoves = new Move[0];
        for (int x = 0; x < this.getConfiguration().getSize(); x++) {
            for (int y = 0; y < this.getConfiguration().getSize(); y++) {
                Piece getAPiece = this.getPiece(x, y);
                if (getAPiece != null && getAPiece.getPlayer().equals(player)) {
                    Move[] pieceOfMoves = getAPiece.getAvailableMoves(this, new Place(x, y));
                    Move[] createNewMoves = new Move[originalMoves.length + pieceOfMoves.length];
                    System.arraycopy(originalMoves, 0, createNewMoves, 0, originalMoves.length); // copying the old one
                    for (int i = originalMoves.length; i < createNewMoves.length; i++) {   // copying the new one
                        createNewMoves[i] = pieceOfMoves[i - originalMoves.length];
                    }
                    originalMoves = createNewMoves;
                }
            }
        }
        return originalMoves;
    }
}
