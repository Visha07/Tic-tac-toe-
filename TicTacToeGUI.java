import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TicTacToeGUI {
    private JFrame frame;
    private JButton[][] buttons;
    private Player player1, player2, currentPlayer;
    private TicTacToe game;

    public TicTacToeGUI() {
        game = new TicTacToe();
        frame = new JFrame("Tic Tac Toe");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);
        buttons = new JButton[3][3];
        frame.setLayout(new GridLayout(3, 3));

        player1 = new HumanPlayer("Sana", 'X');
        player2 = new AIPlayer("AI", 'O');
        currentPlayer = player1;

        initializeButtons();
        frame.setVisible(true);
    }

    private void initializeButtons() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j] = new JButton();
                buttons[i][j].setFont(new Font("Arial", Font.BOLD, 60));
                buttons[i][j].setFocusPainted(false);
                int row = i, col = j; // Capture row and col for use in lambda
                buttons[i][j].addActionListener(e -> handleMove(row, col));
                frame.add(buttons[i][j]);
            }
        }
    }

    private void handleMove(int row, int col) {
        if (!currentPlayer.isValidMove(row, col)) {
            JOptionPane.showMessageDialog(frame, "Invalid move. Try again!");
            return;
        }

        // Place the mark and update GUI
        TicTacToe.placeMark(row, col, currentPlayer.mark);
        buttons[row][col].setText(String.valueOf(currentPlayer.mark));
        buttons[row][col].setEnabled(false);

        // Check for win or draw
        if (TicTacToe.checkColWin() || TicTacToe.checkRowWin() || TicTacToe.checkDiagonalWin()) {
            JOptionPane.showMessageDialog(frame, currentPlayer.name + " wins!");
            resetGame();
        } else if (TicTacToe.checkDraw()) {
            JOptionPane.showMessageDialog(frame, "It's a draw!");
            resetGame();
        } else {
            // Switch players
            currentPlayer = (currentPlayer == player1) ? player2 : player1;

            // AI Move if currentPlayer is AI
            if (currentPlayer instanceof AIPlayer) {
                ((AIPlayer) currentPlayer).makeMove();
                handleAIMove();
            }
        }
    }

    private void handleAIMove() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (TicTacToe.board[i][j] != ' ' && buttons[i][j].getText().equals("")) {
                    buttons[i][j].setText(String.valueOf(TicTacToe.board[i][j]));
                    buttons[i][j].setEnabled(false);
                }
            }
        }

        if (TicTacToe.checkColWin() || TicTacToe.checkRowWin() || TicTacToe.checkDiagonalWin()) {
            JOptionPane.showMessageDialog(frame, currentPlayer.name + " wins!");
            resetGame();
        } else if (TicTacToe.checkDraw()) {
            JOptionPane.showMessageDialog(frame, "It's a draw!");
            resetGame();
        } else {
            currentPlayer = player1; // Switch back to human player
        }
    }

    private void resetGame() {
        game.initBoard();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setText("");
                buttons[i][j].setEnabled(true);
            }
        }
        currentPlayer = player1; // Start with player 1 again
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TicTacToeGUI::new);
    }
}
