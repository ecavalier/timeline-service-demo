Timeline Service
====
-----------------------
팔로우, 포스팅, 뉴스 피드 기능이 포함된 타임라인 서비스 개발

## Running
-----------------------
IntelliJ 에서 _Import Project From SBT_ 로 프로젝트 임포트 후 _Play 2 App_ 환경으로 실행 

## Documentation
-----------------------
* 시스템 시작시 경로
    [`http://localhost:9000`](http://localhost:9000)

* H2 접속을 위해 `Application.scala`에서 다음 옵션을 켰을 경우에 새로 고침 가능한 Responsive 페이지
    ```
    Server.createWebServer("-webAllowOthers","-webPort","8082").start()
    Server.createTcpServer("-tcpAllowOthers","-tcpPort","9092").start()
    ```
    [`http://localhost:9000/swagger-ui/index.html`](http://localhost:9000/swagger-ui/index.html)
