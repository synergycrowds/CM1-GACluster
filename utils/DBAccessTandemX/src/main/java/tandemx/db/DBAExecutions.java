package tandemx.db;

import tandemx.model.Execution;
import tandemx.model.ExecutionDescription;

import java.time.LocalDateTime;

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
}
