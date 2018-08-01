package com.hit.memory;

import java.util.ArrayList;
import java.util.List;
import java.io.*;
import com.hit.dm.*;
import com.hit.algorithm.*;
import com.hit.dao.IDao;

public class CacheUnit<T> {

	private IAlgoCache<Long, DataModel<T>> algoCache;
	private IDao<Long, DataModel<T>> dao;
	public static Integer numberOfSwaps = 0;
	
	public CacheUnit(IAlgoCache<Long, DataModel<T>> algo, IDao<Long, DataModel<T>> dao) {
		algoCache = algo;
		this.dao = dao;
	}
	
	@SuppressWarnings("unchecked")
	public DataModel<T>[] getDataModels(Long[] ids) throws ClassNotFoundException, IOException {

		DataModel<T>[] dataModels = new DataModel[ids.length];
		int i = 0;

		for (Long id : ids) {
			DataModel<T> dataAboutId = algoCache.getElement(id);

			if (dataAboutId == null) {
				dataAboutId = dao.find(id);
				if (dataAboutId != null) { // else - id not found in hard disk
					if (algoCache.isFull()) {
						numberOfSwaps++;
					}
					algoCache.putElement(id, dataAboutId);
				}
			}
			
			dataModels[i++] = dataAboutId;
		}

		return dataModels;
	}
}
