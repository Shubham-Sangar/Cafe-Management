package com.example.demo.restImpl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.rest.DashboardRest;
import com.example.demo.service.DashboardService;

@CrossOrigin(origins="*",methods= {RequestMethod.GET, RequestMethod.POST})
@RestController

public class DashboardRestImpl implements DashboardRest {

	@Autowired
	DashboardService dashboardService;

	@Override
	public ResponseEntity<Map<String, Object>> getCount() {

		return dashboardService.getCount();

	}

}
