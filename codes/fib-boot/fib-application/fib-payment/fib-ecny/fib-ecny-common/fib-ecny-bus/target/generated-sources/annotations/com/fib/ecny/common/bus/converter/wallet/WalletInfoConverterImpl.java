package com.fib.ecny.common.bus.converter.wallet;

import com.fib.ecny.common.bus.dto.wallet.WalletInfoDto;
import com.fib.ecny.common.bus.vo.wallet.WalletInfoVo;
import com.fib.ecny.entity.WalletInfoEntity;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-06-28T11:25:00+0800",
    comments = "version: 1.6.3, compiler: javac, environment: Java 25.0.3 (Oracle Corporation)"
)
@Component
public class WalletInfoConverterImpl implements WalletInfoConverter {

    @Override
    public WalletInfoDto toDto(WalletInfoEntity entity) {
        if ( entity == null ) {
            return null;
        }

        WalletInfoDto walletInfoDto = new WalletInfoDto();

        walletInfoDto.setEcnyCustNo( entity.getEcnyCustNo() );
        walletInfoDto.setWalletId( entity.getWalletId() );
        walletInfoDto.setWalletName( entity.getWalletName() );
        walletInfoDto.setWalletLevel( entity.getWalletLevel() );
        walletInfoDto.setWalletStatus( entity.getWalletStatus() );

        return walletInfoDto;
    }

    @Override
    public List<WalletInfoDto> toDtoList(List<WalletInfoDto> dto) {
        if ( dto == null ) {
            return null;
        }

        List<WalletInfoDto> list = new ArrayList<WalletInfoDto>( dto.size() );
        for ( WalletInfoDto walletInfoDto : dto ) {
            list.add( walletInfoDto );
        }

        return list;
    }

    @Override
    public WalletInfoEntity fromDto(WalletInfoDto dto) {
        if ( dto == null ) {
            return null;
        }

        WalletInfoEntity walletInfoEntity = new WalletInfoEntity();

        walletInfoEntity.setEcnyCustNo( dto.getEcnyCustNo() );
        walletInfoEntity.setWalletId( dto.getWalletId() );
        walletInfoEntity.setWalletName( dto.getWalletName() );
        walletInfoEntity.setWalletLevel( dto.getWalletLevel() );
        walletInfoEntity.setWalletStatus( dto.getWalletStatus() );

        return walletInfoEntity;
    }

    @Override
    public WalletInfoDto fromVo(WalletInfoVo vo) {
        if ( vo == null ) {
            return null;
        }

        WalletInfoDto walletInfoDto = new WalletInfoDto();

        walletInfoDto.setEcnyCustNo( vo.getEcnyCustNo() );
        walletInfoDto.setWalletId( vo.getWalletId() );
        walletInfoDto.setWalletName( vo.getWalletName() );
        walletInfoDto.setWalletLevel( vo.getWalletLevel() );
        walletInfoDto.setWalletStatus( vo.getWalletStatus() );

        return walletInfoDto;
    }

    @Override
    public WalletInfoVo toVo(WalletInfoDto dto) {
        if ( dto == null ) {
            return null;
        }

        WalletInfoVo walletInfoVo = new WalletInfoVo();

        walletInfoVo.setEcnyCustNo( dto.getEcnyCustNo() );
        walletInfoVo.setWalletId( dto.getWalletId() );
        walletInfoVo.setWalletName( dto.getWalletName() );
        walletInfoVo.setWalletLevel( dto.getWalletLevel() );
        walletInfoVo.setWalletStatus( dto.getWalletStatus() );

        return walletInfoVo;
    }
}
