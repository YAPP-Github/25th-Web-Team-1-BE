package org.mainapp.domain.user.service;

import org.domainmodule.user.entity.User;
import org.domainmodule.user.repository.UserRepository;
import org.mainapp.domain.user.exception.UserErrorCode;
import org.mainapp.global.error.CustomException;
import org.mainapp.global.oauth2.userinfo.OAuth2UserInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;

	@Override
	@Transactional
	public User createAndSaveUser(OAuth2UserInfo oAuth2Response) {
		User user = User.createUser(oAuth2Response.getEmail(), oAuth2Response.getName(),
			oAuth2Response.getProfileImage());
		return userRepository.save(user);
	}

	@Override
	@Transactional(readOnly = true)
	public User findUserById(Long userId) {
		return userRepository.findById(userId)
			.orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND));
	}
}
