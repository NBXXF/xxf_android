package com.xxf.hash;

/**
 * @author tamtam180 - kirscheless at gmail.com
 * @see http://google-opensource.blogspot.jp/2011/04/introducing-cityhash.html
 * @see http://code.google.com/p/cityhash/
 *
 */
public class CityHash {

	private static final long k0 = 0xc3a5c85c97cb3127L;
	private static final long k1 = 0xb492b66fbe98f273L;
	private static final long k2 = 0x9ae16a3b2f90404fL;
	private static final long k3 = 0xc949d7c7509e6557L;

	private static long toLongLE(byte[] b, int i) {
		return (((long)b[i+7] << 56) +
				((long)(b[i+6] & 255) << 48) +
				((long)(b[i+5] & 255) << 40) +
				((long)(b[i+4] & 255) << 32) +
				((long)(b[i+3] & 255) << 24) +
				((b[i+2] & 255) << 16) +
				((b[i+1] & 255) <<  8) +
				((b[i+0] & 255) <<  0));
	}
	private  static int toIntLE(byte[] b, int i) {
		return (((b[i+3] & 255) << 24) + ((b[i+2] & 255) << 16) + ((b[i+1] & 255) << 8) + ((b[i+0] & 255) << 0));
	}

	private static long fetch64(byte[] s, int pos) {
		return toLongLE(s, pos);
	}

	private static int fetch32(byte[] s, int pos) {
		return toIntLE(s, pos);
	}

	private static long rotate(long val, int shift) {
		return shift == 0 ? val : (val >>> shift) | (val << (64 - shift));
	}

	private static long rotateByAtLeast1(long val, int shift) {
		return (val >>> shift) | (val << (64 - shift));
	}

	private static long shiftMix(long val) {
		return val ^ (val >>> 47);
	}

	private static final long kMul = 0x9ddfea08eb382d69L;
	private static long hash128to64(long u, long v) {
		long a = (u ^ v) * kMul;
		a ^= (a >>> 47);
		long b = (v ^ a) * kMul;
		b ^= (b >>> 47);
		b *= kMul;
		return b;
	}

	private static long hashLen16(long u, long v) {
		return hash128to64(u, v);
	}

	private static long hashLen0to16(byte[] s, int pos, int len) {
		if (len > 8) {
			long a = fetch64(s, pos + 0);
			long b = fetch64(s, pos + len - 8);
			return hashLen16(a, rotateByAtLeast1(b + len, len)) ^ b;
		}
		if (len >= 4) {
			long a = 0xffffffffL & fetch32(s, pos + 0);
			return hashLen16((a << 3) + len, 0xffffffffL & fetch32(s, pos + len - 4));
		}
		if (len > 0) {
			int a = s[pos + 0] & 0xFF;
			int b = s[pos + (len >>> 1)] & 0xFF;
			int c = s[pos + len - 1] & 0xFF;
			int y = a + (b << 8);
			int z = len + (c << 2);
			return shiftMix(y * k2 ^ z * k3) * k2;
		}
		return k2;
	}

	private static long hashLen17to32(byte[] s, int pos, int len) {
		long a = fetch64(s, pos + 0) * k1;
		long b = fetch64(s, pos + 8);
		long c = fetch64(s, pos + len - 8) * k2;
		long d = fetch64(s, pos + len - 16) * k0;
		return hashLen16(
				rotate(a - b, 43) + rotate(c, 30) + d,
				a + rotate(b ^ k3, 20) - c + len
		);
	}

	private static long[] weakHashLen32WithSeeds(
			long w, long x, long y, long z,
			long a, long b) {

		a += w;
		b = rotate(b + a + z, 21);
		long c = a;
		a += x;
		a += y;
		b += rotate(a, 44);
		return new long[]{ a + z, b + c };
	}

	private static long[] weakHashLen32WithSeeds(byte[] s, int pos, long a, long b) {
		return weakHashLen32WithSeeds(
				fetch64(s, pos + 0),
				fetch64(s, pos + 8),
				fetch64(s, pos + 16),
				fetch64(s, pos + 24),
				a,
				b
		);
	}

