package com.happiify.archive.controller;

import org.apache.commons.io.FilenameUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.content.commons.repository.Store;
import org.springframework.content.fs.config.EnableFilesystemStores;
import org.springframework.content.fs.io.FileSystemResourceLoader;
import org.springframework.content.rest.StoreRestResource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.*;
import com.happiify.archive.domain.FileItem;

import java.io.*;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.happiify.archive.service.fileitem.FileItemService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@RestController
public class FileItemController {

    static final String uploadedFileFolder = "/Users/chenguoyan/Desktop/uploadedfile";
//    static final String uploadedFileFolder = "/var/www/html/api/uploadedfile";

    @RequestMapping(value = "test", method = RequestMethod.GET)
    public String test(@RequestParam String param) {
        return "Test OK" + param;
    }

    @Autowired
    private FileItemService fileItemService;

    @RequestMapping("archive/allfileitems/{userId}")
    public List<FileItem> fetchAllFileItems(@PathVariable int userId) {
        return fileItemService.fetchAllFileItems(userId);
    }

    @RequestMapping(value = "archive/addfileitem", method = RequestMethod.POST)
    public int addFileItem(FileItem item) {
        fileItemService.addFileItem(item);
        return item.getId();
    }

    @RequestMapping(value = "archive/upload", method = RequestMethod.POST)
    public String upload(FileItem item) {

        try {
            if (item.getItem_file() != null) {
                String originalFilename = item.getItem_file().getOriginalFilename();
                item.setPhysical_name(originalFilename);
                item.setItem_size(item.getItem_file().getSize());

                File localFile = new File(uploadedFileFolder, originalFilename);
                item.getItem_file().transferTo(localFile);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "upload failed";
        }
        item.setCreation_date(new Date());
        fileItemService.addFileItem(item);
        return item.getId().toString();
    }

    @RequestMapping(value = "archive/download/{physicalFileName}", method = RequestMethod.GET)
    public String download(@PathVariable String physicalFileName, HttpServletRequest request,
                           HttpServletResponse response) {

        try {
            File toBeDownloadedFile = new File(uploadedFileFolder, physicalFileName);
            String fileExtension = FilenameUtils.getExtension(toBeDownloadedFile.getPath());
            String contentType = fileExtension.equals("mp4") ? "audio/mp3" : "application/octet-stream";
            InputStream inputStream = new FileInputStream(toBeDownloadedFile);
            OutputStream outputStream = response.getOutputStream();
            response.setContentType(contentType);
            response.setContentLength((int) toBeDownloadedFile.length());
            response.addHeader("Content-Disposition", "attachment;filename=" + physicalFileName);
            IOUtils.copy(inputStream, outputStream);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
            return "Download failed";
        }
        return "Success";
    }

    @RequestMapping(value = "archive/delete/{fileItemId}", method = RequestMethod.GET)
    public int deleteFileItem(@PathVariable int fileItemId) {

        fileItemService.deleteFileItem(fileItemId);

        return fileItemId;
    }

    @RequestMapping(value = "archive/rename", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public int renameFileItem(@RequestBody Map<String, String> requestMap) {
        int fileItemId = Integer.parseInt(requestMap.get("fileItemId"));
        String newName = requestMap.get("newName");
        return fileItemService.renameFileItem(fileItemId, newName);
    }

    @RequestMapping(value = "archive/fileitemdetail/{fileItemId}", method = RequestMethod.GET)
    FileItem getFileItemDetail(@PathVariable int fileItemId) {
        return fileItemService.getFileItemDetail(fileItemId);
    }

    @RequestMapping(value = "archive/setpublic/", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    void setFileItemToBePublic(@RequestBody Map<String, String> requestMap) {
        int fileItemId = Integer.parseInt(requestMap.get("fileItemId"));
        boolean isPublic = Boolean.parseBoolean(requestMap.get("isPublic"));
        fileItemService.setFileItemToBePublic(fileItemId, isPublic);
    }

    @RequestMapping(value = "archive/sethealth/{fileItemId}", method = RequestMethod.GET)
    void setFileItemToBeHealthRelated(@PathVariable int fileItemId) {

        fileItemService.setFileItemToBeHealthRelated(fileItemId);
    }

    @RequestMapping(value = "archive/setcategory/{itemId}/{itemCategory}",method = RequestMethod.GET)
    void setFileItemCategory(@PathVariable int itemId, @PathVariable int itemCategory) {
        fileItemService.setFileItemCategory(itemId, itemCategory);
    }

    @RequestMapping(value = "archive/move", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public void moveFileItem(@RequestBody Map<String, String> requestMap) {
        int fileItemId = Integer.parseInt(requestMap.get("item_id"));
        //int newCategory = Integer.parseInt(requestMap.get("new_category"));
        boolean isFolder = Boolean.parseBoolean(requestMap.get("is_folder"));
        String destinationPath = requestMap.get("destination_folder");

        if(!isFolder){
            fileItemService.changeFileItemPath(fileItemId, destinationPath);
        } else {
            String currentPath = requestMap.get("current_path");
            String itemName = requestMap.get("item_name");
            int currentIndex = currentPath.indexOf(itemName);
            String leftPartPath = currentPath.substring(currentIndex);
            String finalNewPath = destinationPath + leftPartPath;
            fileItemService.changeFileItemPath(fileItemId,destinationPath);
            fileItemService.changeFileItemsPathInFolder(currentPath,finalNewPath);

        }

        //fileItemService.setFileItemCategory(fileItemId, newCategory);
    }

}