package org.dixcord.service;

import java.util.List;

import org.dixcord.domain.InquiryVo;
import org.dixcord.mapper.InquiryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.log4j.Log4j;

@Log4j
@Service
public class InquiryServiceImpl implements InquiryService{
	
	@Autowired
	private InquiryMapper mapper;

	@Override
	public int inquiryInsert(InquiryVo ivo) {
		log.info("1대1 문의글 작성... ");
		return mapper.inquiryInsert(ivo);
	}

	@Override
	public List<InquiryVo> userInquirieList(int userCode) {
		log.info("유저코드 가져오기..." + userCode);
		log.warn("유저코드 가져오기..." + userCode);
		return mapper.userInquirieList(userCode);
	}
	
}
