package com.fib.upp.modules.payment.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fib.upp.modules.payment.entity.AcctTransRegister;
import com.fib.upp.modules.payment.mapper.AcctTransRegisterMapper;
import com.fib.upp.modules.payment.service.IAcctTransRegisterService;

@Service("acctTransRegisterService")
public class AcctTransRegisterServiceImpl extends ServiceImpl<AcctTransRegisterMapper, AcctTransRegister>
		implements IAcctTransRegisterService {

}