	private static long hashLen33to64(byte[] s, int pos, int len) {

		long z = fetch64(s, pos + 24);
		long a = fetch64(s, pos + 0) + (fetch64(s, pos + len - 16) + len) * k0;
		long b = rotate(a + z, 52);
		long c = rotate(a, 37);

		a += fetch64(s, pos + 8);
		c += rotate(a, 7);
		a += fetch64(s, pos + 16);

		long vf = a + z;
		long vs = b + rotate(a, 31) + c;

		a = fetch64(s, pos + 16) + fetch64(s, pos + len - 32);
		z = fetch64(s, pos + len - 8);
		b = rotate(a + z, 52);
		c = rotate(a, 37);
		a += fetch64(s, pos + len - 24);
		c += rotate(a, 7);
		a += fetch64(s, pos + len -16);

		long wf = a + z;
		long ws = b + rotate(a, 31) + c;
		long r = shiftMix((vf + ws) * k2 + (wf + vs) * k0);

		return shiftMix(r * k0 + vs) * k2;

	}

	public static long cityHash64(byte[] s, int pos, int len) {

		if (len <= 32) {
			if (len <= 16) {
				return hashLen0to16(s, pos, len);
			} else {
				return hashLen17to32(s, pos, len);
			}
		} else if (len <= 64) {
			return hashLen33to64(s, pos, len);
		}

		long x = fetch64(s, pos + len - 40);
		long y = fetch64(s, pos + len - 16) + fetch64(s, pos + len - 56);
		long z = hashLen16(fetch64(s, pos + len - 48) + len, fetch64(s, pos + len - 24));

		long [] v = weakHashLen32WithSeeds(s, pos + len - 64, len, z);
		long [] w = weakHashLen32WithSeeds(s, pos + len - 32, y + k1, x);
		x = x * k1 + fetch64(s, pos + 0);

		len = (len - 1) & (~63);
		do {
			x = rotate(x + y + v[0] + fetch64(s, pos + 8), 37) * k1;
			y = rotate(y + v[1] + fetch64(s, pos + 48), 42) * k1;
			x ^= w[1];
			y += v[0] + fetch64(s, pos + 40);
			z = rotate(z + w[0], 33) * k1;
			v = weakHashLen32WithSeeds(s, pos + 0, v[1] * k1, x + w[0]);
			w = weakHashLen32WithSeeds(s, pos + 32, z + w[1], y + fetch64(s, pos + 16));
			{ long swap = z; z = x; x = swap; }
			pos += 64;
			len -= 64;
		} while (len != 0);

		return hashLen16(
				hashLen16(v[0], w[0]) + shiftMix(y) * k1 + z,
				hashLen16(v[1], w[1]) + x
		);

	}

	public static long cityHash64WithSeed(byte[] s, int pos, int len, long seed) {
		return cityHash64WithSeeds(s, pos, len, k2, seed);
	}
	public static long cityHash64WithSeeds(byte[] s, int pos, int len, long seed0, long seed1) {
		return hashLen16(cityHash64(s, pos, len) - seed0, seed1);
	}

	public static long[] cityMurmur(byte[] s, int pos, int len, long seed0, long seed1) {

		long a = seed0;
		long b = seed1;
		long c = 0;
		long d = 0;

		int l = len - 16;
		if (l <= 0) {
			a = shiftMix(a * k1) * k1;
			c = b * k1 + hashLen0to16(s, pos, len);
			d = shiftMix(a + (len >= 8 ? fetch64(s, pos + 0) : c));
		} else {

			c = hashLen16(fetch64(s, pos + len - 8) + k1, a);
			d = hashLen16(b + len, c + fetch64(s, pos + len - 16));
			a += d;

			do {
				a ^= shiftMix(fetch64(s, pos + 0) * k1) * k1;
				a *= k1;
				b ^= a;
				c ^= shiftMix(fetch64(s, pos + 8) * k1) * k1;
				c *= k1;
				d ^= c;
				pos += 16;
				l -= 16;
			} while (l > 0);
		}

		a = hashLen16(a, c);
		b = hashLen16(d, b);

		return new long[]{ a ^ b, hashLen16(b, a) };

	}

