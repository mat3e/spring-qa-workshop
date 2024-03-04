# Exercise 9

1. Check how _src/main/resources/application.yaml_ already fine-tunes the app
1. Comment out its content
1. Does the app start? How got it configured? How does it work?
1. Check if handmade bean will take precedence over auto-configurations 
    ```java
   @Bean
   DataSource dataSource() {
       var result = new DriverManagerDataSource("jdbc:h2:file:./filedb;CASE_INSENSITIVE_IDENTIFIERS=TRUE");
       result.setDriverClassName("org.h2.Driver");
       return result;
   }
   ```
