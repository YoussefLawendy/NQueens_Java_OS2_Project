package MultiThreadedNQueensSolver;
import java.util.Scanner;
import java.awt.Color;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
/**
 *
 * @author joola
 */

public class MultiThreadedNQueensSolver {
    private chessGUI myBoard;
    private int[][] board;
    private int boardSize;
    private final Object lock = new Object();
    public MultiThreadedNQueensSolver(chessGUI myBoard,int boardSize){
        this.myBoard=myBoard;
        this.boardSize=boardSize;
        this.board = new int[boardSize][boardSize];
    }


    private boolean isSafe(int row, int col) {
         //  checker  yenfa3 a7ot queen wala la
        for (int i = 0; i < col; i++) {
             // byshof el queens ely mawgoden abl kda 34an y3rf y7ot elqueen elgdeda fen
            if (board[row][i] == 1) {
                return false;
            }
        }

        for (int i = row, j = col; i >= 0 && j >= 0; i--, j--) {
              // check diadonal 
            if (board[i][j] == 1) {
                return false;
            }
        }

        for (int i = row, j = col; i < board.length && j >= 0; i++, j--) {
            if (board[i][j] == 1) {
                return false;
            }
        }

        return true;
    }

    private void  printFinalQueenSolution() {
        synchronized (lock) {
            // da el code ely byzhar fel Running t7t 
            ///////////////////////////////////////////////////////////////////////

            //for (int[] row : board) {
                      // enter in row to fill
            //    for (int cell : row) {
                        // fill every cell in this row 
            //        System.out.print(cell + " ");
            //    }
            //    System.out.println();
            //}
            //System.out.println();

            ///////////////////////////////////////////////////////////////////////
            
            try{
                Thread.sleep(200);
                myBoard.updateChess(board,Thread.currentThread().getName());
            }catch(Exception ex){
                ///
            }
            
        }
    }

    private boolean IsAllQueensIsAllocated(int col) {
            
        // btd5l el queens w ttcheck colum colum 
        if (col == board.length) {
            printFinalQueenSolution();
            return true;  // Indicate that a solution is found
        }
            printFinalQueenSolution();
        // bnbda2 hna n insert lw zero fa mkan fadi laken lw wa7ed yb2a bn3ml
        // backtracking 34an hya kda mlyana w msh hynf3 nzwd
        for (int i = 0; i < board.length; i++) {
            if (isSafe(i, col)) {
                board[i][col] = 1;
                if( IsAllQueensIsAllocated(col + 1)) {
                    
                    return true;  // Propagate the success signal
                }
                board[i][col] = 0;// backtrack 
            }

            
        }

        return false;  // Indicate that no solution is found in this branch
    }

    private void interruptOtherThreads(Thread[] threads, Thread currentThread) {
        // awl ma ytl3li el answer by3ml interruption lba2y el threads la2nna
        //msh m7tagenhom 5las fa mlhomsh lazma enohm ysht8lo delwa2ty
        for (Thread thread : threads) {
            if (thread != currentThread && thread.isAlive()) {
                // Interrupt other alive threads
                thread.interrupt();
            }
        }
    }

    public void solveNQueensbythreading() {
        // enna bnsh8l elthreads 3ala 3add el boardSize like 8x8 then 8 threads
        //b3d kda byshof hal kol queen fe mkanha wala la
        //lw fe mkanha yb2a 5las howa da el7l e3ml interrupt ll threads 
        // lw la2 yb2a e3ml 
        Thread[] threads = new Thread[this.boardSize];

        for (int i = 0; i < this.boardSize; i++) {
            final int row = i;
            threads[i] = new Thread(() -> {
                board[row][0] = 1;
                if (IsAllQueensIsAllocated(1)) {
                    // If this thread found a solution, interrupt other threads
                    interruptOtherThreads(threads, Thread.currentThread());
                }
                
                board[row][0] = 0;
            });
                threads[i].setName("Thread " + i);
            threads[i].start();
        }
    }
}
