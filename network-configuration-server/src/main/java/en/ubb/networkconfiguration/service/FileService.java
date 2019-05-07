package en.ubb.networkconfiguration.service;

import en.ubb.networkconfiguration.domain.enums.FileType;
import en.ubb.networkconfiguration.domain.network.runtime.DataFile;
import en.ubb.networkconfiguration.domain.network.runtime.Network;
import en.ubb.networkconfiguration.validation.exception.business.NotFoundBussExc;

import java.util.List;
import java.util.Optional;

public interface FileService {

    Optional<DataFile> findFile(String classPath);

    Network addFile(long networkID, String classPath, int nLabels, FileType fileType) throws NotFoundBussExc;

    Network removeFile(long networkID, String classPath) throws NotFoundBussExc;

    Optional<DataFile> findFile(long id);

    List<DataFile> getAll();
}
