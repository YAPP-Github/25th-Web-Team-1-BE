package org.mainapp.domain.agent.service;

import java.util.List;

import org.domainmodule.agent.entity.Agent;
import org.domainmodule.agent.entity.type.AgentPlatformType;
import org.domainmodule.agent.repository.AgentRepository;
import org.domainmodule.user.entity.User;
import org.mainapp.domain.agent.controller.response.GetAgentsResponse;
import org.mainapp.domain.user.service.UserService;
import org.mainapp.global.util.SecurityUtil;
import org.snsclient.twitter.dto.response.TwitterUserInfoDto;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AgentService {

	private final AgentRepository agentRepository;
	private final UserService userService;

	/**
	 * X API 로그인을 성공한 후 Agent와 SnsToken 생성 및 저장
	 * @return 생성된 Agent 엔티티
	 */
	public Agent updateOrCreateAgent(TwitterUserInfoDto userInfo) {
		Long userId = SecurityUtil.getCurrentUserId();
		User user = userService.findUserById(userId);

		// Agent가 이미 존재하는지 확인
		return agentRepository.findByAccountIdAndPlatform(userInfo.id(), AgentPlatformType.X)
			.map(existingAgent -> updatAgent(existingAgent, userInfo))
			.orElseGet(() -> createAndSaveAgent(user, userInfo));
	}

	private Agent createAndSaveAgent(User user, TwitterUserInfoDto userInfo) {
		Agent newAgent = Agent.create(
			user,
			AgentPlatformType.X,
			userInfo.id(),
			userInfo.description(),
			userInfo.profileImageUrl(),
			userInfo.subscriptionType()
		);
		return agentRepository.save(newAgent);
	}

	private Agent updatAgent(Agent agent, TwitterUserInfoDto userInfo) {
		agent.updateInfo(userInfo.description(), userInfo.profileImageUrl(), userInfo.subscriptionType());
		return agentRepository.save(agent);
	}

	/**
	 * 사용자에 해당하는 계정 목록을 조회하는 메서드
	 */
	public GetAgentsResponse getAgents() {
		// 사용자 인증 정보 조회
		Long userId = SecurityUtil.getCurrentUserId();

		// 사용자 계정 목록 조회
		List<Agent> agents = agentRepository.findAllByUserId(userId);

		// 반환
		return GetAgentsResponse.from(agents);
	}
}
