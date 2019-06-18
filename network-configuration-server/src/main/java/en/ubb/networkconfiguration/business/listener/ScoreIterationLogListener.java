package en.ubb.networkconfiguration.business.listener;

import en.ubb.networkconfiguration.persistence.domain.network.runtime.NetworkIterationLog;
import en.ubb.networkconfiguration.persistence.domain.network.runtime.NetworkTrainLog;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.TriConsumer;
import org.deeplearning4j.nn.api.Model;
import org.deeplearning4j.optimize.api.BaseTrainingListener;

import java.io.Serializable;

@Slf4j
public class ScoreIterationLogListener extends BaseTrainingListener implements Serializable {


    private int printIterations = 10;
    private Double oldScore;
    private NetworkTrainLog trainlog;
    private TriConsumer<Integer, Double, Double> response;

    /**
     * @param printIterations frequency with which to print scores (i.e., every printIterations parameter updates)
     */
    public ScoreIterationLogListener(int printIterations, NetworkTrainLog trainlog, TriConsumer<Integer, Double, Double> response) {
        this.printIterations = printIterations;
        this.trainlog = trainlog;
        this.response = response;
    }

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
            if (response != null && oldScore != null) {
                response.accept(iteration, score, oldScore);
            }
            oldScore = score;
            log.info("Score at iteration {} is {}", iteration, score);
        }
    }

    public void onIterationGroupDone() {


    }

    @Override
    public String toString() {
        return "ScoreIterationLogListener(" + printIterations + ")";
    }

    public NetworkTrainLog getTrainlog() {
        return trainlog;
    }
}