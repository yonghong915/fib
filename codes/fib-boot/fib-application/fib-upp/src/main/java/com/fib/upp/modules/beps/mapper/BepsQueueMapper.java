package com.fib.upp.modules.beps.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fib.upp.modules.beps.entity.BepsQueue;
import com.fib.upp.modules.beps.entity.BepsQueueHeader;
import com.fib.upp.modules.beps.entity.BepsQueueItem;
import com.fib.upp.util.EnumConstants;

/**
 * 
 * @author fangyh
 * @version 1.0
 * @since 1.0
 * @date 2021-01-25
 */
@DS(EnumConstants.DATASOURCE_UPP)
public interface BepsQueueMapper extends BaseMapper<BepsQueue> {

	@Select("getQueueItemsByQueueId")
	List<BepsQueueItem> getQueueItemsByQueueId(String queueId);

	@Update("updateQueueHeaderStatus")
	int updateQueueHeaderStatus(BepsQueueHeader queueHeader);

	@Update("updateQueueItemStatus")
	int updateQueueItemStatus(BepsQueueItem queueItem);

	@Select("getQueueByQueueType")
	BepsQueue getQueueByQueueType(String queueType);

	@Select("getQueueHeader")
	List<BepsQueueHeader> getQueueHeader(BepsQueueHeader param);

	@Insert("createQueueHeader")
	int createQueueHeader(BepsQueueHeader queueHeader);
}