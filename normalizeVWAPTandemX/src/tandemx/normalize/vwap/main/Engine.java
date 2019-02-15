package tandemx.normalize.vwap.main;

public class Engine {
    public static void main(String[] args) {
        if (args.length <= 0) {
            System.out.println("Tree ID required");
            return;
        }
        try {
            Integer treeId = Integer.parseInt(args[0]);
//            RDMTreeParams params = getParams(treeId);
//            (new Engine()).run(params);
            (new Engine()).run();
        } catch (NumberFormatException ex) {
            System.out.println("Tree ID must be an integer");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void run() {

    }
}
