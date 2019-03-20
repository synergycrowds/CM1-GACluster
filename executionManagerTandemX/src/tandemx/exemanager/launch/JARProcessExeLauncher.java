package tandemx.exemanager.launch;

import java.io.File;
import java.util.Scanner;

public class JARProcessExeLauncher implements ExecutionLauncher {
    private String jarPath;

    public JARProcessExeLauncher(String jarPath) {
        this.jarPath = jarPath;
    }

    /**
     * This implementation launches a process and waits for it to end
     * @param treeId ID of the tree to which these components belong
     * @param executionId ID of the execution to be run
     * @throws Exception
     */
    @Override
    public void submitExecution(Integer treeId, Integer executionId) throws Exception {
        ProcessBuilder processBuilder = new ProcessBuilder("java", "-jar", this.jarPath,
                String.valueOf(treeId), String.valueOf(executionId));
        processBuilder.redirectOutput(new File("NUL")).redirectErrorStream(true);
        Process process = processBuilder.start();
        process.waitFor();
    }
}
