package com.zhou.chapter1.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * 发票信息
 *
 * @author zhoubing
 * @since 2022/07/03 00:20
 */
@Data
@Builder
public class Invoice {
  private String customer;
  private List<Performance> performances;
}
