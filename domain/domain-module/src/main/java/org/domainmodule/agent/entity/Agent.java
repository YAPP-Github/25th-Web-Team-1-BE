package org.domainmodule.agent.entity;

import org.domainmodule.agent.entity.type.AgentPlatform;
import org.domainmodule.agent.entity.type.AgentType;
import org.domainmodule.common.entity.BaseTimeEntity;
import org.domainmodule.snstoken.entity.SnsToken;
import org.domainmodule.user.entity.User;

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
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Agent extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "agent_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sns_token_id", nullable = false)
	private SnsToken snsToken;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private AgentPlatform platform;

	@Column(length = 500)
	private String accessToken;

	@Column(length = 500)
	private String refreshToken;

	@Column(nullable = false, unique = true, length = 100)
	private String accountId;

	@Column(length = 255)
	private String bio;

	@Column(nullable = false)
	private Boolean autoMode;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private AgentType agentType;

	@Column(nullable = false)
	private Boolean isActivated;
}
