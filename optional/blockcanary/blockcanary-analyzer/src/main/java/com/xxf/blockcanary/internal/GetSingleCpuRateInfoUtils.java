package com.xxf.blockcanary.internal;

public class GetSingleCpuRateInfoUtils {
    private static final String TAG = "GetSingleCpuRateInfoUti";

    public static CpuInfo getCpu(String info) {
        if (info == null || info.isEmpty()) {
            return new CpuInfo();
        }
        String[] split = info.split(BlockInfo.SEPARATOR);
        if (split.length > 0) {
            String[] cpuInfo = split[0].split(" ");
            if (cpuInfo.length == 7) {
                return new CpuInfo(deletePercentSymbol(cpuInfo[2]),
                        deletePercentSymbol(cpuInfo[3]),
                        deletePercentSymbol(cpuInfo[4]),
                        deletePercentSymbol(cpuInfo[5]),
                        deletePercentSymbol(cpuInfo[6]));
            }
            if (cpuInfo.length == 3) {
                return new CpuInfo(deletePercentSymbol(cpuInfo[2]));
            }
        }
        return null;
    }

    private static String deletePercentSymbol(String cpuInfo) {
        if (cpuInfo.endsWith("%")) {
            return cpuInfo.substring(0, cpuInfo.lastIndexOf("%"));
        } else {
            return cpuInfo;
        }
    }


}
