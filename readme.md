# Github Java Api
java api wrapper for the github REST v3 api.

## Installation

### gradle
```groovy
dependencies {
    implementation 'com.github.weeb-kun:github-api:{version}'
    // use the latest stable version or if you wish,
    // the latest pre release version.
}
```

### maven
```xml
<dependency>
  <groupId>com.github.weeb-kun</groupId>
  <artifactId>github-api</artifactId>
  <version>$version</version>
  <type>pom</type>
</dependency>
```

## Getting started
Using either oath or a personal access token, authenticate using
`Github.authenticate(clientId, scopes)` or `Github.authenticate(token)`.
This stores the access token in the `Github` class.
After authenticating,
you can now proceed to do your api calls without worrying about the authentication headers

<br>
To see the available scopes and find out what you need, visit the github docs at
https://docs.github.com/en/free-pro-team@latest/developers/apps/scopes-for-oauth-apps

and check out the `com.weebkun.auth.Scopes` class.

## Documentation
docs can be found at https://javadoc.io/doc/com.weebkun/github-api.