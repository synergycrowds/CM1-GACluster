package tandemx.exemanager.launch;

public interface ExecutionLauncher {
    /**
     * Run execution described by an entry in the executions db which is identified by id; the executions DB is
     * identified by the tree ID
     * @param treeId ID of the tree to which these components belong
     * @param executionId ID of the execution to be run
     * @throws Exception when having problems to submit the execution
     */
    void submitExecution(Integer treeId, Integer executionId) throws Exception;
}
