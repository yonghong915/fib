package com.fib.ecny.wallet.converter;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.MapperConfig;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@MapperConfig(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR, injectionStrategy = InjectionStrategy.CONSTRUCTOR, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CustomMapperConfig {
}
