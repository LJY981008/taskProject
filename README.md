<details>
<summary>협업 전략</summary>

## 본 프로젝트는 Git을 활용한 효율적인 협업을 위해 다음과 같은 전략을 따른다.

### 1. 브랜치 전략 수립
프로젝트의 안정성과 개발 효율성을 위해 명확한 브랜치 전략을 수립한다.

`Main/Master` 브랜치: 항상 안정적인 프로덕션 코드만 유지한다. 배포 가능한 상태의 코드가 반영된다.</br>
`Dev` 브랜치: 개발 중인 기능들이 통합되는 브랜치이다. `Main` 브랜치로 병합되기 전 충분한 테스트를 거치는 용도로 활용한다.</br>
`Feature` 브랜치: 새로운 기능 개발 시 `Dev` 브랜치에서 분기하여 생성한다. (예: feature/add-user-login)</br>
`Bugfix` 브랜치: 버그 수정 시 `Dev` 브랜치에서 분기하여 생성한다. (예: bugfix/fix-payment-error)</br>
`Hotfix` 브랜치: 긴급한 버그 수정 시 `Main` 브랜치에서 직접 분기하여 생성한다.</br>

### 2. 작업 시작 전 로컬 저장소 최신화
새로운 작업을 시작하기 전에 항상 로컬 브랜치를 원격 저장소의 최신 상태로 업데이트해야 한다.

예시: `git pull origin dev`

### 3. 새 브랜치 생성 및 이동
작업 목적을 명확히 드러내도록 간결하고 서술적인 브랜치명을 사용한다.

브랜치명 규칙: feature/기능명, bugfix/버그내용, hotfix/긴급수정내용 등</br>
예시: `feature/add-user-login`, `bugfix/fix-payment-error`</br>
새 브랜치를 생성하고 즉시 해당 브랜치로 이동한다.</br>

git checkout -b [새 브랜치명] </br>
예시: `git checkout -b feature/new-dashboard`

### 4. 기능 개발 및 커밋
새 브랜치에서 작업을 수행하고, 기능 단위로 작고 논리적인 커밋을 생성한다. 커밋 메시지는 변경 내용을 명확하게 설명해야 한다.

예시: `git add . `또는 `git add 파일명`</br>
예시: `git commit -m "기능설명"`

### 5. 작업 브랜치 최신화 (중요)
작업 중 다른 협업자가 기준 브랜치(예: main 또는 dev)에 변경 사항을 푸시했을 수 있다. </br>
병합 충돌을 최소화하기 위해 주기적으로 기준 브랜치의 변경 사항을 작업 브랜치로 가져와야 한다.

기준 브랜치로 이동한다.</br>
예시: `git checkout dev`

기준 브랜치를 최신 상태로 업데이트한다.</br>
예시: `git pull origin dev`

다시 작업 브랜치로 이동한다.</br>
`git checkout [작업 브랜치명]`</br>
예시 : `git checkout feature/new-dashboard`

기준 브랜치의 변경 사항을 작업 브랜치로 병합한다.</br>
예시: `git merge dev`

충돌 발생 시, 충돌을 해결한 후 다시 커밋한다.
충돌 해결 후 예시:</br>
git add . # 또는 git add [충돌 파일명] </br>
git commit -m "Merge conflict resolved"

### 6. 원격 저장소에 푸시
로컬 작업 브랜치를 원격 저장소에 푸시한다.</br>
`git push origin [작업 브랜치명]`</br>
예시: `git push origin feature/new-dashboard`

### 7. Pull Request (PR) 생성
GitHub 웹 UI에서 푸시한 작업 브랜치(Feature,Bugfix)를 **Dev 브랜치로 병합하기 위한** Pull Request를 생성한다.

예시: `git checkout dev` -> `git merge [작업 브랜치명]`

- 변경 내용에 대한 상세한 설명</br>
- 관련 이슈 또는 작업 목록 (있는 경우)</br>
- 코드 리뷰를 요청할 팀원 지정</br>

### 8. 코드 리뷰 및 피드백 반영
지정된 리뷰어들은 PR의 코드를 검토하고 피드백을 제공한다.</br>
피드백을 바탕으로 코드를 수정하고, 수정 사항을 다시 작업 브랜치에 커밋하고 푸시한다.

### 9. 병합 (Merge)
리뷰가 완료되고 2명 이상의 협업 인원으로부터 승인(Approve)을 받은 후, PR을 기준 브랜치로 병합한다.</br>

GitHub에서는 일반적으로 세 가지 병합 옵션을 제공하나, 아래 방식을 선택한다.</br>
Create a merge commit (기본값): 병합 커밋을 생성하여 병합 기록을 명확하게 남긴다.

### 10. 병합된 브랜치 삭제
성공적으로 병합된 작업 브랜치는 더 이상 필요 없으므로 삭제한다.</br>
GitHub PR 페이지에서 병합 후 Delete branch 버튼을 클릭한다.

로컬에서 삭제: `git branch -d [브랜치명]`
원격 브랜치 삭제: `git push origin --delete [브랜치명]`

### 커밋 컨벤션
명확하고 일관된 커밋 기록을 위해 다음과 같은 커밋 컨벤션을 따른다.

`Feat: 구현내용 요약` - 새로운 기능 추가 시</br>
`Fix: 수정내용 요약` - 버그 수정 시</br>
`Rename: 이전 파일명 -> 바꾼 파일명` - 파일 이름 변경 시</br>
`Remove: 삭제한 파일명` - 파일 삭제 시</br>
`Refactor: 리팩토링한 내용` - 코드 리팩토링 시</br>
`Comment: 주석 추가한 내용` - 주석 추가 또는 수정 시
</details>
