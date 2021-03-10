package com.toby.domain;

public enum Level {
  GOLD(3, null), SILVER(2, GOLD), BASIC(1, SILVER);

  private final int value;
  private final Level next;

  Level(int value, Level next) {
    this.value = value;
    this.next = next;
  }

  public int intValue() {
    return value;
  }

  public Level nextLevel() {
    return this.next;
  }

  public static Level valueOf(int value) {
    switch (value) {
      case 1:
        return BASIC;
      case 2:
        return SILVER;
      case 3:
        return GOLD;
      default:
        throw new AssertionError("Unknown value : " + value);
    }
  }

  public void 트랜잭션_경계설정_구조() throws Exception {
    // DB Connection 생성
    // 트랜잭션 시작
    try {
      // DAO 메소드 호출
      // 트랜잭션 커밋
    } catch (Exception e) {
      // 트랜잭션 롤백
      throw e;
    } finally {
      // DB Connection 종료
    }
  }
}
