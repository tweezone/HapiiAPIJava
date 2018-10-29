package com.happiify.archive.service.fileitem.impl;

import java.util.List;

import com.happiify.archive.dao.FileItemDao;
import com.happiify.archive.domain.FileItem;
import com.happiify.archive.service.fileitem.FileItemService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FileItemServiceImpl implements FileItemService {

    @Autowired
    private FileItemDao fileItemDao;

    @Override
    public int addFileItem(FileItem item) {
        return fileItemDao.insert(item);
    }

    @Override
    public List<FileItem> fetchAllFileItems(int userId) {
        return fileItemDao.getFileItemsByUserId(userId);
    }

    @Override
    public int deleteFileItem(int fileItemId) {
        return fileItemDao.deleteFileItem(fileItemId);
    }

    @Override
    public int moveFileItem(int fileItemId, String destinationPath){
        return fileItemDao.moveFileItem(fileItemId, destinationPath);
    }

    @Override
    public int renameFileItem(int fileItemId, String newName) {
        return fileItemDao.renameFileItem(fileItemId, newName);
    }

}