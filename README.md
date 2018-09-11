# 工作流服务端
workflow是个java工程，通过spring mvc提供的rest api向外提供服务接口，并且集成工作流服务，包括webapp管理员界面，tasklist任务管理界面，cockpist驾驶舱，以及RestApi操作工作流的接口。

##  一、组件的工程目录结构


```
workflow-server/                        - 工作流服务的根目录
	├─ gradle/                         - gradle工作目录，存放一些gradle命令和gradle-wrapper包。
	├─ .idea/                          - idea工作目录，存放了idea项目的一些描述文件等。
	├─ .build/                         - gradle构建结果的输出目录。
	├─ .gitgnore                       - git忽略文件
	├─ docs                            - 项目相关文档
	├─ scripts                         - 通用脚本组件(git,publish,sonar,jacoco...)
	├─ workflow-backend                - 工程宿主程序
	├─ workflow-contract               - 工作流服务模块契约，包含服务和数据契约
	├─ workflow-service                - 工作流服务实现
	├─ workflow-webapi                 - 工作流服务webapi
	├─ wokflow-common                  - 工作流服务公共模块
	├─ gradlew                         - Gradle start up script for UN*X
	├─ gradlew.bat                     - Gradle startup script for Windows
	├─ build.gradle                    - gradle构建脚本
```

#### 1.contract工程结构
```
	src/                                                                              - 源代码目录（该目录中的目录结构是由gradle java plugin约定的）
	 ├─main/                                                                          - *实现代码*
	 |  ├─java/                                                                       - java代码
	 |  |  ├─com/gs/workflow/webapi/contract/model                                   - dto
	 |  |  ├─com/gs/workflow/webapi/contract/service                                 - service interface
	 |  |
	 |  ├─resources/                                                                  - 静态资源文件(以平级的方式存放*.properties文件，不分文件夹)
	 ├─test/                                                                          - 测试代码
	 |  ├─java/                                                                       - java代码（应该一个Controller类，就对应一个测试类）
	 |  |  └─***Test.java                                                             - 测试代码文件
	 |  └─resources/                                                                  - 静态资源文件和配置文件
```

#### 2. service工程结构
```
	src/                                                                               - 源代码目录（该目录中的目录结构是由gradle java plugin约定的）
	 ├─main/                                                                          - *实现代码*
	 |  ├─java/                                                                       - java代码
	 |  |  ├─com/gs/workflow/service/serviceimpl                                      - 服务实现层
	 |  └─resources/                                                                  - 静态资源文件
	 ├─test/                                                                          - 测试代码
	 |  ├─java/                                                                       - java代码
	 |  |  └─***Test.java                                                             - 测试代码文件
	 |  └─resources/                                                                  - 静态资源文件和配置文件
```

#### 3. webApi工程结构
```
	src/                                                                              - 源代码目录（该目录中的目录结构是由gradle java plugin约定的）
	 ├─main/                                                                          - *实现代码*
	 |  ├─java/                                                                       - java代码
	 |  |  ├─com/gs/workflow/webapi/controller                                       - webapi
	 |  |
	 |  ├─resources/                                                                  - 静态资源文件(以平级的方式存放*.properties文件，不分文件夹)
	 ├─test/                                                                          - 测试代码
	 |  ├─java/                                                                       - java代码（应该一个Controller类，就对应一个测试类）
	 |  |  └─***Test.java                                                             - 测试代码文件
	 |  └─resources/                                                                  - 静态资源文件和配置文件
```





  