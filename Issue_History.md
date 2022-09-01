# 이슈 발생 목록

<details>
<summary>22.08.28</summary>

<!-- summary 아래 한칸 공백 두어야함 -->
###   
> ### Token에서 Claims을 반환하는 함수 parseClaims() 에서 Error 발생
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



<details>
<summary>22.08.30</summary>

<!-- summary 아래 한칸 공백 두어야함 -->
###   
> ### TokenProvider 의 generateTokenDto()에서 repository.save()할 때 에러 발생 (SQL statement Error)
```java
public TokenDto generateTokenDto(Member member) {

    ...
    
    RefreshToken refreshTokenObject = RefreshToken.builder()
                .id(member.getId())
                .member(member)
                .value(refreshToken)
                .build();

    refreshTokenRepository.save(refreshTokenObject);

    ...

}
```
```java
public class RefreshToken extends Timestamped {
    @Id
    @Column(nullable = false)
    private Long id;

    @JoinColumn(name = "member_id", nullable = false)
    @OneToOne(fetch = FetchType.LAZY)
    private Member member;

    @Column(nullable = false)
    private String value;

    public void updateValue(String token){ this.value = token; }
}
```
**문제점 : RefreshToken 의 Column명이 SQL 예약어와 동일해서 발생한 문제<br/>**
```java
public TokenDto generateTokenDto(Member member) {

    ...
    
    RefreshToken refreshTokenObject = RefreshToken.builder()
                .id(member.getId())
                .member(member)
                .token(refreshToken)
                .build();

    refreshTokenRepository.save(refreshTokenObject);

    ...

}
```
```java
public class RefreshToken extends Timestamped {
    @Id
    @Column(nullable = false)
    private Long id;

    @JoinColumn(name = "member_id", nullable = false)
    @OneToOne(fetch = FetchType.LAZY)
    private Member member;

    @Column(nullable = false)
    private String token;

    public void updateToken(String token){ this.token = token; }
}
```
**해결법 : RefreshToken 의 Column명을 예약어와 겹치지 않게 수정해서 해결**

  ***
</details>



<details>
<summary>22.08.31</summary>

<!-- summary 아래 한칸 공백 두어야함 -->
###   
> ### Comment 또는 SubComment 를 작성하려고 할 때, 500 server internal error 발생
```java
public class CommentRequestDto {
    @NotBlank
    private Long postId;
    @NotBlank
    private String content;
}
```

```java
public class SubCommentRequestDto {
    @NotBlank
    private Long commentId;
    @NotBlank
    private String content;
}
```
**문제점 : CommentRequestDto 와 SubCommentRequestDto 에 Long 변수에 @NotBlank 어노테이션을 사용해서 발생한 문제<br/>**

```java
public class CommentRequestDto {
    @NotNull
    private Long postId;
    @NotBlank
    private String content;
}
```

```java
public class SubCommentRequestDto {
    @NotNull
    private Long commentId;
    @NotBlank
    private String content;
}
```
**해결법 : @NotBlank 대신 @NotNull 어노테이션을 사용 (@NotBlank 는 String에 사용해야 적합)**<br/>
참고사이트 : https://bepoz-study-diary.tistory.com/242

***
<br/>

> ### Comment Entity 에 getLikesNum 함수에서 likes 를 참조하려 할 때 Error가 발생
```java
public class Comment extends Timestamped{

    ...

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "comment", cascade = CascadeType.REMOVE)
    @JsonIgnore
    private List<CommentLike> likes;

    ...
    
    public int getLikesNum(){
        int count = 0;

        for(CommentLike like : likes){
            if(like.getComment().equals(this)) ++count;
        }

        return count;
    }
}
```
**문제점 : likes 가 null일 경우 참조하려 해서 NullPointerException 발생<br/>**

```java
public class Comment extends Timestamped{

    ...

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "comment", cascade = CascadeType.REMOVE)
    @JsonIgnore
    private List<CommentLike> likes;

    ...
    
    public int getLikesNum(){
        if(null == likes) return 0;
    
        int count = 0;

        for(CommentLike like : likes){
            if(like.getComment().equals(this)) ++count;
        }

        return count;
    }
}
```
**해결법 : null일 경우 예외처리를 추가해서 해결
추가 사항 : 후에 수정사항에서 getLikesNum() 결국 
    
</details>

