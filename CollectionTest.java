package com.dc.face.guava.collection;

import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

/**
 *  Collector 实现，Spliter实现，Flux,ParabelFlux , sequse(串行)、以天为单位
 */
@Log4j2
public class CollectionTest {

    private static final ImmutableSet<String> accountNumbers = ImmutableSet.of("1", "3");

    public static void main(String[] args) throws InterruptedException {

        CountDownLatch countDownLatch = new CountDownLatch(1);

        Spliterators.emptySpliterator();

        Flux.fromIterable(Arrays.asList(completableFuture(),completableFuture1()))
                .concatMap(Mono::fromCompletionStage)
                .reduce((localDateListMap, localDateListMap2) -> {
                    localDateListMap.putAll(localDateListMap2);
                    return localDateListMap;
                }).subscribe(System.out::println,e-> {},countDownLatch::countDown);

        countDownLatch.await();
    }

    public static Mono<Map<LocalDate, Collection<FundsData>>> completableFuture2() {
        return Flux.fromIterable(CollectionTest.generateData())
                    .collectMultimap(FundsData::getLocalDate);

    }

    public static CompletableFuture<Map<LocalDate, List<FundsData>>> completableFuture() {
        return CompletableFuture.supplyAsync(CollectionTest::generateData)
                .handleAsync((fundsData, throwable) -> fundsData.parallelStream().peek(fundsData1 -> {
                    if (accountNumbers.contains(fundsData1.accountNo)) {
                        fundsData1.hashMap.put(fundsData1.accountNo, fundsData1.accountNo);
                    }
                }).collect(Collectors.groupingBy(FundsData::getLocalDate)));

    }

    public static CompletableFuture<Map<LocalDate, List<FundsData>>> completableFuture1() {
        return CompletableFuture.supplyAsync(CollectionTest::generateData1)
                .handleAsync((fundsData, throwable) -> fundsData.stream().peek(fundsData1 -> {
                    if (accountNumbers.contains(fundsData1.accountNo)) {
                        fundsData1.hashMap.put(fundsData1.accountNo, fundsData1.accountNo);
                    }
                }).collect(Collectors.groupingBy(FundsData::getLocalDate)));

    }

    public static List<FundsData> generateData() {
        FundsData of = FundsData.of(LocalDate.of(2019, 3, 12), new HashMap<>(), "1");
        FundsData of1 = FundsData.of(LocalDate.of(2019, 4, 12), new HashMap<>(), "2");
        FundsData of2 = FundsData.of(LocalDate.of(2019, 5, 12), new HashMap<>(), "2");
        FundsData of3 = FundsData.of(LocalDate.of(2019, 3, 12), new HashMap<>(), "2");
        return Lists.newLinkedList(Arrays.asList(of, of1, of2, of3));
    }

    public static List<FundsData> generateData1() {
        FundsData of4 = FundsData.of(LocalDate.of(2019, 2, 12), new HashMap<>(), "1");
        FundsData of5 = FundsData.of(LocalDate.of(2019, 6, 12), new HashMap<>(), "1");
        FundsData of6 = FundsData.of(LocalDate.of(2019, 6, 12), new HashMap<>(), "3");
        FundsData of7 = FundsData.of(LocalDate.of(2019, 2, 12), new HashMap<>(), "3");
        return Lists.newLinkedList(Arrays.asList(of4, of5, of6, of7));
    }


    @Data
    @AllArgsConstructor(staticName = "of")
    public static class FundsData {

        private LocalDate localDate;

        private HashMap<String, String> hashMap;

        private String accountNo;


    }

}
