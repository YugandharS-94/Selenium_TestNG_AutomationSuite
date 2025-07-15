package com.test.support.driver;

import com.test.support.base.BaseClass;
import org.openqa.selenium.Proxy;

public class ZapProxySetting {
    public static final String ZAP_PROXY_ADDRESS = BaseClass.getConfig().getProperty("zapAddress");
    public static final int ZAP_PROXY_PORT = Integer.parseInt(BaseClass.getConfig().getProperty("zapPort"));
    public static final String ZAP_API_KEY = BaseClass.getConfig().getProperty("zapAPI");

    private ZapProxySetting() {
    }

    public static Proxy zapProxySetup() {
        String proxyServerUrl = ZAP_PROXY_ADDRESS + ":" + ZAP_PROXY_PORT;

        Proxy proxy = new Proxy();
        proxy.setHttpProxy(proxyServerUrl);
        proxy.setSslProxy(proxyServerUrl);
        return proxy;
    }
}
