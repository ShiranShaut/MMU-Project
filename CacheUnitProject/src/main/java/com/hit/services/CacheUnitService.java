package com.hit.services;

import java.io.IOException;

import com.hit.algorithm.IAlgoCache;
import com.hit.algorithm.LRUAlgoCacheImpl;
import com.hit.algorithm.MRUAlgoCacheImpl;
import com.hit.algorithm.RandomAlgoCacheImpl;
import com.hit.algorithm.SecondChance;
import com.hit.dao.DaoFileImpl;
import com.hit.dao.IDao;
import com.hit.dm.DataModel;
import com.hit.memory.CacheUnit;
import com.hit.server.Server;

public class CacheUnitService<T> {

	@SuppressWarnings("rawtypes")
	private CacheUnit cacheUnit;
	public static Integer numberOfGUDRequests = 0;
	private Integer capacity = 0;
	private String algoType = null;
	
	public CacheUnitService(String algoName, Integer capacity) {
		
		this.capacity = capacity;
		IDao<Long, DataModel<String>> dao = new DaoFileImpl<>("src\\main\\resources\\datasource.txt");
		IAlgoCache<Long, DataModel<String>> algo = null;
		
		switch(algoName) {
		case "lru":
		{
			algo = new LRUAlgoCacheImpl<>(capacity);
			algoType = "LRU";
			break;
		}
		case "mru":
		{
			algo = new MRUAlgoCacheImpl<>(capacity);
			algoType = "MRU";
			break;
		}
		case "secondchance":
		{
			algo = new SecondChance<>(capacity);
			algoType = "Second Chance";
			break;
		}
		case "random":
		{
			algo = new RandomAlgoCacheImpl<>(capacity);
			algoType = "Random";
			break;
		}
		}
		
	    cacheUnit = new CacheUnit<>(algo, dao);
	}
	
	public String[] getStatistics() {
		String[] values = new String[5];
		
		values[0] = capacity.toString();
		values[1] = algoType;
		values[2] = Server.numberOfRequests.toString();
		values[3] = numberOfGUDRequests.toString();
		values[4] = cacheUnit.numberOfSwaps.toString();
		
		return values;
	}
	
	boolean	delete(DataModel<T>[] dataModels) {
		DataModel<T>[] models = get(dataModels);
		
		if(models == null || dataModels == null) {
			return false;
		}
		
		for(int i=0; i<dataModels.length; ++i) {
			if(models[i] != null && dataModels[i] != null) {
				models[i].setContent(null);
			}
		}

		return true;
	}
	
	@SuppressWarnings({ "unchecked", "finally" })
	DataModel<T>[] get(DataModel<T>[] dataModels) {
		numberOfGUDRequests++;	
		DataModel<T>[] models = null;

		if(dataModels == null) {
			return null;
		}
		
		try {
			Long[] ids = getIds(dataModels);
			models = cacheUnit.getDataModels(ids);
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		} finally {
			return models;
		}
	}
	
	boolean	update(DataModel<T>[] dataModels) {	
		DataModel<T>[] models = get(dataModels);
		
		if(models == null || dataModels == null) {
			return false;
		}

		for(int i=0; i<dataModels.length; ++i) {
			if(models[i] != null && dataModels[i] != null) {
				models[i].setContent(dataModels[i].getContent());
			}
		}

		return true;
	}
	
	private Long[] getIds(DataModel<T>[] dataModels) {
		Long[] ids = new Long[dataModels.length];
		
		for (int i=0; i<dataModels.length; ++i) {
			ids[i] = dataModels[i].getDataModelId();
		}
		
		return ids;
	}
}
