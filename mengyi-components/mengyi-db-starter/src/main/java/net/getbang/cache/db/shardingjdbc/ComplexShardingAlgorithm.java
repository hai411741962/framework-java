package net.getbang.cache.db.shardingjdbc;

import lombok.NoArgsConstructor;
import org.apache.shardingsphere.api.sharding.complex.ComplexKeysShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.complex.ComplexKeysShardingValue;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * @author chenjun075
 * @version 1.0
 * @date 2020/11/16 14:53
 */
@NoArgsConstructor
public class ComplexShardingAlgorithm implements ComplexKeysShardingAlgorithm<String> {
    private static PartitionByMurmurHash hashAlgorithm;

    static {
        hashAlgorithm = new PartitionByMurmurHash();
    }

    public ComplexShardingAlgorithm(int count) {
        hashAlgorithm.init(count);
    }

    public static int calculate(String value){
        return hashAlgorithm.calculate(value);
    }


    @Override
    public Collection<String> doSharding(Collection<String> collection, ComplexKeysShardingValue<String> complexKeysShardingValue) {
        return complexKeysShardingValue.getColumnNameAndShardingValuesMap()
                .entrySet()
                .stream().findAny().map(vo -> vo.getValue()).get()
                .stream().map(vo -> vo == null ? "" : vo)
                .map(vo -> hashAlgorithm.calculate(vo))
                .map(bucket -> collection.toArray()[bucket].toString())
                .collect(Collectors.toList());
    }
}
