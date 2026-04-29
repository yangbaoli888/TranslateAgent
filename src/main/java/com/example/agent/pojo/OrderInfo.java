package com.example.agent.pojo;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

import java.util.List;

/**
 * @author Brady (brady@sfmail.sf-express.com)
 * @version 1.0
 * @since 2026/4/22 下午5:16
 **/
@Data
public class OrderInfo {
    private String goodsOwnerName;
    private List<Material> materialList;

    @Data
    public static class Material {
        private String materialName;
        @JsonAlias("associatedPackageQuantity")
        private Integer piece;
    }
}
