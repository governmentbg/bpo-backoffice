package com.duosoft.ipas.util.json;
import lombok.*;

@Data
public class AcpTakenItemData {
    private String storage;

    public AcpTakenItemData(String storage) {
        this.storage = storage;
    }
}
