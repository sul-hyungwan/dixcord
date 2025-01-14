package org.dixcord.controller;

import java.util.List;

import org.dixcord.domain.RoomVO;
import org.dixcord.service.RoomSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.log4j.Log4j;

@Log4j
@RestController
public class RoomSearchController {
	
	@Autowired
	private RoomSearchService service;
	
	@GetMapping("/api/rooms/search")
	public List<RoomVO> searchRoomTitles(@RequestParam("query") String query) {
		log.info("검색 : " + query);
		
		List<RoomVO> temp = service.searchRoomTitles(query);
		
		log.warn(temp);
		
        return temp;
    }
}