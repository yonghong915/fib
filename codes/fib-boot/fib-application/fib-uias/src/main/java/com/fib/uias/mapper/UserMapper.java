package com.fib.uias.mapper;

import java.net.HttpURLConnection;
import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fib.uias.entity.UserEntity;

/**
 * 
 * @author fangyh
 * @version 1.0
 * @since 1.0
 * @date 2020-12-17
 */
public interface UserMapper extends BaseMapper<UserEntity> {
	@Select("selectUserList")
	List<UserEntity> selectUserList(IPage<UserEntity> page, UserEntity userEntity);
}
