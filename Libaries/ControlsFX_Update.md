# ControlsFX — Update My Branch from Upstream

This document describes how to update my `grid_row_focus` branch with the latest changes from the official ControlsFX repository while keeping all my own changes.

## Repository setup

| Purpose | Repository / Branch |
|---|---|
| My repository | `https://github.com/Darth4Vader/controlsfx.git` |
| My branch | `grid_row_focus` |
| Official repository | `https://github.com/controlsfx/controlsfx.git` |
| Official branch | `master` |
| My remote | `origin` |
| Official remote | `upstream` |

The important rule is:

> **Fetch from `upstream`, push only to `origin`.**

---

# 1. Make sure I am on my branch

```powershell
git switch grid_row_focus
```

Check the status:

```powershell
git status
```

The working tree should be clean:

```text
On branch grid_row_focus
nothing to commit, working tree clean
```

If there are local changes, stop and deal with them before continuing.

---

# 2. Create a backup branch

Before updating:

```powershell
git branch backup-grid_row_focus
```

If the backup already exists, do not create it again.

For future updates, it is better to create a timestamped backup:

```powershell
git branch backup-grid_row_focus-$(Get-Date -Format "yyyyMMdd-HHmmss")
```

This gives you a safe point to return to if something goes wrong.

---

# 3. Check the remotes

```powershell
git remote -v
```

You should have:

```text
origin    https://github.com/Darth4Vader/controlsfx.git (fetch)
origin    https://github.com/Darth4Vader/controlsfx.git (push)
upstream  https://github.com/controlsfx/controlsfx.git (fetch)
upstream  https://github.com/controlsfx/controlsfx.git (push)
```

If `upstream` does not exist, add it:

```powershell
git remote add upstream https://github.com/controlsfx/controlsfx.git
```

---

# 4. Make upstream fetch-only

To prevent accidentally pushing to the official ControlsFX repository, disable the push URL:

```powershell
git remote set-url --push upstream DISABLED
```

Check:

```powershell
git remote -v
```

You should now see something similar to:

```text
origin    https://github.com/Darth4Vader/controlsfx.git (fetch)
origin    https://github.com/Darth4Vader/controlsfx.git (push)
upstream  https://github.com/controlsfx/controlsfx.git (fetch)
upstream  DISABLED (push)
```

This means:

- `git fetch upstream` works.
- `git pull upstream ...` can work.
- `git push origin ...` works.
- `git push upstream ...` cannot accidentally push to the official repository.

---

# 5. Fetch the latest ControlsFX code

```powershell
git fetch upstream
```

This downloads the latest commits from the official ControlsFX repository.

It does **not** change your current branch.

---

# 6. Check the branches

```powershell
git branch -a
```

You should see something similar to:

```text
* grid_row_focus
  master
  remotes/origin/grid_row_focus
  remotes/origin/master
  remotes/upstream/master
```

---

# 7. Check the history

```powershell
git log --oneline --decorate --graph --all -30
```

This lets you see where your branch and upstream branch are located.

---

# 8. Identify the old starting point

For the current `grid_row_focus` branch, the old common commit was:

```text
e3b3e5ca
```

Your history originally looked like:

```text
e3b3e5ca
    |
    +-- a6bcb22c
    +-- 85189ce9
    +-- 1d361b7f
    +-- 57c0e407
    +-- dbc68d7b
          |
          grid_row_focus
```

---

# 9. See what changed upstream

List files changed by ControlsFX since the old starting point:

```powershell
git diff --name-only e3b3e5ca..upstream/master | Sort-Object -Unique
```

See the actual changes:

```powershell
git diff e3b3e5ca..upstream/master
```

---

# 10. See what changed in my branch

List files changed by my branch since the old starting point:

```powershell
git diff --name-only e3b3e5ca..grid_row_focus | Sort-Object -Unique
```

See my actual changes:

```powershell
git diff e3b3e5ca..grid_row_focus
```

---

# 11. Update my branch

Make sure I am on my branch:

```powershell
git switch grid_row_focus
```

Then rebase my work onto the latest official master:

```powershell
git rebase upstream/master
```

This takes my commits and replays them on top of the latest ControlsFX code.

The result should look conceptually like:

```text
                 upstream/master
                       |
                       v
                latest ControlsFX
                       |
                       +-- My commit
                       |
                       +-- My commit
                       |
                       +-- My commit
                       |
                       +-- My commit
                       |
                       +-- My commit
                              |
                              v
                        grid_row_focus
```

---

# 12. If there are conflicts

If Git reports conflicts:

```powershell
git status
```

Git will show the conflicted files.

Open each conflicted file and resolve the conflict.

After fixing a file:

```powershell
git add path/to/file
```

After fixing all conflicts for the current commit:

```powershell
git rebase --continue
```

If another conflict occurs, repeat:

```powershell
git status
```

Fix the files.

Then:

```powershell
git add path/to/file
git rebase --continue
```

Repeat until the rebase finishes.

---

# 13. Cancel the rebase if necessary

If something goes wrong and you want to completely cancel the rebase:

```powershell
git rebase --abort
```

This returns `grid_row_focus` to the state it had before the rebase.

You can also use the backup branch:

```powershell
git switch backup-grid_row_focus
```

---

# 14. Check the updated history

After a successful rebase:

```powershell
git log --oneline --decorate --graph -20
```

You should see something like:

