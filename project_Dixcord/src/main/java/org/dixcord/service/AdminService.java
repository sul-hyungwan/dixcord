package org.dixcord.service;

import java.util.List;

import org.dixcord.domain.AdminInquiryVO;
import org.dixcord.domain.InquiryVo;

public interface AdminService {
	
	// 모든 문의 내역 가져오기
	public List<InquiryVo> getAllInquiry();
	// 문의 답변 달기
	public int setInquiryAdminMessage(AdminInquiryVO vo);
	// 문의 삭제 하기
	public int deleteInquiryByAdmin(int inquiryNo);
	// 문의 답변 내용 가져오기
	public AdminInquiryVO getAdminInquiryByInquiryNo(int inquiryNo);
	// 방 삭제
	public int deleteRoom(int roomNumber);

}
