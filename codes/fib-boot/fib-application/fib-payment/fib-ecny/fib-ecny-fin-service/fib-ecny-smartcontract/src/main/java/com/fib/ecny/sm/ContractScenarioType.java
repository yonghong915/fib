package com.fib.ecny.sm;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ContractScenarioType {
    CONSUME("ConsumeContract", "消费"),
    SUBSIDY("SubsidyContract", "代发"),
    LOAN("LoanContract", "贷款"),
    COLLECTION("CollectionContract", "委托收款");

    private final String code;
    private final String desc;

}
