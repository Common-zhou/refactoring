package com.zhou.chapter1.model;

import lombok.Builder;
import lombok.Data;

/**
 * @author zhoubing
 * @since 2022/07/03 00:11
 */
@Builder
@Data
public class Player {
  private String name;
  private String type;
}
