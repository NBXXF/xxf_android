## iOS 边缘弹性效果

1. 用smartrefresh 套一层 参考邀请页面 不支持viewpgager等
2. 用setUpOverScroll 分类拓展方法设置,几乎支持所有view,普通的非滚动view也支持,注意:内部是setOnTouchListener的方式实现 注意冲突问题
3. 如果recyclerview 可用recyclerView.edgeEffectFactory = EdgeSpringEffectFactory() viewholder参考:
   EdgeSpringEffectViewHolder recyclerview嵌套recyclerview 有问题 有coorditoarlayout有问题
4. 特别注意 在dialogfragment 里面纵向的 谨慎使用
5. webview 注意:其他网页 都没问题 就我们的前端网页有问题 加了就不能滑动了 蛋疼