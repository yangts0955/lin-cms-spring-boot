package io.github.talelin.latticy.extension.file;

import io.github.talelin.latticy.module.file.Uploader;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

/**
 * 文件上传配置类
 *
 * @author Juzi@TaleLin
 * @author colorful@TaleLin
 */
@Configuration(proxyBeanMethods = false)
public class UploaderConfiguration {

    @Bean
    @Order
    @ConditionalOnMissingBean
    public Uploader uploader(){
        return new QiniuUploader();
    }
}
