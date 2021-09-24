package com.fib.upp.service;

/**
 * 通用服务
 * 
 * @author fangyh
 * @version 1.0.0
 * @date 2021-09-23
 */
public interface ICommonService {
	/**
	 * 获取明细标识号
	 * 在CNAPS2的同一个子系统内，明细标识号唯一标识一个参与机构发起的一笔明细业务，由报文发起参与机构编制，规则为“当前工作日期（8位数字）+报文序号（8位数字，不足8位的，左补0）组成，共16位长度”
	 * 在同一个批量报文中，CNAPS2使用｛CNAPS2子系统号+发起行（特指每笔明细中发起行，在贷记业务中发起行为付款行，在借记业务中发起行为收款行）+明细标识号｝作为明细业务重账检查标准
	 * 
	 * @param systemCode 系统编码 HVPS/BEPS
	 * @return
	 */
	String getTransactionId(String systemCode);

	/**
	 * 报文标识号
	 * 在CNAPS2的同一个子系统内，报文标识号唯一标识一个参与机构发起的一个报文，由报文发起参与机构编制，对于需要业务层面进行分片的大报文来说，多个片段报文使用不同的“报文标识号”。报文标识号的编号规则为：当前工作日期（8位数字）+报文序号（8位数字，不足8位的，左补0）组成，共16位长度，例如2010020200000001
	 * 
	 * @param systemCode 系统编码 HVPS/BEPS
	 * @return
	 */
	String getMessageId(String systemCode);

	/**
	 * 获取人行通道日期
	 * 
	 * @param clearBankCode 清算行号
	 * @param systemCode    系统编码 HVPS/BEPS
	 * @return
	 */
	String getCnapsSystemDate(String clearBankCode, String systemCode);

	String getNextSeqId(String seqName);
}
