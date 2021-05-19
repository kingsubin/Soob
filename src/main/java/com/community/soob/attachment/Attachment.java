package com.community.soob.attachment;

import com.community.soob.post.domain.Post;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "attachment")
@Entity
public class Attachment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attachment_id", updatable = false)
    private Long id;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(name = "hashed_file_name", nullable = false)
    private String hashedFileName;

    @Column(name = "file_path", nullable = false)
    private String filePath;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @Builder
    public Attachment(String fileName, String hashedFileName, String filePath) {
        this.fileName = fileName;
        this.hashedFileName = hashedFileName;
        this.filePath = filePath;
    }

    public void setPost(Post post) {
        this.post = post;
    }
}
