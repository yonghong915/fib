package com.fib.midbiz.efs.api;

import org.springframework.stereotype.Component;

import com.fib.midbiz.common.dto.EfsDTO;

@Component
public interface EfsServiceApi {
	EfsDTO createEfs();
}
