package tandemx.normalize.vwap.main;

import tandemx.db.DBAMarketData;
import tandemx.db.DBAMarketDataHib;
import tandemx.db.DBATreeParams;
import tandemx.db.DBATreeParamsHib;
import tandemx.db.util.Constants;
import tandemx.model.treeparams.NormVWAPParams;
import tandemx.normalize.vwap.pricenorm.NormalizationManager;
import tandemx.normalize.vwap.util.NormVWAPTreeParams;

import java.time.LocalDateTime;

public class Engine {
    public static void main(String[] args) {
        if (args.length <= 0) {
            System.out.println("Tree ID required");
            return;
        }
        try {
            Integer treeId = Integer.parseInt(args[0]);
            NormVWAPTreeParams params = getParams(treeId);
            (new Engine()).run(params);
        } catch (NumberFormatException ex) {
            System.out.println("Tree ID must be an integer");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void run(NormVWAPTreeParams params) {
        long millisecondsToWait = params.getWaitBtwSessions();
        while (true) {
            System.out.println("Session started");
            runNormalizationSession(params.getMinVolumeThreshold());
            System.out.println("Session completed");
            try {
                Thread.sleep(millisecondsToWait);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void runNormalizationSession(double minVolumeThreshold) {
        DBAMarketData dbaMarketData = null;
        try {
            dbaMarketData = new DBAMarketDataHib(Constants.DB_NAME_BASE_MARKET_DATA_KAIKO);
            NormalizationManager normalizationManager = new NormalizationManager(dbaMarketData, minVolumeThreshold);
            LocalDateTime lastDate = normalizationManager.normalizeAllRecords();
            if (lastDate != null) {
                dbaMarketData.updateNormalizedStatus(lastDate);
            }
        } finally {
            if (dbaMarketData != null) {
                dbaMarketData.close();
            }
        }
    }

    private static NormVWAPTreeParams getParams(int treeId) throws Exception {
        DBATreeParams dbaTreeParams = null;
        try {
            dbaTreeParams = new DBATreeParamsHib();
            NormVWAPParams normVWAPParams = dbaTreeParams.getNormVWAPParamsById(treeId);

            if (normVWAPParams == null) {
                throw new Exception("No parameters with this tree id");
            }

            return new NormVWAPTreeParams(normVWAPParams.getWaitBtwSessions(), normVWAPParams.getMinVolumeThreshold());
        } finally {
            if (dbaTreeParams != null) {
                dbaTreeParams.close();
            }
        }
    }
}
