package com.fib.uqcp.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fib.uqcp.entity.ChineseMedicine;
import com.fib.uqcp.mapper.ChineseMedicineMapper;
import com.fib.uqcp.service.ChineseMedicineService;

@Service
public class ChineseMedicineServiceImpl extends ServiceImpl<ChineseMedicineMapper, ChineseMedicine> implements ChineseMedicineService {

}
