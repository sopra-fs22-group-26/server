package ch.uzh.ifi.group26.scrumblebee.rest.dto;

public class RefreshGetDTO {

    private Long id;
    private String type = "Bearer";
    private String token;
    private String refreshToken;

    public void setId(Long id) { this.id = id; }

    public Long getId() { return id; }

    public void setType(String type) { this.type = type; }

    public String getType() { return type; }

    public void setToken(String token) { this.token = token; }

    public String getToken() { return token; }

    public void setRefreshToken(String refreshToken) { this.refreshToken = refreshToken; }

    public String getRefreshToken() { return refreshToken; }

}
