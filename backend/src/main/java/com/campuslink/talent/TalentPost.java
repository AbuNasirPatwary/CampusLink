package com.campuslink.talent;

import com.campuslink.common.model.BaseEntity;
import com.campuslink.user.User;
import jakarta.persistence.*;

@Entity
@Table(name="talent_posts")
public class TalentPost extends BaseEntity {
    @ManyToOne(optional=false,fetch=FetchType.LAZY) private User author;
    @Enumerated(EnumType.STRING) @Column(nullable=false) private TalentCategory category;
    @Column(nullable=false) private String title;
    @Column(length=10000) private String content;
    private String mediaUrl;
    private String thumbnailUrl;
    private String location;
    @Enumerated(EnumType.STRING) @Column(nullable=false) private TalentStatus status=TalentStatus.PUBLISHED;
    @Column(nullable=false) private boolean featured;

    public User getAuthor(){return author;} public void setAuthor(User author){this.author=author;}
    public TalentCategory getCategory(){return category;} public void setCategory(TalentCategory category){this.category=category;}
    public String getTitle(){return title;} public void setTitle(String title){this.title=title;}
    public String getContent(){return content;} public void setContent(String content){this.content=content;}
    public String getMediaUrl(){return mediaUrl;} public void setMediaUrl(String mediaUrl){this.mediaUrl=mediaUrl;}
    public String getThumbnailUrl(){return thumbnailUrl;} public void setThumbnailUrl(String thumbnailUrl){this.thumbnailUrl=thumbnailUrl;}
    public String getLocation(){return location;} public void setLocation(String location){this.location=location;}
    public TalentStatus getStatus(){return status;} public void setStatus(TalentStatus status){this.status=status;}
    public boolean isFeatured(){return featured;} public void setFeatured(boolean featured){this.featured=featured;}
}
