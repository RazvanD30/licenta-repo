package en.ubb.networkconfiguration.dao.specification;

import en.ubb.networkconfiguration.domain.network.runtime.DataFile;
import org.springframework.data.jpa.domain.Specification;

public class DataFileRepoSpec {

    public static Specification<DataFile> hasClasspath(String classPath) {
        return (dataFile, cq, cb) -> cb.equal(dataFile.get("classPath"), classPath);
    }

    public static Specification<DataFile> hasNLabels(int nLabels) {
        return (dataFile, cq, cb) -> cb.equal(dataFile.get("nLabels"), nLabels);
    }
}
