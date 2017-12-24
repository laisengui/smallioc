微小型的IOC容器
======================================================
起因
--------------------------------------------------
   起因是由于我在编写工作中需要的GUI小工具时候遇到了很多对象之间引用的麻烦，突然感觉到习惯了spring 带来的IOC的便利后很难回到原始的状态。<br>
  我试图把spring中的IOC功能拆分开来，最后得到了将近3M的各种jar包，而我写的代码才100k。spring实在是太强大了，但他也越来越臃肿，而我只想要简单的IOC功能。<br>
  最终我决定为自己写一个微型的纯粹的IOC框架，顺便学习下Spring。

设计
----------------------------------------------------
  因为离开了AOP后其实Ioc的一些功能也会受到影响。所以这个工程只有很弱的基础IOC功能<br>
  我想让工程往极简方向发展，所以取消了XML 和properties配置文件。<br>
  支持的最简单的配置放在了初始化容器之前的静态方法里,包括日志记录<br>
  所有代码加起来的大小只有50k，适用于自己练习小的project项目 或制作小工具等<br>
  工程只是简单完成 并使用在了我自己的一个小工具上，未经过任何完整的测试<br>
  代码里只有 cn路径下是源码，其他都是无效的测试类<br>

使用
---------------------------------------------------
  核心包 cn.lsg.smallioc.core 内的ApplicationContext是 唯一入口<br>

```java
public class Test{
public static void main(String[] args) {
  //设置默认日志记录器配置
  ApplicationContext.setDefaultLoggerParams(true, false, LEVEL.debug, "C:/Users/Administrator/Desktop");
  
  //获取容器对象 首次获取容器对象必须传入一个或多个扫描的包路径，
  ApplicationContext app = ApplicationContext.getContext("com");
  //或者
  ApplicationContext app = ApplicationContext.getContext("com","cn.lsg","org.lsg");
  //当容器初始化完成后 则可以传空参获取容器对象
  ApplicationContext app = ApplicationContext.getContext();
  
  
  //获取非注入的获取实例对象可以通过ApplicationContext内的方法
   A a1=app.getbean(A.class);//根据类型
   A a2=(A) app.getbean("aaa");//根据id 要强制转换
   A a3=app.getbean("aaa",A.class);//根据id
   }}
```
  
将类纳入容器管理目前只支持注解  这里的注解只有两个:<br>
inject(id)    id不输则默认按类型注入,可以是根据接口类型注入但要保证接口下只有唯一的受管类，否则必须使用id  <br>
member(id,single)   id默认首字母小写的类名，默认单例<br>
```java
@Member(id="jijij")
public class A implements O{
    @Inject
    public B b;
}
```
```java
 @Member(single=false)
public class B implements O {

    @Inject(id="jijij")
    public A a;
    
    @Inject
    public C c;
 
  }
```
  
 
