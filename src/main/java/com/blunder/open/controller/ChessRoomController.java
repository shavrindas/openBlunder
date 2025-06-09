package com.blunder.open.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.blunder.open.entity.ChessRoom;
import com.blunder.open.entity.User;
import com.blunder.open.service.ChessRoomService;
import com.blunder.open.service.UserService;

import jakarta.servlet.http.HttpSession;



@Controller
@RequestMapping("/chess")
public class ChessRoomController {

	
    @Autowired
    private UserService userService;
    
    @Autowired
    private ChessRoomService chessRoomService;
    
	@PostMapping("/room")
	public String createRoom(@RequestParam String roomName,
	                         @RequestParam(required = false) String roomPass,
	                         @RequestParam Integer hostUserId,
	                         HttpSession session,
	                         Model model) {
	    User hostUser = userService.findById(hostUserId); // 유저 조회

	    if (hostUser == null) {
	        return "redirect:/home"; // 실패 처리
	    }

	    ChessRoom room = new ChessRoom();
	    room.setRoomName(roomName);
	    room.setRoomPass(roomPass);
	    room.setHostUser(hostUser);
	    room.setRoomStatus(ChessRoom.RoomStatus.WAITING);

	    // 필요한 경우 색상 랜덤 배정
	    room.setHostColor(ChessRoom.ChessColor.WHITE);
	    room.setGuestColor(ChessRoom.ChessColor.BLACK);
	    room.setPlayTurn(ChessRoom.ChessColor.WHITE);

	    ChessRoom savedRoom = chessRoomService.save(room);

	    // 생성 후 바로 게임방 입장
	    return "redirect:/chess/game/" + savedRoom.getId();
	}

	@GetMapping("/game/{roomId}")
	public String enterGameRoom(@PathVariable Integer roomId, HttpSession session, Model model) {
	    User user = (User) session.getAttribute("user");
	    if (user == null) return "redirect:/login";

	    Optional<ChessRoom> roomOpt = chessRoomService.findById(roomId);
	    if (roomOpt.isEmpty()) return "redirect:/home";

	    ChessRoom room = roomOpt.get();

	    model.addAttribute("user", user);
	    model.addAttribute("room", room);

	    return "ChessGame/ChessGamePage";  // Thymeleaf 게임 페이지 뷰 이름
	}


	
	@GetMapping("/waiting-rooms")
	@ResponseBody
	public List<ChessRoom> getWaitingRooms() {
	    return chessRoomService.findWaitingRooms();
	}

	
	@PostMapping("/room/{roomId}/join")
	public String joinRoom(@PathVariable Integer roomId, HttpSession session) {
	    User user = (User) session.getAttribute("user");
	    if (user == null) return "redirect:/login";

	    Optional<ChessRoom> roomOpt = chessRoomService.findById(roomId);
	    if (roomOpt.isEmpty()) return "redirect:/home";

	    ChessRoom room = roomOpt.get();
	    
	    if (room.getGuestUser() == null) {
	        room.setGuestUser(user);
	        chessRoomService.save(room);
	    }

	    return "redirect:/chess/game/" + room.getId();
	}


}

