package com.zhou.chapter1.exception;

/**
 * @author zhoubing
 * @since 2022/07/03 00:27
 */
public class InvoiceException extends RuntimeException {
  public InvoiceException(String message) {
    super(message);
  }
}
