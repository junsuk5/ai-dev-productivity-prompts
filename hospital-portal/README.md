# 병원정보 포털 (교육용)

공공기관 병원정보 포털의 백엔드를 단순화한 교육용 Java 17 프로젝트다.
6시간 동안 **이 프로젝트 하나만** 다룬다.

## 이 프로젝트에 대해 알아야 할 것

- 모든 테스트가 **통과한다.** 그런데도 **수정할 가치가 있는 문제가 여러 개 남아 있다.**
- 오늘 그 문제를 **전부 고치지 않는다.** 하나만 고치고 나머지는 기록해서 넘긴다.
- AI가 찾아낸 것이 아니라 **사람이 근거를 확인하고 판단한 것**만 결과로 인정한다.

## 기능

| 영역 | 하는 일 | 실무에서 이런 게 터진다 |
| --- | --- | --- |
| `search` | 병원 검색 결과와 페이징 판정 | 마지막 페이지에서 빈 목록을 한 번 더 조회 |
| `hira` | 공공 API HTTP 응답과 XML 파싱 | 연계 장애를 정상 0건으로 집계 |
| `audit` | 요청 감사 로그 문자열 생성 | 로그에 인증키가 남아 감사 지적 |
| `document` | 기관별 문서 조회 | 타 기관 문서 열람 |
| `facility` | 시설 정보 일괄 등록 | 배치 실패인데 일부만 반영 |
| `stats` | 기관별 통계 동기화 | 손대기 무서운 클래스가 계속 커짐 |

코드는 교육용으로 단순화했지만 **결함의 유형은 공공 시스템에서 실제로 발생하는 것들**이다.
오른쪽 열은 각 영역에서 실제로 보고되는 장애·지적 사례다.

## 실행

JDK 17 이상이 필요하다. Maven, Gradle, 외부 네트워크는 필요 없다.
JUnit 4 라이브러리는 `lib/`에 포함되어 있다.

Windows PowerShell:

```powershell
java -version
.\scripts\run.bat
.\scripts\verify.bat
```

macOS:

```bash
java -version
./scripts/run.sh
./scripts/verify.sh
```

- `run` : 애플리케이션을 실행해 **현재 동작을 한국어로 출력**한다
- `verify` : `javac -Xlint:all -Werror` 컴파일 경고 검사와 JUnit 4 테스트를 실행한다

**`run` 출력은 Java 코드를 읽지 않고도 현재 동작을 확인할 수 있는 근거다.**
AI의 주장을 검증할 때 가장 먼저 여기를 본다.

`verify` 실행 중 `[Fatal Error] ... "items"` 메시지가 보이는 것은 **정상이다.**
잘못된 XML을 일부러 파싱해 보는 테스트가 출력하는 메시지이며, 마지막 줄의 `OK`가
실제 결과다.

## 프로젝트 제약

아래는 기술적인 정답이 아니라 **이 프로젝트의 선택**이다. AI가 다른 구조를 제안하면
"가능한가"와 "지금 바꿀 가치가 있는가"를 나눠서 판단한다.

- 외부 API, API 키, 데이터베이스를 사용하지 않는다
- XML은 Java 표준 DOM API로 파싱한다
- Spring, 외부 XML 라이브러리, 빌드 도구를 추가하지 않는다
- 교육용 응답은 최대 100건이므로 스트리밍 파서는 범위 밖이다
- 공개 API 원문 값을 임의의 타입으로 변환하지 않는다

## 실습 진행

`STUDENT_TASK.md`부터 읽는다.

```text
STUDENT_TASK.md      전체 진행 방법
docs/SECTION_01.md   섹션별 과제 (01~06)
docs/RESULT_TEMPLATE.md / PROGRESS_TEMPLATE.md / HANDOFF_TEMPLATE.md
```

## VS Code

이 폴더(`hospital-portal`)를 `File > Open Folder`로 연다. 상위 폴더를 열지 않는다.
Extension Pack for Java가 설치되어 있으면 `lib/`의 JUnit 4가 자동으로 인식된다.

**최종 판정은 편집기의 녹색 실행 버튼이 아니라 `verify` 스크립트 출력으로 한다.**
