package com.hit.dm;

import java.io.Serializable;
import java.util.Arrays;

public class DataModel<T> implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long dataModelId;
	private T content;
	
	public DataModel(Long id, T content) {
		this.dataModelId = id;
		this.content = content;
	}

	@Override
	public int hashCode() {
		return content.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return content.equals(obj);
	}

	@Override
	public String toString() {
		return Arrays.toString((byte[])content) + " " + dataModelId.toString();
	}
	
	public Long getDataModelId(){
		return dataModelId;
	}
	
	public void setDataModelId(Long id){
		this.dataModelId = id;
	}
	
	public T getContent() {
		return content;
	}
	
	public void setContent(T content){
		this.content = content;
	}
}