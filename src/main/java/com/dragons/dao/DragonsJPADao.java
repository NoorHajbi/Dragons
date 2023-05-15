package com.dragons.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;

import com.dragons.entities.DragonEntity;

public class DragonsJPADao {

	private EntityManagerFactory entityManagerFactory;
	private EntityManager entityManager;

	public DragonsJPADao() {
		entityManagerFactory = Persistence.createEntityManagerFactory("DragonEntity");
		entityManager = entityManagerFactory.createEntityManager();
	}

	public void createTable() {
	}

	public void save(DragonEntity dragon) {
		EntityTransaction transaction = entityManager.getTransaction();
		try {
			transaction.begin();
			entityManager.persist(dragon);
			transaction.commit();
		} catch (Exception e) {
			transaction.rollback();
			e.printStackTrace();
		}
	}

	public void updateLocation(int id, String location) {
		EntityTransaction transaction = entityManager.getTransaction();
		try {
			transaction.begin();
			DragonEntity dragon = entityManager.find(DragonEntity.class, id);
			if (dragon != null) {
				dragon.setLocation(location);
				entityManager.persist(dragon);
				transaction.commit();
			} else {
				System.out.println("DragonEntity not found with id: " + id);
			}
		} catch (Exception e) {
			transaction.rollback();
			e.printStackTrace();
		}
	}

	public DragonEntity findById(int id) {
		return entityManager.find(DragonEntity.class, id);
	}

	@SuppressWarnings("unchecked")
	public List<DragonEntity> findAll() {
		try {
			Query query = entityManager.createQuery("SELECT d FROM DragonEntity d");
			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}


	public void update(DragonEntity dragon) {
		EntityTransaction transaction = entityManager.getTransaction();
		try {
			transaction.begin();
			entityManager.merge(dragon);
			transaction.commit();
		} catch (Exception e) {
			transaction.rollback();
			e.printStackTrace();
		}
	}

	public void delete(int id) {
		EntityTransaction transaction = entityManager.getTransaction();
		try {
			transaction.begin();
			DragonEntity dragon = entityManager.find(DragonEntity.class, id);
			if (dragon != null) {
				entityManager.remove(dragon);
				transaction.commit();
			} else {
				System.out.println("DragonEntity not found with id: " + id);
			}
		} catch (Exception e) {
			transaction.rollback();
			e.printStackTrace();
		}
	}

	public void close() {
		entityManager.close();
		entityManagerFactory.close();
	}
}
