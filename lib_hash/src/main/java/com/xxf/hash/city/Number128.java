package com.xxf.hash.city;

/**
 * Created by hexiufeng on 2017/6/9.
 */
public class Number128 {
  private  long lowValue;
  private  long hiValue;

  public Number128(long lowValue,long hiValue){
    this.setLowValue(lowValue);
    this.setHiValue(hiValue);
  }

  public long getLowValue() {
    return lowValue;
  }

  public long getHiValue() {
    return hiValue;
  }

  public void setLowValue(long lowValue) {
    this.lowValue = lowValue;
  }

  public void setHiValue(long hiValue) {
    this.hiValue = hiValue;
  }
}
