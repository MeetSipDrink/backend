package com.meetsipdrink.board.entity;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class PostImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postImage;

    @NotNull
    @Column(name = "post_image_URL", nullable = false)
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "POST_ID")
    private Post post;

    public void setPost(Post post) {
        this.post = post;
        if(!post.getPostImages().contains(this)) {
            post.setPostImages(this);
        }
    }
}
