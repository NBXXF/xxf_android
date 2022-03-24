package com.xxf.hash.city.cityhash;

import com.xxf.hash.city.Hashable;
import com.xxf.hash.city.Number128;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

/**
 * 完全迁移在cityhash的官方c版本，
 *
 * Created by hexiufeng on 2017/6/8.
 */
public class CityHash implements Hashable {
  private static  final boolean IS_BIG_EDIAN = !"little".equals(System.getProperty("sun.cpu.endian"));

  // Some primes between 2^63 and 2^64 for various uses.
  public static final long k0 = 0xc3a5c85c97cb3127L;
  public static final long k1 = 0xb492b66fbe98f273L;
  public static final long k2 = 0x9ae16a3b2f90404fL;
  public static final long kMul = 0x9ddfea08eb382d69L;

  // Magic numbers for 32-bit hashing.  Copied from Murmur3.
  public static final int c1 = 0xcc9e2d51;
  public static final int c2 = 0x1b873593;


  @Override
  public int hash32(String raw) {
    byte[] byteArray = convertString2UTF8(raw);
    int len = byteArray.length;
    if (len <= 24) {
      return len <= 12 ?
              (len <= 4 ? hash32Len0to4(byteArray) : hash32Len5to12(byteArray)) :
              hash32Len13to24(byteArray);
    }

    // len > 24
    int h = len, g = c1 * len, f = g;
    int a0 = rotate32(fetch32(byteArray, len - 4) * c1, 17) * c2;
    int a1 = rotate32(fetch32(byteArray, len - 8) * c1, 17) * c2;
    int a2 = rotate32(fetch32(byteArray, len - 16) * c1, 17) * c2;
    int a3 = rotate32(fetch32(byteArray, len - 12) * c1, 17) * c2;
    int a4 = rotate32(fetch32(byteArray, len - 20) * c1, 17) * c2;
    h ^= a0;
    h = rotate32(h, 19);
    h = h * 5 + 0xe6546b64;
    h ^= a2;
    h = rotate32(h, 19);
    h = h * 5 + 0xe6546b64;
    g ^= a1;
    g = rotate32(g, 19);
    g = g * 5 + 0xe6546b64;
    g ^= a3;
    g = rotate32(g, 19);
    g = g * 5 + 0xe6546b64;
    f += a4;
    f = rotate32(f, 19);
    f = f * 5 + 0xe6546b64;
    int iters = (len - 1) / 20;

    int pos = 0;
    do {
      a0 = rotate32(fetch32(byteArray,pos) * c1, 17) * c2;
      a1 = fetch32(byteArray,pos + 4);
      a2 = rotate32(fetch32(byteArray,pos + 8) * c1, 17) * c2;
      a3 = rotate32(fetch32(byteArray,pos + 12) * c1, 17) * c2;
      a4 = fetch32(byteArray,pos + 16);
      h ^= a0;
      h = rotate32(h, 18);
      h = h * 5 + 0xe6546b64;
      f += a1;
      f = rotate32(f, 19);
      f = f * c1;
      g += a2;
      g = rotate32(g, 18);
      g = g * 5 + 0xe6546b64;
      h ^= a3 + a1;
      h = rotate32(h, 19);
      h = h * 5 + 0xe6546b64;
      g ^= a4;
      g = Integer.reverseBytes(g) * 5;
      h += a4 * 5;
      h = Integer.reverseBytes(h);
      f += a0;
      int swapValue = f;
      f = g;
      g = h;
      h = swapValue;

      pos += 20;
    } while (--iters != 0);

    g = rotate32(g, 11) * c1;
    g = rotate32(g, 17) * c1;
    f = rotate32(f, 11) * c1;
    f = rotate32(f, 17) * c1;
    h = rotate32(h + g, 19);
    h = h * 5 + 0xe6546b64;
    h = rotate32(h, 17) * c1;
    h = rotate32(h + f, 19);
    h = h * 5 + 0xe6546b64;
    h = rotate32(h, 17) * c1;
    return h;
  }

