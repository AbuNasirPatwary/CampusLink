package com.campuslink.complaint;

import com.campuslink.common.model.BaseEntity;
import com.campuslink.user.User;
import jakarta.persistence.*;

@Entity
@Table(name = "complaint_posts")
public class ComplaintPost extends BaseEntity {
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private User author;
    @Column(nullable = false) private String title;
    @Column(nullable = false, length = 5000) private String content;
    private String imageUrl;
    private String videoUrl;
    @Enumerated(EnumType.STRING) @Column(nullable = false)
    private ComplaintStatus status = ComplaintStatus.OPEN;
    @Column(length = 3000) private String officialResponse;

    public User getAuthor(){return author;} public void setAuthor(User author){this.author=author;}
    public String getTitle(){return title;} public void setTitle(String title){this.title=title;}
    public String getContent(){return content;} public void setContent(String content){this.content=content;}
    public String getImageUrl(){return imageUrl;} public void setImageUrl(String imageUrl){this.imageUrl=imageUrl;}
    public String getVideoUrl(){return videoUrl;} public void setVideoUrl(String videoUrl){this.videoUrl=videoUrl;}
    public ComplaintStatus getStatus(){return status;} public void setStatus(ComplaintStatus status){this.status=status;}
    public String getOfficialResponse(){return officialResponse;} public void setOfficialResponse(String officialResponse){this.officialResponse=officialResponse;}
}
