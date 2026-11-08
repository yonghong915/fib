package com.fib.ecny;

import java.util.Objects;
import java.util.Set;

public enum TqzProductStatus {
    DRAFT,
    ACTIVE,
    DISABLED,
    LOGOUT;

    public boolean canTransferTo(TqzProductStatus target) {
        if (Objects.isNull(target)) {
            return false;
        }
        return switch (this) {
            //草稿可审核通过转正常(ACTIVE)，或注销
            case DRAFT -> Set.of(ACTIVE, LOGOUT).contains(target);
            case ACTIVE -> Set.of(DISABLED, LOGOUT).contains(target);
            case DISABLED -> Set.of(ACTIVE, LOGOUT).contains(target);

            //LOGOUT，终态，无对外流转
            case LOGOUT -> false;
        };
    }
}