  @Override
  public long hash64(String raw) {
    byte[] byteArray = convertString2UTF8(raw);
    int len = byteArray.length;
    if (len <= 32) {
      if (len <= 16) {
        return hashLen0to16(byteArray);
      } else {
        return hashLen17to32(byteArray);
      }
    } else if (len <= 64) {
      return hashLen33to64(byteArray);
    }

    // For strings over 64 bytes we hash the end first, and then as we
    // loop we keep 56 bytes of state: v, w, x, y, and z.
    long x = fetch64(byteArray, len - 40);
    long y = fetch64(byteArray, len - 16) + fetch64(byteArray, len - 56);
    long z = hashLen16(fetch64(byteArray, len - 48) + len, fetch64(byteArray, len - 24));
    Number128 v = weakHashLen32WithSeeds(byteArray, len - 64, len, z);
    Number128 w = weakHashLen32WithSeeds(byteArray, len - 32, y + k1, x);
    x = x * k1 + fetch64(byteArray,0);

    // Decrease len to the nearest multiple of 64, and operate on 64-byte chunks.
    len = (len - 1) & ~63;
    int pos = 0;
    do {
      x = rotate(x + y + v.getLowValue() + fetch64(byteArray , pos + 8), 37) * k1;
      y = rotate(y + v.getHiValue() + fetch64(byteArray , pos + 48), 42) * k1;
      x ^= w.getHiValue();
      y += v.getLowValue() + fetch64(byteArray, pos+ 40);
      z = rotate(z + w.getLowValue(), 33) * k1;
      v = weakHashLen32WithSeeds(byteArray,pos, v.getHiValue() * k1, x + w.getLowValue());
      w = weakHashLen32WithSeeds(byteArray,  pos + 32, z + w.getHiValue(), y + fetch64(byteArray, pos + 16));
      // swap z,x value
      long swapValue = x;
      x = z;
      z = swapValue;
      pos += 64;
      len -= 64;
    } while (len != 0);
    return hashLen16(hashLen16(v.getLowValue(), w.getLowValue()) + shiftMix(y) * k1 + z,
            hashLen16(v.getHiValue(), w.getHiValue()) + x);
  }

  @Override
  public  long hash64WithSeeds(String raw, long seed0, long seed1){
    return hashLen16(hash64(raw) - seed0, seed1);
  }

  @Override
  public long hash64WithSeed(String raw, long seed){
    return hash64WithSeeds(raw, k2, seed);
  }

  @Override
  public Number128 hash128(String raw){
    byte[] byteArray = this.convertString2UTF8(raw);
    int len = byteArray.length;
    return len >= 16 ?
            hash128WithSeed(byteArray,16,
                    new Number128(fetch64(byteArray,0), fetch64(byteArray , 8) + k0)) :
            hash128WithSeed(byteArray, 0, new Number128(k0, k1));
  }

