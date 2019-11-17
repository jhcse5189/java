# BitTorrent in Java

<div class=pull-right>
    prof. Choonhwa Lee
</div>

* * *

* TCPServer / TCPClient

1. 일단 C:/TCPServer/dlwlrma.jfif, C:/TCPClient로 만드세요.

        (Client 경로를 만들어주지 않으면 폴더를 알아서 안만들더군요...)

2. git clone 해서 받으세요.

        (3 파일을 한 폴더에 저장해도 됩니다.)

3. Server, Client 각각 main이 있으므로 컴파일 해줍니다.

        $ javac TCPServer.java
        $ javac TCPClient.java

4. `java`로 실행한 후, `h`를 입력하면 각각 사용 설명을 볼 수 있습니다.

        (그냥 서버열고 s, 클라이언트열고 c 누르면 되긴합니다.)

5. 그리하면 이지금!님이 Client에 Server로부터 전송됩니다아ㅣㅣ!

* * *

* Assignment #1

> Implement file chunk download client & server peer. 

~~~
- one-to-one TCP connection
- client does download directly
- use blocking I/O (don't need to use thread yet)
~~~

- [ ] Blocking I/O

        Infinitely wait for result of I/O request.
        Whole processes are blocked, therefore can't perform any other operation if it requests.

* Assignment #2

> Implement swarming protocol in P2P method.

* * *