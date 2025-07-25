package com.example.features.request;

public class OrderRequest {
    private String nickname;

    public OrderRequest() {
    }

    public OrderRequest(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @Override
    public String toString() {
        return "OrderRequest{" +
                "nickname='" + nickname + '\'' +
                '}';
    }
}
