package com.happiify.archive.dao;

import com.happiify.archive.domain.FileItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FileItemDao {
    int insert(FileItem item);

    int deleteFileItem(int fileItemId);

    int moveFileItem(@Param("fileItemId") int fileItemId,
                     @Param("destinationPath") String destinationPath,
                     @Param("isFolder") boolean isFolder,
                     @Param("currentPath") String currentPath);

    int renameFileItem(int fileItemId, String newName);

    void move(int fileItemId, String destinationPath);

    void share(List<String> users);

    void makePublic(boolean currentStatus);

    List<FileItem> getFileItemsByUserId(int userId);
}