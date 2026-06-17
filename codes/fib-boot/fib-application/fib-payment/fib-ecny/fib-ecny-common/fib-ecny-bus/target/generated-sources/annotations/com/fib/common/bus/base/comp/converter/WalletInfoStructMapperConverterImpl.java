package com.fib.common.bus.base.comp.converter;

import com.fib.common.bus.base.comp.dto.WalletInfoDto;
import com.fib.entity.WalletInfoEntity;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-06-18T05:55:28+0800",
    comments = "version: 1.6.3, compiler: javac, environment: Java 26.0.1 (Oracle Corporation)"
)
@Component
public class WalletInfoStructMapperConverterImpl implements WalletInfoStructMapperConverter {

    @Override
    public WalletInfoDto toDto(WalletInfoEntity walletInfoEntity) {
        if ( walletInfoEntity == null ) {
            return null;
        }

        WalletInfoDto walletInfoDto = new WalletInfoDto();

        return walletInfoDto;
    }

    @Override
    public List<WalletInfoDto> toDtoList(List<WalletInfoDto> walletInfoDto) {
        if ( walletInfoDto == null ) {
            return null;
        }

        List<WalletInfoDto> list = new ArrayList<WalletInfoDto>( walletInfoDto.size() );
        for ( WalletInfoDto walletInfoDto1 : walletInfoDto ) {
            list.add( walletInfoDto1 );
        }

        return list;
    }

    @Override
    public WalletInfoEntity fromDto(WalletInfoDto walletInfoDto) {
        if ( walletInfoDto == null ) {
            return null;
        }

        WalletInfoEntity walletInfoEntity = new WalletInfoEntity();

        return walletInfoEntity;
    }
}
