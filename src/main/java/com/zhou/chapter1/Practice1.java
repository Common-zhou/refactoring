package com.zhou.chapter1;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sun.corba.se.impl.resolver.SplitLocalResolverImpl;
import com.zhou.chapter1.exception.InvoiceException;
import com.zhou.chapter1.model.Invoice;
import com.zhou.chapter1.model.InvoiceResult;
import com.zhou.chapter1.model.Performance;
import com.zhou.chapter1.model.Player;
import com.zhou.chapter1.util.FileUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Type;
import java.util.ArrayList;
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
      InvoiceResult statement = statement(invoice, playerMap);

      String plainText = InvoiceResultRenderPlainText.renderPlainText(statement);

      System.out.println(plainText);
    }

  }

  private static Object getInputData(String originPath, Type type) throws FileNotFoundException {

    String path = Practice1.class.getClassLoader().getResource(originPath).getPath();

    String invoicesJson = FileUtil.readFile(new File(path));

    return gson.fromJson(invoicesJson, type);

  }

  public static InvoiceResult statement(Invoice invoice, Map<String, Player> playerMap) {

    InvoiceResult result = new InvoiceResult();
    result.setCustomer(invoice.getCustomer());

    List<Performance> performances = invoice.getPerformances();

    result.setTotalVolumeCredits(getTotalVolumeCredits(performances, playerMap));

    List<InvoiceResult.InvoicePlayerDetail> playerDetail = new ArrayList<>();
    result.setPlayerDetail(playerDetail);


    for (Performance performance : performances) {
      Player player = playerMap.get(performance.getPlayID());

      int thisAmount = amountForPerformance(performance, player);

      playerDetail.add(InvoiceResult.InvoicePlayerDetail.builder()
          .playName(player.getName()).amount(thisAmount).audience(performance.getAudience())
          .build());
    }

    return result;
  }

  private static int getTotalVolumeCredits(List<Performance> performances, Map<String, Player> playerMap) {
    int volumeCredits = 0;

    for (Performance performance : performances) {
      Player player = playerMap.get(performance.getPlayID());

      volumeCredits += volumeCreditsFor(performance, player);
    }
    return volumeCredits;
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
}
