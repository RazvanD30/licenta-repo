package en.ubb.networkconfiguration.business.service;

import en.ubb.networkconfiguration.business.validation.exception.BusinessException;
import en.ubb.networkconfiguration.business.validation.exception.FileAccessBussExc;
import en.ubb.networkconfiguration.business.validation.exception.NotFoundBussExc;
import en.ubb.networkconfiguration.persistence.domain.network.enums.FileType;
import en.ubb.networkconfiguration.persistence.domain.network.runtime.DataFile;
import en.ubb.networkconfiguration.persistence.domain.network.runtime.Network;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import javax.xml.crypto.Data;
import java.util.List;
import java.util.Optional;

public interface FileService {

    Optional<DataFile> findFile(String fileName);

    DataFile addFile(@NotNull MultipartFile file, int nLabels) throws FileAccessBussExc;

    Network linkFile(long networkID, String fileName, FileType fileType) throws NotFoundBussExc, FileAccessBussExc;

    Network unlinkFile(long networkID, String fileName) throws NotFoundBussExc;

    Network linkFile(long networkId, long dataFileId, FileType fileType) throws NotFoundBussExc;

    DataFile removeFile(String fileName) throws NotFoundBussExc;

    DataFile removeFile(long dataFileId) throws NotFoundBussExc;

    Optional<DataFile> findFile(long id);

    List<DataFile> getAll();

    List<DataFile> getTrainFiles(long networkId) throws NotFoundBussExc;

    List<DataFile> getTestFiles(long networkId) throws NotFoundBussExc;
}
