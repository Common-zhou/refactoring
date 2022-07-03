package com.zhou.chapter1;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhou.chapter1.exception.InvoiceException;
import com.zhou.chapter1.model.Invoice;
import com.zhou.chapter1.model.Performance;
import com.zhou.chapter1.model.Player;
import com.zhou.chapter1.util.FileUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * 1.重构，尝试把一些逻辑包装成方法
 * 2.注意方法的入参
 *
 * @author zhoubing
 * @since 2022/07/03 00:03
 */
public class Practice1 {

  private static final Gson gson = new Gson();

  public static void main(String[] args) throws FileNotFoundException {

    Type listInvoiceType = new TypeToken<List<Invoice>>() {
    }.getType();

    Type playerType = new TypeToken<Map<String, Player>>() {
    }.getType();

    // 准备参数
    List<Invoice> invoices = (List<Invoice>) getInputData("invoices.json", listInvoiceType);
    Map<String, Player> playerMap = (Map<String, Player>) getInputData("players.json", playerType);

    for (Invoice invoice : invoices) {
      String statement = statement(invoice, playerMap);
      System.out.println(statement);
    }

  }

  private static Object getInputData(String originPath, Type type) throws FileNotFoundException {

    String path = Practice1.class.getClassLoader().getResource(originPath).getPath();

    String invoicesJson = FileUtil.readFile(new File(path));

    return gson.fromJson(invoicesJson, type);

  }

  public static String statement(Invoice invoice, Map<String, Player> playerMap) {

    int totalAmount = 0;
    int volumeCredits = 0;

    String result = String.format("Statement for %s %n", invoice.getCustomer());

    List<Performance> performances = invoice.getPerformances();
    for (Performance performance : performances) {
      Player play = playerMap.get(performance.getPlayID());

      int thisAmount = 0;

      thisAmount = amountForPerformance(performance, play);

      volumeCredits += volumeCreditsFor(performance, play);

      result += String.format("%s : %s  (%s seats) %n", play.getName(),
          formatNumber(thisAmount / 100), performance.getAudience());
      totalAmount += thisAmount;

    }

    result += String.format("Amount owed is $%s %n", formatNumber(totalAmount / 100));

    result += String.format("You earned %s credits %n", volumeCredits);
    return result;
  }

  private static int volumeCreditsFor(Performance performance, Player play) {
    int result = 0;
    result += Math.max(performance.getAudience() - 30, 0);

    if ("comedy".equals(play.getType())) {
      result += Math.floor(performance.getAudience() / 5);
    }
    return result;
  }

  /**
   * 计算一个表演的账单
   *
   * @param performance
   * @param play
   * @return
   */
  private static int amountForPerformance(Performance performance, Player play) {
    int result;
    switch (play.getType()) {
      case "tragedy":
        result = 40000;
        if (performance.getAudience() > 30) {
          result += 1000 * (performance.getAudience() - 30);
        }
        break;
      case "comedy":
        result = 30000;
        if (performance.getAudience() > 20) {
          result += 10000 + 500 * (performance.getAudience() - 20);
        }
        result += 300 * performance.getAudience();
        break;
      default:
        throw new InvoiceException("not allowed type");
    }
    return result;
  }

  private static String formatNumber(Number number) {
    return "$" + number.toString();
  }
}
