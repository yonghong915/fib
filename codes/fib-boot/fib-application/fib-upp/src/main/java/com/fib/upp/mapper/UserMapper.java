package com.fib.upp.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fib.upp.entity.UserEntity;

@Mapper
public interface UserMapper extends BaseMapper<UserEntity> {
	List<UserEntity> getQueueItemsByQueueId();
}
