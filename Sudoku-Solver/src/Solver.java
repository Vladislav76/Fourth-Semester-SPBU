import org.sat4j.minisat.SolverFactory;
import org.sat4j.reader.DimacsReader;
import org.sat4j.reader.Reader;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.IProblem;
import org.sat4j.specs.ISolver;

public class Solver {

    public static int[][] solve(String fileName) {
        ISolver solver = SolverFactory.newDefault();

        Reader reader = new DimacsReader(solver);
        try {
            IProblem problem = reader.parseInstance(fileName);
            if (problem.isSatisfiable()) {
                System.out.println("Satisfiable!");
                return getSolution(problem.model());
            }
            else {
                System.out.println("Unsatisfiable!");
            }
        }
        catch (ContradictionException e) {
            System.out.println("Unsatisfiable!");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static int[][] getSolution(int[] model) {
        int[][] solution = new int[9][9];
        for (int p = 0; p < 729; p++) {
            if (model[p] > 0) {
                int i = p / 81;
                int j = p % 81 / 9;
                int k = p % 9 + 1;
                solution[i][j] = k;
            }
        }
        return solution;
    }
}
