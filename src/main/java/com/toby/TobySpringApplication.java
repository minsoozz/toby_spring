package com.toby;

import java.util.Scanner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TobySpringApplication {

  public static void main(String[] args) {
    /*SpringApplication.run(TobySpringApplication.class, args);*/

    Scanner sc = new Scanner(System.in);
    System.out.println("물건 가격을 입력하세요");
    int price = sc.nextInt();
    System.out.println("손님이 지불한 금액을 입력하세요");
    int pay = sc.nextInt();
    int result = pay - price;
    String coin[] = {"오천원", "천원", "오백원", "백원", "오십원", "십원"};
    int temp[] = new int[6];
    int abc[] = {5000, 1000, 500, 100, 50, 10};
    for(int i=0; i<abc.length; i++) {
      if(result > abc[i]) {
        temp[i] = result / abc[i];
        result = result - result/abc[i] * abc[i];
      }
    }
    System.out.println(temp[0]);
    System.out.println(temp[1]);
    System.out.println(temp[2]);
    System.out.println(temp[3]);
    System.out.println(temp[4]);
    System.out.println(temp[5]);
  }
}
