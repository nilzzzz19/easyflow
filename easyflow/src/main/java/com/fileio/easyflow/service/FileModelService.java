package com.fileio.easyflow.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fileio.easyflow.dao.FileModelDao;
import com.fileio.easyflow.dao.FileModelDaoImpl;
import com.fileio.easyflow.model.FileModel;

@Service
public class FileModelService {

   
    private FileModelDaoImpl fileModelDao;
    
    @Autowired
    public FileModelService(FileModelDaoImpl fileModelDao) {
    	this.fileModelDao=fileModelDao;
    }

	/* service methods */
    public void saveFile(FileModel fileModel) {
        fileModelDao.save(fileModel);
    }

    public FileModel findFileById(Long id) {
        return fileModelDao.findById(id);
    }
    
    public List<FileModel> getAllFiles() {
        return fileModelDao.findAll();
    }
}
