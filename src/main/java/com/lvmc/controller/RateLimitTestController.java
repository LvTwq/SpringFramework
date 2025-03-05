package com.lvmc.controller;

import com.lvmc.annotation.SimpleRateLimit;
import com.lvmc.component.RedisDistributedLock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 吕茂陈
 * @date 2022-10-08 15:43
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class RateLimitTestController {


	@SimpleRateLimit
	@GetMapping("/limit")
	public String limit() {
		log.info("limit");
		return "ok";
	}


	@GetMapping("/limit1")
	public String limit1() {
		log.info("limit1");
		return "ok";
	}


	@GetMapping("/nolimit")
	public String nolimit() {
		log.info("no limit");
		return "ok";
	}


	private final RedisDistributedLock redisDistributedLock;


	public void performTaskWithLock(String lockKey, String clientId, long expirationTime) {
		boolean lockAcquired = redisDistributedLock.acquireLock(lockKey, clientId, expirationTime);

		if (lockAcquired) {
			try {
				// 业务逻辑
			} finally {
				redisDistributedLock.releaseLock(lockKey, clientId);
			}
		} else {
			// 未获得锁，处理相关逻辑（例如等待或放弃）
		}
	}
}
