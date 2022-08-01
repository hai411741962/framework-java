package net.getbang.cache.db;


import net.getbang.cache.db.config.MybatisPlusConfig;
import net.getbang.cache.db.config.TenantConfig;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.context.annotation.Import;

/**
 * @author chenjun
 * db组件自动加载入口
 */
@AutoConfigureBefore(MybatisPlusAutoConfiguration.class)
@Import(value = {MybatisPlusConfig.class, TenantConfig.class})
public class DBAutoConfig {

}
