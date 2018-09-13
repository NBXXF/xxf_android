package com.xxf.arch;

import com.xxf.arch.annotation.BindVM;
import com.xxf.arch.annotation.BindView;
import com.xxf.arch.fragment.XXFFragment;
import com.xxf.arch.viewmodel.XXFViewModel;

/**
 * @author youxuan  E-mail:youxuan@icourt.cc
 * @version 2.3.1
 * @Description
 * @Company Beijing icourt
 * @date createTime：2018/9/11
 */
@BindView(R.layout.fragment_test)
@BindVM(XXFViewModel.class)
public class TestFragment extends XXFFragment {

}
