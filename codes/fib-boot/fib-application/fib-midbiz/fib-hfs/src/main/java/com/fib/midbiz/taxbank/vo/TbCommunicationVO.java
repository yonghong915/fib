package com.fib.midbiz.taxbank.vo;

import java.math.BigInteger;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TbCommunicationVO {
	private BigInteger id;

	public BigInteger getId() {
		return id;
	}

	public void setId(BigInteger id) {
		this.id = id;
	}

}
