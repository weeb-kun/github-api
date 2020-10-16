# Scopes

This is a short guide on the github OAuth scopes.
Some of the more common scopes are `repo` and `user`.
these are very useful to retrieve public and private info about a user and their repos.

note: the scopes <i>limit</i> access for the application.
they do not grant additional access other than those that are already granted for the user.
see the github docs for more info.

## Scopes list

- `repo` - grants full access to public and private repos.
    - `repo:status` - grants read write access to private and public repo commit statuses.
- `repo_deployment` - grants access to private and public deployment statuses.
- `public_repo` - limits access to public repos.
- `repo:invite` - grants access to invitations for collaboration.
- `security_events` - grants read write access to security events in the code scanning API.
- `admin:repo_hook` - grants read, write, ping and delete access to public and private repo hooks.
this limits access to repo hooks only. use `repo` or `public_repo` to grant full access, including hooks.
- `write:repo_hook` - grants read, write and ping access to hooks in public and private repos.
- `read:repo_hook` - grants read and ping access for repo hooks.
- `admin:org` - grants full access to the user's organisations, including their teams, projects and memberships.
    - `write:org` - grants read write access to organisations.
    - `read:org` - limits read-only access to organisation membership, projects and teams.
- `admin:public_key` - grants full access to public keys.
    - `write:public_key` - grants read and create access to public keys.
    - `read:public_key` - grants read access to public keys.
- `admin:org_hook` - grants full access to organisation hooks.
- `gist` - grants write access to gists.
- `notifications` - Grants:<br>
                    read access to a user's notifications<br>
                    mark as read access to threads<br>
                    watch and unwatch access to a repository, and<br>
                    read, write, and delete access to thread subscriptions.
- `user` - grants read write access to the authenticated user's profile.<br>
note: this scope includes `user:email` and `user:follow`.
    - `read:user` - grants read access to the user's profile data.
    - `user:email` - grants read access to the user's email address.
    - `user:follow` - grants access to follow or unfollow other users.
- `delete_repo` - grants ability to delete the user's repositories.
- `write:discussion` - grants read write access to team discussions.
    - `read:discussion` - limits read access to team discussions.
- `write:packages` - grants upload and publish access for github packages.
    - `read:packages` - grants download access for github packages.
    - `delete:packages` - grants ability to delete packages.
- `admin:gpg_key` - grants full access to gpg keys.
    - `write:gpg_key` - grants read and create access to gpg keys.
    - `read:gpg_key` - limits to read access for gpg keys.
- `workflow` - grants read write access for github actions workflow files.

for more info about scopes, visit the github docs at https://docs.github.com/en/free-pro-team@latest/developers/apps/scopes-for-oauth-apps