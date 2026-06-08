package com.example.juliecreations.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class HydraCollection<T> {
    @SerializedName("member")
    private List<T> member;

    public List<T> getMember() {
        return member;
    }

    public void setMember(List<T> member) {
        this.member = member;
    }
}