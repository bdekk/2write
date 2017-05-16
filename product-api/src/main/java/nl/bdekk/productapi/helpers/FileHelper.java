package nl.bdekk.productapi.helpers;

import nl.bdekk.productapi.domain.ProjectFile;
import nl.bdekk.productapi.domain.entity.FileEntity;
import nl.bdekk.productapi.domain.entity.ProjectEntity;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class FileHelper {


    public static List<ProjectFile> convertFileEntitiesToProjectFiles(List<FileEntity> fileEntities) {
        List<ProjectFile> projectFiles = fileEntities.stream()
                .map(fileEntity -> convertFileEntityToProjectFile(fileEntity))
                .collect(Collectors.toList());
        return projectFiles;
    }

    public static ProjectFile convertFileEntityToProjectFile(FileEntity fileEntity) {
        ProjectFile pf = new ProjectFile();
        pf.setName(fileEntity.getName());
        pf.setPath(fileEntity.getPath());
        pf.setId(fileEntity.getId());
        return pf;
    }

    public static List<FileEntity> convertFilesToFileEntities(Map<String, String> files, ProjectEntity entity) {
        List<FileEntity> fileEntities = files.entrySet()
                .stream()
                .map(file -> convertFileToFileEntity(file, entity))
                .collect(Collectors.toList());
        return fileEntities;
    }

    public static FileEntity convertFileToFileEntity(Map.Entry<String, String> file, ProjectEntity entity) {
        FileEntity fe = new FileEntity();
        fe.setName(file.getKey());
        fe.setPath(file.getValue());
        fe.setProject(entity);
        return fe;
    }

    public static Optional<ProjectEntity> getProjectEntityById(EntityManager manager, long id) {
        TypedQuery<ProjectEntity> query = manager.createNamedQuery(ProjectEntity.FIND_BY_ID, ProjectEntity.class);
        query = query.setParameter("id", id);
        try {
            return Optional.of(query.getSingleResult());
        } catch (NoResultException nre) {
            return Optional.empty();
        }
    }
}
