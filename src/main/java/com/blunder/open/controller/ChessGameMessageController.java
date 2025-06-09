package com.blunder.open.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.blunder.open.entity.ChessRoom;
import com.blunder.open.service.ChessRoomService;

import java.util.Optional;

@Controller
public class ChessGameMessageController {

    @Autowired
    private ChessRoomService chessRoomService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/move")
    public void handleMove(@Payload MoveMessage message) {
    Optional<ChessRoom> roomOpt = chessRoomService.findById(message.getRoomId());
    	if (roomOpt.isEmpty()) return;

    	ChessRoom room = roomOpt.get();

    	ChessRoom.ChessColor nextTurn =
    	    room.getPlayTurn() == ChessRoom.ChessColor.WHITE
    	        ? ChessRoom.ChessColor.BLACK
    	        : ChessRoom.ChessColor.WHITE;

    	room.setFen(message.getFen());
    	room.setPgn(message.getPgn());
    	room.setPlayTurn(nextTurn);
    	chessRoomService.save(room);
    	message.setPlayTurn(nextTurn.name());
    	messagingTemplate.convertAndSend(
    	    "/topic/room/" + message.getRoomId(),
    	    message
    	);

    }

    public static class MoveMessage {
        private Integer roomId;
        private String fen;
        private String pgn;
        private String playTurn; 
        
        public Integer getRoomId() { return roomId; }
        public void setRoomId(Integer roomId) { this.roomId = roomId; }

        public String getFen() { return fen; }
        public void setFen(String fen) { this.fen = fen; }

        public String getPgn() { return pgn; }
        public void setPgn(String pgn) { this.pgn = pgn; }
        
        public String getPlayTurn() { return playTurn; }
        public void setPlayTurn(String playTurn) { this.playTurn = playTurn; }
    }
}
