package org.domainmodule.post.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.domainmodule.post.entity.Post;
import org.domainmodule.post.entity.type.PostStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<Post, Long> {

	// post와 SnsToken을 한번에 조회
	@Query("""
    SELECT p FROM Post p
    JOIN FETCH p.postGroup pg
    JOIN FETCH pg.agent a
    JOIN SnsToken t ON t.agent.id = a.id
    WHERE p.uploadTime BETWEEN :startTime AND :endTime
    And p.status = :status
""")
	List<Post> findPostsWithSnsTokenByTimeRange(LocalDateTime startTime, LocalDateTime endTime, @Param("status") PostStatus status);
}
