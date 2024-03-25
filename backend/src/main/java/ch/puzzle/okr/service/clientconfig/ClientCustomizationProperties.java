package ch.puzzle.okr.service.clientconfig;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;

@ConfigurationProperties("okr.clientcustomization")
public class ClientCustomizationProperties {
    private String favicon;
    private String logo;
    private String title;
    private HashMap<String, String> customStyles = new HashMap<>();

    public void setCustomStyles(HashMap<String, String> customStyles) {
        this.customStyles = customStyles;
    }

    public String getFavicon() {
        return favicon;
    }

    public void setFavicon(String favicon) {
        this.favicon = favicon;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public HashMap<String, String> getCustomStyles() {
        return customStyles;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
