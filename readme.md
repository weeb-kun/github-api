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
Then, you should set the user-agent to a suitable user agent with the `Github.setAgent(agent)` method,
although we will provide a default user agent for you. "Java-github-api".<br>

After authenticating and setting the user-agent,
you can now proceed to do your api calls without worrying about the authentication headers.

To see the available scopes and find out what you need, visit the github docs at
https://docs.github.com/en/free-pro-team@latest/developers/apps/scopes-for-oauth-apps

and check out the `com.weebkun.auth.Scopes` class.

### Resources
this section lists some resources you can manipulate.
do take note that some calls require proper authentication.
remember to check that you have the correct scopes.
#### repositories

- `static get(owner, name)`<br>
get a repository by a given owner and name of the repo.

- `static getAllPublic(since, perPage, visibility)`<br>
get all public repos updated since a given timestamp. paginate using perPage.
filter the results by visibility.

- `class Builder`<br>
create a new repository.
instantiate a new `Builder` object and set the properties needed,
then call `build()` on the builder instance to create the repo.
see the docs for more info.

- `static create(templateOwner, templateName, owner, name, description, isPrivate)`<br>
create a new repository from a template repo.

- `class UpdateRepo`<br>
another builder class to update an existing repo.
call `update()` on an existing repository to get back an `UpdateRepo` object.
use this object to update the repository before calling `updateRepo.update()`
to persist to github.
see the docs for more info.

- `static delete(owner, name)`<br>
deletes a repository with a given owner and name.

- `Github.listUserRepos(params)`<br>
lists the authenticated user's repos.
the `params` object is an `Options` object that specifies any query params in the request.
see `com.weebkun.api.Options` for more info.
see https://docs.github.com/en/free-pro-team@latest/rest/reference/repos#list-repositories-for-the-authenticated-user
for more info on the query params.

- `Organisation.getRepos(org)`<br>
gets a list of repos in an organisation.

- `class Organisation.OrgBuilder`<br>
builder class to create a repo in an organisation.
authenticated user must have explicit permission to create repos in the specified organisation.

#### Users

- `Github.getAuthenticatedUser()`<br>
returns the authenticated user object.

for more methods and examples, visit the docs at https://javadoc.io/doc/com.github.weeb-kun/github-api/0.0.1/index.html.

## Documentation
docs can be found at https://javadoc.io/doc/com.github.weeb-kun/github-api/0.0.1/index.html.