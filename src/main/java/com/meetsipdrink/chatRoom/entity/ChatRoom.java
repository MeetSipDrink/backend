package com.meetsipdrink.chatRoom.entity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.meetsipdrink.audit.Auditable;
import com.meetsipdrink.chatRoomParticipant.entity.ChatRoomParticipant;
import com.meetsipdrink.member.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "chat_room")
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ChatRoom  extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatRoomId;

    @Column(name = "room_name", nullable = false)
    private String roomName;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "host_id")  // 방장 정보를 저장할 외래키
    private Member host;


    @Getter
    @Setter
    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference // 정방향 참조: ChatRoom에서 Member 목록을 직렬화
    private Set<Member> participant = new HashSet<>();


    public void addMember(Member member) {
        if (!participant.contains(member)) {
            participant.add(member);
            member.setChatRoom(this);
        }
    }

    public void removeMember(Member member) {
        if (participant.contains(member)) {
            participant.remove(member);
            member.setChatRoom(null);  // Member 객체에서도 ChatRoom 제거
        }
    }
}
