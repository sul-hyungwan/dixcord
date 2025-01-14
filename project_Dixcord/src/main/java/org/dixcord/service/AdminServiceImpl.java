package org.dixcord.service;

import java.util.List;

import org.dixcord.domain.AdminInquiryVO;
import org.dixcord.domain.InquiryVo;
import org.dixcord.mapper.AdminMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.log4j.Log4j;

@Log4j
@Service
public class AdminServiceImpl implements AdminService{

	@Autowired
	private AdminMapper mapper;
	
	@Override
	public List<InquiryVo> getAllInquiry() {
		return mapper.getAllInquiry();
	}
	
	@Transactional
	@Override
	public int setInquiryAdminMessage(AdminInquiryVO vo) {
		int result = mapper.setInquiryAdminMessage(vo);
		if(result > 0) {
			return mapper.updateInquiryResult(vo.getInquiryNo());
		}else {
			return 0;
		}
	}
	
	@Override
	public int deleteInquiryByAdmin(int inquiryNo) {
		return mapper.deleteInquiryByAdmin(inquiryNo);
	}
	
	@Override
	public AdminInquiryVO getAdminInquiryByInquiryNo(int inquiryNo) {
		return mapper.getAdminInquiryByInquiryNo(inquiryNo);
	}
	
	@Transactional
	@Override
	public int deleteRoom(int roomNumber) {
		int result = mapper.deleteRoomMember(roomNumber);
		if(result > 0) {
			return mapper.deleteRoom(roomNumber);
		}else {
			return mapper.deleteRoom(roomNumber);
		}
	}
}
