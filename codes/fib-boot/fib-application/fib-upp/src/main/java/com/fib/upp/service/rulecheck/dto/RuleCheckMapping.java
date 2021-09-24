package com.fib.upp.service.rulecheck.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RuleCheckMapping {
	static RuleCheckMapping MAPPER = Mappers.getMapper(RuleCheckMapping.class);

	@Mappings({ @Mapping(source = "systemCode", target = "sysCode") })
	RuleCheckDTO convert(BankOrgDTO bankDto);
}
