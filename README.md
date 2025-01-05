# Spring Boot + Kotlin + MyBatis で Docker Compose 統合

ためしに実装してみたもの。

## bootRun

```bash
./gradlew bootRun
```

プロジェクト直下の `compose.yaml` に記載したコンテナが起動する。

UT とテーブル定義を共通にしたいので `src/test/resources` を初期化ディレクトリとしてマウント。アプリケーション停止後もデータを残すために `mysql/data` ディレクトリをマウント。


## bootTestRun

```bash
./gradlew bootTestRun
```

`src/test/kotlin/com/example/demo/TestDemoApplication.kt` が起動する。

このとき起動するコンテナは MyContainers.kt で定義。テーブル初期化は `src/test/resources/application.properties` で `spring.sql.init.mode=always` と指定しているので、同じディレクトリの schema.sql がコンテナ起動時に実行される。

## UT

bootTestRun と同様に MyContainers.kt で定義したコンテナが起動。テーブル初期化も同様。

## 参考

- https://docs.spring.io/spring-boot/reference/features/dev-services.html
- https://testcontainers.com/guides/testing-spring-boot-rest-api-using-testcontainers/#_add_schema_creation_script