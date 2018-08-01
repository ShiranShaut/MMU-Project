package com.hit.model;

import java.util.Observable;

public class CacheUnitModel extends Observable implements Model {

	CacheUnitClient cacheUnitClient = new CacheUnitClient();

	public CacheUnitModel() {

	}

	public <T> void updateModelData(T t) {
		if (((String) t).equals("showStatistics")) {
			setChanged();
			notifyObservers(cacheUnitClient.send(t.toString()));
		}
		else {
			cacheUnitClient.send(t.toString());
		}
	}
}