package com.toby.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User {

  String id;
  String password;
  String name;
  Level level;
  int login;
  int recommend;
  String email;

  public void upgradeLevel() {
    Level nextLevel = this.level.nextLevel();
    if(nextLevel == null){
      throw new IllegalStateException(this.level + "은 업글 불가");
    } else {
      this.level = nextLevel;
    }
  }
}
