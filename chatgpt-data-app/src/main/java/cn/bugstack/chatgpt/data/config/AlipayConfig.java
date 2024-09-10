package cn.bugstack.chatgpt.data.config;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author atticus
 * @Date 19/8/2024 23:07
 * @description: 支付宝沙箱支付配置
 */
@Configuration
@EnableConfigurationProperties(AlipayConfigProperties.class)
public class AlipayConfig {
    /**
     * @description:
     * @author: attiucs
     * @param: properties 支付信息配置对象
     * @return: AlipayClient 支付调用的客户端，即支付宝沙箱支付客户端，用于向沙箱发送信息。
     **/
    @Bean(name = "AlipayClient")
    @ConditionalOnProperty(value = "alipay.enabled", havingValue = "true", matchIfMissing = false)
    public AlipayClient alipayClient(AlipayConfigProperties properties){
        return new DefaultAlipayClient(properties.getGatewayUrl(),
                properties.getApp_id(),
                properties.getMerchant_private_key(),
                properties.getFormat(),
                properties.getCharset(),
                properties.getAlipay_public_key(),
                properties.getSign_type());

    }
}
