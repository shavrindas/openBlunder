package com.blunder.open.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "chess_room")
public class ChessRoom {
	
	public enum RoomStatus {
	    WAITING,
	    PLAYING,
	    FINISHED
	}

	public enum ChessColor {
	    WHITE,
	    BLACK
	}
	

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "room_name", nullable = false, unique = true)
    private String roomName;

    @Column(name = "room_pass")
    private String roomPass;

    @ManyToOne
    @JoinColumn(name = "host_user_id")
    private User hostUser;

    @ManyToOne
    @JoinColumn(name = "guest_user_id")
    private User guestUser;

    @Enumerated(EnumType.STRING)
    @Column(name = "room_status")
    private RoomStatus roomStatus = RoomStatus.WAITING;

    @Enumerated(EnumType.STRING)
    @Column(name = "host_color")
    private ChessColor hostColor = ChessColor.BLACK;

    @Enumerated(EnumType.STRING)
    @Column(name = "guest_color")
    private ChessColor guestColor = ChessColor.WHITE;

    @Enumerated(EnumType.STRING)
    @Column(name = "play_turn")
    private ChessColor playTurn = ChessColor.WHITE;

    @Column(name = "pgn", columnDefinition = "TEXT")
    private String pgn;

    @Column(name = "fen", columnDefinition = "TEXT")
    private String fen;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    // Getters and Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getRoomName() { return roomName; }
    public void setRoomName(String roomName) { this.roomName = roomName; }

    public String getRoomPass() { return roomPass; }
    public void setRoomPass(String roomPass) { this.roomPass = roomPass; }

    public User getHostUser() { return hostUser; }
    public void setHostUser(User hostUser) { this.hostUser = hostUser; }

    public User getGuestUser() { return guestUser; }
    public void setGuestUser(User guestUser) { this.guestUser = guestUser; }

    public RoomStatus getRoomStatus() { return roomStatus; }
    public void setRoomStatus(RoomStatus roomStatus) { this.roomStatus = roomStatus; }

    public ChessColor getHostColor() { return hostColor; }
    public void setHostColor(ChessColor hostColor) { this.hostColor = hostColor; }

    public ChessColor getGuestColor() { return guestColor; }
    public void setGuestColor(ChessColor guestColor) { this.guestColor = guestColor; }

    public ChessColor getPlayTurn() { return playTurn; }
    public void setPlayTurn(ChessColor playTurn) { this.playTurn = playTurn; }

    public String getPgn() { return pgn; }
    public void setPgn(String pgn) { this.pgn = pgn; }

    public String getFen() { return fen; }
    public void setFen(String fen) { this.fen = fen; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}




