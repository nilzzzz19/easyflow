package com.fileio.easyflow.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.fileio.easyflow.model.FileModel;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;

@Repository
@Transactional
public class FileModelDaoImpl implements FileModelDao {

	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public void save(FileModel fileModel) {
		entityManager.persist(fileModel);
	}

	@Override
	public FileModel findById(Long id) {
		return entityManager.find(FileModel.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<FileModel> findAll() {
		Query q = entityManager.createQuery("FROM FileModel");
		return q.getResultList();
	}

	@Override
	public void delete(FileModel fileModel) {
		entityManager.remove(fileModel);
	}

}
