# Exercise 2

1. Clone this repo and open app from _ex2/start_
1. Add `java-test-fixtures`
   plugin ([docs](https://docs.gradle.org/6.8.3/userguide/java_testing.html#sec:java_test_fixtures))
    * You need to “refresh” after changing _build.gradle_ file
      [![refresh](../docs/img/refresh.png)](../docs/img/refresh.png)
1. Move `InMemoryAccountRepository` file to `testFixtures`, to `io.github.mat3e.downloads.limiting` package
    * Use `testFixturesImplementation`, so `testFixtures` can access `Repository` from Spring dependencies
1. Is this still possible to create `InMemoryAccountRepository` under tests code?
