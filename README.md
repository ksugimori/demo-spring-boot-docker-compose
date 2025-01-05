# Spring Boot + Kotlin + MyBatis で Docker Compose 統合

ためしに実装してみたもの。

bootRun で起動した場合は compose.yaml で `mysql/data` ディレクトリをマウント。UT では TestContainers でコンテナ起動。

## 参考

https://docs.spring.io/spring-boot/reference/features/dev-services.html