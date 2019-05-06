package en.ubb.networkconfiguration.repo;

import en.ubb.networkconfiguration.domain.network.runtime.DataFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface DataFileRepo extends JpaRepository<DataFile, Long>, JpaSpecificationExecutor<DataFile> {

}
