package com.trievosoftware.application.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.validation.constraints.NotBlank;

/**
 * Properties specific to Sia.
 * <p>
 * Properties are configured in the application.yml file.
 * See {@link io.github.jhipster.config.JHipsterProperties} for a good example.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {
    private final Api api = new Api();
    private final Discord discord = new Discord();

    public Api getApi() {
        return api;
    }

    public Discord getDiscord() {
        return discord;
    }

    public static class Api {
        private String openweathermap;
        private String google;
        private String youtube;
        private String giphy;
        private String cryptoCompare;

        public String getOpenweathermap() {
            return openweathermap;
        }

        public void setOpenweathermap(String openweathermap) {
            this.openweathermap = openweathermap;
        }

        public String getGoogle() {
            return google;
        }

        public void setGoogle(String google) {
            this.google = google;
        }

        public String getYoutube() {
            return youtube;
        }

        public void setYoutube(String youtube) {
            this.youtube = youtube;
        }

        public String getGiphy() {
            return giphy;
        }

        public void setGiphy(String giphy) {
            this.giphy = giphy;
        }

        public String getCryptoCompare() {
            return cryptoCompare;
        }

        public void setCryptoCompare(String cryptoCompare) {
            this.cryptoCompare = cryptoCompare;
        }
    }

    public static class Discord {
        @NotBlank
        private String token;

        private String authorId;

        @NotBlank
        private String prefix;

        @NotBlank
        private String alternatePrefix;

        private String shards;

        private String guildId;

        private Integer schedulerPoolSize;

        private String logWebookUrl;

        private String categoryId;


        public String getToken() {
            return token;
        }

        public String getAuthorId() {
            return authorId;
        }

        public String getPrefix() {
            return prefix;
        }

        public String getAltPrefix() {
            return alternatePrefix;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public void setAuthorId(String authorId) {
            this.authorId = authorId;
        }

        public void setPrefix(String prefix) {
            this.prefix = prefix;
        }

        public void setAlternatePrefix(String alternatePrefix) {
            this.alternatePrefix = alternatePrefix;
        }

        public String getAlternatePrefix() {
            return alternatePrefix;
        }

        public String getShards() {
            return shards;
        }

        public String getGuildId() {
            return guildId;
        }

        public Integer getSchedulerPoolSize() {
            return schedulerPoolSize;
        }

        public void setShards(String shards) {
            this.shards = shards;
        }

        public void setGuildId(String guildId) {
            this.guildId = guildId;
        }

        public void setSchedulerPoolSize(Integer schedulerPoolSize) {
            this.schedulerPoolSize = schedulerPoolSize;
        }

        public String getLogWebookUrl() {
            return logWebookUrl;
        }

        public void setLogWebookUrl(String logWebookUrl) {
            this.logWebookUrl = logWebookUrl;
        }

        public String getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(String categoryId) {
            this.categoryId = categoryId;
        }
    }

}
