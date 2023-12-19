package com.fib.cmms.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fib.cmms.entity.ChineseMedicine;
import com.fib.cmms.mapper.ChineseMedicineMapper;
import com.fib.cmms.service.ChineseMedicineService;

@Service
public class ChineseMedicineServiceImpl extends ServiceImpl<ChineseMedicineMapper, ChineseMedicine> implements ChineseMedicineService {

}
