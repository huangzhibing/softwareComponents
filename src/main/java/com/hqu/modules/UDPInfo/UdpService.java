package com.hqu.modules.UDPInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

public class UdpService implements ApplicationListener<ContextRefreshedEvent> {

    /**
     * 日志对象
     */
    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        logger.info("=============开始加载udp端口监听服务=============");
        UDPServer udp = new UDPServer();
        udp.start();
        logger.info("=============结束加载udp端口监听服务=============");
    }
}
