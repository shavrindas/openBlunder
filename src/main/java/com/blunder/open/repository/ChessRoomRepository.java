package com.blunder.open.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.blunder.open.entity.ChessRoom;
import com.blunder.open.entity.ChessRoom.RoomStatus;


@Repository
public interface ChessRoomRepository extends JpaRepository<ChessRoom, Integer> {

	List<ChessRoom> findByRoomStatus(RoomStatus waiting);
	//List<ChessRoom> findByHostUserIdOrGuestUserId(Integer hostId, Integer guestId);

	@Query("SELECT r FROM ChessRoom r WHERE r.hostUser.id = :hostId OR r.guestUser.id = :guestId")
	List<ChessRoom> findByHostUserIdOrGuestUserId(@Param("hostId") Integer hostId, @Param("guestId") Integer guestId);

	
}
