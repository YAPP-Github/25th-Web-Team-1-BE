package org.domainmodule.user.entity;

import java.time.LocalDateTime;

import org.domainmodule.common.entity.BaseTimeEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "token_id")
	private Long id;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@Column(nullable = false, unique = true, length = 500)
	private String token;

	@Column(nullable = false)
	private LocalDateTime expirationDate;

	@Builder
	private RefreshToken(User user, String token) {
		this.user = user;
		this.token = token;
		this.expirationDate = LocalDateTime.now().plusDays(1); //TODO 리프래쉬 토큰 만료시간으로 변경해야 함
	}

	public void renewToken(String token) {
		this.token = token;
	}
}
