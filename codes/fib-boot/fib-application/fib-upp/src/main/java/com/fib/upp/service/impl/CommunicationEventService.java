package com.fib.upp.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fib.upp.entity.CommunicationEventEntity;
import com.fib.upp.mapper.CommunicationEventMapper;
import com.fib.upp.service.ICommunicationEventService;

@Service("communicationEventService")
public class CommunicationEventService extends ServiceImpl<CommunicationEventMapper, CommunicationEventEntity> implements ICommunicationEventService {
    
}
