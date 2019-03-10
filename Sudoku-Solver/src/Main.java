import java.util.List;

public class Main {

    public static void main(String[] args) {
       runSolver("data/sudoku_1");
    }

    public static void runSolver(String fileName) {
        List<Integer> data = Sudoku.loadFileData(fileName);
        Sudoku.createTheory(data);
        int[][] values = Solver.solve("data/theory.cnf");
        if (values != null) {
            Sudoku.printSudoku(values);
        }
    }
}
