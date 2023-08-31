package com.fib.upp.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fib.upp.entity.BizTbClobEntity;
import com.fib.upp.mapper.BizTbClobMapper;
import com.fib.upp.service.IBizTbClobService;

@Service("bizTbClobService")
public class BizTbClobService extends ServiceImpl<BizTbClobMapper, BizTbClobEntity> implements IBizTbClobService {

}
