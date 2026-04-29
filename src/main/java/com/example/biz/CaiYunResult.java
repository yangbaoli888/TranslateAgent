package com.example.biz;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author Brady (brady@sfmail.sf-express.com)
 * @version 1.0
 * @since 2026/4/28 下午4:10
 **/
@Data
public class CaiYunResult {
    private String status;
    private Long server_time;
    private BigDecimal[] location;


    public static class Weather {


        public static class Hourly {
            private String status;
            private String description;
            
        }
    }
}
