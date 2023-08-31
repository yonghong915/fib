package com.fib.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class CustomsEventListener implements ApplicationListener<ApplicationEvent> {
	private static final Logger LOGGER = LoggerFactory.getLogger(CustomsEventListener.class);

	@Async
	@Override
	// @Transactional
	public void onApplicationEvent(ApplicationEvent event) {
		LOGGER.info("Received to event is [{}],source={}", event.getClass(), event.getSource());
		if ((event instanceof CustomsEvent evt) && (evt.getSource() instanceof CustomEntity entity)) {
			String serviceName = entity.getServiceName();
			LOGGER.info("serviceName={}", serviceName);
		}
		/*
		 * if (event instanceof BusinessTrackEvent) { BusinessTrackEventEntity
		 * businessTrackEventEntity = (BusinessTrackEventEntity) event.getSource();
		 * businessTrackEventEntity.setOperatorUsername(accountManagement.getUserName(
		 * businessTrackEventEntity.getOperatorUserId()));
		 * businessTrackMapper.saveBusinessTrack(businessTrackEventEntity); }
		 */

	}
}
