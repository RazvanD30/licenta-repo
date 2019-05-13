package en.ubb.networkconfiguration.business.listener;

import en.ubb.networkconfiguration.persistence.domain.network.runtime.NetworkIterationLog;
import en.ubb.networkconfiguration.persistence.domain.network.runtime.NetworkTrainLog;
import lombok.extern.slf4j.Slf4j;
import org.deeplearning4j.nn.api.Model;
import org.deeplearning4j.optimize.api.BaseTrainingListener;

import java.io.Serializable;

@Slf4j
public class ScoreIterationLogListener extends BaseTrainingListener implements Serializable {


    private int printIterations = 10;
    private NetworkTrainLog trainlog;

    /**
     * @param printIterations frequency with which to print scores (i.e., every printIterations parameter updates)
     */
    public ScoreIterationLogListener(int printIterations, NetworkTrainLog trainlog) {
        this.printIterations = printIterations;
        this.trainlog = trainlog;
    }

    /**
     * Default constructor printing every 10 iterations
     */
    public ScoreIterationLogListener() {
    }

    @Override
    public void iterationDone(Model model, int iteration, int epoch) {
        if (printIterations <= 0)
            printIterations = 1;
        if (iteration % printIterations == 0) {
            double score = model.score();
            this.trainlog.addNetworkIterationLog(NetworkIterationLog.builder()
                    .score(score)
                    .iteration(iteration)
                    .build());
            log.info("Score at iteration {} is {}", iteration, score);
        }
    }

    @Override
    public String toString() {
        return "ScoreIterationLogListener(" + printIterations + ")";
    }

    public NetworkTrainLog getTrainlog() {
        return trainlog;
    }
}