package com.fib.autoconfigure.ruleengine.drools;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;

@Service
public class DroolsRuleServiceImpl {
	@Resource
	private DroolsManager droolsManager;

	/**
	 * 模拟数据库
	 */
	private Map<Long, DroolsRule> droolsRuleMap = new HashMap<>(16);

	// @Override
	public List<DroolsRule> findAll() {
		return new ArrayList<>(droolsRuleMap.values());
	}

	// @Override
	public void addDroolsRule(DroolsRule droolsRule) {
		droolsRule.validate();
		droolsRule.setCreatedTime(new Date());
		droolsRuleMap.put(droolsRule.getRuleId(), droolsRule);
		droolsManager.addOrUpdateRule(droolsRule);
	}

	// @Override
	public void updateDroolsRule(DroolsRule droolsRule) {
		droolsRule.validate();
		droolsRule.setUpdateTime(new Date());
		droolsRuleMap.put(droolsRule.getRuleId(), droolsRule);
		droolsManager.addOrUpdateRule(droolsRule);
	}

	// @Override
	public void deleteDroolsRule(Long ruleId, String ruleName) {
		DroolsRule droolsRule = droolsRuleMap.get(ruleId);
		if (null != droolsRule) {
			droolsRuleMap.remove(ruleId);
			droolsManager.deleteDroolsRule(droolsRule.getKieBaseName(), droolsRule.getKiePackageName(), ruleName);
		}
	}
}
