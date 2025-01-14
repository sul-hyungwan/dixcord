package org.dixcord.service;

import java.util.List;

import org.dixcord.domain.NoticeAttachVO;
import org.dixcord.domain.NoticeVO;
import org.dixcord.mapper.NoticeAttachMapper;
import org.dixcord.mapper.NoticeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.log4j.Log4j;

@Log4j
@Service
public class NoticeServiceImpl implements NoticeService {

   @Autowired
   private NoticeMapper mapper;
   @Autowired
   private NoticeAttachMapper attachMapper;
   
   @Override
   public List<NoticeVO> noticeList() {
      log.info("공지글 리스트...");
      return mapper.noticeList();
   }

   @Override
   public NoticeVO read(int idx) {
      log.info("공지글 읽기...");
      return mapper.read(idx);
   }

   @Override
   public int modify(NoticeVO nvo) {
      log.info("공지글 수정...");
      return mapper.modify(nvo);
   }

   @Transactional
   @Override
   public int delete(int idx) {
      log.info("공지글 삭제...");
      
      attachMapper.deleteNotice(idx);
      return mapper.delete(idx);
   }
   
   @Transactional
   @Override
   public int insert(NoticeVO nvo, NoticeAttachVO navo) {
       // 게시글 등록
       log.info("게시글 번호 확인 전 : " + nvo.getIdx());
       int result = mapper.insert(nvo);
       log.info("게시글 등록 후 idx 번호 : " + nvo.getIdx());

       if (navo != null) { // 파일 정보가 있을 때만 첨부파일 등록
           navo.setIdx(nvo.getIdx());
           attachMapper.insertImg(navo);
       }

       return result;
   }
//   log.info("공지글 등록...");
//   return mapper.insert(nvo);

//   @Override
//   public int insertImg(NoticeAttachVO navo) {
//      return attachMapper.insertImg(navo);
//   }
   
   @Override
   public List<NoticeAttachVO> getAttachList(int idx) {
      log.info("getAttachList... " + idx);
      return attachMapper.findByIdx(idx);
   }

   @Override
   public String readTitle(int idx) {
      return mapper.readTitle(idx);
   }

   @Override
   public String noticeImage(int idx) {
      return attachMapper.noticeImage(idx);
   }
   
   
}
