package com.hit.dao;
import java.io.*;

@SuppressWarnings("hiding")
public interface IDao<ID extends Serializable,String> {
	void delete(String t) throws IllegalArgumentException;
	void save(String t);
	String find(ID id) throws IllegalArgumentException;
}