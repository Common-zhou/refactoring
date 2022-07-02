package com.zhou.chapter1.model;

import lombok.Builder;
import lombok.Data;

/**
 * 演出场
 *
 * @author zhoubing
 * @since 2022/07/03 00:18
 */
@Data
@Builder
public class Performance {
  private String playID;
  private int audience;
}
