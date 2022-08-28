# 이슈 발생 목록

<details>
<summary>22.08.28</summary>

<!-- summary 아래 한칸 공백 두어야함 -->
###   
> ### Token에서 Claim을 반환하는 함수 parseClaims() 에서 Error 발생
```java
public Claims parseClaims(String accessToken) {
    try {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJwt(accessToken).getBody();
    } catch (ExpiredJwtException e) {
        return e.getClaims();
    }
}
```
**문제점 : Jwts의 내장함수 parseClaimsJwt 가 문제였음<br/>**
```java
public Claims parseClaims(String accessToken) {
    try {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
    } catch (ExpiredJwtException e) {
        return e.getClaims();
    }
}
```
**해결법 : Jwts의 내장함수 parseClaimsJwt 대신 parseClaimsJws 를 사용해서 해결**

  ***
<br/>

> ### WebSecurityConfig.class 에 Bean으로 생성한 passwordEncoder를 찾지 못 함
```java
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    ...

    @Bean
    public PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }
    
    ...
    
}
```
**문제점 : WebSecurityConfigurerAdapter 를 상속받은 게 문제**
```java
public class WebSecurityConfig {

    ...

    @Bean
    public PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }
    
    ...
    
}
```
**해결법 : 상속을 해제했더니 제대로 등록되서 찾는다**
  
</details>


