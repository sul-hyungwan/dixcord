package org.dixcord.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.dixcord.domain.ChatHistoryVO;
import org.dixcord.service.ChatService;
import org.dixcord.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import lombok.extern.log4j.Log4j;

@Log4j
@Component
public class WebSocketChatHandler extends TextWebSocketHandler {

   private final ChatService chatService;
   
   private final RoomService rservice;

   // 고정 방 번호 1번 사용자 관리
   private final Set<WebSocketSession> room1Sessions = ConcurrentHashMap.newKeySet();

   @Autowired
   public WebSocketChatHandler(ChatService chatService, RoomService rservice) {
      this.chatService = chatService;
      this.rservice = rservice;
   }

   // 방 실행하는 타는 함수
   @Override
   public void afterConnectionEstablished(WebSocketSession session) throws Exception {
//      System.out.println("Connection established: " + session.getId());
//        System.out.println(Inet4Address.getLocalHost().getHostAddress()); 

      // 쿼리 문자열 가져오기
      String query = session.getUri().getQuery();

      // 쿼리 문자열 파라미터 분리
      String[] params = query.split("&");

      String userCodeParam = null;
      String textChatNoParam = null;

      // 각 파라미터에서 key=value 형태 확인 및 값 추출
      for (String param : params) {
         if (param.startsWith("userCode=")) {
            userCodeParam = param.replace("userCode=", "");
         } else if (param.startsWith("textChatNo=")) {
            textChatNoParam = param.replace("textChatNo=", "");
         }
      }

      // 유저 코드 저장
      if (userCodeParam != null) {
         log.warn("유저 코드 : " + userCodeParam);
         Integer userCode = Integer.parseInt(userCodeParam);
         session.getAttributes().put("userCode", userCode);
      }

      // 채팅방 번호 저장
      if (textChatNoParam != null) {
         log.warn("채팅방 번호 : " + textChatNoParam);
         Integer textChatNo = Integer.parseInt(textChatNoParam);
         session.getAttributes().put("textChatNo", textChatNo);
      }

      // 방 번호 및 사용자 등록
      room1Sessions.add(session);
      if (userCodeParam == null) {
         System.out.println("User code not found in URL.");
         return;
      }
      if(Integer.parseInt(textChatNoParam) == 0) {
         log.warn("0번 채팅방은 없다");
         return;
      }
      List<ChatHistoryVO> history = chatService.getChatHistory(Integer.parseInt(textChatNoParam));
      ObjectMapper mapper = new ObjectMapper();
      history.forEach(action -> {
         System.out.println("history chatGuid : " + action.getChatGuid());
      });
      

      for (ChatHistoryVO chat : history) {
			String messageJson = mapper.writeValueAsString(
					Map.of(
						    "message", chat.getMessage(),
						    "date", chat.getTextDate(),
						    "userCode", chat.getUserCode(),
						    "userNickName", chat.getUserNickName() != null ? chat.getUserNickName() : "알 수 없는 사용자",
						    "textChatNo", Integer.parseInt(textChatNoParam),
						    "userIcon", chat.getUserIcon(),
						    "chatguid", chat.getChatGuid(),
						    "editText", "(수정됨)",
						    "chatImg", chat.getChatImg() != null ? "http:\\\\" + chat.getChatImg() : "noChatImg",
						    "gifSrc", chat.getGifSrc() != null ? chat.getGifSrc() : "noGifSrc"
						));

			session.sendMessage(new TextMessage(messageJson));
		}
   }

   // handleTextMessage 메서드는 React가 웹소켓을 통해 메시지를 보냈을 때 서버가 바로 받는 부분
   @Override
   protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
      System.out.println("Received message: " + message.getPayload());

      // 메시지를 JSON 문자열로 파싱 (클라이언트가 보낸 JSON 메시지 파싱)
      ObjectMapper mapper = new ObjectMapper();
      Map<String, Object> receivedMessage = mapper.readValue(message.getPayload(), Map.class);

      // 서버에서 오브젝트로 던진거 가져오는 법 (receivedMessage.get)
      ChatHistoryVO chat = new ChatHistoryVO();
      String action = (String) receivedMessage.get("action");
      System.out.println("findAction : " + action);

      if ("deleteMessage".equals(action)) {
         int userCode = (int) receivedMessage.get("userCode");
         String chatGuid = ((String) receivedMessage.get("chatguid"));
         log.warn("chatDeleteGuid : " + chatGuid);
         chat.setChatGuid(chatGuid);
         chat.setUserCode(userCode);
         int result = chatService.deleteChatHistory(chat);

         JsonObject messagePayload = new JsonObject();
         messagePayload.addProperty("userCode", 0);
//         messagePayload.addProperty("userNickName", newChat.getUserNickName());
//         messagePayload.addProperty("message", newChat.getMessage());
//         messagePayload.addProperty("date", newChat.getTextDate());
//         messagePayload.addProperty("userIcon", newChat.getUserIcon());
         messagePayload.addProperty("chatguid", chatGuid);

         // Gson 인스턴스를 생성합니다.
         Gson gson = new Gson();

         System.out.println("deleteMessage 성공 " + result);
         if (result > 0) {
            for (WebSocketSession userSession : room1Sessions) {
               if (userSession.isOpen()) {
                  userSession.sendMessage(new TextMessage(gson.toJson(messagePayload)));
               }
            }
         }
      }

