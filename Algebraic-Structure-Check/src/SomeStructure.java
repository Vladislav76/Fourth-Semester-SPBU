import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class SomeStructure {

    private int[][] mCayleyTable;

    public int[][] getCayleyTable() {
        return mCayleyTable;
    }

    private void setCayleyTable(int[][] table) {
        mCayleyTable = table;
    }

    public static SomeStructure newInstance(String fileName) {
        SomeStructure structure = new SomeStructure();
        int[][] table = null;

        try {
            BufferedReader in = new BufferedReader(new FileReader(fileName));
            int k = 0;
            int size = 0;

            while (in.ready()) {
                String[] strings = in.readLine().split(" ");

                if (k == 0) {
                    size = strings.length;
                    table = new int[size][size];
                }

                if (strings.length == size) {
                    for (int i = 0; i < strings.length; i++) {
                        table[k][i] = Integer.parseInt(strings[i]);
                    }
                } else {
                    throw new IllegalStateException("Incorrect input format");
                }
                k++;
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        structure.setCayleyTable(table);
        return structure;
    }
}
