package en.ubb.networkconfiguration.service.impl;

import en.ubb.networkconfiguration.dao.DataFileRepo;
import en.ubb.networkconfiguration.dao.NetworkRepo;
import en.ubb.networkconfiguration.dao.specification.DataFileRepoSpec;
import en.ubb.networkconfiguration.domain.enums.FileType;
import en.ubb.networkconfiguration.domain.network.runtime.DataFile;
import en.ubb.networkconfiguration.domain.network.runtime.Network;
import en.ubb.networkconfiguration.service.FileService;
import en.ubb.networkconfiguration.validation.exception.business.NotFoundBussExc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FileServiceImpl implements FileService {

    private final DataFileRepo dataFileRepo;

    private final NetworkRepo networkRepo;

    @Autowired
    public FileServiceImpl(DataFileRepo dataFileRepo, NetworkRepo networkRepo) {
        this.dataFileRepo = dataFileRepo;
        this.networkRepo = networkRepo;
    }

    @Override
    public Network addFile(long networkID, String classPath, int nLabels, FileType fileType) throws NotFoundBussExc {
        return this.networkRepo.findById(networkID).map(persistedNetwork ->
                this.findFile(classPath)
                        .map(dataFile -> {
                            persistedNetwork.addFile(dataFile, fileType);
                            return persistedNetwork;
                        })
                        .orElseGet(() -> {
                            DataFile dataFile = DataFile.builder()
                                    .classPath(classPath)
                                    .nLabels(nLabels)
                                    .networks(new ArrayList<>())
                                    .build();
                            DataFile persistedDataFile = dataFileRepo.save(dataFile);
                            persistedNetwork.addFile(persistedDataFile, fileType);
                            return persistedNetwork;
                        })
        ).orElseThrow(() -> new NotFoundBussExc("Network with id " + networkID + " not found"));
    }

    @Override
    public Network removeFile(long networkID, String classPath) throws NotFoundBussExc {

        final Network persistedNetwork = this.networkRepo.findById(networkID)
                .orElseThrow(() -> new NotFoundBussExc("Network with id " + networkID + " not found"));

        final DataFile persistedDataFile = this.findFile(classPath)
                .orElseThrow(() -> new NotFoundBussExc("File with classpath " + classPath + " not found"));

        persistedNetwork.removeFile(persistedDataFile);
        return persistedNetwork;
    }

    @Override
    public Optional<DataFile> findFile(String classPath) {
        return this.dataFileRepo.findOne(Specification.where(DataFileRepoSpec.hasClasspath(classPath)));
    }

    @Override
    public Optional<DataFile> findFile(long id){
        return this.dataFileRepo.findById(id);
    }

    @Override
    public List<DataFile> getAll(){
        return this.dataFileRepo.findAll();
    }
}
