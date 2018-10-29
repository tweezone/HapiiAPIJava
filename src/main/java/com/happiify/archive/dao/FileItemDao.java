package com.happiify.archive.dao;

import com.happiify.archive.domain.FileItem;

import java.util.List;

public interface FileItemDao {
    int insert(FileItem item);
    int deleteFileItem(int fileItemId);
    int moveFileItem(int fileItemId, String destinationPath);
    int renameFileItem(int fileItemId, String newName);
    void move(int fileItemId, String destinationPath);
    void share(List<String> users);
    void makePublic(boolean currentStatus);
    List<FileItem> getFileItemsByUserId(int userId);
}