	public static long[] cityHash128WithSeed(byte[] s, int pos, int len, long seed0, long seed1) {

		if (len < 128) {
			return cityMurmur(s, pos, len, seed0, seed1);
		}

		long[] v = new long[2], w = new long[2];
		long x = seed0;
		long y = seed1;
		long z = k1 * len;

		v[0] = rotate(y ^ k1, 49) * k1 + fetch64(s, pos);
		v[1] = rotate(v[0], 42) * k1 + fetch64(s, pos + 8);
		w[0] = rotate(y + z, 35) * k1 + x;
		w[1] = rotate(x + fetch64(s, pos + 88), 53) * k1;

		do {
			x = rotate(x + y + v[0] + fetch64(s, pos + 8), 37) * k1;
			y = rotate(y + v[1] + fetch64(s, pos + 48), 42) * k1;

			x ^= w[1];
			y += v[0] + fetch64(s, pos + 40);
			z = rotate(z + w[0], 33) * k1;
			v = weakHashLen32WithSeeds(s, pos + 0, v[1] * k1, x + w[0]);
			w = weakHashLen32WithSeeds(s, pos + 32, z + w[1], y + fetch64(s, pos + 16));
			{ long swap = z; z = x; x = swap; }
			pos += 64;
			x = rotate(x + y + v[0] + fetch64(s, pos + 8), 37) * k1;
			y = rotate(y + v[1] + fetch64(s, pos + 48), 42) * k1;
			x ^= w[1];
			y += v[0] + fetch64(s, pos + 40);
			z = rotate(z + w[0], 33) * k1;
			v = weakHashLen32WithSeeds(s, pos, v[1] * k1, x + w[0]);
			w = weakHashLen32WithSeeds(s, pos + 32, z + w[1], y + fetch64(s, pos + 16));
			{ long swap = z; z = x; x = swap; }
			pos += 64;
			len -= 128;
		} while (len >= 128);

		x += rotate(v[0] + z, 49) * k0;
		z += rotate(w[0], 37) * k0;

		for (int tail_done = 0; tail_done < len; ) {
			tail_done += 32;
			y = rotate(x + y, 42) * k0 + v[1];
			w[0] += fetch64(s, pos + len - tail_done + 16);
			x = x * k0 + w[0];
			z += w[1] + fetch64(s, pos + len - tail_done);
			w[1] += v[0];
			v = weakHashLen32WithSeeds(s, pos + len - tail_done, v[0] + z, v[1]);
		}

		x = hashLen16(x, v[0]);
		y = hashLen16(y + z, w[0]);

		return new long[]{
				hashLen16(x + v[1], w[1]) + y,
				hashLen16(x + w[1], y + v[1])
		};

	}

