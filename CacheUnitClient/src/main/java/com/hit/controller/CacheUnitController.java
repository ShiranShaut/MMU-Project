package com.hit.controller;

import java.util.Observable;
import java.util.Observer;

import org.omg.CosNaming.IstringHelper;

import com.hit.model.Model;
import com.hit.view.View;

public class CacheUnitController implements Controller, Observer {
	Model model;
	View view;

	public CacheUnitController(Model model, View view) {
		this.model = model;
		this.view = view;
	}

	@Override
	public void update(Observable observable, Object arg) {
		if(observable instanceof Model) {
			view.updateUIData(arg);
		}
		else if(observable instanceof View) {
			model.updateModelData(arg);
		}
	}
}