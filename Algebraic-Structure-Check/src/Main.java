public class Main {

    public static void check(String fileName) {
        SomeStructure structure = SomeStructure.newInstance(fileName);
        String output = null;
        switch (StructureCheck.getTypeStructure(structure)) {
            case StructureCheck.NO_STRUCTURE:
                output = "No structure";
                break;
            case StructureCheck.UNKNOWN_STRUCTURE:
                output = "Unknown structure";
                break;
            case StructureCheck.MAGMA_STRUCTURE:
                output = "Magma structure";
                break;
            case StructureCheck.SEMIGROUP_STRUCTURE:
                output = "Semigroup structure";
                break;
            case StructureCheck.MONOID_STRUCTURE:
                output = "Monoid structure";
                break;
            case StructureCheck.COMMUTATIVE_MONOID_STRUCTURE:
                output = "Commutative monoid structure";
                break;
            case StructureCheck.GROUP_STRUCTURE:
                output = "Group structure";
                break;
            case StructureCheck.ABELIAN_GROUP_STRUCTURE:
                output = "Abelian group structure";
                break;
        }
        System.out.println(fileName + ": " + output);
    }

    public static void main(String[] args) {
        check("input/example_1");
        check("input/example_2");
        check("input/example_3");
    }
}
