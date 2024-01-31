package com.fib.uqcp.ctrler;

import java.util.ArrayList;
import java.util.List;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fib.autoconfigure.crypto.annotation.AlgorithmType;
import com.fib.autoconfigure.crypto.annotation.Decrypt;
import com.fib.autoconfigure.crypto.annotation.Encrypt;

@RestController
@RequestMapping(value = "/test")
public class TestCtrler {

	@PostMapping(value = "/list")
	@Encrypt(symmetricAlgorithm = AlgorithmType.SymmetricAlgorithm.SM4, asymmetricAlgorithm = AlgorithmType.AsymmetricAlgorithm.SM2, digestAlgorithm = AlgorithmType.DigestAlgorithm.SM3)
	@Decrypt(symmetricAlgorithm = AlgorithmType.SymmetricAlgorithm.SM4, asymmetricAlgorithm = AlgorithmType.AsymmetricAlgorithm.SM2, digestAlgorithm = AlgorithmType.DigestAlgorithm.SM3)
	public List<String> findList(@Validated @RequestBody TestVO vo) {
		System.out.println("查询所有数据:");
		List<String> rtnList = new ArrayList<>();
		rtnList.add("ok");
		rtnList.add("aaa");
		return rtnList;
	}
}
