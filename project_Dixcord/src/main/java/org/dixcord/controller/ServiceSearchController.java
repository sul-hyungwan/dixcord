package org.dixcord.controller;

import java.util.List;

import org.dixcord.domain.NoticeVO;
import org.dixcord.domain.RoomVO;
import org.dixcord.service.ServiceSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.log4j.Log4j;

@Log4j
@RestController
public class ServiceSearchController {
	
	@Autowired
    private ServiceSearchService service;

	@GetMapping("/api/service/search")
    public List<NoticeVO> search(@RequestParam("keyword") String keyword) {
		
		log.info("검색 : " + keyword);
		
		List<NoticeVO> temp = service.search(keyword);
        
        log.warn(temp);
		
        return temp;
    }
}
