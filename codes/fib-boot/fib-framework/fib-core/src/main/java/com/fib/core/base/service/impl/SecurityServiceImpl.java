package com.fib.core.base.service.impl;

import org.springframework.stereotype.Service;

import com.fib.core.base.service.ISecurityService;
import com.fib.core.util.ConstantUtil;

@Service("securityService")
public class SecurityServiceImpl implements ISecurityService {

	@Override
	public String queryPrivateKey(String systemCode) {
		String privateKey = "";
		if (ConstantUtil.UPP_SYSTEM_CODE.equals(systemCode)) {
			privateKey = "MIGTAgEAMBMGByqGSM49AgEGCCqBHM9VAYItBHkwdwIBAQQgaoYFkmCx0POJqdrTzXkPqHVsgJvcTceM64+z3tKL4iygCgYIKoEcz1UBgi2hRANCAASxvoSh13+tKwF0gCZJC6UUbZ12U7vlh7+5l+TXOBpNSJLLRwFVIU6BjYBDFMv5KYYtXbigpoboiOIl1J9xyJ9+";
		} else if ("100001".equals(systemCode)) {
			privateKey = "MIGTAgEAMBMGByqGSM49AgEGCCqBHM9VAYItBHkwdwIBAQQgMHuFx4Ww2QToOSWNeG2XFLJtoqh10sRRaZnYp+cO1T2gCgYIKoEcz1UBgi2hRANCAATuzT9+2dtL/FztfhBHgvtinUI50M8RZBEvN8+y8nQSfjJ5yKd0OUcvj1tbL5C6bFsc8ak6PpsJbPYcpnk42Qj+";
		}
		return privateKey;
	}

	@Override
	public String queryPublicKey(String systemCode) {
		String publicKey = "";
		if (ConstantUtil.UPP_SYSTEM_CODE.equals(systemCode)) {
			publicKey = "MFkwEwYHKoZIzj0CAQYIKoEcz1UBgi0DQgAEsb6Eodd/rSsBdIAmSQulFG2ddlO75Ye/uZfk1zgaTUiSy0cBVSFOgY2AQxTL+SmGLV24oKaG6IjiJdSfcciffg==";
		} else if ("100001".equals(systemCode)) {
			publicKey = "MFkwEwYHKoZIzj0CAQYIKoEcz1UBgi0DQgAE7s0/ftnbS/xc7X4QR4L7Yp1COdDPEWQRLzfPsvJ0En4yecindDlHL49bWy+QumxbHPGpOj6bCWz2HKZ5ONkI/g==";
		}
		return publicKey;
	}

}
