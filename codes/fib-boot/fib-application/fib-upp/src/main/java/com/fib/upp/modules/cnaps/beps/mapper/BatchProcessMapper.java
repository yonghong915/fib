package com.fib.upp.modules.cnaps.beps.mapper;

import java.util.List;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.fib.upp.modules.cnaps.beps.entity.BatchProcess;
import com.fib.upp.modules.cnaps.beps.entity.BatchProcessDetail;
import com.fib.upp.modules.cnaps.beps.entity.BatchProcessGroup;
import com.fib.upp.util.UppEnumConstants;

/**
 * 
 * @author fangyh
 * @version 1.0
 * @date 2022-04-15 16:37:10
 */
@DS(UppEnumConstants.DATASOURCE_UPP)
public interface BatchProcessMapper {
	int insertBatchProcessGroup(BatchProcessGroup bpg);

	List<BatchProcessGroup> getBatchProcessGroup(BatchProcessGroup bpg);

	int updateBatchProcessGroup(BatchProcessGroup bpg);

	int insertBatchProcess(BatchProcess bp);

	List<BatchProcess> getBatchProcessList(BatchProcess bp);

	int updateBatchProcess(BatchProcess bp);

	int insertBatchProcessDetail(BatchProcessDetail bpd);

	List<BatchProcessDetail> getBatchProcessDetail(BatchProcessDetail bpd);

	int updateBatchProcessDetail(BatchProcessDetail bpd);

	int insertBatch4BPD(List<BatchProcessDetail> list);
}