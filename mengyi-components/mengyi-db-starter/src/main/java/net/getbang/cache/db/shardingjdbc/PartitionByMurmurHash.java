package net.getbang.cache.db.shardingjdbc;

import lombok.extern.slf4j.Slf4j;

import java.util.Random;

/**
 * consistancy hash, murmur hash
 * implemented by Guava
 *
 * @author wuzhih
 */
@Slf4j
public class PartitionByMurmurHash {
    private int count;

    public void init(int count) {
        this.count = count;
    }


    public Integer calculate(String columnValue) {
        // 扰动函数
        return  (count - 1) & (columnValue.hashCode() ^ (columnValue.hashCode() >>> 16));
    }

    public static void main(String[] args) {
        int min = 0;
        int max = 0;
        for (int i = 0; i < 100000; i++) {
            int count = 16;
            String columnValue = "dafa" + Math.random();
            // 扰动函数
            int idx = (count - 1) & (columnValue.hashCode() ^ (columnValue.hashCode() >>> 16));

            min = Math.min(min, idx);
            max = Math.max(max, idx);
        }
        System.out.println(min + "<>" + max);
    }

}
