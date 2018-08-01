package com.hit.driver;

import com.hit.controller.CacheUnitController;
import com.hit.controller.Controller;
import com.hit.model.CacheUnitModel;
import com.hit.model.Model;
import com.hit.view.CacheUnitView;
import com.hit.view.View;

public class Driver {
	
	public static void main(String[] args){
		Model model = new CacheUnitModel();
		View view = new CacheUnitView();
		Controller controller = new CacheUnitController(model, view);
		
		((CacheUnitModel)model).addObserver(controller);
		((CacheUnitView)view).addObserver(controller);
		view.start();
		}
}