package ru.ivanov.cucumber.UrlConfigs;

import org.aeonbits.owner.Config;

@Config.Sources({"classpath:url.properties"})
public interface UrlConfigurator extends Config {
    @Key("loading_page_url")
    String loadingUrl();
    @Key("API_url")
    String APIUrl();
}
