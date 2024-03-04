# Exercise 4

1. Rewrite tests to use `MockitoExtension`
1. Skip mocking `AccountSetting` where possible
1.  Add custom assertion for `AccountSetting` (in `testFixtures`), so it can be read e.g.
    ```java
    thenAccount(existingAccount).cannotDownloadMoreThan(1)
    ```
