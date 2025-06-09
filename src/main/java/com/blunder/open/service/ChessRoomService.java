package com.blunder.open.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blunder.open.entity.ChessRoom;
import com.blunder.open.repository.ChessRoomRepository;

@Service
public class ChessRoomService {

    @Autowired
    private ChessRoomRepository chessRoomRepository;

	
	public ChessRoom save(ChessRoom room) {
	    return chessRoomRepository.save(room);
	}


	public Optional<ChessRoom> findById(Integer roomId) {
		 return chessRoomRepository.findById(roomId);
	}


	public List<ChessRoom> findWaitingRooms() {
	    return chessRoomRepository.findByRoomStatus(ChessRoom.RoomStatus.WAITING);
	}


	public void clearRoomsForUser(Integer userId) {
	    List<ChessRoom> rooms = chessRoomRepository.findByHostUserIdOrGuestUserId(userId, userId);

	    for (ChessRoom room : rooms) {
	        chessRoomRepository.delete(room);

	    }
	}




    
}
