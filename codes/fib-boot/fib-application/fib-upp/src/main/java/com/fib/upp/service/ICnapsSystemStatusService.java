package com.fib.upp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fib.upp.entity.cnaps.CnapsSystemStatus;

import cn.hutool.core.lang.Opt;

public interface ICnapsSystemStatusService extends IService<CnapsSystemStatus> {
	Opt<CnapsSystemStatus> getCnapsSystemStatus(CnapsSystemStatus css);
}