  @Override
  public Number128 hash128WithSeed(String raw, final Number128 seed){
    byte[] byteArray = this.convertString2UTF8(raw);
    return hash128WithSeed(byteArray,0,seed);
  }
  private Number128 hash128WithSeed(final byte[] byteArray, int start, final Number128 seed) {
    int len = byteArray.length - start;

    if (len < 128) {
      return cityMurmur(Arrays.copyOfRange(byteArray,start,byteArray.length), seed);
    }

    // We expect len >= 128 to be the common case.  Keep 56 bytes of state:
    // v, w, x, y, and z.
    Number128 v = new Number128(0L,0L);
    Number128 w = new Number128(0L,0L);
    long x = seed.getLowValue();
    long y = seed.getHiValue();
    long z = len * k1;
    v.setLowValue(rotate(y ^ k1, 49) * k1 + fetch64(byteArray, start));
    v.setHiValue(rotate(v.getLowValue(), 42) * k1 + fetch64(byteArray, start + 8));
    w.setLowValue(rotate(y + z, 35) * k1 + x);
    w.setHiValue(rotate(x + fetch64(byteArray, start + 88), 53) * k1);

    // This is the same inner loop as CityHash64(), manually unrolled.
    int pos = start;
    do {
      x = rotate(x + y + v.getLowValue() + fetch64(byteArray,pos + 8), 37) * k1;
      y = rotate(y + v.getHiValue() + fetch64(byteArray,pos + 48), 42) * k1;
      x ^= w.getHiValue();
      y += v.getLowValue() + fetch64(byteArray,pos + 40);
      z = rotate(z + w.getLowValue(), 33) * k1;
      v = weakHashLen32WithSeeds(byteArray,pos, v.getHiValue() * k1, x + w.getLowValue());
      w = weakHashLen32WithSeeds(byteArray,pos + 32, z + w.getHiValue(), y + fetch64(byteArray,pos + 16));

      long swapValue = x;
      x = z;
      z = swapValue;
      pos += 64;
      x = rotate(x + y + v.getLowValue() + fetch64(byteArray,pos + 8), 37) * k1;
      y = rotate(y + v.getHiValue() + fetch64(byteArray,pos + 48), 42) * k1;
      x ^= w.getHiValue();
      y += v.getLowValue() + fetch64(byteArray,pos + 40);
      z = rotate(z + w.getLowValue(), 33) * k1;
      v = weakHashLen32WithSeeds(byteArray,pos, v.getHiValue() * k1, x + w.getLowValue());
      w = weakHashLen32WithSeeds(byteArray,pos + 32, z + w.getHiValue(), y + fetch64(byteArray,pos + 16));
      swapValue = x;
      x = z;
      z = swapValue;
      pos += 64;
      len -= 128;
    } while (len >= 128);
    x += rotate(v.getLowValue() + z, 49) * k0;
    y = y * k0 + rotate(w.getHiValue(), 37);
    z = z * k0 + rotate(w.getLowValue(), 27);
    w.setLowValue(w.getLowValue()*9);
    v.setLowValue(v.getLowValue()*k0);

    // If 0 < len < 128, hash up to 4 chunks of 32 bytes each from the end of s.
    for (int tail_done = 0; tail_done < len; ) {
      tail_done += 32;
      y = rotate(x + y, 42) * k0 + v.getHiValue();
      w.setLowValue(w.getLowValue() + fetch64(byteArray , pos + len - tail_done + 16));
      x = x * k0 + w.getLowValue();
      z += w.getHiValue() + fetch64(byteArray ,pos + len - tail_done);
      w.setHiValue(w.getHiValue() + v.getLowValue());
      v = weakHashLen32WithSeeds(byteArray, pos + len - tail_done, v.getLowValue() + z, v.getHiValue());
      v.setLowValue(v.getLowValue() * k0);
    }
    // At this point our 56 bytes of state should contain more than
    // enough information for a strong 128-bit hash.  We use two
    // different 56-byte-to-8-byte hashes to get a 16-byte final result.
    x = hashLen16(x, v.getLowValue());
    y = hashLen16(y + z, w.getLowValue());
    return new Number128(hashLen16(x + v.getHiValue(), w.getHiValue()) + y,
            hashLen16(x + w.getHiValue(), y + v.getHiValue()));

  }

  private int hash32Len0to4(final byte[] byteArray) {
    int b = 0;
    int c = 9;
    int len = byteArray.length;
    for (int i = 0; i < len; i++) {
      int v = byteArray[i];
      b = b * c1 + v;
      c ^= b;
    }
    return fmix(mur(b, mur(len, c)));
  }

   private int hash32Len5to12(final byte[] byteArray) {
    int len = byteArray.length;
    int a = len, b = len * 5, c = 9, d = b;
    a += fetch32(byteArray,0);
    b += fetch32(byteArray , len - 4);
    c += fetch32(byteArray, ((len >>> 1) & 4));
    return fmix(mur(c, mur(b, mur(a, d))));
  }
  private int hash32Len13to24(byte[] byteArray) {
    int len = byteArray.length;
    int a = fetch32(byteArray, (len >>> 1) - 4 );
    int b = fetch32(byteArray, 4);
    int c = fetch32(byteArray, len - 8);
    int d = fetch32(byteArray, (len >>> 1));
    int e = fetch32(byteArray,0);
    int f = fetch32(byteArray, len - 4);
    int h = len;

    return fmix(mur(f, mur(e, mur(d, mur(c, mur(b, mur(a, h)))))));
  }

