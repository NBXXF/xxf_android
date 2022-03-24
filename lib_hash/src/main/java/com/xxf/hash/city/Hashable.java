package com.xxf.hash.city;

/**
 * Created by hexiufeng on 2017/6/8.
 */
public interface Hashable {
  int hash32(String raw);
  long hash64(String raw);
  long hash64WithSeeds(String raw, long seed0, long seed1);
  long hash64WithSeed(String raw, long seed);
  Number128 hash128(String raw);
  Number128 hash128WithSeed(String raw, final Number128 seed);

}
