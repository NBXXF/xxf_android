

@file:Suppress("unused")

package com.xxf.ktx

import androidx.activity.ComponentActivity
import androidx.annotation.MainThread
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelLazy
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore

val applicationViewModelStore by lazy { ViewModelStore() }

@MainThread
inline fun <reified VM : ViewModel> ComponentActivity.applicationViewModels(
  noinline factoryProducer: () -> ViewModelProvider.Factory = { defaultViewModelProviderFactory }
): Lazy<VM> =
  createApplicationViewModelLazy(factoryProducer)

@MainThread
inline fun <reified VM : ViewModel> Fragment.applicationViewModels(
  noinline factoryProducer: () -> ViewModelProvider.Factory = { defaultViewModelProviderFactory }
): Lazy<VM> =
  createApplicationViewModelLazy(factoryProducer)

@MainThread
inline fun <reified VM : ViewModel> createApplicationViewModelLazy(
  noinline factoryProducer: () -> ViewModelProvider.Factory
) =
  ViewModelLazy(VM::class, { applicationViewModelStore }, factoryProducer)
