package tandemx.db;

import tandemx.model.Execution;
import tandemx.model.ExecutionCurrencyPair;
import tandemx.model.ExecutionDescription;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface DBAExecutions {
    /**
     * Method to be called when access to the database is no longer needed.
     * Method must be called to close connection to the database
     */
    void close();

    /**
     * Get the execution description corresponding to the execution with the smallest ID which was not completed
     * @return the oldest uncompleted execution; null if there is no such execution
     */
    ExecutionDescription getOldestUncompletedExecutionDescription();

    /**
     * Update the execution completion timestamps
     * @param executionId ID of the execution to be updated
     * @param executionTimestampBegin timestamp of the moment in which the completion of the execution started
     * @param executionTimestampEnd timestamp of the moment in which the completion of the execution ended
     */
    void updateExecutionCompletionTimestamps(Integer executionId, LocalDateTime executionTimestampBegin,
                                             LocalDateTime executionTimestampEnd);

    /**
     * Get the lates execution with respect to the data end timestamp;
     * @return the latest execution
     */
    Execution getLatestExecution();

    /**
     * Insert into the DB an execution description
     * @return the execution description with the generated ID
     */
    ExecutionDescription insertExecutionDescription(ExecutionDescription executionDescription);

    /**
     * Get all execution descriptions that have a certain data timestamp end
     * @param dataTimestampEnd value for the data timestamp end of all returned execution descriptions
     * @return list of all execution descriptions satisfying the condition
     */
    List<ExecutionDescription> getExecutionDescriptionsByDataTimestampEnd(LocalDate dataTimestampEnd);

    /**
     * Insert into the DB exchangeCurrencyPairs
     * @param executionCurrencyPairs list of entries to be persisted
     * @return list of the same entries, but with the assigned ID
     */
    List<ExecutionCurrencyPair> insertExecutionCurrencyPairs(List<ExecutionCurrencyPair> executionCurrencyPairs);

    /**
     * Get the execution description with the given execution ID
     * @param executionId the ID of the returned execution description
     * @return the execution description if it exists, null otherwise
     */
    ExecutionDescription getExecutionDescriptionById(int executionId);
}
