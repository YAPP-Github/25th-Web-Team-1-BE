package org.domainmodule.post.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.domainmodule.common.entity.BaseAuditEntity;
import org.domainmodule.post.entity.type.PostStatusType;
import org.domainmodule.postgroup.entity.PostGroup;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseAuditEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "post_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "post_group_id")
	private PostGroup postGroup;

	@Column(length = 500)
	private String title;

	@Column(length = 500)
	private String summary;

	@Column(columnDefinition = "TEXT")
	private String content;

	@Enumerated(EnumType.STRING)
	private PostStatusType status;

	@Column(name = "upload_time")
	private LocalDateTime uploadTime;

	private Integer displayOrder;

	@OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE, orphanRemoval = true)
	private List<PostImage> postImages = new ArrayList<>();

	@OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE, orphanRemoval = true)
	private List<PromptHistory> promptHistories = new ArrayList<>();

	@Builder(access = AccessLevel.PRIVATE)
	private Post(
		PostGroup postGroup,
		String title,
		String summary,
		String content,
		PostStatusType status,
		LocalDateTime uploadTime,
		Integer displayOrder
	) {
		this.postGroup = postGroup;
		this.title = title;
		this.summary = summary;
		this.content = content;
		this.status = status;
		this.uploadTime = uploadTime;
		this.displayOrder = displayOrder;
	}

	public static Post create(
		PostGroup postGroup,
		String title,
		String summary,
		String content,
		PostStatusType status,
		LocalDateTime uploadTime,
		Integer displayOrder
	) {
		return Post.builder()
			.postGroup(postGroup)
			.title(title)
			.summary(summary)
			.content(content)
			.status(status)
			.uploadTime(uploadTime)
			.displayOrder(displayOrder)
			.build();
	}

	public void updateStatus(PostStatusType status) {
		this.status = status;
	}

	public void updateUploadTime(LocalDateTime uploadTime) {
		this.uploadTime = uploadTime;
	}

	public void updateDisplayOrder(Integer displayOrder) {
		this.displayOrder = displayOrder;
	}

	public void updateContent(String content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return "Post{"
			+ "title='" + title + '\''
			+ ", summary='" + summary + '\''
			+ ", content='" + content + '\''
			+ ", status=" + status
			+ ", uploadTime=" + uploadTime + '\''
			+ ", displayOrder=" + displayOrder
			+ '}';
	}

	public void updatePostContent(String summary, String content, PostStatusType status) {
		this.summary = summary;
		this.content = content;
		this.status = status;
	}
}
