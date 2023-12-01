//package com.xxf.json.demo
//
//import com.google.gson.annotations.SerializedName
//import com.spirytusz.booster.annotation.Boost
//import com.squareup.moshi.Json
//import com.squareup.moshi.JsonClass
//
//@Boost
//@JsonClass(generateAdapter = true)
//data class Foo(
//    @Json(name = "foo_int")
//    @SerializedName("foo_int")
//    val intValue: Int = 0,
//
//    @Json(name = "foo_string")
//    @SerializedName("foo_string")
//    val stringValue: String = "",
//
//    @Json(name = "foo_long")
//    @SerializedName("foo_long")
//    val longValue: Long = 0L,
//
//    @Json(name = "foo_boolean")
//    @SerializedName("foo_boolean")
//    val booleanValue: Boolean = false,
//
//    @Json(name = "foo_double")
//    @SerializedName("foo_double")
//    val doubleValue: Double = 0.0,
//
//    @Json(name = "foo_bar")
//    @SerializedName("foo_bar")
//    val bar: Bar = Bar(),
//
//    @Json(name = "foo_list_long")
//    @SerializedName("foo_list_long")
//    val listLong: List<Long> = listOf(),
//
//    @Json(name = "foo_list_bar")
//    @SerializedName("foo_list_bar")
//    val listBar: List<Bar> = listOf(),
//
//    @Json(name = "foo_set_double")
//    @SerializedName("foo_set_double")
//    val setLong: Set<Double> = setOf(),
//
//    @Json(name = "foo_set_bar")
//    @SerializedName("foo_set_bar")
//    val setBar: Set<Bar> = setOf(),
//
//    @Json(name = "foo_list_list_long")
//    @SerializedName("foo_list_list_long")
//    val nestedList: List<List<Long>> = listOf(),
//
//    @Json(name = "foo_list_set_long")
//    @SerializedName("foo_list_set_long")
//    val listSet: List<Set<Long>> = listOf(),
//
//    @Json(name = "foo_nullable_bean")
//    @SerializedName("foo_nullable_bean")
//    val nullableBean: NullableBean = NullableBean(),
//
//    @Json(name = "foo_test_enum")
//    @SerializedName("foo_test_enum")
//    val testEnum: TestEnum = TestEnum.HELLO,
//
//    @Json(name = "var_bean")
//    @SerializedName("var_bean")
//    val varFieldBean: VarFieldBean = VarFieldBean()
//)
//
//@Boost
//@JsonClass(generateAdapter = true)
//data class Bar(
//    @Json(name = "bar_int")
//    @SerializedName("bar_int")
//    val intValue: Int = 0,
//
//    @Json(name = "bar_long")
//    @SerializedName("bar_long")
//    val longValue: Long = 0L,
//
//    @Json(name = "bar_string")
//    @SerializedName("bar_string")
//    val stringValue: String = ""
//)
//
////@Boost
////class Bar2(
////    @SerializedName("bar_int")
////    val intValue: Int,
////    @SerializedName("bar_long")
////    val longValue: Long ,
////    @SerializedName("bar_string")
////    val stringValue: String
////)
//
//@Boost
//@JsonClass(generateAdapter = true)
//data class NullableBean(
//    @Json(name = "nullable_int")
//    @SerializedName("nullable_int")
//    val intValue: Int? = null,
//
//    @Json(name = "nullable_string")
//    @SerializedName("nullable_string")
//    val stringValue: String? = null,
//
//    @Json(name = "nullable_long")
//    @SerializedName("nullable_long")
//    val longValue: Long? = null,
//
//    @Json(name = "nullable_boolean")
//    @SerializedName("nullable_boolean")
//    val booleanValue: Boolean? = null,
//
//    @Json(name = "nullable_double")
//    @SerializedName("nullable_double")
//    val doubleValue: Double? = null,
//
//
//    @Json(name = "nullable_bar")
//    @SerializedName("nullable_bar")
//    val bar: Bar? = null,
//
//    @Json(name = "nullable_list_long")
//    @SerializedName("nullable_list_long")
//    val list: List<Long>? = null,
//
//
//    @Json(name = "nullable_list_bar")
//    @SerializedName("nullable_list_bar")
//    val listBar: List<Bar>? = null
//)
//
//@Boost
//@JsonClass(generateAdapter = true)
//data class VarFieldBean(
//    @Json(name = "var_int")
//    @SerializedName("var_int", alternate = ["var_int2"])
//    var intValue: Int = 0,
//
//
//    @Json(name = "var_long")
//    @SerializedName("var_long")
//    var longValue: Long = 0L,
//
//    @Json(name = "val_double")
//    @SerializedName("val_double")
//    val doubleValue: Double = 0.0
//) {
//
//
//    @Json(name = "var_out_constructor_list_long")
//    @SerializedName("var_out_constructor_list_long")
//    var outConstructorListLongValue: List<Long> = listOf()
//}
//
//
//enum class TestEnum(val intValue: Int) {
//    HELLO(1),
//    HI(2),
//    FINE(3),
//    THANKS(4)
//}