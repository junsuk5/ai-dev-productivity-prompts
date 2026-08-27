# XML fixture 출처

모든 fixture는 건강보험심사평가원 병원정보서비스 응답 구조를 교육 목적으로 단순화한
synthetic 자료다. 실제 응답을 캡처한 자료가 아니며 API 키나 개인식별정보를 포함하지 않는다.

| 파일 | HTTP 상태 | API `resultCode` | 내용 |
| --- | ---: | --- | --- |
| `hospitals-success.xml` | 200 | `00` | 전체 11건 중 1페이지 |
| `hospitals-empty.xml` | 200 | `00` | 전체 0건 |
| `hospitals-page-full.xml` | 200 | `00` | 전체 10건, 페이지 크기 10 |
| `hospitals-service-error.xml` | 200 | `30` | 서비스 키 미등록 |
| `hospitals-malformed.xml` | 200 | `00` | XML 종료 태그 누락 |

- dataset: 건강보험심사평가원 병원정보서비스 구조 참고
- operation: `getHospBasisList` 구조 참고
- provenance: `synthetic`
- secret scan: API 키와 인증정보 없음
