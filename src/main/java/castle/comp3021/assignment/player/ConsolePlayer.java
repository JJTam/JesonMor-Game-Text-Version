package castle.comp3021.assignment.player;

import castle.comp3021.assignment.protocol.*;
import org.jetbrains.annotations.NotNull;

import java.util.Scanner;


/**
 * The player that makes move according to user input from console.
 */
public class ConsolePlayer extends Player {
    public ConsolePlayer(String name, Color color) {
        super(name, color);
    }

    public ConsolePlayer(String name) {
        this(name, Color.GREEN);
    }

    /**
     * Choose a move from available moves.
     * This method will be called by {@link Game} object to get the move that the player wants to make when it is the
     * player's turn.
     * <p>
     * {@link ConsolePlayer} returns a move according to user's input in the console.
     * The console input format should conform the format described in the assignment description.
     * (e.g. {@literal a1->b3} means move the {@link Piece} at {@link Place}(x=0,y=0) to {@link Place}(x=1,y=2))
     * Note that in the {@link Game}.board, the index starts from 0 in both x and y dimension, while in the console
     * display, x dimension index starts from 'a' and y dimension index starts from 1.
     * <p>
     * Hint: be sure to handle invalid input to avoid invalid {@link Move}s.
     * <p>
     * <strong>Attention: Student should make sure the {@link Move} returned is valid.</strong>
     * <p>
     * <strong>Attention: {@link Place} object uses integer as index of x and y-axis, both starting from 0 to
     * facilitate programming.
     * This is VERY different from the coordinate used in console display.</strong>
     *
     * @param game           the current game object
     * @param availableMoves available moves for this player to choose from.
     * @return the chosen move
     */
    @Override
    public @NotNull Move nextMove(Game game, Move[] availableMoves) {
        // TODO student implementation
        while (true) {
            System.out.printf("[%s] Make a Move: ", game.getCurrentPlayer().getName());
            Scanner scanner = new Scanner(System.in);
            String userInput = scanner.nextLine();
            if (userInput.contains("->")) {
                String[] inputArray = userInput.toLowerCase().split("->");
                if (inputArray.length == 2) {
                    String inputA = inputArray[0].strip();
                    String inputB = inputArray[1].strip();
                    if (!inputA.isBlank() && !inputB.isBlank()
                            && !inputA.contains(" ") && !inputB.contains(" ") && inputA.length() >= 2 && inputB.length() >= 2) {
                        char letterA = inputA.toCharArray()[0];
                        char letterB = inputB.toCharArray()[0];
                        String stringToDigitA = inputA.substring(1);
                        String stringToDigitB = inputB.substring(1);
                        if (Character.isLetter(letterA) && Character.isLetter(letterB)) {   // must start with letter
                            int digitA = 0;
                            int digitB = 0;
                            for (int i = 0; i < stringToDigitA.length(); i++) {
                                if (!Character.isDigit(stringToDigitA.toCharArray()[i])) {
                                    break;
                                }
                                if (i == stringToDigitA.length() - 1) {
                                    digitA = Integer.parseInt(stringToDigitA);
                                }
                            }

                            for (int i = 0; i < stringToDigitB.length(); i++) {
                                if (!Character.isDigit(stringToDigitB.toCharArray()[i])) {
                                    break;
                                }
                                if (i == stringToDigitB.length() - 1) {
                                    digitB = Integer.parseInt(stringToDigitB);
                                }
                            }

                            if (digitA != 0 && digitB != 0) {
                                int convertedLetterA = letterA - 97;  // ascii code
                                int convertedLetterB = letterB - 97;
                                int convertedDigitA = digitA - 1;
                                int convertedDigitB = digitB - 1;
                                for (var move : availableMoves) {
                                    if (move.getSource().x() == convertedLetterA
                                            && move.getSource().y() == convertedDigitA
                                            && move.getDestination().x() == convertedLetterB
                                            && move.getDestination().y() == convertedDigitB) {
                                        System.out.printf("%s moved piece at s(%d, %d) to s(%d, %d)\n",
                                                game.getCurrentPlayer().getName(),
                                                convertedLetterA, convertedDigitA, convertedLetterB, convertedDigitB);
                                        return move;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}
