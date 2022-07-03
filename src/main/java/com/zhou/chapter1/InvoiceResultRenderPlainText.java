package com.zhou.chapter1;

import com.zhou.chapter1.model.InvoiceResult;
import lombok.experimental.UtilityClass;

/**
 * @author zhoubing
 * @since 2022/07/03 15:25
 */
@UtilityClass
public class InvoiceResultRenderPlainText {
  public String renderPlainText(InvoiceResult invoiceResult) {
    String result = String.format("Statement for %s %n", invoiceResult.getCustomer());

    for (InvoiceResult.InvoicePlayerDetail playerDetail : invoiceResult.getPlayerDetail()) {
      result += String.format("%s : %s  (%s seats) %n", playerDetail.getPlayName(),
          usd(playerDetail.getAmount() / 100), playerDetail.getAudience());
    }

    result += String.format("Amount owed is %s %n", usd(invoiceResult.getTotalAmount() / 100));

    result += String.format("You earned %s credits %n", invoiceResult.getTotalVolumeCredits());
    return result;

  }

  private String usd(Number number) {
    return "$" + number.toString();
  }
}
