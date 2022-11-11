package com.southwind.controller;

import com.southwind.annotation.SimpleRateLimit;
import com.taptap.ratelimiter.annotation.RateLimit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 吕茂陈
 * @date 2022-10-08 15:43
 */
@Slf4j
@RestController
public class RateLimitTestController {


	@SimpleRateLimit
	@GetMapping("/limit")
	public String limit() {
		log.info("limit");
		return "ok";
	}


	//	@SimpleRateLimit(limit = 5)
	@RateLimit(rate = 5, rateInterval = "10s")
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
}
