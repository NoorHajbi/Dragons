package com.dragons.controllers;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.primefaces.event.CellEditEvent;
import org.primefaces.event.RowEditEvent;

import com.dragons.dao.DragonsDao;
import com.dragons.models.Dragon;

@ManagedBean(name = "mbDragons")
@SessionScoped
public class DragonsController {

	private DragonsDao dao = new DragonsDao();
	private Dragon selectedModel;
	private List<Dragon> modelList;
	private Dragon model;

	@PostConstruct
	public void init() {
		dao = new DragonsDao();
		modelList = dao.findAll();
		selectedModel = new Dragon();
	}

	public void updateLocation(CellEditEvent<?> event) {
		Object newValue = event.getNewValue();
		String location = (newValue != null) ? newValue.toString() : "";
		Dragon dragon = modelList.get(event.getRowIndex());
		dao.updateLocation(dragon.getId(), location);
		new FacesMessage("Success", " Location updated successfully");

	}

	public void onRowEdit(RowEditEvent<Dragon> event) {
		Dragon dragon = event.getObject();
		dao.update(dragon);
		new FacesMessage("Success", " Dragon of name= " + dragon.getName() + " updated successfully");

	}

	public void onRowCancel(RowEditEvent<Dragon> event) {
	}

	public void add() {
		dao.save(selectedModel);
		modelList.add(selectedModel);
		selectedModel = new Dragon();
		model = new Dragon();

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage("Success", "Dragon data added successfully"));

	}

	public void loadModelList() {
		modelList = dao.findAll();
	}

	public void delete(int id) {
		dao.delete(id);
//		dragonsList.removeIf(s -> s.getId() == id);
		loadModelList();

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage("Success", "Dragon of id = " + id + " deleted successfully"));

	}

	public List<Dragon> getModelList() {
		if (modelList == null) {
			modelList = dao.findAll();
		}
		return modelList;
	}

	public void setModelList(List<Dragon> modelList) {
		this.modelList = modelList;
	}

	public Dragon getSelectedModel() {
		return selectedModel;
	}

	public void setSelectedModel(Dragon selectedModel) {
		this.selectedModel = selectedModel;
	}

	public Dragon getModel() {
		return model;
	}

	public void setModel(Dragon model) {
		this.model = model;
	}

	// not used for now
	public void update(Dragon dragon) {
		dao.update(dragon);
		loadModelList();
	}

	public void createTable() {
		dao.createTable();
	}

	public void update() {
		dao.update(selectedModel);
		selectedModel = new Dragon();
	}
}
