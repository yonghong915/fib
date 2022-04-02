package com.fib.autoconfigure.ruleengine.drools;

import org.kie.api.KieBase;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieSession;
import org.kie.internal.utils.KieHelper;

public class NewKieBase {
	public static KieBase rulekieBase(String rule) {// rule值就是我们动态传入的规则内容
		KieHelper helper = new KieHelper();
		KieBase kieBase = null;
		try {
			helper.addContent(rule, ResourceType.DRL);
			kieBase = helper.build();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return kieBase;
	}

	public static void get(KieBase kieBase, Object obj) {
		KieSession kieSession = kieBase.newKieSession();
		kieSession.insert(obj);
		kieSession.fireAllRules();
		kieSession.dispose();
	}
}