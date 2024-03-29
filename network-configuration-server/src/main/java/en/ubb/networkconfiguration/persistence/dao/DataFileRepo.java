package en.ubb.networkconfiguration.persistence.dao;

import en.ubb.networkconfiguration.persistence.domain.network.runtime.DataFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface DataFileRepo extends JpaRepository<DataFile, Long>, JpaSpecificationExecutor<DataFile> {

}
