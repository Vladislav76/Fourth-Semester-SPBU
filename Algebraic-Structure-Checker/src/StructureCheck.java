public class StructureCheck {

    public static final int UNKNOWN_STRUCTURE = 1;
    public static final int MAGMA_STRUCTURE = 2;
    public static final int SEMIGROUP_STRUCTURE = 3;
    public static final int MONOID_STRUCTURE = 4;
    public static final int COMMUTATIVE_MONOID_STRUCTURE = 5;
    public static final int GROUP_STRUCTURE = 6;
    public static final int ABELIAN_GROUP_STRUCTURE = 7;
    public static final int NO_STRUCTURE = 8;

    public static int getTypeStructure(SomeStructure structure) {
        int[][] table = structure.getCayleyTable();
        if (table != null) {
            if (isClosed(table)) {
                if (isAssociative(table)) {
                    int neutralElement = getNeutralElement(table);
                    if (neutralElement != -1) {
                        if (hasEveryoneReverse(table, neutralElement)) {
                            if (isCommutative(table)) {
                                return ABELIAN_GROUP_STRUCTURE;
                            } else {
                                return GROUP_STRUCTURE;
                            }
                        } else {
                            if (isCommutative(table)) {
                                return COMMUTATIVE_MONOID_STRUCTURE;
                            } else {
                                return MONOID_STRUCTURE;
                            }
                        }
                    } else {
                        return SEMIGROUP_STRUCTURE;
                    }
                } else {
                    return MAGMA_STRUCTURE;
                }
            } else {
                return UNKNOWN_STRUCTURE;
            }
        } else {
            return NO_STRUCTURE;
        }
    }

    private static boolean isClosed(int[][] table) {
        int maxNumber = table.length - 1;
        for (int i = 0; i < table.length; i++) {
            for (int j = 0; j < table.length; j++) {
                if (table[i][j] < 0 || table[i][j] > maxNumber) {
                    return false;
                }
            }
        }
        return true;
    }

    private static boolean isAssociative(int[][] table) {
        for (int a = 0; a < table.length; a++) {
            for (int b = 0; b < table.length; b++) {
                for (int c = 0; c < table.length; c++) {
                    if (table[table[a][b]][c] != table[a][table[b][c]]) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private static int getNeutralElement(int[][] table) {
        for (int e = 0; e < table.length; e++) {
            int k = 0;
            for (int a = 0; a < table.length; a++) {
                if (table[a][e] != table[e][a] || table[e][a] != a || table[a][e] != a) {
                    break;
                }
                k++;
            }
            if (k == table.length) {
                return e;
            }
        }
        return -1;
    }

    private static boolean isCommutative(int[][] table) {
        for (int a = 0; a < table.length; a++) {
            for (int b = 0; b < table.length; b++) {
                if (table[a][b] != table[b][a]) {
                    return false;
                }
            }
        }
        return true;
    }

    private static boolean hasEveryoneReverse(int[][] table, int neutral) {
        for (int a = 0; a < table.length; a++) {
            boolean hasReverse = false;
            for (int b = 0; b < table.length; b++) {
                if (table[a][b] == table[b][a] && table[b][a] == neutral) {
                    hasReverse = true;
                    break;
                }
            }
            if (!hasReverse) {
                return false;
            }
        }
        return true;
    }
}