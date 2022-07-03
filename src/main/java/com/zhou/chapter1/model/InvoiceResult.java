package com.zhou.chapter1.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author zhoubing
 * @since 2022/07/03 15:03
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceResult {

  /**
   * 获得的消费分
   */
  private int totalVolumeCredits;

  /**
   * 顾客名字
   */
  private String customer;

  /**
   * 演出场详细信息
   */
  private List<InvoicePlayerDetail> playerDetail;

  /**
   * 所需话费的总额
   */
  @Setter(AccessLevel.NONE)
  private int totalAmount;

  public int getTotalAmount() {
    if (playerDetail == null) {
      return 0;
    }
    int totalAmount = 0;
    for (InvoicePlayerDetail detail : playerDetail) {
      totalAmount += detail.getAmount();
    }
    return totalAmount;
  }

  @Data
  @Builder
  @Accessors(chain = true)
  @NoArgsConstructor
  @AllArgsConstructor
  public static class InvoicePlayerDetail {
    /**
     * 剧的名字
     */
    private String playName;

    /**
     * 观众数目
     */
    private int audience;

    /**
     * 需支付钱数目
     */
    private int amount;
  }
}
