# MngRunningHorse

## TODO

- [x] 一个主菜单应该向用户提供一个操作选项列表 
- [x] 能够查看卫星列表
- [x] 能够注册添加一个新卫星，添加新卫星要符合[规则](#卫星约束)
- [x] 能够编辑修改现有卫星信息 
- [x] 能够删除卫星
- [x] 能够按名称查询卫星
- [x] 能够根据ID封锁卫星 
- [x] 能够激活已经封锁了的卫星，重新启用
- 能干净地退出应用程序
- 程序开始至少已经有5个卫星已经预加载到数组列表中
- 数组列表的开始大小应该为10
- 强化封装性
- 单独分出输出
- 分离检测参数

## 卫星约束

1. 最低卫星轨道为1.2
2. 卫星轨道之间的距离不得小于0.2
3. 每一个卫星必须有一个唯一的COSPARID，总长度为6位，由两位国家编码加上四位数字组成
4. 每一个卫星都有一个容易识别的名称，可以由所属国家命名
5. 卫星可以有两种状态，一种状态是处于激活可用状态，另一种是处于封锁非激活状态，但卫星任然在固定轨道飞行，只是处于不可用状态
6. 卫星报废后，应该及时被删除，腾出轨道空间
