package com.fib.upp.modules.cnaps.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fib.upp.modules.cnaps.entity.CnapsSystemStatus;

import cn.hutool.core.lang.Opt;

public interface ICnapsSystemStatusService extends IService<CnapsSystemStatus> {
	Opt<CnapsSystemStatus> getCnapsSystemStatus(CnapsSystemStatus css);
}
