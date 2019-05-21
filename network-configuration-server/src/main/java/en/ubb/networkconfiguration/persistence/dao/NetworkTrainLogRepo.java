package en.ubb.networkconfiguration.persistence.dao;

import en.ubb.networkconfiguration.persistence.domain.network.runtime.NetworkTrainLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NetworkTrainLogRepo extends JpaRepository<NetworkTrainLog,Long> {

    @Query("SELECT trainLog FROM NetworkTrainLog trainLog WHERE trainLog.network.id = :id ORDER BY trainLog.createDateTime DESC ")
    List<NetworkTrainLog> getAllOrderByDateAsc(@Param("id") long id);
}
