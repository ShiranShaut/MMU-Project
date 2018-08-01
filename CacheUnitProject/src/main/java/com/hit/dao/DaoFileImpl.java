package com.hit.dao;

import java.io.*;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;

import org.omg.PortableInterceptor.USER_EXCEPTION;

import com.hit.dm.DataModel;

public class DaoFileImpl<T> implements IDao<Long, DataModel<T>> {
	private String file;
	private Map<Long, DataModel<T>> dataModelsMap = new LinkedHashMap<Long, DataModel<T>>();

	@SuppressWarnings("unchecked")
	public DaoFileImpl(String filePath) {
		
		String currentDirectory = Paths.get(".").toAbsolutePath().normalize().toString();
		file = currentDirectory + "\\" + filePath;
		
		/*for(Integer i=0; i<10; ++i) {
			Long num = Long.parseLong(i.toString());
			dataModelsMap.put(num, new DataModel<T>(num, (T)i));
		}
		
		try {
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file));
			out.writeObject(dataModelsMap);
		} catch (IOException e1) {
			e1.printStackTrace();
		}*/
		
		try {
			@SuppressWarnings("resource")
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
			dataModelsMap = (Map<Long, DataModel<T>>) in.readObject();
		}catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void delete(DataModel<T> t) throws IllegalArgumentException {
		if (dataModelsMap.size() > 0) {
			try {
				dataModelsMap.remove(t.getDataModelId());
			
				ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file));
				out.writeObject(dataModelsMap);
				out.flush();
				out.close();

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void save(DataModel<T> t) {
		try {
			dataModelsMap.put(t.getDataModelId(), t);
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file));
			out.writeObject(dataModelsMap);
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public DataModel<T> find(Long id) throws IllegalArgumentException {
		return dataModelsMap.get(id);
	}
}