```text
* <new hash> My latest commit
* <new hash> My previous commit
* <new hash> My previous commit
* <new hash> My previous commit
* <new hash> My first commit
* <upstream hash> Latest ControlsFX commit
* <upstream hash> Previous ControlsFX commit
* ...
```

The hashes of your commits will change after a rebase.

This is normal.

For example:

```text
Before:

dbc68d7b  My commit


After:

a3fec077  My commit
```

The commit message and changes are preserved, but the commit hash changes because the parent commit changed.

---

# 15. Check that the working tree is clean

```powershell
git status
```

Expected:

```text
On branch grid_row_focus
nothing to commit, working tree clean
```

---

# 16. Verify that my commits are still present

```powershell
git log --oneline upstream/master..grid_row_focus
```

This shows commits that exist on my branch but not on the official upstream branch.

---

# 17. Compare my branch with the latest upstream

To see the actual code differences:

```powershell
git diff upstream/master..grid_row_focus
```

To see only the changed filenames:

```powershell
git diff --name-only upstream/master..grid_row_focus
```

This answers:

> What is different between my current branch and the latest official ControlsFX master?

---

# 18. Compare in Eclipse

In Eclipse, make sure `upstream/master` is visible under:

```text
Git Repositories
└── controlsfx
    └── Branches
        └── Remote Tracking
            └── upstream
                └── master
```

Then compare:

```text
upstream/master
       VS
grid_row_focus
```

Eclipse will show the changed files and allow you to open each file in a side-by-side comparison.

---

# 19. Build and test

Run the build:

```powershell
.\gradlew build
```

Or run the tests:

```powershell
.\gradlew test
```

Fix any problems before pushing.

---

# 20. Push the updated branch to my repository

Because `rebase` changes the commit hashes, use:

```powershell
git push --force-with-lease origin grid_row_focus
```

This pushes to:

```text
https://github.com/Darth4Vader/controlsfx.git
```

It does **not** push to the official ControlsFX repository.

Do **not** use:

```powershell
git push upstream grid_row_focus
```

---

# 21. Verify the push

Check the status:

```powershell
git status
```

Then:

```powershell
git log --oneline --decorate --graph -15
```

Your local branch and `origin/grid_row_focus` should point to the same latest commit.

---

# Complete update procedure for future updates

Once everything is configured, the normal procedure is:

```powershell
git switch grid_row_focus

git status

git fetch upstream

git branch backup-grid_row_focus-$(Get-Date -Format "yyyyMMdd-HHmmss")

git rebase upstream/master
```

If there are conflicts:

```powershell
git status
```

Fix the conflicted files.

Then:

```powershell
git add <fixed-file>
git rebase --continue
```

Repeat until the rebase finishes.

Then:

```powershell
git status

git log --oneline --decorate --graph -20

git diff --name-only upstream/master..grid_row_focus

.\gradlew build

git push --force-with-lease origin grid_row_focus
```

---

# Important rules

## Fetch from upstream

Use:

```powershell
git fetch upstream
```

This gets the latest official ControlsFX code.

## Rebase onto upstream

Use:

```powershell
git rebase upstream/master
```

This puts your changes on top of the latest official code.

## Push only to origin

Use:

```powershell
git push origin grid_row_focus
```

After a rebase:

```powershell
git push --force-with-lease origin grid_row_focus
```

## Never push to upstream

Do not use:

```powershell
git push upstream ...
```

The upstream push URL is intentionally disabled.

---

# Repository model

```text
                  Official ControlsFX
                  controlsfx/controlsfx
                          |
                          |
                    upstream/master
                          |
                          | fetch
                          v
                 +-------------------+
                 |   My local repo   |
                 +-------------------+
                          |
                          |
                     grid_row_focus
                          |
                          | push
                          v
                  My GitHub mirror
                  Darth4Vader/controlsfx
                          |
                        origin
```

The workflow is:

```text
                    FETCH
Official ControlsFX ───────────────> My local repository
                                      |
                                      |
                                   REBASE
                                      |
                                      v
                               grid_row_focus
                                      |
                                      |
                                    PUSH
                                      |
                                      v
                              My GitHub mirror
```

## Final safety rules

- **Fetch from:** `upstream`
- **Rebase onto:** `upstream/master`
- **Work on:** `grid_row_focus`
- **Push to:** `origin/grid_row_focus`
- **Never push to:** `upstream`
- **Use:** `git push --force-with-lease`
- **Keep a backup branch before rebasing**
- **Run tests before pushing**

## Final Step — Build the Required JAR Files

After the rebase/update, build and check the project using:

```powershell
.\gradlew build check
```

The build should produce these six required JAR files:

```text
controlsfx-11.2.5-SNAPSHOT.jar
controlsfx-11.2.5-SNAPSHOT-sources.jar
controlsfx-samples-11.2.5-SNAPSHOT.jar
controlsfx-samples-11.2.5-SNAPSHOT-sources.jar
fxsampler-1.0.12-SNAPSHOT.jar
fxsampler-1.0.12-SNAPSHOT-sources.jar
```

> **Important:** This project requires **JDK 17** for the Gradle build. If your system is using another Java version, switch to JDK 17 before running the command.

```powershell
$env:JAVA_HOME="C:\Program Files\Java\jdk-17"
$env:Path="$env:JAVA_HOME\bin;$env:Path"
```

Verify:

```powershell
java -version
```

Then run:

```powershell
.\gradlew build check
``