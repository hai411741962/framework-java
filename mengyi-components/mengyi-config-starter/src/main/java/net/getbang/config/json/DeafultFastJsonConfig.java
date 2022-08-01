package net.getbang.config.json;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

@Configuration
public class DeafultFastJsonConfig {

    @Bean
    public HttpMessageConverters fastJsonHttpMessageConverters(List<HttpMessageConverter<?>> converters) {
        FastJsonHttpMessageConverter fjc = new FastJsonHttpMessageConverter();
        FastJsonConfig fj = new FastJsonConfig();
        fj.setSerializerFeatures(new SerializerFeature[]{
                SerializerFeature.DisableCircularReferenceDetect,
                SerializerFeature.BrowserCompatible,
                SerializerFeature.WriteEnumUsingToString,
                SerializerFeature.WriteNullListAsEmpty,
                SerializerFeature.WriteMapNullValue
        });
        fjc.setFastJsonConfig(fj);
        TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));
        List<MediaType> fastMediaTypes = new ArrayList();
        fastMediaTypes.add(MediaType.APPLICATION_JSON);
        fastMediaTypes.add(MediaType.TEXT_HTML);
        fjc.setSupportedMediaTypes(fastMediaTypes);
        converters.add(fjc);
        return new HttpMessageConverters(converters);
    }


}