      if ("editMessage".equals(action)) {
         String chatGuid = ((String) receivedMessage.get("chatguid"));
         String editMessage = ((String) receivedMessage.get("message"));
         String updateGuid = ((String) receivedMessage.get("chatguid"));
         chat.setChatGuid(updateGuid);
         chat.setMessage(editMessage);

         int result = chatService.saveEditedMessage(chat);

         if (result > 0) {
            ChatHistoryVO updatedChat = chatService.getChatHistoryUpdate(updateGuid);

            JsonObject editPayload = new JsonObject();
            editPayload.addProperty("action", "editMessage");
            editPayload.addProperty("userCode", 1);
            editPayload.addProperty("userNickName", updatedChat.getUserNickName());
            editPayload.addProperty("message", updatedChat.getMessage());
            editPayload.addProperty("date", updatedChat.getTextDate());
            editPayload.addProperty("userIcon", updatedChat.getUserIcon());
            editPayload.addProperty("chatguid", updateGuid);
            editPayload.addProperty("editText", "(수정됨)");

            Gson gson = new Gson();
            for (WebSocketSession userSession : room1Sessions) {
               if (userSession.isOpen()) {
                  userSession.sendMessage(new TextMessage(gson.toJson(editPayload)));
               }
            }
         }
      }

      if ("sendMessage".equals(action)) {
			// 기본 채팅 메시지 처리
			log.warn("기본 채팅 메시지 처리");
			// 메시지만 ChatHistoryVO 객체에 담아 저장
			ChatHistoryVO standardChat = new ChatHistoryVO();
			standardChat.setMessage((String) receivedMessage.get("message"));         // 유저 메시지 저장
			standardChat.setUserNickName((String) receivedMessage.get("userNickName")); // 유저 닉네임 저장
			standardChat.setUserIcon((String) receivedMessage.get("userIcon"));         // 유저 아이콘 저장
			int userCode = (int) receivedMessage.get("userCode");                  
			standardChat.setUserCode(userCode);                                 // 유저 코드 저장
			int textChatNo = (int) receivedMessage.get("textChatNo");
			standardChat.setTextChatNo(textChatNo);                              // 채팅방 번호 저장

			String chatImg = (String) receivedMessage.get("img");
			String gifSrc = (String) receivedMessage.get("gifSrc");

			System.out.println("클라이언트에서 온 이미지 확인: " + chatImg);
			System.out.println("클라이언트에서 온 GIF 확인2: " + gifSrc);

			if (chatImg != null && !chatImg.isEmpty()) {
				standardChat.setChatImg(chatImg);
				chatService.saveChatHistory(standardChat);

				// 현재 시스템 시간 포맷팅
				SimpleDateFormat formatter = new SimpleDateFormat("yy.MM.dd a h:mm");
				String formattedDate = formatter.format(new Date());
				standardChat.setTextDate(formattedDate);
			} else if (gifSrc != null && !gifSrc.isEmpty()) { 
				standardChat.setGifSrc(gifSrc);  
				chatService.saveChatHistory(standardChat);

				// 현재 시스템 시간 포맷팅
				SimpleDateFormat formatter = new SimpleDateFormat("yy.MM.dd a h:mm");
				String formattedDate = formatter.format(new Date());
				standardChat.setTextDate(formattedDate);
			} else {
				// 둘 다 없으면 그냥 저장
				chatService.saveChatHistory(standardChat);
			}

			// 채팅 기록 저장 서비스 호출
			ChatHistoryVO newChat = chatService.getChatHistoryNow(textChatNo);
			JsonObject messagePayload = new JsonObject();
			messagePayload.addProperty("userCode", newChat.getUserCode());
			messagePayload.addProperty("userNickName", newChat.getUserNickName());
			messagePayload.addProperty("message", newChat.getMessage());
			messagePayload.addProperty("date", newChat.getTextDate());
			messagePayload.addProperty("textChatNo", textChatNo);
			messagePayload.addProperty("userIcon", newChat.getUserIcon());
			messagePayload.addProperty("chatguid", newChat.getChatGuid());
			if(chatImg != null && !chatImg.isEmpty()) {
				log.warn("이미지 파일 : " + newChat.getChatImg());
				messagePayload.addProperty("chatImg", "http:\\" + newChat.getChatImg());
			}else if(gifSrc != null && !gifSrc.isEmpty()) {
				messagePayload.addProperty("gifSrc", newChat.getGifSrc());
				
			}



			// Gson 인스턴스를 생성합니다.
			Gson gson = new Gson();
			// 방 1번 사용자끼리 메시지 브로드캐스트
			for (WebSocketSession userSession : room1Sessions) {
				if (userSession.isOpen()) {
					userSession.sendMessage(new TextMessage(gson.toJson(messagePayload)));
				}
			}
		}
      
