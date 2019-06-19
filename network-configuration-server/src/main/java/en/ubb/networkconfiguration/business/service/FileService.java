package en.ubb.networkconfiguration.business.service;

import en.ubb.networkconfiguration.business.validation.exception.BusinessException;
import en.ubb.networkconfiguration.business.validation.exception.NotFoundBussExc;
import en.ubb.networkconfiguration.persistence.domain.network.enums.FileType;
import en.ubb.networkconfiguration.persistence.domain.network.runtime.DataFile;
import en.ubb.networkconfiguration.persistence.domain.network.runtime.Network;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

public interface FileService {

    Optional<DataFile> findFile(String classPath);

    DataFile addFile(String classPath, int nLabels);

    DataFile addFile(@NotNull MultipartFile file, int nLabels) throws BusinessException;

    Network addFile(long networkID, String classPath, int nLabels, FileType fileType) throws NotFoundBussExc;

    Network unlinkFile(long networkID, String classPath) throws NotFoundBussExc;

    Network linkFile(long networkId, long dataFileId, FileType fileType) throws NotFoundBussExc;

    DataFile removeFile(long dataFileId) throws NotFoundBussExc;

    Optional<DataFile> findFile(long id);

    List<DataFile> getAll();

    List<DataFile> getTrainFiles(long networkId) throws NotFoundBussExc;

    List<DataFile> getTestFiles(long networkId) throws NotFoundBussExc;
}
