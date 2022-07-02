package com.zhou.chapter1;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhou.chapter1.exception.InvoiceException;
import com.zhou.chapter1.model.Invoice;
import com.zhou.chapter1.model.Performance;
import com.zhou.chapter1.model.Player;
import com.zhou.chapter1.util.FileUtil;
import jdk.internal.org.objectweb.asm.TypeReference;
import sun.reflect.misc.FieldUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author zhoubing
 * @since 2022/07/03 00:03
 */
public class Practice1 {
  public static void main(String[] args) throws FileNotFoundException {
    String invoicesPath = Practice1.class.getClassLoader().getResource("invoices.json").getPath();
    String playersPath = Practice1.class.getClassLoader().getResource("players.json").getPath();

    String invoicesJson = FileUtil.readFile(new File(invoicesPath));
    String playersJson = FileUtil.readFile(new File(playersPath));

    Gson gson = new Gson();

    Type type = new TypeToken<List<Invoice>>() {
    }.getType();

    List<Invoice> res = gson.fromJson(invoicesJson, type);

    for (Invoice re : res) {
      System.out.println(re);
    }

    Type playerType = new TypeToken<Map<String, Player>>() {
    }.getType();
    Map<String, Player> playerMap = gson.fromJson(playersJson, playerType);

    for (Map.Entry<String, Player> entry : playerMap.entrySet()) {
      System.out.println(entry.getKey() + "___" + entry.getValue());
    }


  }

  public static String statement(Invoice invoice, Map<String, Player> playerMap) {

    int totalAmount = 0;
    int volumeCredits = 0;

    String result = String.format("Statement for %s\n", invoice.getCustomer());

    List<Performance> performances = invoice.getPerformances();
    for (Performance performance : performances) {
      Player play = playerMap.get(performance.getPlayID());

      int thisAmount = 0;

      switch (play.getType()) {
        case "tragedy":
          thisAmount = 40000;
          if (performance.getAudience() > 30) {
            thisAmount += 1000 * (performance.getAudience() - 30);
          }
          break;
        case "comedy":
          thisAmount = 30000;
          if (performance.getAudience() > 20) {
            thisAmount += 10000 + 500 * (performance.getAudience() - 20);
          }
          thisAmount += 300 * performance.getAudience();
          break;
        default:
          throw new InvoiceException("not allowed type");
      }
      volumeCredits += Math.max(performance.getAudience() - 30, 0);

      if ("comedy".equals(play.getType())) {
        volumeCredits += Math.floor(performance.getAudience() / 5);
      }

      result += String.format("%s : %s  (%s seats)\n", play.getName(),
          formatNumber(thisAmount / 100), performance.getAudience());
      totalAmount += thisAmount;

    }

    result += String.format("Amount owed is $%s\n", formatNumber(totalAmount / 100));

    result += String.format("You earned %s credits\n", volumeCredits);
    return result;
  }

  private static String formatNumber(Number number) {
    return "$" + number.toString();
  }
}
