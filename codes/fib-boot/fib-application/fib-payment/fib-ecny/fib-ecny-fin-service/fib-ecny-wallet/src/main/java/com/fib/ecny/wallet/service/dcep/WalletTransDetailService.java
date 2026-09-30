package com.fib.ecny.wallet.service.dcep;

import cn.hutool.core.lang.Pair;
import cn.hutool.core.util.StrUtil;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

public class WalletTransDetailService {
    private  void cardOrder(){
        List domesticCardList = new ArrayList();
        String mesgType = "";
        List<BindCardDto> bindCardList = new ArrayList();
        bindCardList.stream().
                filter(bindCardDto -> (StrUtil.equalsAny(bindCardDto.getBindStatus(),"BOUND","UNBOUND"))
                && StrUtil.equalsAny(bindCardDto.getBindMode(),"BCA01","BCA02"))
                .sorted(Comparator.comparing(BindCardDto::getCardOrder))
                .forEach(data-> domesticCardList.add(this.getDomesticCard(data,mesgType)));
    }

    private Object getDomesticCard(BindCardDto data, String mesgType) {
        return  null;
    }

    static void main() {
        List<GroupSumCheck> checks = new ArrayList<>();
        long num = checks.stream().map(GroupSumCheck::getTotalDetailedCount).mapToLong(Long::longValue).sum();
        checks.stream().mapToLong(GroupSumCheck::getTotalDetailedCount).sum();
    }
    private List<GroupSumCheck> getGroupSumChecks(String batchNo,String partitionId,List<String> allGroupdIds){
        Executor ttlExecutor = null;
        List<CompletableFuture<GroupSumCheck>> list = new ArrayList<>();
        for(String groupId : allGroupdIds){
            CompletableFuture<GroupSumCheck> groupSumCheckCompletableFuture = CompletableFuture.supplyAsync(()->{
                try{
                    Pair<Long,String> pair = calculateSumHash(buildReader(batchNo,partitionId,groupId));
                    GroupSumCheck check = new GroupSumCheck();
                    check.setGroupId(groupId);
                    check.setHash(pair.getValue());
                    check.setTotalDetailedCount(pair.getKey());
//                    check.setHash(pair.getSecond());
//                    check.setTotalDetailedCount(pair.getFirst());
                    return check;
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            },ttlExecutor);
            list.add(groupSumCheckCompletableFuture);
        }

        try {
            CompletableFuture.allOf(list.toArray(new CompletableFuture[0])).join();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return list.stream().map(CompletableFuture::join).collect(Collectors.toList());
    }

    private Pair<Long, String> calculateSumHash(Object o) {
        Pair<Long,String> pair = new Pair<>(1l,"23");
        return pair;
    }

    private Object buildReader(String batchNo, String partitionId, String groupId) {
        return null;
    }
}
