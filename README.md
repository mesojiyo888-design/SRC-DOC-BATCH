# 배치 API 사용 가이드

## Job 목록

| Job Bean 이름         | 방식              | 설명             |
|-----------------------|-------------------|------------------|
| documentSyncJob       | 단일 Tasklet      | 문서 동기화       |
| documentApprovalJob   | Step 1→2→3 순차   | 결재 마감 처리    |
| documentReportJob     | Step 1→2→3 (선택) | 보고서 생성       |


## 젠킨스 파이프라인 예시
pipeline {
    agent any

    triggers {
        cron('0 2 * * *')  // 매일 02:00
    }

    stages {
        stage('Run Batch') {
            steps {
                sh 'java -jar /app/SRC-DOC-BATCH.jar --spring.batch.job.names=documentSyncJob'
            }
        }
    }

    post {
        success { echo 'documentSyncJob 완료' }
        failure { echo 'documentSyncJob 실패' }
    }


# setp별 실행
pipeline {
    agent any

    triggers {
        cron('0 2 * * *')  // 매일 02:00
    }

    parameters {
        string(name: 'TARGET_STEP', defaultValue: '', description: 'Step 지정 (비우면 전체 실행)')
    }

    stages {
        stage('Run Batch') {
            steps {
                script {
                    def cmd = 'java -jar /app/SRC-DOC-BATCH.jar --spring.batch.job.names=documentReportJob'
                    if (params.TARGET_STEP?.trim()) {
                        cmd += " --targetStep=${params.TARGET_STEP}"
                    }
                    sh cmd
                }
            }
        }
    }

    post {
        success { echo 'documentReportJob 완료' }
        failure { echo 'documentReportJob 실패' }
    }
}
