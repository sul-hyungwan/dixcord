package org.dixcord.service;

import java.util.List;

import org.dixcord.domain.InquiryVo;

public interface InquiryService {
	public int inquiryInsert(InquiryVo ivo);
	public List<InquiryVo> userInquirieList(int userCode);
}
