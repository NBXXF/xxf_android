package com.xxf.arch.model

fun <S : Any> ProjectStepSetting<S>.verifyStepSetting(): Boolean {
    return this.verifyStepSetting(this.stepSettingData)
}

fun <S : Any> ProjectStepSetting<S>.nextStepSetting(): ProjectStepSetting<*> {
    return this.nextStepSetting(this.stepSettingData)
}