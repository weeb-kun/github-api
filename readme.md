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

### Scopes

for more info on the scopes available, check this page: [scopes.md](scopes.md)

To see the available scopes and find out what you need, visit the github docs at
https://docs.github.com/en/free-pro-team@latest/developers/apps/scopes-for-oauth-apps

and check out the [com.weebkun.auth.Scopes](src/main/java/com/weebkun/auth/Scopes.java) class.

### Resources
this section lists some resources you can manipulate.
do take note that some calls require proper authentication.
remember to check that you have the correct scopes.
#### repositories

Get a public repository with `Repository.get(owner, repoName)`.
List the authenticated user's repositories with `Github.listUserRepos(params)`.

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

#### The Options object
In certain methods, an Options parameter `params` will be expected,
this is to configure the query params in the request.

To configure the `Options` object, simply instantiate a new `Options` object and set the public fields.

##### Fields
- `visibility` - specify the visibility of the resource requested.
- `affiliation` - specify the affiliation to the authenticated user.
- `type` - specify the type of repository to get. cannot be used with `visibility` or `affiliation`.
- `sort` - specify the field to sort by.
- `direction` - specify the direction of sorting.
- `perPage` - specify the number of results per page.
- `page` - specify the current page to get.
- `since` - specify to only get updates after this timestamp.
- `before` - specify to only get updates before this timestamp.

for more info, see the github docs at https://docs.github.com/en/free-pro-team@latest/rest/reference/repos#list-repositories-for-the-authenticated-user.
this specific api call accepts all the query params stated.

#### Media Types
The github api provides various media types and previews that require special accept headers.
By default the `nebula-preview` and `mercy-preview` is used,
but you can use whatever preview you like.

These media types can be found at `com.weebkun.api.MediaTypes`.

For more info on the api previews, visit https://docs.github.com/en/free-pro-team@latest/rest/overview/api-previews.

For more methods and examples, visit the docs at https://javadoc.io/doc/com.github.weeb-kun/github-api/0.0.1/index.html.

## Modifying the default http client
This library uses Okhttp. if you want to configure your own client,
use the Github.Builder class. use this class to configure custom interceptors and event listeners,
but make sure you provide the proper headers. (`accept`, `authorization`, `user-agent`)

To provide a custom client,
create a new `Github.Builder` object, add the desired interceptors and event listeners,
and then call `.build()`.

## Documentation
docs can be found at https://javadoc.io/doc/com.github.weeb-kun/github-api/0.0.1/index.html.
