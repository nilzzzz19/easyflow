package com.fileio.easyflow.dao;

import java.util.List;

import com.fileio.easyflow.model.FileModel;

public interface FileModelDao {
	void save(FileModel fileModel);

	FileModel findById(Long id);

	List<FileModel> findAll();

	void delete(FileModel fileModel);
}
