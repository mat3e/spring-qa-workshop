# Exercise 5

1. Use BDD methods in tests
1. Introduce something we own and can mock
    1. Add `reporting` package next to `limiting`
    1. Add `ReportingFacade`, e.g. with `recordEvent` method
    1. Move `MeterRegistry` and incrementing there
    1. Think of further extensions - e.g. logging
    1. Bonus: test that logging output
1. We refactor both code and tests, but later changing the reporting part shouldn't change tests