	public static long[] cityHash128(byte[] s, int pos, int len) {

		if (len >= 16) {
			return cityHash128WithSeed(
					s, pos + 16,
					len - 16,
					fetch64(s, pos + 0) ^ k3,
					fetch64(s, pos + 8)
			);
		} else if (len >= 8) {
			return cityHash128WithSeed(
					new byte[0], 0, 0,
					fetch64(s, pos + 0) ^ (len * k0),
					fetch64(s, pos + len -8) ^ k1
			);
		} else {
			return cityHash128WithSeed(s, pos, len, k0, k1);
		}

	}

//	private static class CRCContext {
//		private long a, b, c, d, e;
//		private long t;
//		private long f, g, h, i, j;
//		private byte[] s;
//		private int pos;
//		private int len;
//		private long seed;
//		private long[] result;
//		private CRCContext(byte[] s, int pos, int len, int seed) {
//			this.s = s;
//			this.pos = pos;
//			this.len = len;
//			this.seed = 0xffffffffL & seed;
//			this.result = new long[4];
//		}
//		private void init() {
//			a = fetch64(s, pos + 56) + k0;
//			b = fetch64(s, pos + 96) + k0;
//			c = result[0] = hashLen16(b, len);
//			d = result[1] = fetch64(s, pos + 120) * k0 + len;
//			e = fetch64(s, pos + 184) + seed;
//			f = seed;
//			g = 0;
//			h = 0;
//			i = 0;
//			j = 0;
//			t = c + d;
//			System.out.printf("ia)%d,%d,%d,%d,%d\n", a,b,c,d,e);
//			System.out.printf("if)%d,%d,%d,%d,%d\n", f,g,h,i,j);
//		}
//		private void chunk(long multiplier, int z) {
//			long old_a = a;
//			a = rotate(b, 41 ^ z) * multiplier + fetch64(s, pos + 0);
//			b = rotate(c, 27 ^ z) * multiplier + fetch64(s, pos + 8);
//			c = rotate(d, 41 ^ z) * multiplier + fetch64(s, pos + 16);
//			d = rotate(e, 33 ^ z) * multiplier + fetch64(s, pos + 24);
//			e = rotate(t, 25 ^ z) * multiplier + fetch64(s, pos + 32);
//			t = old_a;
//
//			f = CRCUtils.crc32(f, a);
//			g = CRCUtils.crc32(g, b);
//			h = CRCUtils.crc32(h, c);
//			i = CRCUtils.crc32(i, d);
//			j = CRCUtils.crc32(j, e);
//
//			System.out.printf("a)%d,%d,%d,%d,%d\n", a,b,c,d,e);
//			System.out.printf("f)%d,%d,%d,%d,%d\n", f,g,h,i,j);
//
//			pos += 40;
//		}
//		private void finish() {
//			j += i << 32;
//			a = hashLen16(a, j);
//			h += g << 32;
//			b += h;
//			c = hashLen16(c, f) + i;
//			d = hashLen16(d, e + result[0]);
//			j += e;
//			i += hashLen16(h, t);
//			e = hashLen16(a, d) + j;
//			f = hashLen16(b, c) + a;
//			g = hashLen16(j, i) + c;
//			result[0] = e + f + g + h;
//			a = shiftMix((a + g) * k0) * k0 + b;
//			result[1] += a + result[0];
//			a = shiftMix(a * k0) * k0 + c;
//			result[2] = a + result[1];
//			a = shiftMix((a + e) * k0) * k0;
//			result[3] = a + result[2];
//		}
//	}
//
//	private static long[] cityHashCrc256Long(byte[] s, int pos, int len, int seed) {
//
//		System.out.printf("-----> %d, %d, %d\n", pos, len, seed);
//
//		CRCContext ctx = new CRCContext(s, pos, len, seed);
//		ctx.init();
//
//		int iters = ctx.len / 240;
//		ctx.len -= iters * 240;
//		do {
//			ctx.chunk(1, 1); ctx.chunk(k0, 0);
//			ctx.chunk(1, 1); ctx.chunk(k0, 0);
//			ctx.chunk(1, 1); ctx.chunk(k0, 0);
//		} while (--iters > 0);
//
//		while (ctx.len >= 40) {
//			ctx.chunk(k0, 0);
//			ctx.len -= 40;
//		}
//
//		if (len > 0) {
//			ctx.pos += ctx.len - 40;
//			ctx.chunk(k0, 0);
//		}
//
//		ctx.finish();
//		return ctx.result;
//
//	}
//
//	private static long[] cityHashCrc256Short(byte[] s, int pos, int len) {
//		byte[] buffer = new byte[240];
//		System.arraycopy(s, pos, buffer, 0, len);
//		return cityHashCrc256Long(buffer, 0, 240, ~len);
//	}
//
//	public static long[] cityHashCrc256(byte[] s, int pos, int len) {
//		if (len >= 240) {
//			return cityHashCrc256Long(s, pos, len, 0);
//		} else {
//			return cityHashCrc256Short(s, pos, len);
//		}
//	}
//
//	public static long[] cityHashCrc128WithSeed(byte[] s, int pos, int len, long seed0, long seed1) {
//		if (len <= 900) {
//			return cityHash128WithSeed(s, pos, len, seed0, seed1);
//		} else {
//			long[] result = cityHashCrc256(s, pos, len);
//			long u = seed1 + result[0];
//			long v = seed0 + result[1];
//			return new long[] {
//					hashLen16(u, v + result[2]),
//					hashLen16(rotate(v, 32), u * k0 + result[3])
//			};
//		}
//	}
//
//	public static long[] cityHashCrc128(byte[] s, int pos, int len) {
//		if (len <= 900) {
//			return cityHash128(s, pos, len);
//		} else {
//			long[] result = cityHashCrc256(s, pos, len);
//			return new long[]{ result[2], result[3] };
//		}
//	}

}