package com.happiify.archive.service.fileitem;

import java.util.List;
import com.happiify.archive.domain.FileItem;

public interface FileItemService {
    int addFileItem(FileItem item);
    List<FileItem> fetchAllFileItems(int userId);
    int deleteFileItem(int fileItemId);
    int moveFileItem(int fileItemId, String destinationPath, boolean isFolder, String currentPath);
    int renameFileItem(int fileItemId, String newName);
}