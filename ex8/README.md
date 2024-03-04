# Exercise 8

1. Add dedicated `@Configuration` classes to reporting and limiting
1. Make it the only way of defining beans
1. Make `@Configuration` classes themselves expecting beans in their constructors
    * I/O beans (repository, metrics, clock)
    * Other modules (e.g. limiting requires `ReportingFacade`)
1. Bonus: check bean names in `ApplicationContext` now