      if("alarm".equals(action)) {
         log.warn("알람알람알람");
         // alarm 을 받아서 처리
         int userCode = (int) receivedMessage.get("userCode");
         log.warn("알람알람알람" + userCode);
         return;
      }
      
      if("userLogin".equals(action)) {
         int userCode = (int) receivedMessage.get("userCode");
         JsonObject messagePayload = new JsonObject();
         messagePayload.addProperty("action", "USERSTATE_UPDATE");
         messagePayload.addProperty("userCode", userCode);
         
         Gson gson = new Gson();
         
         for (WebSocketSession userSession : room1Sessions) {
            if (userSession.isOpen()) {
               userSession.sendMessage(new TextMessage(gson.toJson(messagePayload)));
            }
         }
      }
      
      if(action.equals("welcomeMessage")) {
         int userCode = (int) receivedMessage.get("userCode");
         String userNickName = (String) receivedMessage.get("userNickName");
         int roomNumber = (int) receivedMessage.get("roomNumber");
         int textChatNo = chatService.getTextChatNo(roomNumber)[0];
         String welcomeMessage = rservice.getRoomInfo(roomNumber).getWelcomeMessage();
         
         
         JsonObject messagePayload = new JsonObject();
         messagePayload.addProperty("action", "welcomeMessage");
         messagePayload.addProperty("userCode", userCode);
         messagePayload.addProperty("userNickName", userNickName);
         messagePayload.addProperty("textChatNo", textChatNo);
         messagePayload.addProperty("welcomeMessage", welcomeMessage);
         
         // 현재 시스템 시간 포맷팅
         SimpleDateFormat formatter = new SimpleDateFormat("yy.MM.dd a h:mm");
         String formattedDate = formatter.format(new Date());
         messagePayload.addProperty("formattedDate", formattedDate);
         
         ChatHistoryVO vo = new ChatHistoryVO();
         vo.setMessage(welcomeMessage);
         vo.setTextChatNo(textChatNo);
         vo.setTextDate(formattedDate);
         vo.setUserNickName(userNickName);
         vo.setUserCode(userCode);
         vo.setUserIcon("welcome");
         chatService.saveChatHistory(vo);
         
         Gson gson = new Gson();
         
         for (WebSocketSession userSession : room1Sessions) {
            if (userSession.isOpen()) {
               userSession.sendMessage(new TextMessage(gson.toJson(messagePayload)));
            }
         }
      }
      
      if(action.equals("exitRoom")) {
          int userCode = (int) receivedMessage.get("userCode");
          String userNickName = (String) receivedMessage.get("userNickName");
          int roomNumber = (int) receivedMessage.get("roomNumber");
          int textChatNo = chatService.getTextChatNo(roomNumber)[0];
          String exitMessage = "님이 방을 나가셨습니다.";
          
          
          JsonObject messagePayload = new JsonObject();
          messagePayload.addProperty("action", "exitRoom");
          messagePayload.addProperty("userCode", userCode);
          messagePayload.addProperty("userNickName", userNickName);
          messagePayload.addProperty("textChatNo", textChatNo);
          messagePayload.addProperty("exitMessage", exitMessage);
          
          // 현재 시스템 시간 포맷팅
          SimpleDateFormat formatter = new SimpleDateFormat("yy.MM.dd a h:mm");
          String formattedDate = formatter.format(new Date());
          messagePayload.addProperty("formattedDate", formattedDate);
          
          ChatHistoryVO vo = new ChatHistoryVO();
          vo.setMessage(exitMessage);
          vo.setTextChatNo(textChatNo);
          vo.setTextDate(formattedDate);
          vo.setUserNickName(userNickName);
          vo.setUserCode(userCode);
          vo.setUserIcon("exit");
          chatService.saveChatHistory(vo);
          
          Gson gson = new Gson();
          
          for (WebSocketSession userSession : room1Sessions) {
             if (userSession.isOpen()) {
                userSession.sendMessage(new TextMessage(gson.toJson(messagePayload)));
             }
          }
       }
       
      if ("close".equals(action)) {
         log.warn("클로즈");
         room1Sessions.remove(session);
      }
   }

   // 메시지를 방 내 모든 사용자에게 브로드캐스팅
   private void broadcastToRoom(Map<String, Object> message) throws Exception {
      ObjectMapper mapper = new ObjectMapper();
      String messageJson = mapper.writeValueAsString(message);
      log.warn("브로드캐스팅 함수 탔음");
      for (WebSocketSession userSession : room1Sessions) {
         if (userSession.isOpen()) {
            userSession.sendMessage(new TextMessage(messageJson));
            log.warn("브로드캐스팅 함수 끝");
         }
      }
   }

   @Override
   public void afterConnectionClosed(WebSocketSession session, org.springframework.web.socket.CloseStatus status)
         throws Exception {
      System.out.println("Connection closed: " + session.getId() + " with status: " + status);

      // 연결이 끊어진 사용자를 방에서 제거
      room1Sessions.remove(session);
   }
}
