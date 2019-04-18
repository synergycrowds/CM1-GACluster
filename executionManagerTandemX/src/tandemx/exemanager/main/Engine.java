package tandemx.exemanager.main;

import tandemx.db.*;
import tandemx.db.util.Constants;
import tandemx.exemanager.create.ExecutionCreator;
import tandemx.exemanager.launch.ExecutionLauncher;
import tandemx.exemanager.launch.JARProcessExeLauncher;
import tandemx.exemanager.util.EMTreeParams;
import tandemx.model.treeparams.EMParams;

public class Engine {
    public static void main(String[] args) {
        if (args.length <= 1) {
            System.out.println("Tree ID and jar path are required");
            return;
        }
        try {
            Integer treeId = Integer.parseInt(args[0]);
            EMTreeParams params = getParams(treeId);
            Engine engine = new Engine();
            engine.startExecutionLauncher(args[1], treeId, params.getWaitBtwSessions());
            engine.run(params);
        } catch (NumberFormatException ex) {
            System.out.println("Tree ID must be an integer");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void run(EMTreeParams params) {
        long millisecondsToWaitBetweenSession = params.getWaitBtwSessions();
        while (true) {
            runExecutionsCreationSession(params.getNumberOfObservations(), params.getStepSize(),
                    params.getMinNumberOfSymbols(), params.getVolumeThreshold(), params.getNormPriceThreshold());
            try {
                Thread.sleep(millisecondsToWaitBetweenSession);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void runExecutionsCreationSession(int numberOfObservations, int stepSize, int minNumberOfSymbols,
                                              double volumeThreshold, double normPriceThreshold) {
        DBAMarketData dbaMarketData = null;
        DBAExecutions dbaExecutions = null;
        try {
            dbaMarketData = new DBAMarketDataHib(Constants.DB_NAME_BASE_MARKET_DATA_KAIKO);
            dbaExecutions = new DBAExecutionsHib(Constants.DB_NAME_BASE_EXECUTIONS);
            ExecutionCreator executionCreator = new ExecutionCreator(dbaMarketData, dbaExecutions, numberOfObservations,
                    stepSize, minNumberOfSymbols, volumeThreshold, normPriceThreshold);
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

    private void startExecutionLauncher(String jarPath, Integer treeId, long millisecondsToWaitBetweenSession) {
        (new Thread(() -> {
            while (true) {
                try {
                    runExecutionLaunching(jarPath, treeId);
                    Thread.sleep(millisecondsToWaitBetweenSession);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        })).start();
    }

    private void runExecutionLaunching(String jarPath, Integer treeId) throws Exception {
        DBAExecutions dbaExecutions = null;
        DBAMarketData dbaMarketData = null;
        try {
            dbaExecutions = new DBAExecutionsHib(Constants.DB_NAME_BASE_EXECUTIONS);
            dbaMarketData = new DBAMarketDataHib(Constants.DB_NAME_BASE_MARKET_DATA_KAIKO);
            ExecutionLauncher exelauncher = new JARProcessExeLauncher(jarPath);
            Integer oldestUncompletedExecutionId = dbaExecutions.getOldestUncompletedExecutionId();
            while (oldestUncompletedExecutionId != null) {
                exelauncher.submitExecution(treeId, oldestUncompletedExecutionId);
                oldestUncompletedExecutionId = dbaExecutions.getOldestUncompletedExecutionId();
            }
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
                    emParams.getStepSize(), emParams.getMinNumberOfSymbols(), emParams.getVolumeThreshold(),
                    emParams.getNormPriceThreshold());
        } finally {
            if (dbaTreeParams != null) {
                dbaTreeParams.close();
            }
        }
    }
}
