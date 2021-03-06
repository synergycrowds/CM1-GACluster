package tandemx.db;


import tandemx.model.treeparams.EMParams;
import tandemx.model.treeparams.NormVWAPParams;
import tandemx.model.treeparams.RDMParams;
import tandemx.model.treeparams.TreeParams;

public interface DBATreeParams {
    /**
     * Method to be called when access to the database is no longer needed.
     * Method must be called to close connection to the database
     */
    void close();

    /**
     * Returns the TreeParams with the given id; if no TreeParams with this id is found, returns null.
     * @param treeId the tree id
     * @return the TreeParams with the given id or null if no such TreeParams exists
     */
    TreeParams getTreeParamsById(Integer treeId);

    /**
     * Returns the RDMParams with the given id; if no RDMParams with this id is found, returns null.
     * @param treeId the tree id
     * @return the RDMParams with the given id or null if no such RDMParams exists
     */
    RDMParams getRDMParamsById(Integer treeId);

    /**
     * Returns the NormVWAPParams with the given id; if no NormVWAPParams with this id is found, returns null.
     * @param treeId the tree id
     * @return the NormVWAPParams with the given id or null if no such RDMParams exists
     */
    NormVWAPParams getNormVWAPParamsById(Integer treeId);

    /**
     * Returns the EMParams with the given id; if no EMParams with this id is found, returns null.
     * @param treeId the tree id
     * @return the EMParams with the given id or null if no such EMParams exists
     */
    EMParams getEMParamsById(Integer treeId);
}
