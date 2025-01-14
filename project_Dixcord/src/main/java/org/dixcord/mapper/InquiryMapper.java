package org.dixcord.mapper;

import java.util.List;

import org.dixcord.domain.InquiryVo;

public interface InquiryMapper {
	public int inquiryInsert(InquiryVo ivo);
	public List<InquiryVo> userInquirieList(int userCode);
}
