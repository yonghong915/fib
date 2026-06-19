package com.fib.ecny.common.bus.dto.wallet;

public class WalletInfoDto {
    /**
     *
     */
    private String walletId;

    /**
     *
     */
    private String walletName;

    /**
     *
     */
    private String walletLevel;

    /**
     *
     */
    private String walletStatus;


    public String getWalletId() {
        return walletId;
    }

    public void setWalletId(String walletId) {
        this.walletId = walletId;
    }

    public String getWalletName() {
        return walletName;
    }

    public void setWalletName(String walletName) {
        this.walletName = walletName;
    }

    public String getWalletLevel() {
        return walletLevel;
    }

    public void setWalletLevel(String walletLevel) {
        this.walletLevel = walletLevel;
    }

    public String getWalletStatus() {
        return walletStatus;
    }

    public void setWalletStatus(String walletStatus) {
        this.walletStatus = walletStatus;
    }
}