  private long hashLen0to16(byte[] byteArray) {
    int len = byteArray.length;
    if (len >= 8) {
      long mul = k2 + len * 2;
      long a = fetch64(byteArray,0) + k2;
      long b = fetch64(byteArray,len - 8);
      long c = rotate(b, 37) * mul + a;
      long d = (rotate(a, 25) + b) * mul;
      return hashLen16(c, d, mul);
    }
    if (len >= 4) {
      long mul = k2 + len * 2;
      long a = fetch32(byteArray,0)&0xffffffffL;
      return hashLen16(len + (a << 3), fetch32(byteArray , len - 4)&0xffffffffL, mul);
    }
    if (len > 0) {
      int a = byteArray[0]&0xff;
      int b = byteArray[len >>> 1]&0xff;
      int c = byteArray[len - 1]&0xff;
      int y = a + (b << 8);
      int z = len + (c << 2);
      return shiftMix(y * k2 ^ z * k0) * k2;
    }
    return  k2;
  }

  // This probably works well for 16-byte strings as well, but it may be overkill
// in that case.
  private long hashLen17to32(byte[] byteArray) {
    int len = byteArray.length;
    long mul = k2 + len * 2;
    long a = fetch64(byteArray,0) * k1;
    long b = fetch64(byteArray, 8);
    long c = fetch64(byteArray, len - 8) * mul;
    long d = fetch64(byteArray, len - 16) * k2;
    return hashLen16(rotate(a + b, 43) + rotate(c, 30) + d,
            a + rotate(b + k2, 18) + c, mul);
  }

  private long hashLen33to64(byte[] byteArray) {
    int len = byteArray.length;
    long mul = k2 + len * 2;
    long a = fetch64(byteArray,0) * k2;
    long b = fetch64(byteArray, 8);
    long c = fetch64(byteArray, len - 24);
    long d = fetch64(byteArray, len - 32);
    long e = fetch64(byteArray, 16) * k2;
    long f = fetch64(byteArray, 24) * 9;
    long g = fetch64(byteArray, len - 8);
    long h = fetch64(byteArray, len - 16) * mul;
    long u = rotate(a + g, 43) + (rotate(b, 30) + c) * 9;
    long v = ((a + g) ^ d) + f + 1;
    long w = Long.reverseBytes((u + v) * mul) + h;
    long x = rotate(e + f, 42) + c;
    long y = (Long.reverseBytes((v + w) * mul) + g) * mul;
    long z = e + f + c;
    a = Long.reverseBytes((x + z) * mul + y) + b;
    b = shiftMix((z + a) * mul + d + h) * mul;
    return b + x;
  }

  private long loadUnaligned64(final byte[] byteArray,final int start) {
    long result = 0;
    OrderIter orderIter = new OrderIter(8,this.IS_BIG_EDIAN);
    while(orderIter.hasNext()){
      int next = orderIter.next();
      long value = (byteArray[next + start]&0xffL)<<(next * 8);
      result |= value;
    }
    return result;
  }

  private int loadUnaligned32(final byte[] byteArray,final int start) {
    int result = 0;
    OrderIter orderIter = new OrderIter(4,this.IS_BIG_EDIAN);
    while(orderIter.hasNext()){
      int next = orderIter.next();
      int value = (byteArray[next + start]&0xff)<<(next * 8);
      result |= value;
    }
    return result;
  }

  private long fetch64(byte[] byteArray,final int start) {
    return loadUnaligned64(byteArray,start);
  }

  private int fetch32(byte[] byteArray,final int start) {
    return loadUnaligned32(byteArray,start);
  }

  private long rotate(long val, int shift) {
    // Avoid shifting by 64: doing so yields an undefined result.
    return shift == 0 ? val : ((val >>> shift) | (val << (64 - shift)));
  }

