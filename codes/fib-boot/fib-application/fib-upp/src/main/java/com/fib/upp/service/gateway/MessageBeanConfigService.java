package com.fib.upp.service.gateway;

import java.io.File;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.fib.upp.service.gateway.message.metadata.MessageMetaData;
import com.fib.upp.service.gateway.message.metadata.MessageMetadataManager;

@Service
@CacheConfig(cacheNames = "messageBeanCacheManager")
public class MessageBeanConfigService implements IConfig {
	private Logger logger = LoggerFactory.getLogger(MessageBeanConfigService.class);

	@Cacheable(key = "#channelId")
	@Override
	public Object load(String channelId, Object... params) {
		logger.info("MessageBeanConfigService->{},{}", channelId, params);
		String messageBeanDir = params[0].toString();
		Map<String, MessageMetaData> messageMap = MessageMetadataManager.loadMetadataGroup(channelId, new File(messageBeanDir));
		return messageMap;
	}

}
