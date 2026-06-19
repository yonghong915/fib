package com.fib.ecny.common.bus.converter.wallet;

import com.fib.ecny.common.bus.dto.wallet.WalletInfoDto;
import com.fib.ecny.entity.WalletInfoEntity;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-06-19T07:19:32+0800",
    comments = "version: 1.6.3, compiler: javac, environment: Java 26.0.1 (Oracle Corporation)"
)
@Component
public class WalletInfoMapperConverterImpl implements WalletInfoMapperConverter {

    @Override
    public WalletInfoDto toDto(WalletInfoEntity walletInfoEntity) {
        if ( walletInfoEntity == null ) {
            return null;
        }

        WalletInfoDto walletInfoDto = new WalletInfoDto();

        walletInfoDto.setWalletId( walletInfoEntity.getWalletId() );
        walletInfoDto.setWalletName( walletInfoEntity.getWalletName() );
        walletInfoDto.setWalletLevel( walletInfoEntity.getWalletLevel() );
        walletInfoDto.setWalletStatus( walletInfoEntity.getWalletStatus() );

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

        walletInfoEntity.setWalletId( walletInfoDto.getWalletId() );
        walletInfoEntity.setWalletName( walletInfoDto.getWalletName() );
        walletInfoEntity.setWalletLevel( walletInfoDto.getWalletLevel() );
        walletInfoEntity.setWalletStatus( walletInfoDto.getWalletStatus() );

        return walletInfoEntity;
    }
}
