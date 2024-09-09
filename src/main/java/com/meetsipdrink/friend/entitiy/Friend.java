package com.meetsipdrink.friend.entitiy;

import com.meetsipdrink.audit.Auditable;
import com.meetsipdrink.member.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

import static javax.persistence.CascadeType.*;
import static javax.persistence.EnumType.*;

@Entity
@Table(name = "friend", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"requester_Id", "recipient_Id"})
})
@Getter
@Setter
@NoArgsConstructor
public class Friend extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long friendId;

    @ManyToOne(cascade = ALL)
    @JoinColumn(name = "requester_id")
    private Member requester;

    @ManyToOne(cascade = ALL)
    @JoinColumn(name = "recipient_id")
    private Member recipient;


    @Enumerated(STRING)
    @Column(nullable = false)
    private Status friendStatus = Status.PENDING;

    public void setRequester(Member requester) {
        this.requester = requester;
        if (!requester.getSentFriendRequests().contains(this)) {
            requester.addSentFriendRequest(this);
        }
    }

    public void setRecipient(Member recipient) {
        this.recipient = recipient;
        if (!recipient.getReceivedFriendRequests().contains(this)) {
            recipient.addReceivedFriendRequest(this);
        }
    }

    public enum Status {
        PENDING("대기중"),
        ACCEPTED("요청 수락"),
        REJECTED("요청 거절"),
        DISCONNECTED("친구 끊김");
        @Getter
        private final String status;
        Status(String status) {
            this.status = status;
        }
    }
}
