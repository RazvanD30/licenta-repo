package en.ubb.networkconfiguration.persistence.dao.specification;

import en.ubb.networkconfiguration.persistence.domain.network.runtime.DataFile;
import org.springframework.data.jpa.domain.Specification;

public class DataFileSpec {

    public static Specification<DataFile> hasName(String name) {
        return (dataFile, cq, cb) -> cb.equal(dataFile.get("name"), name);
    }

    public static Specification<DataFile> hasNLabels(int nLabels) {
        return (dataFile, cq, cb) -> cb.equal(dataFile.get("nLabels"), nLabels);
    }
}
