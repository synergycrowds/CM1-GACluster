package tandemx.exemanager.main;

import tandemx.db.*;
import tandemx.db.util.Constants;
import tandemx.exemanager.create.ExecutionCreator;
import tandemx.exemanager.util.EMTreeParams;
import tandemx.model.treeparams.EMParams;

public class Engine {
    public static void main(String[] args) {
        if (args.length <= 0) {
            System.out.println("Tree ID required");
            return;
        }
        try {
            Integer treeId = Integer.parseInt(args[0]);
            EMTreeParams params = getParams(treeId);
            (new Engine()).run(params);
        } catch (NumberFormatException ex) {
            System.out.println("Tree ID must be an integer");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void run(EMTreeParams params) {
        long millisecondsToWaitBetweenSession = params.getWaitBtwSessions();
        while (true) {
            runExecutionsCreationSession(params.getNumberOfObservations(), params.getStepSize());
            try {
                Thread.sleep(millisecondsToWaitBetweenSession);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void runExecutionsCreationSession(int numberOfObservations, int stepSize) {
        DBAMarketData dbaMarketData = null;
        DBAExecutions dbaExecutions = null;
        try {
            dbaMarketData = new DBAMarketDataHib(Constants.DB_NAME_BASE_MARKET_DATA_KAIKO);
            dbaExecutions = new DBAExecutionsHib(Constants.DB_NAME_BASE_EXECUTIONS);
            ExecutionCreator executionCreator = new ExecutionCreator(dbaMarketData, dbaExecutions, numberOfObservations,
                    stepSize);
            executionCreator.createExecutionsWhilePossible();
        } finally {
            if (dbaExecutions != null) {
                dbaExecutions.close();
            }
            if (dbaMarketData != null) {
                dbaMarketData.close();
            }
        }
    }

    private static EMTreeParams getParams(int treeId) throws Exception {
        DBATreeParams dbaTreeParams = null;
        try {
            dbaTreeParams = new DBATreeParamsHib();
            EMParams emParams = dbaTreeParams.getEMParamsById(treeId);

            if (emParams == null) {
                throw new Exception("No parameters with this tree id");
            }

            return new EMTreeParams(emParams.getWaitBtwSessions(), emParams.getNumberOfObservations(),
                    emParams.getStepSize());
        } finally {
            if (dbaTreeParams != null) {
                dbaTreeParams.close();
            }
        }
    }
}
