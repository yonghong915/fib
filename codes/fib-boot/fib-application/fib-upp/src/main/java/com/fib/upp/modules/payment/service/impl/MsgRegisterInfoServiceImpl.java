package com.fib.upp.modules.payment.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fib.upp.modules.payment.entity.MsgRegisterInfo;
import com.fib.upp.modules.payment.mapper.MsgRegisterInfoMapper;
import com.fib.upp.modules.payment.service.IMsgRegisterInfoService;

@Service("msgRegisterInfoService")
public class MsgRegisterInfoServiceImpl extends ServiceImpl<MsgRegisterInfoMapper, MsgRegisterInfo>
		implements IMsgRegisterInfoService {

}