  static int rotate32(int val, int shift) {
    // Avoid shifting by 32: doing so yields an undefined result.
    return shift == 0 ? val : ((val >>> shift) | (val << (32 - shift)));
  }

  private long hashLen16(long u, long v, long mul) {
    // Murmur-inspired hashing.
    long a = (u ^ v) * mul;
    a ^= (a >>> 47);
    long b = (v ^ a) * mul;
    b ^= (b >>> 47);
    b *= mul;
    return b;
  }

  private long hashLen16(long u, long v) {
    return hash128to64(new Number128(u, v));
  }

  private long hash128to64(final Number128 number128) {
    // Murmur-inspired hashing.
    long a = (number128.getLowValue() ^number128.getHiValue()) * kMul;
    a ^= (a >>> 47);
    long b = (number128.getHiValue() ^ a) * kMul;
    b ^= (b >>> 47);
    b *= kMul;
    return b;
  }

  private long shiftMix(long val) {
    return val ^ (val >>> 47);
  }

  private int fmix(int h)
  {
    h ^= h >>> 16;
    h *= 0x85ebca6b;
    h ^= h >>> 13;
    h *= 0xc2b2ae35;
    h ^= h >>> 16;
    return h;
  }

  private int mur(int a, int h) {
    // Helper from Murmur3 for combining two 32-bit values.
    a *= c1;
    a = rotate32(a, 17);
    a *= c2;
    h ^= a;
    h = rotate32(h, 19);
    return h * 5 + 0xe6546b64;
  }

  private Number128 weakHashLen32WithSeeds(
          long w, long x, long y, long z, long a, long b) {
    a += w;
    b = rotate(b + a + z, 21);
    long c = a;
    a += x;
    a += y;
    b += rotate(a, 44);
    return new Number128(a + z, b + c);
  }

  // Return a 16-byte hash for s[0] ... s[31], a, and b.  Quick and dirty.
  private Number128 weakHashLen32WithSeeds(
    byte[] byteArray, int start,long a, long b) {
    return weakHashLen32WithSeeds(fetch64(byteArray,start),
            fetch64(byteArray , start+8),
            fetch64(byteArray, start+16),
            fetch64(byteArray,start+24),
            a,
            b);
  }

  private Number128 cityMurmur(final byte[] byteArray, Number128 seed) {
    int len = byteArray.length;
    long a = seed.getLowValue();
    long b = seed.getHiValue();
    long c = 0L;
    long d = 0L;
    int l = len - 16;
    if (l <= 0) {  // len <= 16
      a = shiftMix(a * k1) * k1;
      c = b * k1 + hashLen0to16(byteArray);
      d = shiftMix(a + (len >= 8 ? fetch64(byteArray, 0) : c));
    } else {  // len > 16
      c = hashLen16(fetch64(byteArray, len - 8) + k1, a);
      d = hashLen16(b + len, c + fetch64(byteArray , len - 16));
      a += d;
      int pos = 0;
      do {
        a ^= shiftMix(fetch64(byteArray,pos) * k1) * k1;
        a *= k1;
        b ^= a;
        c ^= shiftMix(fetch64(byteArray,pos + 8) * k1) * k1;
        c *= k1;
        d ^= c;
        pos += 16;
        l -= 16;
      } while (l > 0);
    }
    a = hashLen16(a, c);
    b = hashLen16(d, b);
    return new Number128(a ^ b, hashLen16(b, a));
  }

  private static class OrderIter{
    private final int size;
    private final boolean isBigEdian;
    private int index;

    OrderIter(int size,boolean isBigEdian){
      this.size = size;
      this.isBigEdian = isBigEdian;
    }
    boolean hasNext(){
      return index < size;
    }
    int next(){
      if(!isBigEdian){
        return index++;
      }else{
        return size - 1 - index++;
      }
    }
  }

  private  byte[] convertString2UTF8(String raw){
    try {
      return raw.getBytes("UTF-8");
    } catch (UnsupportedEncodingException e) {
      // don't happen
      throw  new RuntimeException(e);
    }
  }
}
