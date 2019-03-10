import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Sudoku {

    private static final String FILE_NAME = "data/theory.cnf";

    public static void createTheory(List<Integer> givenValues) {
        int clausesNumber = 0;
        int variablesNumber = 729;
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < givenValues.size(); i++) {
            sb.append(givenValues.get(i));
            sb.append(" 0\n");
            clausesNumber++;
        }

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                for (int k = 1; k <= 9; k++) {
                    append(i, j, k, sb, false);
                }
                sb.append("0\n");

                for (int k = 1; k <= 9; k++) {
                    append(i, k - 1, j + 1, sb, false);
                }
                sb.append("0\n");

                for (int k = 1; k <= 9; k++) {
                    append(k - 1, i, j + 1, sb, false);
                }
                sb.append("0\n");
                clausesNumber += 3;

                for (int k = 1; k <= 9; k++) {
                    for (int t = 1; t <= 9; t++) {
                        if (t != k) {
                            append(i, j, k, sb, true);
                            append(i, j, t, sb, true);
                            sb.append("0\n");

                            append(i, k - 1, j + 1, sb, true);
                            append(i, t - 1, j + 1, sb, true);
                            sb.append("0\n");

                            append(k - 1, i, j + 1, sb, true);
                            append(t - 1, i, j + 1, sb, true);
                            sb.append("0\n");

                            clausesNumber += 3;
                        }
                    }
                }
            }
        }

        for (int m = 0; m <= 2; m++) {
            for (int n = 0; n <= 2; n++) {
                for (int k = 1; k <= 9; k++) {
                    for (int i = 0; i <= 2; i++) {
                        for (int j = 0; j <= 2; j++) {
                            for (int a = 0; a <= 2; a++) {
                                for (int b = 0; b <= 2; b++) {
                                    if (a != i || b != j) {
                                        append(3 * m + i, 3 * n + j, k, sb, true);
                                        append(3 * m + a, 3 * n + b, k, sb, true);
                                        sb.append("0\n");
                                        clausesNumber++;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        for (int m = 0; m <= 2; m++) {
            for (int n = 0; n <= 2; n++) {
                for (int k = 1; k <= 9; k++) {
                    for (int i = 0; i <= 2; i++) {
                        for (int j = 0; j <= 2; j++) {
                            append(3 * m + i, 3 * n + j, k, sb, false);
                        }
                    }
                    sb.append("0\n");
                    clausesNumber++;
                }
            }
        }

        sb.insert(0, "p cnf " + variablesNumber + " " + clausesNumber + "\n");

        try {
            FileWriter writer = new FileWriter(FILE_NAME);
            writer.write(sb.toString());
            writer.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Integer> loadFileData(String fileName) {
        ArrayList<Integer> givenValues = new ArrayList<>();

        try {
            BufferedReader in = new BufferedReader(new FileReader(fileName));
            int i = 0;
            while (in.ready()) {
                int j = 0;
                String[] strings = in.readLine().split(" ");
                for (String s : strings) {
                    if (!s.matches("[._]")) {
                        givenValues.add(getValue(i, j, Integer.parseInt(s)));
                    }
                    j++;
                }
                i++;
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return givenValues;
    }

    private static void append(int i, int j, int k, StringBuilder sb, boolean inverted) {
        if (inverted) {
            sb.append("-");
        }
        sb.append(getValue(i, j, k));
        sb.append(" ");
    }

    private static int getValue(int i, int j, int k) {
        return i * 81 + j * 9 + k;
    }

    public static void printSudoku(int[][] values) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                System.out.print(values[i][j] + " ");
            }
            System.out.println();
        }
    }
}