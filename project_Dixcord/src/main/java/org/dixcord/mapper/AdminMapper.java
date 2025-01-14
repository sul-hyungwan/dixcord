package org.dixcord.mapper;

import java.util.List;

import org.dixcord.domain.AdminInquiryVO;
import org.dixcord.domain.InquiryVo;

public interface AdminMapper {

	// 모든 문의 내역 가져오기
	public List<InquiryVo> getAllInquiry();
	// 문의 답변 달기
	public int setInquiryAdminMessage(AdminInquiryVO vo);
	// 답변 작성 완료 시 문의 상태 업데이트
	public int updateInquiryResult(int inquiryNo);
	// 문의 삭제 하기
	public int deleteInquiryByAdmin(int inquiryNo);
	// 문의 답변 내용 가져오기
	public AdminInquiryVO getAdminInquiryByInquiryNo(int inquiryNo);
	// 방 멤버 삭제
	public int deleteRoomMember(int roomNumber);
	// 방 삭제
	public int deleteRoom(int roomNumber);
}
