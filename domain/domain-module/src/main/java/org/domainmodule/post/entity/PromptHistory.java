package org.domainmodule.post.entity;

import org.domainmodule.common.entity.BaseTimeEntity;
import org.domainmodule.post.entity.type.PostPromptType;

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
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PromptHistory extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "prompt_history_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "post_id")
	private Post post;

	@Column(columnDefinition = "TEXT")
	private String prompt;

	@Column(columnDefinition = "TEXT")
	private String response;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private PostPromptType promptType;

	@Builder(access = AccessLevel.PRIVATE)
	private PromptHistory(Post post, String prompt, String response, PostPromptType promptType) {
		this.post = post;
		this.prompt = prompt;
		this.response = response;
		this.promptType = promptType;
	}

	public static PromptHistory createPromptHistory(
		Post post,
		String prompt,
		String response,
		PostPromptType promptType
	) {
		return PromptHistory.builder()
			.post(post)
			.prompt(prompt)
			.response(response)
			.promptType(promptType)
			.build();
	}
}
