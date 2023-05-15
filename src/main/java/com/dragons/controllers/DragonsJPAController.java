package com.dragons.controllers;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.primefaces.event.CellEditEvent;
import org.primefaces.event.RowEditEvent;

import com.dragons.dao.DragonsJPADao;
import com.dragons.entities.DragonEntity;

@ManagedBean(name = "mbDragonsJPA")
@SessionScoped
public class DragonsJPAController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3370292406373333209L;
	@Inject
	private DragonsJPADao dao;
	private DragonEntity selectedModel;
	private List<DragonEntity> modelList;
	private DragonEntity model;

	@PostConstruct
	public void init() {
		modelList = dao.findAll();
	}

	public void updateLocation(CellEditEvent<?> event) {
		Object newValue = event.getNewValue();
		String location = (newValue != null) ? newValue.toString() : "";
		DragonEntity dragon = modelList.get(event.getRowIndex());
		dao.updateLocation(dragon.getId(), location);
		new FacesMessage("Success", " Location updated successfully");

	}

	public void onRowEdit(RowEditEvent<DragonEntity> event) {
		DragonEntity dragon = event.getObject();
		dao.update(dragon);
		new FacesMessage("Success", " DragonEntity of name= " + dragon.getName() + " updated successfully");

	}

	public void onRowCancel(RowEditEvent<DragonEntity> event) {
	}

	public void add() {
		dao.save(selectedModel);
		modelList.add(selectedModel);
		selectedModel = new DragonEntity();
		model = new DragonEntity();

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage("Success", "DragonEntity data added successfully"));

	}

	public void loadModelList() {
		modelList = dao.findAll();
	}

	public void delete(int id) {
		dao.delete(id);
//		dragonsList.removeIf(s -> s.getId() == id);
		loadModelList();

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage("Success", "DragonEntity of id = " + id + " deleted successfully"));

	}

	public List<DragonEntity> getModelList() {
		if (modelList == null) {
			modelList = dao.findAll();
		}
		return modelList;
	}

	public void setModelList(List<DragonEntity> modelList) {
		this.modelList = modelList;
	}

	public DragonEntity getSelectedModel() {
		return selectedModel;
	}

	public void setSelectedModel(DragonEntity selectedModel) {
		this.selectedModel = selectedModel;
	}

	public DragonEntity getModel() {
		return model;
	}

	public void setModel(DragonEntity model) {
		this.model = model;
	}

	// not used for now
	public void update(DragonEntity dragon) {
		dao.update(dragon);
		loadModelList();
	}

	public void createTable() {
		dao.createTable();
	}

	public void update() {
		dao.update(selectedModel);
		selectedModel = new DragonEntity();
	}
}
