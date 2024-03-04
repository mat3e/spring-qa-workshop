# Exercise 3

1. Continue with the app
1. Maintain tests through `jvm-test-suite` ([docs](https://docs.gradle.org/current/userguide/jvm_test_suite_plugin.html))
1. Replace current maintaining (`tasks.named('test')`) with 
    ```groovy
    testing {
        suites {
            configureEach {
                useJUnitJupiter()
                dependencies {
                    implementation 'org.springframework.boot:spring-boot-starter-test'
                }
            }
    
            integrationTest(JvmTestSuite) {
                dependencies {
                    implementation project()
                }
    
                targets {
                    all {
                        testTask.configure {
                            shouldRunAfter test
                        }
                    }
                }
            }
        }
    }
    
    tasks.named('check') {
        dependsOn testing.suites.integrationTest
    }
    ```
1. Move `DownloadsApplicationTests` to `integrationTest`
