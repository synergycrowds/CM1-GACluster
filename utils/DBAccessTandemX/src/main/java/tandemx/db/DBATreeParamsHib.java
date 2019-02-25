package tandemx.db;

import tandemx.model.treeparams.NormVWAPParams;
import tandemx.model.treeparams.RDMParams;
import tandemx.model.treeparams.TreeParams;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class DBATreeParamsHib implements DBATreeParams{
    private static String PERSISTENCE_UNIT = "treeparamsTandemX";

    private EntityManagerFactory factory;

    public DBATreeParamsHib() {
        this.factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
    }

    @Override
    public void close() {
        factory.close();
    }

    @Override
    public TreeParams getTreeParamsById(Integer treeId) {
        EntityManager manager = factory.createEntityManager();
        TreeParams treeParams = manager.find(TreeParams.class, treeId);
        manager.close();
        return treeParams;
    }

    @Override
    public RDMParams getRDMParamsById(Integer treeId) {
        EntityManager manager = factory.createEntityManager();
        RDMParams rdmParams = manager.find(RDMParams.class, treeId);
        manager.close();
        return rdmParams;
    }

    @Override
    public NormVWAPParams getNormVWAPParamsById(Integer treeId) {
        EntityManager manager = factory.createEntityManager();
        NormVWAPParams normVWAPParams = manager.find(NormVWAPParams.class, treeId);
        manager.close();
        return normVWAPParams;
    }
}
