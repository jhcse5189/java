# BitTorrent in Java

                                                                             prof. Choonhwa Lee
                                                                             컴퓨터소프트웨어학부
                                                                               2018009143 조현서

* * *

* Assignment #1

> Implement file chunk download client & server peer. 

~~~
- one-to-one TCP connection
- client does download directly
- use blocking I/O (don't need to use thread yet)
~~~

- [X] Blocking I/O

        Infinitely wait for result of I/O request.
        Whole processes are blocked, therefore can't perform any other operation if it requests.

Server와 Client에 모두 Blocking I/O을 통한 Socket networking을\
구현하기위해 `java.io.*`, `java.net.*`을 import하여 사용하였다.

- [X] data file specified as a command-line argument

        Server( a seeder )'s command-line arguments will be (IP, TCP port#) of
        Client( a leecher peer ), along with file name to share with Client.

        What file to seek is also specified as Client's command-line argument.

 

- [ ] file is chopped into chunks of 10KB



* * *

* Assignment #2

> Implement swarming protocol in P2P method.

~~~
- 5 peers in the network - one seeder & four leechers
- (don't need to tracker for this assignment)
- ...
~~~

* * *