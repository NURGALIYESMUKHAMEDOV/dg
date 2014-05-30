package com.scottrade.datagovernance.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.scottrade.datagovernance.domain.DataEntity;
import com.scottrade.datagovernance.dto.DataEntityDTO;
import com.scottrade.datagovernance.exception.NotFoundException;
import com.scottrade.datagovernance.service.DataEntityService;
import com.scottrade.datagovernance.util.DtoFactory;

/**
 * Handles requests for the Data Entity service.
 */
@Controller
public class DataEntityController {
	private static final Logger logger = LoggerFactory
			.getLogger(DataEntityController.class);

	/**
	 * URI Constants used by this controller.
	 */
	public static final String GET_ENTITY = "/dataEntity/{id}";
	public static final String GET_ENTITY_DEPENDENTS = "/dataEntity/dependents/{id}";
	public static final String GET_ALL_ENTITIES = "/allDataEntities/";
	public static final String ADD_ENTITY = "/dataEntity/add";
	public static final String EDIT_ENTITY = "/dataEntity/edit";
	public static final String DELETE_ENTITY = "/dataEntity/delete/{id}";

	DataEntityService deSvc;
	DtoFactory deDTOfactory;

	@Autowired
	public DataEntityController(DataEntityService dataEntitySvc,
			DtoFactory deDTOfactory) {
		this.deSvc = dataEntitySvc;
		this.deDTOfactory = deDTOfactory;
	}

	// --- Error handlers
	@ExceptionHandler(NotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ResponseBody
	public String handleNotFoundException(NotFoundException e) {
		return e.getMessage();
	}

	@RequestMapping(value = GET_ALL_ENTITIES, method = RequestMethod.GET)
	public @ResponseBody
	List<DataEntity> getAllDataEntities() {
		logger.info("Start --> Getting ALL Data Entities = ");
		List<DataEntityDTO> deList = null;
		if (null != deSvc.getAll()) {
			deList = new ArrayList<DataEntityDTO>();
			for (DataEntity de : deSvc.getAll()) {
				deList.add(deDTOfactory.createDataEntity(de));
			}
		}
		return deSvc.getAll();
		//return deList;
	}

	@RequestMapping(value = GET_ENTITY, method = RequestMethod.GET)
	public @ResponseBody
	DataEntity getDataEntity(@PathVariable int id) {
		logger.info("Start --> Getting a Data Entity = ");
		DataEntity de = deSvc.getById(id);
		if (null != de) {
			return de;
			//return deDTOfactory.createDataEntity(de);
		} else {
			return null;
		}
	}

	@RequestMapping(value = DELETE_ENTITY, method = RequestMethod.GET)
	public @ResponseBody
	String purgeDataEntity(@PathVariable int id) {
		logger.info("Start --> Deleting a Data Entity = ");
		deSvc.deleteDataEntity(id);
		return "Deleted DataEntity: " + id;
	}

	/**
	 * Modifies a DataEntity's values. Spring automatically binds the parameters in the
	 * request to the DataEntity argument.
	 * 
	 * @param dateEntity
	 * @return String indicating success or failure of save
	 */
	@RequestMapping(value = EDIT_ENTITY, method = RequestMethod.POST)
	@ResponseBody
	public String modifyDataEntity(@RequestBody DataEntity dataEntity) {
		logger.info("Start --> SAVE a Data Entity = " + dataEntity);
		if (null != dataEntity && dataEntity.getEntityId() > 0) {
			deSvc.editDataEntity(dataEntity);
		}
		return "Saved DataEntity: " + dataEntity.toString();
	}

	/**
	 * Creates a new DataEntity. Spring automatically binds the parameters in the
	 * request to the DataEntity argument.
	 * 
	 * @param dateEntity
	 * @return String indicating success or failure of save
	 */
	@RequestMapping(value = ADD_ENTITY, method = RequestMethod.POST)
	@ResponseBody
	public String createDataEntity(@RequestBody DataEntity dataEntity) {
		logger.info("Start --> SAVE a Data Entity = " + dataEntity);
		if (null != dataEntity && dataEntity.getEntityId() == 0) {
			deSvc.addDataEntity(dataEntity);
		}
		return "Saved DataEntity: " + dataEntity.toString();
	}
}