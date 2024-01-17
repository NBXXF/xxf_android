主要功能选择：

LocalDate- 没有时间的日期 获取年、月、日、星期几
LocalTime- 没有日期的时间 只会获取 时分秒 信息。
LocalDateTime- 获取 年月日时分秒，等于 LocalDate + LocalTime 。
Instant- 时间线上的瞬时点
Duration：两个时间之间的持续时间,存储秒和纳秒
Period：两个日期之间的持续时间,存储年，月和日



TemporalAdjusters 类中的常用工厂方法：
dayOfWeekInMonth 创建一个新的日期，它的值为同一个月中每一周的第几天
firstDayOfMonth 创建一个新的日期，它的值为当月的第一天
firstDayOfNextMonth 创建一个新的日期，它的值为下月的第一天
firstDayOfNextYear 创建一个新的日期，它的值为明年的第一天
firstDayOfYear 创建一个新的日期，它的值为当年的第一天
firstInMonth 创建一个新的日期，它的值为同一个月中，第一个符合星期几要求的值
lastDayOfMonth 创建一个新的日期，它的值为当月的最后一天
lastDayOfNextMonth 创建一个新的日期，它的值为下月的最后一天
lastDayOfNextYear 创建一个新的日期，它的值为明年的最后一天
lastDayOfYear 创建一个新的日期，它的值为今年的最后一天
lastInMonth 创建一个新的日期，它的值为同一个月中，最后一个符合星期几要求的值
next / previous 创建一个新的日期，并将其值设定为日期调整后或者调整前，第一个符合指定星 期几要求的日期
nextOrSame / previousOrSame 创建一个新的日期，并将其值设定为日期调整后或者调整前，第一个符合指定星 期几要求的日期，如果该日期已经符合要求，直接返回该对象