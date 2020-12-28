package com.fib.gateway.message.packer;

import java.util.Iterator;

/**
 * XML消息组包器
 * 
 * @author fangyh
 * @version 1.0
 * @since 1.0
 * @date 2020-12-28
 */
public class XmlMessagePacker extends AbstractMessagePacker {

	@Override
	public byte[] pack() {
		if (null == this.message) {

		}
		if (null == this.messageBean) {

		}

		this.messageBean.setMetadata(this.message);
		Iterator var1 = this.message.getFields().values().iterator();

		/*
		 * <template><![CDATA[ <?xml version="1.0" encoding="GBK"?> <MSG> <HEAD>
		 * <MSGNO>${msgNo}</MSGNO> </HEAD> <CFX> <JYLSH>${serialNo}</JYLSH>
		 * <HXLSH>${coreNo}</HXLSH> <RETCODE>${returnCode}</RETCODE>
		 * <RETMSGS>${returnMessage}</RETMSGS> </CFX> </MSG> ]]> </template>
		 */
		byte[] templateByte = this.message.getTemplate().getBytes();

		// byte[] var4 = this.A(this.messageBean, templateByte);
		return null;
	}

}
