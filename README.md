KU_OPENSW_Project
====================
Konkuk University, OpenSW Final Projecct

Basic Project Description
---------------------------
이 프로젝트는 1학기 OpenSW 과목의 기말 프로젝트 입니다.<br>

What is this?
-------------
운영체제에는 여러가지 프로세스 스케쥴링 알고리즘이 존재합니다.<br>
그 중에도 선점 알고리즘, 비선점 알고리즘으로 나뉘며, 선점과 비선점도 각각 여러 알고리즘으로 나뉩니다.<br>
<br>
이 프로젝트는 여러 프로세스 스케쥴링 알고리즘 중, 비선점의 FCFS/SJF/Priority 스케쥴링을 직접 구현해 본 프로젝트 입니다.

이 프로젝트는
- 각 스케쥴링의 간단한 설명을 볼 수 있으며
- 각 스케쥴링을 직접 "시뮬레이션"을 할 수 있습니다.<br>
  시뮬레이션: 후술할 Work Queue를 통해 알고리즘의 성능 판단 기준으로 얘기하는 평균 대기시간, 평균 반환시간을 계산해서 사용자에게 보여줍니다. 실제로 작업 큐에 명시된 burst_time에 따라 본 프로그램은 "sleep"합니다.

Supported Algorithms
---------------------
현재, 사용할 수 있는 알고리즘은 다음과 같습니다.
- FCFS
- SJF
- Priority<br>
  단, Priority 스케쥴링에서 문제가 지적되어 왔던 "기아 현상"은 에이징 기법(Aging)을 이용해 기아 현상이 일어나지 않도록 방지하였습니다.<br>
  구현 방식: 작업 큐에 도착한 시간과, 기다린 시간이 일정 이상 지나면 우선 순위를 높여줌.

참고 서적
---------
이 알고리즘들을 구현하기 위해서 Operating System Concepts, 10th Edition 책을 사용 하였고, 추가적으로 운영체제 강의에서 언급된 내용을 제 언어로 이해한 부분도 있습니다.

Work Queue
-----------
작업 큐는 실제로 운영체제에서 운영되는 작업 큐와 컨셉 자체는 비슷 하지만, 구현과 구조는 완전히 다릅니다.<br>
예시로 조그만한 테스트 케이스는 https://github.com/KangDroid/KU_OPENSW_Project/blob/master/testJob.json 를 참고하실 수 있습니다.<br>
<br>
기본적으로 JSON 형태의 파일을 띄고 있으며, 대략적인 구조는 다음과 같습니다.<br>
- Job: Outer-Identifier <br>
  - job_identifier
  - priority
  - burst_time
  
이러한 형식으로 json이 이루어져 있고, 각 element는 모두 존재해야 합니다.<br>
- Job_identifier: JSON 안에서 그냥 작업들을 구분하기 위한 넘버 자체입니다. 중복이 되어도 상관 없습니다.
- priority: Priority Scheduling 알고리즘에서 사용되는 정보 입니다. 이 정보에 따라 작업 큐의 순서가 바뀌며, Aging을 해야되는 경우, 해당 값이 변경되기도 합니다.
- burst_time: 전체적으로 쓰이는 정보 입니다. SJF Scheduling 알고리즘에서는 이 값을 기준으로 작업 큐의 순서가 바뀌며, SJF를 포함한 다른 알고리즘의 경우에는 burst_time에 정의된<br>
  시간 만큼 본 프로그램이 "sleep"하게 됩니다. 즉, 이 burst_time의 단위는 "ms"이며, 이 값이 너무 길 경우, 프로그램이 그 만큼 sleep 할 수 있다는 의미입니다.
