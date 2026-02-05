package com.fib.midbiz.campuscard.ctrler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fib.midbiz.campuscard.service.IProtocolService;
import com.fib.midbiz.common.dto.campuscard.ProtocolDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/protocol")
@Tag(name = "协议管理", description = "协议的新增、查询、更新、删除接口") // 对应原 @Api
public class ProtocolCtrler {
	private static final Logger LOGGER = LoggerFactory.getLogger(ProtocolCtrler.class);

	private IProtocolService protocolService;

	public ProtocolCtrler(IProtocolService protocolService) {
		this.protocolService = protocolService;
	}

	@PostMapping("/signProtocol")
	@Operation(summary = "根据ID查询协议", description = "传入协议唯一ID，返回协议详细信息") // 对应原 @ApiOperation
	public String signProtocol(@RequestBody ProtocolDTO protocolDto,
			@Parameter(description = "协议唯一ID", required = true) @PathVariable Long id) {
		LOGGER.info("protocolDto=[{}]", protocolDto);
		int rows = protocolService.signProtocol(protocolDto);
		if (rows == 0) {
			return "error";
		}
		return "success";
	}
}
