#!/usr/bin/env groovy
pipeline {
    agent {
        label 'commonagent'
    }

    environment {
        ARTIFACTORY_URI = 'https://artefacts.tax.service.gov.uk/artifactory'
        TMP = "${WORKSPACE}/tmp"
    }

    stages {
        stage('Tests') {
            stages {
                stage('cip_phone_number-validation validateAll') {
                    steps {
                        ansiColor('xterm') {
                            sh("sbt -Dsbt.override.build.repos=true -Djava.io.tmpdir=\"$TMP\" validateAll")
                        }
                    }
                }
                stage('cip_phone_number-validation scalastyle') {
                    steps {
                        ansiColor('xterm') {
                            sh("sbt -Djava.io.tmpdir=\"$TMP\" scalastyle")
                        }
                    }
                }
                stage('cip_phone_number-validation tests') {
                    steps {
                        ansiColor('xterm') {
                            sh("sbt -Djava.io.tmpdir=\"$TMP\" coverage test IntegrationTest/test coverageOff coverageReport")
                        }
                    }
                }
            }
        }

        stage('Package and publish') {
            when {
                environment name: 'GIT_BRANCH', value: 'origin/main'
            }
            steps {
                ansiColor('xterm') {
                    withCredentials([usernamePassword(credentialsId: 'artifactory_api_access', passwordVariable: 'ARTIFACTORY_PASSWORD', usernameVariable: 'ARTIFACTORY_USERNAME')]) {
                        sh("./get_version.sh")
                        script {
                            build_version = readFile "${TMP}/RELEASE_VERSION"
                            currentBuild.description = "Release: " + build_version
                        }
                        sh("sbt -Dsbt.override.build.repos=true -Djava.io.tmpdir=\"$TMP\" validate publish")
                        sh('./tagger.sh tag')
                    }
                }
            }
        }
    }

    post {
        success {
            archiveArtifacts allowEmptyArchive: true, artifacts: 'target/bobby-reports/'
            junit("target/scalastyle-result.xml")
            junit("target/test-reports/*.xml")
            junit("target/int-test-reports/*.xml")
            step([$class: 'ScoveragePublisher', reportDir: 'target/scala-2.12/scoverage-report', reportFile: 'scoverage.xml'])
            publishHTML([allowMissing: true, alwaysLinkToLastBuild: true, keepAll: true, reportDir: 'target/test-reports/html-report', reportFiles: 'index.html', reportName: 'Unit test HTML Report', reportTitles: ''])
            publishHTML([allowMissing: true, alwaysLinkToLastBuild: true, keepAll: true, reportDir: 'target/int-test-reports/html-report', reportFiles: 'index.html', reportName: 'IT test HTML Report', reportTitles: ''])
        }
    }
}
